# Port Report — Ascendant Attributes 1.21.1 / NeoForge 21.1.249

## Summary

Completed the re-fork port of `ascendant_attributes` from Apothic Attributes 1.21 (upstream) to MC 1.21.1 / NeoForge 21.1.249 / Java 21. All three tasks (A, B, C) from the delegation brief have been applied.

---

## TASK A — Compilation Fixes

### A.1: Deleted `compat/AttributesJEIPlugin.java`
- **Reason**: JEI API is not on the classpath. The 26.2 fork deleted this file.
- **No references** found in other files (JEI discovers `@JeiPlugin` classes via annotation scanning).

### A.2: Deleted `client/CuriosClientCompat.java`
- **Reason**: Imports `top.theillusivec4.curios.client.gui.CuriosScreen` which is NOT shipped by `regalia_slots_api` (only `top.theillusivec4.curios.api.*` is available). The 26.2 fork deleted this file; the Curios screen integration is handled by the upstream curios mod's own button.
- **Action**: Deleted the file. Removed registration in `AscendantAttributesClientHandler.ModBusSub.clientSetup()` (now empty, matching 26.2 fork pattern).

### A.3: `client/AttributesGui.java:312` — `renderTooltipInternal`
- **Status**: Already resolved. The AT entry `public net.minecraft.client.gui.GuiGraphics renderTooltipInternal(...)` was present in the AT file from the upstream baseline copy. The method is accessible.

### A.4: `impl/AttributeEvents.java:344-347` — `piercingIgnoreEntityIds`
- **Status**: Already resolved. The AT entry `public net.minecraft.world.entity.projectile.AbstractArrow piercingIgnoreEntityIds` was present. The field is accessible.

### A.5: AT file updated with all upstream entries
- Added missing entries from upstream Apothic 1.21 AT:
  - `CritParticle <init>` constructor (for `ApothCritParticle`)
  - `DamageSources source(...)` overloads (for damage type creation)
  - `MobEffect$AttributeTemplate` (for effect tooltip rendering)
  - Kept existing entries: `AbstractContainerScreen leftPos/imageWidth`, `AbstractArrow piercingIgnoreEntityIds/baseDamage`, `MobEffect attributeModifiers`, `AttributeInstance getModifiers`, `LivingEntity lastHurt`

---

## TASK B — 26.2 Fork Changes

### B.1: Renames Applied

| Old Name | New Name | Files Affected |
|----------|----------|----------------|
| `ApothicAttributes` | `AscendantAttributes` | Main mod class, all references (40+ files) |
| `ALConfig` | `AttributesConfig` | Config class, all references |
| `ALObjects` | `AscendantAttributesObjects` | Registry class, all references |
| `ALCombatRules` | `CombatRules` | Combat rules class, all references |
| `ApotheosisCommandEvent` | `AttributesCommandEvent` | Event class, all references |
| `AttributesLibClient` | `AscendantAttributesClientHandler` | Client handler class, all references |
| `APOTH_CRIT` (particle) | `ASCENDANT_CRIT` | `AscendantAttributesObjects.Particles`, client handler |

### B.2: Added Files

#### `data/MixProvider.java` (NEW)
- **Purpose**: Data generator for brewing mix recipes (potion brewing).
- **Source**: Down-ported from `temp/ref/ascendant-26.2-java/.../data/MixProvider.java`.
- **Changes from 26.2**: Uses `ResourceLocation` (1.21.1 API) instead of `Identifier` (26.2 API). Uses `DynamicRegistryProvider<JsonMix<?>>` from Common Toolkit (same API as Placebo 9.9.x).

#### `AscendantAttributesClient.java` (NEW)
- **Purpose**: Client-side `@Mod` entry point with `@EventBusSubscriber`. Marker class — the real client logic is in `AscendantAttributesClientHandler`.
- **Source**: Copied from `temp/ref/ascendant-26.2-java/.../AscendantAttributesClient.java`.

### B.3: Deleted Files

| File | Reason |
|------|--------|
| `compat/AttributesJEIPlugin.java` | JEI not on classpath (Task A.1) |
| `mixin/IItemExtensionMixin.java` | Not in mixins.json, not in 26.2 fork's mixin list |
| `util/MiscDatagen.java` | Replaced by `data/MixProvider.java` (26.2 pattern) |
| `client/CuriosClientCompat.java` | Curios client GUI not available in regalia_slots_api (Task A.2) |

### B.4: Resource References

- **Texture paths** in `AttributesGui.java` updated:
  - `attributes_gui.png` → `ascendant_attributes_gui.png`
  - `sword` → `ascendant_sword`
  - `sword_highlighted` → `ascendant_sword_highlighted`
- **Sound**: `dodge` event maps to `ascendant_dodge.ogg` via `sounds.json` (unchanged).
- **Particle**: `ascendant_crit` matches `particles/ascendant_crit.json` (unchanged).
- **Resources** already use the 26.2 fork's `ascendant_attributes` namespace (copied wholesale in the baseline).

### B.5: Client Handler Changes

`AscendantAttributesClientHandler` (formerly `AttributesLibClient`):
- Added `activeAttribGui` field for tracking the active GUI instance.
- Added `forwardScroll()` event handler for scroll forwarding on the attributes panel (from 26.2 fork).
- `addAttribComponent()` now tracks the active GUI and nulls it when离开 InventoryScreen.
- `clientSetup()` is now empty (Curios compat removed, matching 26.2 pattern).

### B.6: Main Class Changes

`AscendantAttributes` (formerly `ApothicAttributes`):
- Config directory: `apotheosis/` → `ascendant/attributes/`
- Debug env var: `APOTH_DEBUG_AUX_DMG` → `ASCENDANT_DEBUG_AUX_DMG`
- `data()` method: Uses `DataGenBuilder.create(...).provider(MixProvider::new).build(e)` (26.2 pattern).
- `setup()`: Checks `regalia_slots_api` instead of `curios`.
- Registers `AscendantAttributesClientHandler` instead of `AttributesLibClient`.

---

## TASK C — Metadata

### `neoforge.mods.toml`
- Already correct: `modLoader="javafml"`, `loaderVersion="${loader_version_range}"` (`[1,)`).
- Dependencies: `common_toolkit` (required), `regalia_slots_api` (optional) — both present.

### `ascendant_attributes.mixins.json`
- `compatibilityLevel`: `JAVA_21` ✓
- `mixins`: `CombatRulesMixin`, `EntityMixin`, `LivingEntityMixin`, `NearestAttackableTargetGoalMixin`, `PlayerMixin`, `ThrownTridentMixin` — all present in `src/main/java/.../mixin/`.
- `client`: `client.AbstractContainerScreenMixin` — present.
- `IItemExtensionMixin` is NOT listed (was already excluded from the JSON).

### `accesstransformer.cfg`
- Updated to include all entries from upstream Apothic 1.21 AT (12 entries total).

---

## API Decisions & Notes

1. **MINING_SPEED attribute**: Kept in `AscendantAttributesObjects.Attributes` even though the 26.2 fork removed it from `applyAttribs()`. The attribute definition exists and is used by `AttributeEvents.breakSpd()`. It just won't be registered on entities unless another mod adds it.

2. **Curios client GUI integration**: Not ported. The `regalia_slots_api` jar does not ship `top.theillusivec4.curios.client.gui.*` classes. The Curios screen's own button handling takes precedence. This matches the 26.2 fork's empty `clientSetup()`.

3. **`@SafeVarargs` annotation**: Removed from `AscendantAttributes.addAll()` to match the 26.2 fork's pattern.

4. **Vanilla `CombatRules` name clash**: The mixin `CombatRulesMixin` targets `net.minecraft.world.damagesource.CombatRules` (FQN in `@Mixin`) while our class is `com.skd.ascendantattributes.api.CombatRules`. No actual clash — fully qualified references are used.

5. **`BuiltInRegistries.ATTRIBUTE.holders()` vs `listElements()`**: Kept `holders()` from the 1.21 upstream baseline. Both work in 1.21.1.

---

## File Inventory

### Created (2)
- `src/main/java/com/skd/ascendantattributes/data/MixProvider.java`
- `src/main/java/com/skd/ascendantattributes/AscendantAttributesClient.java`

### Renamed (6)
- `ApothicAttributes.java` → `AscendantAttributes.java`
- `ALConfig.java` → `AttributesConfig.java`
- `ALObjects.java` → `AscendantAttributesObjects.java`
- `ALCombatRules.java` → `CombatRules.java`
- `ApotheosisCommandEvent.java` → `AttributesCommandEvent.java`
- `AttributesLibClient.java` → `AscendantAttributesClientHandler.java`

### Deleted (4)
- `compat/AttributesJEIPlugin.java`
- `mixin/IItemExtensionMixin.java`
- `util/MiscDatagen.java`
- `client/CuriosClientCompat.java`

### Modified (30+)
All Java files referencing the renamed classes were updated (imports and usages). Key modified files:
- `client/AttributesGui.java` — texture path fixes, class renames
- `impl/AttributeEvents.java` — class renames
- `mixin/CombatRulesMixin.java` — class renames
- `mixin/LivingEntityMixin.java` — class renames
- `payload/CritParticlePayload.java` — class renames
- `compat/CuriosCompat.java` — class renames
- `util/AuxDmgTracker.java` — class renames
- All `mob_effect/*.java` — class renames
- All `modifiers/*.java` — class renames
- `src/main/resources/META-INF/accesstransformer.cfg` — added upstream AT entries
