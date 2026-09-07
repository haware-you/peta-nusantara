# Product

## Register

brand

## Users

One primary user: the author, studying. At a desk in the evening, lamp on, this map open beside *Garis Waktu Nusantara* (the bilingual timeline it accompanies), scrubbing back and forth to check where a polity sat, why it sat there, and how sure anyone is about it. Bahasa Indonesia and English are equal first-class languages, switched mid-session.

Secondary: anyone the author shares the link with. They arrive cold, often on a phone, without the timeline open. The page must still make its argument on its own.

The job to be done is understanding, not lookup. "Sumatra" on the timeline is a column label; here it has to become a place with a strait in front of it.

## Product Purpose

An interactive historical map of the Indonesian archipelago, 300 CE to now, with a deep-time prologue. It exists because the timeline answers *when* well and *where* badly, and because the mechanism behind most of these polities is maritime position, which a column chart cannot show.

It also exists to make one honest cartographic claim: pre-colonial Nusantara borders are largely unknowable. Mandala states were spheres of influence, not territories. So influence renders as a bloom fading from a capital, confidence tier controls how diffuse the bloom is, and only surveyed administrative lines (the VOC, the Dutch East Indies, the Van Mook Line) get a hard edge. Scrubbing across 1600 and watching fuzzy influence give way to a crisp line is the argument.

Success: a visitor leaves understanding that these powers were explained by position, and that uncertainty is a property of the record, not a footnote. Recall of names and dates is a welcome side effect, not the goal.

## Brand Personality

Careful, cartographic, honest.

Voice of a hydrographic chart or a good atlas plate: it states what is known in ink, shows what is inferred as a wash, and says out loud where the record runs out. Never breathless, never decorative. Bilingual throughout, with the caveat present in both languages on every view.

Emotional goal: the quiet satisfaction of a thing finally making sense. Ternate matters because cloves grew there. Palembang matters because it is upstream of the strait's mouth. The map should feel like it is explaining, not performing.

References: Times Atlas plates, 19th-century Dutch sea charts, engraved rulers and mono captions, colour only where it encodes a legend entry.

## Anti-references

- **Dashboard chrome.** SaaS panels, card grids, KPI tiles, glass blur, accent stripes. This is a chart plate, not an admin tool. The rail explains; it does not report metrics.
- **Territory assertion**, derived from the map's own binding rules rather than chosen as a taste: filled polygons with hard borders for pre-colonial polities, faction colours claiming land, the familiar Majapahit shape drawn as fact. The New Order's cartography of the PKI as a territorial enemy is the specific error to avoid.
- **Heritage decoration.** Batik, wayang silhouettes, flag red-and-white as ornament. Cultural reading comes from the content and the two languages, not from the palette.

## Design Principles

1. **Uncertainty is a visual property.** Low confidence renders blurrier. A blank region at every year says something about the historiography, and gets its own empty state. Nothing about doubt lives only in a footnote.
2. **Every hue is a legend entry.** Gold means influence and the time cursor. Teal means sea lanes. Violet means surveyed colonial edges. The faction and violence layers own their own channels. Controls are ink so that no UI accent can be mistaken for a claim on the map.
3. **Edges match the evidence.** Bloom for tribute and reach; dashed lines for guerrilla zones and party support; a hard surveyed edge only where a boundary was actually negotiated and mapped.
4. **Explain the position, not just the state.** Selecting a polity answers why here, how power worked, and how it ended, in that order, because that is the order a learner needs them.
5. **Two views, one dataset.** The map must read as a sibling of the timeline: shared entity records, shared category colours, shared paper. Divergence surfaces as a build warning, never as a quiet disagreement.

## Accessibility & Inclusion

- WCAG 2.2 AA: 4.5:1 for text, 3:1 for UI components and map marks, in both the paper and dark themes.
- Colour never carries meaning alone. Every coloured channel is paired with a label, a hatch, a dash pattern, or a legend row; the 1965-66 layer is a flat hatch precisely so it is not read as influence.
- Keyboard-first map: every region and era is reachable by Tab, Enter and the arrow keys, with visible focus rings on both grounds. The rail copy promises this and the page must keep the promise.
- `prefers-reduced-motion` collapses bloom and rail transitions.
- Both languages are complete, not one translated afterward. Text stays selectable and translatable; inline SVG, never canvas, for that reason.
- Text halos on map labels in both themes, so small mono captions survive over land, ocean and bloom.
