# Delegation brief — Ascendant Attributes: finish the 1.21.1 / NeoForge 21.1.249 port

## Mission

`ascendant_attributes` currently exists only for Minecraft 26.2. We are creating a
**Minecraft 1.21.1 / NeoForge 21.1.249** version. Strategy: **re-fork from the
upstream original** (`Apothic Attributes` 2.10.1, branch `1.21`, MC 1.21.1,
NeoForge 21.1.235, Java 21), then **carry over anything the 26.2 fork added or
changed on top of it**. The 26.2 repo is a reference for "what the fork did to
Apothic Attributes" — never copy 26.2-specific API into this port.

A mechanical baseline is already in `src/main/java` (upstream Apothic 1.21,
renamed). It does **not** compile yet — **34 errors**, see below.

## Paths (all inside the work dir — sandbox blocks outside reads)

| What | Path |
|---|---|
| **Work dir** (edit here) | `G:/Proyectos/Mods_Minecraft/ascendant_attributes/neoforge/1.21.1` |
| Upstream Apothic Attributes 1.21 — Java (1.21.1 API truth) | `temp/ref/apothic-1.21-java/dev/shadowsoffire/apothic_attributes/` |
| Upstream Apothic 1.21 — resources / build.gradle / gradle.properties | `temp/ref/apothic-1.21-resources/`, `temp/ref/apothic-1.21-build.gradle`, `temp/ref/apothic-1.21-gradle.properties` |
| The **26.2 fork** — Java (identity + "what to add" reference, NOT API) | `temp/ref/ascendant-26.2-java/com/skd/ascendantattributes/` |
| 26.2 fork — resources / build.gradle | `temp/ref/ascendant-26.2-resources/`, `temp/ref/ascendant-26.2-build.gradle` |
| 26.1 Apothic (the source the 26.2 fork was made from) | `temp/ref/apothic-26.1-java/` |

`temp/` is gitignored — reference only.

## What the baseline already did (mechanically)

- Copied upstream Apothic 1.21 `src/main/java` (minus the `repack/evalex/` package —
  the 26.2 fork uses the real `com.udojava:EvalEx:2.7` via `jarJar`, kept here).
- Package `dev.shadowsoffire.apothic_attributes` -> `com.skd.ascendantattributes`.
- Placebo dependency renamed in imports: `dev.shadowsoffire.placebo` ->
  `com.skd.commontoolkit` (our fork Common Toolkit — jar is in `libs/`), `Placebo`
  -> `CommonToolkit`, `PlaceboClient` -> `CommonToolkitClient`.
- modid `apothic_attributes` -> `ascendant_attributes` (string literals,
  translation-key prefixes `"apothic_attributes." -> "ascendant_attributes."`,
  asset/data paths).
- Build: workspace `net.neoforged.moddev` template retargeted to MC 1.21.1 /
  NeoForge 21.1.249 / Java 21; `libs/common_toolkit-1.21.1-…jar` +
  `libs/regalia_slots_api-1.21.1-…jar` wired as `compileOnly`+`localRuntime`;
  `jarJar EvalEx:2.7`; `modLoader`/`loaderVersion` added to
  `src/main/templates/META-INF/neoforge.mods.toml`. `mixins.json` at `JAVA_21`.

## HARD CONSTRAINTS

1. **Target API = Minecraft 1.21.1 + NeoForge 21.1.249 + Java 21.** Upstream
   Apothic 1.21 (`temp/ref/apothic-1.21-java`) is the API truth. Do NOT introduce
   26.2-only API from `temp/ref/ascendant-26.2-java` (e.g. the 26.x
   equipment-slot API — 1.21.1 has the plain `net.minecraft.world.entity.EquipmentSlot`
   enum; the 26.2 fork's `modifiers/` shim package is a 26.x artefact and is **not
   needed** here unless a surviving call requires it — prefer the upstream 1.21
   code path).
2. **Dependencies**: required `common_toolkit`, optional `regalia_slots_api`. Do
   NOT add new library deps or bump anything. `common_toolkit` 1.21.1 is a re-fork
   of Placebo 9.9.2, so its API matches Placebo 9.9.x (what Apothic 1.21 expects) —
   use `com.skd.commontoolkit.*` where upstream used `dev.shadowsoffire.placebo.*`.
3. **Do NOT run git or gradle.** The operator builds and verifies.
4. Preserve upstream license headers verbatim (MIT; the config code carries an
   LGPL-2.1 header inherited via Placebo — keep it). Do NOT rename `Apothic` /
   `Apotheosis` inside license headers or Javadoc that describes the upstream
   project.
5. All code, comments, your report: **English**.

## TASK A — make it compile (34 errors, ~5 clusters)

1. **`compat/AttributesJEIPlugin.java`** — needs the JEI API, which is not on the
   classpath. The **26.2 fork deleted this file**. Delete it here too, and remove
   any reference to it (it's a `@JeiPlugin` discovered by JEI, unlikely to be
   referenced elsewhere — confirm).
2. **`client/CuriosClientCompat.java`** — imports `top.theillusivec4.curios.client.gui.*`.
   Our `regalia_slots_api` jar ships a verbatim `top.theillusivec4.curios.api`
   package but **not** the `curios.client.*` GUI classes. Compare with the 26.2
   fork's Curios compat (`temp/ref/ascendant-26.2-java/.../client/AscendantAttributesClientHandler.java`,
   `.../compat/CuriosCompat.java`, `.../compat/CurioEquipmentSlot.java`): port that
   approach to 1.21.1, using only the API surface `regalia_slots_api` 1.21.1
   actually exposes (`top.theillusivec4.curios.api.*` + `com.skd.regaliaslotsapi.*`).
   If a client GUI hook genuinely isn't available, guard it / drop it and note it.
3. **`client/AttributesGui.java:312`** — `GuiGraphics#renderTooltipInternal` is
   private. Use the public 1.21.1 tooltip API (`GuiGraphics#renderTooltip(Font, List<Component>, Optional<TooltipComponent>, int, int)`
   or `renderComponentTooltip`), or add a narrow access-transformer entry in
   `src/main/resources/META-INF/accesstransformer.cfg` if the 26.2 fork did that
   (check its AT file).
4. **`impl/AttributeEvents.java:344-347`** — `AbstractArrow#piercingIgnoreEntityIds`
   is private. Check the 26.2 fork: it either has an AT entry
   (`public net.minecraft.world.entity.projectile.AbstractArrow piercingIgnoreEntityIds`)
   or routes through `mixin/ThrownTridentMixin` / an accessor. Mirror whichever it
   does, on 1.21.1.
5. Any remaining `cannot find symbol` against `com.skd.commontoolkit.*` — the
   method/class exists in Common Toolkit 1.21.1 under a slightly different name
   (it dropped a few Placebo subsystems). Check
   `libs/common_toolkit-1.21.1-…jar` contents (or the CT 1.21.1 repo if referenced)
   and adjust the call.

## TASK B — carry over the 26.2 fork's changes

Diff the renamed baseline against `temp/ref/ascendant-26.2-java`. The 26.2 fork:

- **Renamed** (apply the same renames here so the identity matches):
  `ApothicAttributes` (main `@Mod` class) -> `AscendantAttributes`;
  `client/AttributesLibClient` -> `AscendantAttributesClient`;
  `ALConfig` -> `AttributesConfig`; `api/ALCombatRules` -> `api/CombatRules`;
  `api/ALObjects` -> `api/AscendantAttributesObjects`;
  `event/ApotheosisCommandEvent` -> `event/AttributesCommandEvent`. Rename the
  file, the class, the constructor and every reference; update `mixins.json` and
  `neoforge.mods.toml` (`@Mod` id class) accordingly. Keep the `AL*` -> project
  names consistent with the 26.2 tree.
- **Added** (port these from `temp/ref/ascendant-26.2-java`, on 1.21.1 API):
  `data/MixProvider.java` (datagen provider for the crit particle mix),
  `client/AscendantAttributesClientHandler.java`, and anything else present in the
  26.2 tree but not in the baseline. For each, take the 26.2 file and down-port
  its API to 1.21.1 using the upstream Apothic 1.21 siblings as the reference.
- **Removed**: `compat/AttributesJEIPlugin` (Task A.1), `mixin/IItemExtensionMixin`,
  `util/MiscDatagen` — confirm against the 26.2 tree and drop the same set, fixing
  references.
- **Resources**: the 26.2 fork renamed assets (`apoth_crit` -> `ascendant_crit`,
  `dodge.ogg` -> `ascendant_dodge.ogg`, `attributes_gui.png` ->
  `ascendant_attributes_gui.png`, mob-effect sprites moved under
  `gui/sprites/mob_effect/`, etc.) and rewrote the lang files with
  `ascendant_attributes.*` keys. The baseline `src/main/resources` is **already the
  26.2 fork's resources** (copied wholesale). Make sure every `ResourceLocation` /
  translation key / model reference in the Java code points at those 26.2 asset
  names, not the upstream `apothic_attributes` names. Cross-check
  `temp/ref/ascendant-26.2-resources` for the exact names.

## TASK C — metadata

- `src/main/templates/META-INF/neoforge.mods.toml`: verify deps
  (`common_toolkit` required, `regalia_slots_api` optional) and that
  `modLoader`/`loaderVersion` (`${loader_version_range}` -> `[1,)`) are present.
- `src/main/resources/ascendant_attributes.mixins.json`: `mixins` / `client`
  arrays must match the files actually in
  `src/main/java/com/skd/ascendantattributes/mixin/`. `compatibilityLevel`
  `JAVA_21`.
- `src/main/resources/META-INF/accesstransformer.cfg`: add the entries Task A
  needs (compare with the 26.2 fork's AT file).

## Deliverable

1. Tasks A/B/C applied under `src/`.
2. Correct for `./gradlew build` on NeoForge 21.1.249 by inspection.
3. `docs/PORT_REPORT_1.21.1.md` (English): every file created / renamed / deleted /
   modified with the reason; anything where you guessed at the 1.21.1 API; any
   26.2 feature intentionally not ported.

Work only inside `G:/Proyectos/Mods_Minecraft/ascendant_attributes/neoforge/1.21.1`.
