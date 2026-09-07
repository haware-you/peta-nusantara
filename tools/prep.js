// Phase 0 — Natural Earth 50m -> simplified archipelago geometry.
//
// Reads   data/ne50.geojson   (see tools/fetch-geometry.js)
// Writes  data/land.js        (a `var LAND = [...]` constant, inlined at build)
//
// The artifact CSP forbids runtime fetches, so the geometry ships inside the
// HTML. That makes simplification load-bearing rather than optional cleanup:
// raw Natural Earth for Indonesia's ~17,000 islands is unusable at this budget.

const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..');
const DATA = path.join(ROOT, 'data');
const SRC = path.join(DATA, 'ne50.geojson');
const OUT = path.join(DATA, 'land.js');

if (!fs.existsSync(SRC)) {
  console.error('Missing ' + path.relative(ROOT, SRC) + '\nRun: node tools/fetch-geometry.js');
  process.exit(1);
}

const gj = JSON.parse(fs.readFileSync(SRC, 'utf8'));

// View window for the archipelago. Matches VIEW in the template.
const BBOX = { w: 93.0, e: 141.6, s: -11.6, n: 7.6 };
const PAD = 1.2; // clamp window, slightly wider than the viewBox

const TOL = 0.035; // Douglas-Peucker tolerance in degrees, ~3.9 km
const MIN_AREA = 0.0015; // drop islets under ~18 km^2 at the equator

// Neighbours are included so the archipelago doesn't float in a void; they are
// drawn in the same recessive land tone and carry no entities.
const KEEP = new Set([
  'Indonesia', 'Malaysia', 'Brunei', 'East Timor', 'Timor-Leste',
  'Papua New Guinea', 'Singapore', 'Philippines',
]);

const nameOf = (f) =>
  f.properties.NAME || f.properties.name || f.properties.ADMIN || f.properties.SOVEREIGNT;

function ringBBox(r) {
  let w = 1e9, e = -1e9, s = 1e9, n = -1e9;
  for (const [x, y] of r) {
    if (x < w) w = x; if (x > e) e = x;
    if (y < s) s = y; if (y > n) n = y;
  }
  return { w, e, s, n };
}

const intersects = (b) =>
  !(b.e < BBOX.w || b.w > BBOX.e || b.n < BBOX.s || b.s > BBOX.n);

// Shoelace area in square degrees.
function ringArea(r) {
  let a = 0;
  for (let i = 0, j = r.length - 1; i < r.length; j = i++) {
    a += (r[j][0] * r[i][1]) - (r[i][0] * r[j][1]);
  }
  return Math.abs(a / 2);
}

function segDist(p, a, b) {
  let x = a[0], y = a[1];
  let dx = b[0] - x, dy = b[1] - y;
  if (dx !== 0 || dy !== 0) {
    const t = ((p[0] - x) * dx + (p[1] - y) * dy) / (dx * dx + dy * dy);
    if (t > 1) { x = b[0]; y = b[1]; }
    else if (t > 0) { x += dx * t; y += dy * t; }
  }
  dx = p[0] - x; dy = p[1] - y;
  return Math.sqrt(dx * dx + dy * dy);
}

// Iterative Douglas-Peucker; recursion would blow the stack on long rings.
function simplify(pts, tol) {
  if (pts.length <= 3) return pts.slice();
  const keep = new Uint8Array(pts.length);
  keep[0] = keep[pts.length - 1] = 1;
  const stack = [[0, pts.length - 1]];
  while (stack.length) {
    const [lo, hi] = stack.pop();
    let maxD = -1, idx = -1;
    for (let i = lo + 1; i < hi; i++) {
      const d = segDist(pts[i], pts[lo], pts[hi]);
      if (d > maxD) { maxD = d; idx = i; }
    }
    if (maxD > tol && idx > 0) {
      keep[idx] = 1;
      stack.push([lo, idx], [idx, hi]);
    }
  }
  const out = [];
  for (let i = 0; i < pts.length; i++) if (keep[i]) out.push(pts[i]);
  return out;
}

const clamp = (v, lo, hi) => (v < lo ? lo : v > hi ? hi : v);

// Clamp to the padded window and collapse the runs that clamping creates.
// Offscreen detail is invisible behind the viewBox but still costs bytes.
function clampRing(r) {
  const out = [];
  let px = null, py = null;
  for (const [x0, y0] of r) {
    const x = +clamp(x0, BBOX.w - PAD, BBOX.e + PAD).toFixed(2);
    const y = +clamp(y0, BBOX.s - PAD, BBOX.n + PAD).toFixed(2);
    if (x === px && y === py) continue;
    out.push([x, y]);
    px = x; py = y;
  }
  return out;
}

const rings = [];
for (const f of gj.features) {
  if (!KEEP.has(nameOf(f))) continue;
  const g = f.geometry;
  if (!g) continue;
  const polys = g.type === 'Polygon' ? [g.coordinates] : g.coordinates;
  for (const poly of polys) {
    const outer = poly[0]; // interior rings are noise at this scale
    if (!intersects(ringBBox(outer))) continue;
    if (ringArea(outer) < MIN_AREA) continue;
    let r = clampRing(simplify(outer, TOL));
    if (r.length < 4) continue;
    if (ringArea(r) < MIN_AREA) continue;
    rings.push({ country: nameOf(f), area: ringArea(r), pts: r });
  }
}

// Largest first, so the big islands paint before the specks.
rings.sort((a, b) => b.area - a.area);

const encoded = rings.map((r) => r.pts.map((p) => p[0] + ',' + p[1]).join(' '));
const js = 'var LAND = ' + JSON.stringify(encoded) + ';\n';

fs.mkdirSync(DATA, { recursive: true });
fs.writeFileSync(OUT, js);

const totalPts = rings.reduce((s, r) => s + r.pts.length, 0);
const kb = js.length / 1024;
console.log(
  'rings ' + rings.length + '  points ' + totalPts + '  ' + kb.toFixed(1) + ' KB'
);
// Phase 0 gate.
if (kb > 100) {
  console.error('GATE FAILED: geometry over 100 KB. Raise TOL or MIN_AREA.');
  process.exit(1);
}
console.log('gate ok (budget 100 KB)');
