# Membangun dan menjalankan Garis Waktu Nusantara.
#
# Memakai Azul Zulu FX, JDK yang sudah memuat modul JavaFX, sehingga tidak perlu
# --module-path terpisah.

$ErrorActionPreference = "Stop"
$jdk = "C:\Program Files\Zulu\zulu-21"

if (-not (Test-Path $jdk)) {
    throw "Zulu FX JDK tidak ditemukan di $jdk. Pasang dengan: winget install Azul.ZuluFX.21.JDK"
}

Set-Location $PSScriptRoot

if (Test-Path out) { Remove-Item out -Recurse -Force }

$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
& "$jdk\bin\javac.exe" -d out $sources
if (-not $?) { throw "Kompilasi gagal." }

Copy-Item -Path "src\main\resources\*" -Destination "out" -Recurse -Force

& "$jdk\bin\java.exe" -cp out id.nusantara.ui.TimelineApp
