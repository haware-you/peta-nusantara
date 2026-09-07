// Replaces a SOURCES entity's placeholder image with a real, downloaded file.
//
// Usage: node tools/embed-image.js <path-to-image-file> "<entity name.id key>"
// Example: node tools/embed-image.js ~/Downloads/nagarakertagama.jpg "Majapahit"
//
// The entity key must match a name.id used in DATA/MAPX/SOURCES in
// src/peta.template.html exactly (Indonesian name, as it appears there).
//
// This only swaps base64 image data. It does not check or set the license —
// verify the file's actual license and credit line on its source page
// yourself and update the `license`/`license_confirmed`/`credit` fields in
// the SOURCES entry by hand if they need correcting.

const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..');
const TPL = path.join(ROOT, 'src', 'peta.template.html');

const [, , imagePath, entityKey] = process.argv;

if (!imagePath || !entityKey) {
  console.error('Usage: node tools/embed-image.js <image-file> "<entity name.id key>"');
  process.exit(1);
}

if (!fs.existsSync(imagePath)) {
  console.error('No such file: ' + imagePath);
  process.exit(1);
}

const ext = path.extname(imagePath).toLowerCase();
const MIME = { '.jpg': 'image/jpeg', '.jpeg': 'image/jpeg', '.png': 'image/png', '.webp': 'image/webp' };
const mime = MIME[ext];
if (!mime) {
  console.error('Unsupported extension "' + ext + '" — expected .jpg, .jpeg, .png, or .webp.');
  process.exit(1);
}

const bytes = fs.readFileSync(imagePath);
const dataUri = 'data:' + mime + ';base64,' + bytes.toString('base64');

let tpl = fs.readFileSync(TPL, 'utf8');

// Find this entity's IMG(...) call and replace only its first argument (the
// image data). Every entity currently passes the shared PLACEHOLDER_IMG
// variable there; swap in an inline data URI for just this one. Matching the
// first argument — PLACEHOLDER_IMG, or a prior 'data:...' string from a
// re-embed — keeps a plain PLACEHOLDER_IMG replace from hitting every entity.
const keyEsc = entityKey.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
const entryRe = new RegExp("('" + keyEsc + "':\\s*\\{[\\s\\S]*?image:\\s*IMG\\()(PLACEHOLDER_IMG|'data:[^']*')");
if (!entryRe.test(tpl)) {
  console.error('Could not find a SOURCES["' + entityKey + '"] entry with an IMG(...) image slot.');
  console.error('Check the key matches name.id exactly, and that this entity has image: IMG(...), not image: null.');
  process.exit(1);
}

tpl = tpl.replace(entryRe, function(_, prefix){
  return prefix + "'" + dataUri + "'";
});

fs.writeFileSync(TPL, tpl, 'utf8');
console.log('Embedded ' + path.basename(imagePath) + ' (' + (bytes.length / 1024).toFixed(1) +
  ' KB) into SOURCES["' + entityKey + '"].image. Run `npm run build` to regenerate peta-nusantara.html.');
