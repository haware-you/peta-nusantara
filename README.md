# Nusantara

Sejarah kerajaan Nusantara sebagai data Java.

## Keputusan tata letak

Berkas dipisah **per pulau**, bukan per periode. Alasannya: sebuah kerajaan
selalu berada di satu tempat, tetapi hampir tidak pernah muat di satu periode --
Majapahit (1293-1527) melintasi batas era mana pun yang kita tarik. Memisahkan
berkas berdasarkan waktu akan memaksa duplikasi atau pilihan sembarang.

Waktu tetap ada, tetapi sebagai **field**, bukan sebagai struktur berkas.
`timeline/` menyediakan sumbu kronologis di atas partisi geografis itu.

Peristiwa berada di paketnya sendiri (`events/`) karena kerap melibatkan lebih
dari satu polity -- kadang lebih dari satu pulau. Perang Bubat adalah contoh
utamanya: tidak ada satu berkas pulau yang bisa memilikinya sendiri. Peristiwa
merujuk peserta lewat id (`Kingdom#getId()`), bukan lewat referensi langsung.

## Struktur

    id/nusantara/
      model/      Island, Era, YearRange, Kingdom, Ruler, Source
      data/       satu berkas per pulau (JawaData, SumatraData, ...)
      events/     HistoricalEvent, Account, EventType, kumpulan peristiwa
      registry/   NusantaraRegistry -- titik masuk tunggal
      timeline/   Timeline + TimelineWindow (state slider)

## Menambah data

- Kerajaan baru: tambahkan ke berkas pulaunya.
- Pulau baru: satu konstanta `Island`, satu kelas `*Data`, satu baris di
  `NusantaraRegistry.load()`.
- Peristiwa baru: tambahkan ke `events/`; jika melintasi pulau, ke
  `NusantaraEvents`.

## Sumber yang berselisih

`HistoricalEvent` menyimpan beberapa `Account`, masing-masing terikat pada satu
`Source` dan satu sudut pandang. Peristiwa dengan lebih dari satu account
otomatis ditandai `isContested()`. Bubat memuat tiga: Pararaton, Kidung Sunda,
dan Carita Parahyangan -- ketiganya tidak sepakat soal motif.

## Antarmuka

`id.nusantara.ui` berisi antarmuka JavaFX: pita era dan peta kecil dengan kotak
pilihan yang bisa diseret, jalur per pulau berisi batang polity, titik peristiwa,
penelusur tahun, serta panel rincian dengan tab per sumber.

Beberapa keputusan tampilannya sengaja menyimpang dari papan analitik biasa:

- **Tidak ada histogram.** Tinggi batang di papan analitik berarti "seberapa
  ramai"; di sini kepadatan hanya menandakan sumber yang selamat. Pita era
  dipakai sebagai gantinya.
- **Batang, bukan hanya titik.** Sebagian besar data berupa rentang, sehingga
  lapisan utamanya batang bergaya Gantt; peristiwa sesaat menjadi titik di atasnya.
- **Tepi memudar** untuk rentang bertanda `c.`, membedakan tanggal perkiraan dari
  tanggal pasti tanpa satu kata penjelasan.
- **Lingkaran putus-putus** untuk peristiwa yang sumbernya berselisih; setiap
  versi mendapat tabnya sendiri.

Warna pulau dan jenis peristiwa hidup di `ui/Palette.java` karena bergantung pada
data; token netral ada di `ui/nusantara.css`.

## Menjalankan

Antarmuka membutuhkan JavaFX. Cara termudah adalah Azul Zulu FX, yaitu JDK yang
sudah memuat modulnya:

    winget install Azul.ZuluFX.21.JDK
    .\run.ps1

Atau secara manual:

    javac -d out $(find src/main/java -name '*.java')
    cp -r src/main/resources/* out/
    java -cp out id.nusantara.ui.TimelineApp    # antarmuka
    java -cp out id.nusantara.Main              # ringkasan di terminal
