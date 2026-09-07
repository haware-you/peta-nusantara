// Downloads the Natural Earth 50m admin-0 countries file into data/.
//
// Natural Earth is public domain (see LICENSE note in README). GADM is *not*
// free for commercial use — if you ever swap the source, re-check the licence
// before anything monetised ships. (PRD risk R-5.)

const fs = require('fs');
const path = require('path');

const URL =
  'https://raw.githubusercontent.com/nvkelso/natural-earth-vector/master/geojson/ne_50m_admin_0_countries.geojson';

const DATA = path.join(__dirname, '..', 'data');
const OUT = path.join(DATA, 'ne50.geojson');

if (fs.existsSync(OUT)) {
  console.log('data/ne50.geojson already present — skipping download');
  process.exit(0);
}

fs.mkdirSync(DATA, { recursive: true });

console.log('fetching Natural Earth 50m ...');
fetch(URL)
  .then((res) => {
    if (!res.ok) throw new Error('HTTP ' + res.status);
    return res.arrayBuffer();
  })
  .then((buf) => {
    fs.writeFileSync(OUT, Buffer.from(buf));
    console.log('wrote data/ne50.geojson (' + (buf.byteLength / 1048576).toFixed(1) + ' MB)');
  })
  .catch((err) => {
    console.error('download failed:', err.message);
    process.exit(1);
  });
