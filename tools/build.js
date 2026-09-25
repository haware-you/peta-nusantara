// Inlines data/land.js into src/peta.template.html -> peta-nusantara.html
//
// One HTML file is the delivery format: the artifact CSP forbids runtime
// fetches, so all code and data are inlined. The one exception is the entity
// images in `source images/`, which are served beside the page on Vercel
// (see .vercelignore). A copy delivered on its own drops them at runtime and
// keeps the credit line, rather than inlining ~8 MB of photographs.

const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..');
const TPL = path.join(ROOT, 'src', 'peta.template.html');
const LAND = path.join(ROOT, 'data', 'land.js');
const OUT = path.join(ROOT, 'peta-nusantara.html');

const PLACEHOLDER = '/*__LAND__*/';

for (const f of [TPL, LAND]) {
  if (!fs.existsSync(f)) {
    console.error('Missing ' + path.relative(ROOT, f) + '\nRun: npm run prep');
    process.exit(1);
  }
}

const tpl = fs.readFileSync(TPL, 'utf8');
if (!tpl.includes(PLACEHOLDER)) {
  console.error('Template is missing the ' + PLACEHOLDER + ' placeholder.');
  process.exit(1);
}

const land = fs.readFileSync(LAND, 'utf8').trim();
const out = tpl.replace(PLACEHOLDER, land);

fs.writeFileSync(OUT, out, 'utf8');

const kb = Buffer.byteLength(out, 'utf8') / 1024;
console.log('wrote peta-nusantara.html  ' + kb.toFixed(1) + ' KB');
console.log('build ok');
