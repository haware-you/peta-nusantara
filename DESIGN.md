---
name: Peta Nusantara
description: A hand-tinted chart of the archipelago, where pigment means inferred influence and ink means a surveyed line.
colors:
  paper: "#f3ede0"
  paper-surface: "#fbf8f1"
  paper-surface-2: "#f1eadb"
  paper-surface-3: "#e8dfcb"
  paper-line: "#ddd2ba"
  chart-sea: "#dfe8ec"
  chart-land: "#ebe3cc"
  chart-land-edge: "#bda87a"
  ink: "#221c14"
  ink-2: "#5b5347"
  ink-3: "#8c8371"
  control-ink: "#2b261f"
  control-on: "#fbf8f1"
  tribute-gold: "#96691a"
  tribute-gold-warm: "#b5822a"
  sienna-wash: "#a35f2a"
  survey-violet: "#4a3aa7"
  sea-lane-teal: "#1f7a70"
  sea-lane-teal-hot: "#146a60"
  migration-blue: "#2c7d93"
  republic-red: "#b8402f"
  islamic-green: "#2f7d4f"
  communist-plum: "#a8446f"
  christian-blue: "#2f6fae"
  no-plurality: "#b3a892"
  violence-umber: "#6e3a2e"
  category-classical: "#2a78d6"
  category-islamic: "#eb6834"
  category-modern: "#4f463b"
  night-ground: "#0c0b09"
  night-surface: "#14120e"
  night-ink: "#f3ecdd"
  night-gold: "#d9ab55"
typography:
  display:
    fontFamily: "Fraunces, Georgia, serif"
    fontSize: "clamp(1.25rem, 2.4vw, 1.7rem)"
    fontWeight: 700
    lineHeight: 1.1
    letterSpacing: "-0.01em"
  headline:
    fontFamily: "Fraunces, Georgia, serif"
    fontSize: "1.12rem"
    fontWeight: 700
    lineHeight: 1.25
    letterSpacing: "-0.005em"
  title:
    fontFamily: "Inter, system-ui, sans-serif"
    fontSize: "0.88rem"
    fontWeight: 600
    lineHeight: 1.3
  body:
    fontFamily: "Inter, system-ui, sans-serif"
    fontSize: "0.82rem"
    fontWeight: 400
    lineHeight: 1.6
  label:
    fontFamily: "IBM Plex Mono, ui-monospace, monospace"
    fontSize: "0.63rem"
    fontWeight: 600
    lineHeight: 1.5
    letterSpacing: "0.14em"
  numeral:
    fontFamily: "IBM Plex Mono, ui-monospace, monospace"
    fontSize: "1.55rem"
    fontWeight: 600
    lineHeight: 1
    letterSpacing: "-0.01em"
  caption:
    fontFamily: "IBM Plex Mono, ui-monospace, monospace"
    fontSize: "8.5px"
    fontWeight: 400
    letterSpacing: "0.11em"
rounded:
  swatch: "2px"
  tag: "3px"
  block: "8px"
  pill: "999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "12px"
  lg: "16px"
  xl: "22px"
components:
  segment-button:
    backgroundColor: "transparent"
    textColor: "{colors.ink-2}"
    typography: "{typography.title}"
    padding: "7px 14px"
  segment-button-active:
    backgroundColor: "{colors.control-ink}"
    textColor: "{colors.control-on}"
    typography: "{typography.title}"
    padding: "7px 14px"
  toggle-pill:
    backgroundColor: "{colors.paper-surface-2}"
    textColor: "{colors.ink-2}"
    rounded: "{rounded.pill}"
    padding: "6px 13px 6px 6px"
  toggle-pill-on:
    backgroundColor: "{colors.paper-surface-2}"
    textColor: "{colors.ink}"
    rounded: "{rounded.pill}"
    padding: "6px 13px 6px 6px"
  entity-card:
    backgroundColor: "{colors.paper-surface-2}"
    textColor: "{colors.ink}"
    rounded: "{rounded.block}"
    padding: "12px 14px"
  entity-card-selected:
    backgroundColor: "{colors.paper-surface-2}"
    textColor: "{colors.ink}"
    rounded: "{rounded.block}"
    padding: "12px 14px"
  overlay-block:
    backgroundColor: "{colors.paper-surface-2}"
    textColor: "{colors.ink-2}"
    rounded: "{rounded.block}"
    padding: "12px 14px"
  legend-panel:
    backgroundColor: "{colors.paper-surface}"
    textColor: "{colors.ink-2}"
    rounded: "{rounded.block}"
    padding: "10px 13px"
  tag:
    backgroundColor: "transparent"
    textColor: "{colors.ink-2}"
    rounded: "{rounded.tag}"
    padding: "1px 5px"
---

# Design System: Peta Nusantara

## 1. Overview

**Creative North Star: "The Hand-Tinted Chart"**

An engraved sea chart that someone has tinted by hand. The plate is paper and ink: neutral ground, hairline rules, monospace captions set with wide tracking. Onto that plate, pigment is washed wherever the record is inferred rather than known: a sphere of influence blooms from a capital and fades, and the lower the confidence the more it diffuses. Ink is reserved for what was actually surveyed. A colonial boundary or the Van Mook Line gets a crisp stroke because a surveyor drew one; Majapahit never does, because no one ever did.

Two grounds, one chart. On paper the wash is multiply-blended pigment. On the night ground it becomes screen-blended light radiating from the same centres. The data does not change between them; the physics of the medium does. Chrome (header, rail, scrubber) is deliberately quiet on both grounds so that nothing in the interface can be mistaken for a claim on the map.

The system explicitly rejects dashboard chrome (panels, card grids, KPI tiles, glass blur, accent stripes), territory assertion (filled polygons with hard borders for pre-colonial polities), and heritage decoration (batik, wayang, flag red-and-white as ornament). Density is moderate: one map, one rail, one scrubber, and a standing caveat that is never hidden.

**Key Characteristics:**
- Every hue on the page is a legend entry; controls are ink.
- Uncertainty is rendered, not footnoted: blur and diffusion scale with confidence tier.
- Edges match the evidence: bloom, dashed line, or surveyed stroke.
- Mono captions with wide tracking on the map and in every label.
- Flat surfaces with hairline borders; the only glow is the time cursor.
- Bilingual by construction; nothing is styled for one language only.

## 2. Colors: The Tinted Plate

Neutral paper and ink carry the chrome; a small set of named pigments carry meaning, and no pigment is ever decorative.

### Primary
- **Tribute Gold** (#96691a paper, #d9ab55 night): the map's subject. Era name, the time cursor and its glow, the density histogram, active-power counts, the caveat lead, and the selected entity's border and wash. It means influence and time, and nothing else.
- **Sienna Wash** (canonical oklch(56% 0.13 55), #a35f2a): the pigment of the influence bloom on paper, multiply-blended. Shifted redder than Tribute Gold so the wash reads as tint on the warm land rather than mud. On the night ground the bloom uses Tribute Gold itself, screen-blended.
- **Survey Violet** (#4a3aa7 paper, #9085e9 night): the only colour permitted a hard edge. VOC and Dutch East Indies boundaries, the Dutch-held zones of 1945-49, the colonial category mark.

### Secondary
- **Sea Lane Teal** (#1f7a70, hot #146a60; night #3f9d92, hot #63c8bb): dashed maritime routes and their captions. The Sea Lanes toggle knob wears this colour when on.
- **Migration Blue** (#2c7d93, text #1f6478; night #7fb6c9, text #9fd0de): the Austronesian migration paths in the deep-time prologue. Its own channel, neither influence nor control.

### Tertiary: The Faction Channel
Used only inside the 1945-49, 1955 and 1965-66 overlays. Each is paired with a label or legend row; the colour is never the only carrier.
- **Republic Red** (#b8402f, night #d0503f): PNI and the Republic. Red belongs here and nowhere else.
- **Islamic Green** (#2f7d4f, night #46a06a): Masyumi, NU, Darul Islam.
- **Communist Plum** (#a8446f, night #c9628f): PKI. Deliberately not red; see PRODUCT.md.
- **Christian Blue** (#2f6fae, night #5b9bd5): Parkindo, Partai Katolik.
- **No Plurality** (#b3a892, night #574f42): regions outside the franchise or without a plurality.
- **Violence Umber** (canonical oklch(38% 0.08 35), #6e3a2e; night hatch oklch(55% 0.09 35), text oklch(80% 0.06 35)): the 1965-66 layer, rendered as flat hatch. Browner and darker than Republic Red so it cannot be read as a party.

Timeline category marks, carried from the sibling chart for the rail's leading dots: **Classical** (#2a78d6), **Islamic** (#eb6834), **Colonial** (Survey Violet), **Modern** (canonical oklch(40% 0.02 60), #4f463b, a near-ink charcoal because red is already spoken for).

### Neutral
- **Paper** (#f3ede0): page ground. **Paper Surface** (#fbf8f1) for header, rail and scrubber; **Surface 2** (#f1eadb) for cards and pills; **Surface 3** (#e8dfcb) for hover.
- **Chart Sea** (canonical oklch(92.5% 0.014 215)) and **Chart Land** (oklch(91.5% 0.026 88), edge oklch(76% 0.055 85)): the sea leans cool and the land leans warm so pigment separates from ground.
- **Ink** (#221c14), **Ink 2** (#5b5347), **Ink 3** (#8c8371): text hierarchy, era dots at rest, region captions. **Paper Line** (#ddd2ba): every hairline.
- **Control Ink** (canonical oklch(24% 0.012 70)) with **Control On** (#fbf8f1): active segment, toggle knobs, focus rings.
- Night ground: **Night Ground** (#0c0b09), **Night Surface** (#14120e), surfaces #1c1913 and #231e16, **Night Ink** (#f3ecdd), line #2a2419. Control Ink inverts to oklch(92% 0.012 80) on #14120e.
- Text halo: rgba(251,248,241,0.92) on paper, rgba(12,11,9,0.90) on night. Every map caption carries one.

### Named Rules
**The Legend Rule.** A colour may appear on the page only if it corresponds to a legend entry. If you cannot name what a hue means, it is ink or it is gone.

**The Ink Controls Rule.** Interactive chrome is Control Ink. The single exception is a toggle that switches on a coloured layer, which wears that layer's colour when on. Gold on a button is forbidden.

**The Surveyed Edge Rule.** A hard stroke is permitted only on Survey Violet, the Van Mook Line and the Republic's negotiated zones. Every pre-colonial entity is a radial bloom whose diffusion is set by confidence tier: high (.62/.36/.13/0), medium (.46/.28/.10/0), low (.32/.21/.08/0).

**The Not-Red Rule.** Red is the Republic's. The PKI is plum; the killings are umber; the modern category is charcoal.

## 3. Typography

**Display Font:** Fraunces (with Georgia, serif)
**Body Font:** Inter (with system-ui, sans-serif)
**Label/Mono Font:** IBM Plex Mono (with ui-monospace, monospace)

**Character:** A serif masthead sitting above a plate that is otherwise captioned in monospace. Fraunces at 700 is used sparingly, for the title and rail headings only, so that its weight reads as a plate title rather than as editorial affectation. Inter carries the explanatory prose at small sizes. Plex Mono, uppercase and widely tracked, is the engraver's hand: eyebrows, era names, region names on the map, route labels, the ruler. This pairing is committed identity, not a greenfield choice; do not swap families.

### Hierarchy
- **Display** (700, clamp(1.25rem, 2.4vw, 1.7rem), 1.1, tracking -0.01em): the page title only.
- **Headline** (700, 1.12rem, 1.25): rail headings (region name, overlay title at 0.98rem).
- **Numeral** (Plex Mono 600, 1.55rem, 1): the current year in the era badge. The largest thing on the map.
- **Title** (Inter 600, 0.88rem, 1.3): entity names.
- **Body** (Inter 400, 0.82rem, 1.6): explanations, lore answers, overlay prose. Rail is 336px wide, so measure stays under 60ch by construction.
- **Label** (Plex Mono 600, 0.60 to 0.68rem, tracking 0.13 to 0.15em, uppercase): eyebrow, rail labels, era name, lore keys, overlay kickers, legend headings.
- **Caption** (Plex Mono 400, 7 to 8.5px, tracking 0.06 to 0.13em, uppercase for regions and routes): map text. Always haloed.

### Named Rules
**The Engraver's Hand Rule.** Anything that names a thing on the map is monospace, uppercase, tracked and haloed. Anything that explains a thing is Inter, sentence case, 1.6 line-height.

**The One Serif Rule.** Fraunces appears at exactly two sizes: the masthead and rail headings. It is never body, never a label, never italic.

## 4. Elevation

The system is flat. Depth comes from tonal layering of paper surfaces (Paper, Surface, Surface 2, Surface 3) separated by 1px Paper Line hairlines, and from blend modes on the map itself: multiply on paper, screen on night. No surface casts a shadow at rest.

### Shadow Vocabulary
- **Time cursor glow** (`box-shadow: 0 0 0 4px rgba(150,105,26,0.22), 0 0 14px 4px rgba(150,105,26,0.30)` on paper; night uses rgba(217,171,85,0.20) and 0.34): the scrubber thumb only. It is the one object on the page that glows, because it is made of the same light as the bloom.
- **Focus ring** (`0 0 0 3px <ground>, 0 0 0 5px <control-ink-ring>` on the thumb; `outline: 2px solid <control-ink-ring>` elsewhere): state, not elevation.

### Named Rules
**The One Glow Rule.** If something other than the scrubber thumb has a glow, it is wrong. Cards, pills, legends and blocks sit flat on their surface with a hairline.

## 5. Components

Quiet instruments. Flat, hairline-bordered, ink-coloured controls that stay out of the map's way. Nothing on the chrome is louder than the map.

### Buttons
- **Shape:** fully round segment group and pills (999px).
- **Segment (language):** transparent on Surface 2, Ink 2 text at 600 / 0.76rem, 7px 14px padding. Active: Control Ink background, Control On text.
- **Toggle pill:** Surface 2 with a Paper Line hairline; a 28x17px knob on the left whose 13px dot is Ink 3 at rest. On: text becomes Ink, border Ink 3, knob fills Control Ink with a Control On dot translated 11px. The Sea Lanes pill fills its knob with Sea Lane Teal instead.
- **Hover / Focus:** hover only shifts text to Ink; focus is a 2px Control Ink ring at 2px offset. Transitions 140 to 150ms on colour and transform, 300ms on map opacity, all on the shared ease-out curve (cubic-bezier(.22, 1, .36, 1)).
- **Era dots:** 5px Ink 3 dots under the ruler inside a 24x20px hit area, scaling 1.55x and turning Tribute Gold when current.

### Chips
- **Tag** ("archipelago-wide"): Plex Mono 0.6rem uppercase, Ink 2 text, 1px Paper Line border, 3px radius, 1px 5px padding. Never coloured.
- **Legend swatch:** 12 to 13px block, 2 to 3px radius, hairline in rgba(34,28,20,0.18), filled with the channel colour.

### Cards / Containers
- **Entity card:** Surface 2, 1px Paper Line, 8px radius, 12px 14px padding. Leading 8px category dot before the name. Hover: Surface 3. Selected: 1px Tribute Gold border and a 7% gold wash (rgba(150,105,26,0.07)); the description, confidence line and the three lore answers unfold inline.
- **Overlay block** (rail explainer for a faction layer): 1px border in the channel's accent line (#cfa863), 8px radius, 12px 14px, gold wash background. Grave variant (1965-66): Violence Umber border and a 6% umber tint.
- **Legend panel** (over the map): translucent Paper Surface (rgba(251,248,241,0.92)), 1px Paper Line, 8px radius, 10px 13px. Hidden below 900px because the rail carries the same rows.
- **No nesting.** A card never contains a card; lore answers are keyed by a hanging mono label, not boxed.

### Inputs / Fields
- **Era scrubber:** a transparent native range over an SVG ruler: 1px base line, 0.8px minor and major ticks, dashed scale-break marks, and a Tribute Gold density histogram at 18% (16% on night). Thumb: 15px Tribute Gold Warm circle with the time cursor glow. Focus: ground-offset Control Ink ring.
- There are no text inputs.

### Navigation
- **Header:** Paper Surface with a bottom hairline; eyebrow label in Ink 3, Fraunces title, Inter subtitle in Ink 2 at 54ch. Controls right-aligned in two rows, stacking left-aligned below 900px and full-width below 560px.
- **Rail:** 336px, Paper Surface, left hairline, scrolls internally above 900px; becomes a stacked section with a top hairline below.

### The Influence Bloom
The signature mark. A radial gradient from each capital or port node, radius set by reach, confidence tier and view scale, filled with Sienna Wash on paper (multiply) or Tribute Gold on night (screen). Three gradient definitions, one per tier, differ only in stop opacities so that lower confidence is visibly more diffuse. Nodes are 3px Tribute Gold Warm dots with haloed mono captions. Blooms fade in and out over 300ms; region hover shows a dashed 0.8px gold outline at 7% fill, selection a solid one at 10%.

### Faction Overlays
1945-49 and 1955 zones: 1.1px stroke, 15% fill, in the channel colour; diffuse control uses a 5 4 dash and 9% fill. The Van Mook Line: 1.6px in near-black ink (#2a2116, #e8dcc0 on night) with a 9 3 2 3 dash, the hardest edge on the map. 1965-66: 1px Violence Umber stroke over a hatch pattern, never a bloom. Point events are 1.3px hollow circles with haloed captions.

## 6. Do's and Don'ts

### Do:
- **Do** render every pre-colonial entity as a radial bloom whose diffusion follows its confidence tier. Low confidence must look blurrier.
- **Do** keep the "approximate spheres of influence" caveat visible in both languages on every era view.
- **Do** keep controls in Control Ink and let only the Sea Lanes toggle borrow its layer's teal.
- **Do** pair every coloured channel with a label, dash pattern, hatch or legend row.
- **Do** halo every map caption: rgba(251,248,241,0.92) on paper, rgba(12,11,9,0.90) on night.
- **Do** separate surfaces with 1px Paper Line hairlines and tonal steps, never with shadows.
- **Do** define every token in bare `:root` first, then redefine in the dark media block and in `[data-theme="dark"]`, keeping all three in sync.
- **Do** use OKLCH for any new colour, with chroma reduced near the lightness extremes.

### Don't:
- **Don't** build dashboard chrome: no SaaS panels, card grids, KPI tiles, glass blur, or accent stripes.
- **Don't** assert territory: no filled polygon with a hard border for any pre-colonial polity, no faction colour claiming land, no Majapahit shape drawn as fact.
- **Don't** add heritage decoration: no batik, wayang silhouettes, or flag red-and-white as ornament.
- **Don't** use `border-left` or `border-right` wider than 1px as a coloured accent. Selection is a full hairline plus a wash; category is a leading dot.
- **Don't** put Tribute Gold on a button, knob or focus ring. If it is gold, it is influence or time.
- **Don't** use red for anything but the Republic. PKI is plum, 1965-66 is umber, the modern category is charcoal.
- **Don't** give the 1965-66 layer a bloom or a glow. It is a flat hatch because it is a record, not radiating power.
- **Don't** use pure #000 or #fff anywhere, or a shadow on anything but the scrubber thumb.
- **Don't** set Fraunces in italic, at body size, or as a label.
- **Don't** style text for one language: both Bahasa Indonesia and English must fit every label and caption.
- **Don't** replace inline SVG with canvas; text must stay selectable and translatable.
