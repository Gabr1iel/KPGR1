# ✅ TODO – PGF2 Úloha 1 (Grafika)

## 🔷 Zobrazení těles

* [x] Koule (definovaná středem a poloměrem)
* [x] Krychle (čtyřstěn)
* [x] Válec
* [x] Kužel
* [x] Netriviální vícestěn (Ikosaédr D20 + geodetický D100)
* [x] Komolý jehlan / kužel

---

## 🔷 Reprezentace

* [x] Topologie (vrcholy, hrany, plochy) – VB (Vertex), IB (hrany/trojúhelníky), PB (Part+TopologyType)
* [x] Geometrie – Point3D v Vertex
* [x] Rozšířený vertex – třída Vertex (position + color) s Vectorizable pro interpolaci
* [x] Možnost ukládání hran a ploch – IB sdílený buffer, Part rozděluje LINES / TRIANGLES

---

## 🔷 Transformace těles (klávesnice)

* [x] Translance
* [x] Rotace (správně kolem jednotlivých os)
* [x] Zoom (scale)
* [x] Výběr aktivního tělesa

---

## 🔷 Kamera

* [x] Rozhlížení myší (azimut, zenit)
* [x] Pohyb (WASD)

---

## 🔷 Projekce (klávesa P)

* [x] Pravoúhlá
* [x] Perspektivní

---

## 🔷 Rasterizace

* [x] Hrany – LineRasterizerBresenhamZ (z-test)
* [x] Plochy – TriangleRasterizer (scanline + z-buffer)

---

## 🔷 Řešení viditelnosti

* [x] Z-buffer pro hrany – LineRasterizerBresenhamZ
* [x] Z-buffer pro plochy – TriangleRasterizer + ZBuffer

---

## 🔷 Ořezání (clipping)

* [x] Rychlé ořezání zobrazovacím objemem
* [x] Ořezání – rozklad úseček/trojúhelníků (Liang-Barsky lines + Sutherland-Hodgman triangles)
* [x] Ořezání při rasterizaci (pixel bounds check v rasterizérech)

---

## 🔷 Režimy zobrazení

* [x] Přepínání drátového modelu / vyplněných ploch (M)

---

## 🔷 Zobrazení povrchu

* [x] Jednobarevné plochy
* [x] Interpolace barev (po vrcholech)
* [x] Mapování textur
* [x] Zapnutí/vypnutí textury na aktivním tělese

---

## 🔷 Zobrazení os

* [x] RGB osy (šipka = 1 hrana + 1 trojúhelník)

---

## 🔷 Osvětlení

* [x] Ambientní složka
* [x] Difuzní složka
* [x] Vizualizace světla (koule)
* [x] Barva světla = difuzní barva
* [x] Světlo jako aktivní těleso (možnost transformace)

---

## 🔷 Odevzdání

* [ ] Export aplikace v požadovaném formátu

---

## 🔷 GitLab

* [x] Vytvořený privátní repozitář
* [x] Pravidelné commitování

---

# ⭐ Bonusy (optional)

* [x] Animace světla v čase
* [x] Jiná topologie než seznam trojúhelníků/hran
* [x] Shader interface
* [x] Perspektivně korektní interpolace
* [x] Spekulární složka osvětlení
* [x] Osvětlené těleso z kubických ploch

---

## 🔷 Vlastní rozšíření

* [x] UI (infopanel, buttony, combobox)
