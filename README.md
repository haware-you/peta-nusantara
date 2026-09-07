# Peta Nusantara

An interactive historical map of the Indonesian archipelago, built as a companion
view over the bilingual dataset behind *Garis Waktu Nusantara* (a timeline of
Indonesian history). Scrub through eras and watch spheres of influence expand
and contract.

Ships as a single self-contained HTML file. No build step at runtime, no
framework, no network dependency after load.

## Why a map

The timeline answers *when* and *how long* well. It answers *where* badly —
"Sumatra" is a column label, not a place. The mechanism behind most of these
polities is maritime position, and a column chart cannot show position.

A map adds that dimension, and introduces a failure mode the timeline does not
have: **maps assert borders, and pre-colonial Nusantara borders are largely
unknowable.** That constraint shapes the whole design rather than sitting in a
footnote.

## The accuracy constraint

Pre-colonial polities here were **mandala states** — concentric spheres of
influence held through tribute, marriage alliance, and naval reach, fading
outward with no administrative boundary and frequently overlapping. The familiar
Majapahit territorial map descends from the Nagarakretagama's (1365) enumeration
of tribute-sending places: a list of polities, not a border survey.

So four rules bind the rendering:

1. No pre-colonial entity renders as a filled polygon with a hard edge. Influence
   is a radial gradient from capital and port nodes, fading outward.
2. Every era view carries a visible "approximate spheres of influence" caveat,
   in both languages.
3. The per-entity confidence tier drives gradient diffusion. Low-confidence
   entities render visibly blurrier — uncertainty becomes a visual property
   rather than a footnote nobody reads.
4. Colonial-era entities (VOC, Dutch East Indies) *may* use hard edges, because
   those genuinely had surveyed administrative boundaries.

Rule 4 is the point of the whole thing. Drag the scrubber across 1600 and fuzzy
gold influence gives way to a crisp violet administrative line: that is the
moment cartographic statehood arrives in the archipelago, and it is the argument
for building this rather than reusing an existing historical atlas.

## Build

Requires Node 18+ (uses global `fetch`). No dependencies.

```bash
npm run all
```

Which runs three steps, each with a gate:

| Step | Command | Does | Gate |
| --- | --- | --- | --- |
| Fetch | `npm run fetch` | Downloads Natural Earth 50m admin-0 into `data/` | — |
| Prep | `npm run prep` | Clips to the archipelago, Douglas–Peucker simplifies to ~3.9 km, drops islets under ~18 km², rounds to 2dp | geometry ≤ 100 KB |
| Build | `npm run build` | Inlines the geometry into the template | bundle ≤ 250 KB |

Current output: **25.6 KB** of geometry inside an **89.5 KB** page.

Edit `src/peta.template.html`, not `peta-nusantara.html` — the latter is
generated and overwritten on every build.

## Layout

```
src/peta.template.html   the page; /*__LAND__*/ marks the geometry injection point
tools/fetch-geometry.js  downloads Natural Earth source data
tools/prep.js            simplification pipeline (Phase 0)
tools/build.js           inlines geometry, checks the size budget
peta-nusantara.html      build output — the deliverable
data/                    gitignored: source geometry and generated constant
```

## Design notes

**Geography unit.** ~12 hand-defined historical regions, not 38 modern
provinces. Provinces postdate every polity on the map by centuries and encode
the wrong unit for pre-colonial history. The regions are navigation zones, never
territorial claims.

**Renderer.** Inline SVG and CSS. Thirty regions do not need a GPU, and text
stays selectable and translatable, which matters more when the product is
bilingual. No camera flythrough, terrain, or shader effects — decided out of
scope, not deferred.

**Time scale.** The scrubber reuses the chart's three segments (300–1500,
1500–1945, 1945–now) rather than a linear scale, which would put 60% of the
track before the first entity. Segment boundaries render as a visible
scale-break mark. The handle scrubs freely and settles on era boundaries,
because recall needs stable nameable states, not arbitrary years. A density
histogram behind the track shows how many entities are active at each point, so
sparse centuries read as "little recorded here" rather than as broken UI.

**Ground.** Dark, deliberately. Influence renders as radial bloom; on
paper-cream a soft radial gradient reads as a *stain*, on near-black it reads as
*light* — which is the semantic wanted. This is a conscious override of the
light/dark parity requirement in the spec.

**The blank third.** Papua and much of eastern Nusa Tenggara are nearly absent
from the dataset. On a chart that is invisible; on a map it is a conspicuous
blank. A region empty at *every* year gets a different empty state from one
merely empty this year — it says the historiography of the archipelago is built
around states and written sources, and structurally under-records non-state
societies. The blank is itself a finding.

## The study layer

Selecting any polity opens three answers, in the order a learner needs them:

- **Why here** — the geographic logic. This is the map's thesis: most of these
  powers are explained by position. Palembang is upstream of the strait's mouth;
  Ternate is one of the few places cloves grew; Trowulan has rice behind it and
  sea lanes in front.
- **How power worked** — rarely territory. Chokepoints, tribute, rice surplus,
  monopoly, or a free port that a monopolist could not tolerate.
- **How it ended** — which is what a timeline shows and a map cannot.

These live in a `LORE` table keyed by Indonesian name, alongside `MAPX`, so the
entity records themselves stay byte-identical to the timeline's. A build-time
check verifies that every entity has a complete bilingual entry; a mistyped key
surfaces as a console warning rather than a silently empty panel.

## Before the kingdoms

The map's scale starts at 300 CE, which is not the beginning. Scrub to the far
left and the rail opens a deep-time prologue — Homo erectus at Sangiran, Homo
floresiensis on Flores, the Austronesian migration, Dong Son bronze, the first
Indian and Chinese trade contact — presented as a list, because those spans are
far too long to plot against the rest of the scale.

The map draws the **Austronesian migration** for that state: out of Taiwan
through the Philippines, then west toward Sumatra and east toward Papua. These
were the longest ocean voyages anyone on earth was making at the time, and they
are why the languages across the whole region are related. Medium-high
confidence in the model; the precise routes and dates are still argued over,
and the map says so.

## The faction layers (1945–49, 1955, 1965–66)

Three overlays activate automatically when the scrubber enters their window.
They are drawn in their own colour channel, separate from the gold influence
bloom, because they make a different kind of claim.

**1945–1949 — control.** Four sub-phases: the proclamation's claim without
control; Linggadjati's de facto recognition of Java, Sumatra and Madura; the
Renville reduction; and Operatie Kraai. The **Van Mook Line** gets the hardest
edge anywhere on this map, because unlike everything else here it was actually
negotiated and surveyed. Madiun (Sept 1948) and the Darul Islam proclamation
(Aug 1949) are point-anchored events, not zones.

**1955 — support.** Regional plurality for PNI, Masyumi, NU, PKI and the
Christian parties. This is the one moment where nationalist, Islamic and
communist blocs are all simultaneously and honestly mappable. The runner-up
field matters as much as the winner: it is where the PKI actually appears, as
the strong second in Central Java. Papua is marked outside the franchise — it
was still under Dutch rule and did not vote.

**1965–66 — violence.** Rendered as flat hatch in its own colour, never as
bloom. Influence bloom is the visual language of prestige radiating from a
centre; using it for mass killing would be obscene. Concentrations in Central
Java, East Java, Bali and North Sumatra, with Buru Island marked for the
detention period. Estimates range from 500,000 to over a million and remain
contested; no one has ever been prosecuted.

### On drawing the PKI

The PKI never held territory. It was a mass party with an electoral base, not a
state, and its only armed bid for control was about three weeks at Madiun in
1948. Drawing it as a shaded territory would repeat exactly the error the rest
of this map exists to avoid — and it would reproduce the New Order's own
cartography, which portrayed the party as a territorial enemy. So it appears
as a point event in 1948 and as vote share in 1955, and the map says why in
both languages.

The communist channel is deliberately **not red**. Red is PNI's and the
Republic's colour in this period, and using it for the PKI would collapse a
distinction the layers exist to draw.

## Data provenance

Entity data is carried verbatim from the timeline. Map-specific fields (nodes,
reach, sea lanes) live in a separate `MAPX` table keyed by Indonesian name, so
divergence from the timeline surfaces as a console warning rather than the two
views quietly disagreeing.

Geometry is [Natural Earth](https://www.naturalearthdata.com/) 50m, public
domain. Note that GADM — a common alternative — is restricted for commercial
use; re-check the licence before anything monetised ships.

Historical content was assembled for personal reference from general historical
knowledge and English Wikipedia. Regional borders, lineages, and many
ancient-kingdom dates (especially pre-1500) are simplified and contested among
historians. Each entity carries a confidence tier; anything marked medium or
below should be re-verified against a primary source before it informs published
work. **Don't cite this academically.**

## Status

Phases 0–4 are built: geometry pipeline, static map with click-to-reveal, era
scrubber with density track, study mode, sea lanes.

Not built, deliberately:

- **Scroll narrative mode.** Gated behind two weeks of actual study use *and* a
  decision to turn this into a content piece. It conflicts with click
  exploration — a camera that moves while you aim at Sumatra makes you miss.
- **Chart/map unification.** The map currently ships as its own page, so the
  entity data is duplicated between the two views. Mitigated by the `MAPX`
  keying above, not solved. Merging would make the drift structurally
  impossible.

## Licence

MIT for the code. Historical content and Natural Earth geometry as noted above.
