# Ascendant Attributes (1.21.1) — Changelog

Branch `minecraft/1.21.1/neoforge-21.1.249/production`. History independent of the 26.2 branch.

## [0.0.0-beta.2] - 2026-08-31

### Fixed

- **Client crash on startup**: `AscendantAttributesClient` had `@EventBusSubscriber` but no `@SubscribeEvent` methods, causing `IllegalArgumentException` during mod loading.

### Technical

- Removed unused `@EventBusSubscriber` from client entry class — event handling is registered via NeoForge bus elsewhere.

## [0.0.0-beta.1] - 2026-08-31

### Added

- **Initial port to Minecraft 1.21.1 / NeoForge 21.1.249** (Java 21). Strategy: **re-fork**
  from the upstream Apothic Attributes 1.21 sources (2.10.1, NeoForge 21.1.235), rebranded to
  the Ascendant Attributes identity — not a back-port of the 26.2 fork.
- Feature parity with the 26.2 line: 20 custom attributes, 7 status effects, the Shift-to-inspect
  attribute GUI, EvalEx-driven configurable combat formulas, and Regalia Slots API (Curios) compat.

### Technical

- Package `dev.shadowsoffire.apothic_attributes` → `com.skd.ascendantattributes`; modid
  `apothic_attributes` → `ascendant_attributes`; class `Apothic*`/`AL*` → project convention
  (`AscendantAttributes`, `AttributesConfig`, `CombatRules`, …).
- Dependency **Placebo → Common Toolkit** (`com.skd.commontoolkit`); **Curios → Regalia Slots
  API** (optional). Both consumed from `libs/`.
- Upstream's repacked EvalEx (`repack/evalex`) dropped in favour of `jarJar com.udojava:EvalEx:2.7`.
- Dropped `compat/AttributesJEIPlugin` (no JEI on classpath — matches the 26.2 fork),
  `mixin/IItemExtensionMixin`, `util/MiscDatagen`.
- Build: workspace `net.neoforged.moddev` template retargeted to NeoForge 21.1.249 / Java 21;
  `modLoader`/`loaderVersion` added to `neoforge.mods.toml`; mixins `JAVA_21`.
- Verified: `./gradlew build` OK; `./gradlew runServer` reaches `Done`, `ascendant_attributes`
  + `common_toolkit` + `regalia_slots_api` all load, no mixin/registration errors.
- Port detail: `docs/PORT_REPORT_1.21.1.md`.
