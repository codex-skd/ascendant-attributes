# Mapa de renombrado — Ascendant Attributes (Fase 0)

> Generado decompilando `ApothicAttributes-26.1.2-3.0.1.jar` con Vineflower 1.12.0 (mismo decompilador que usa el propio NeoForge MDK, ya en la caché de Gradle) a `temp/apothic_attributes-src/` (no versionado). 47 archivos `.java`, package raíz `dev.shadowsoffire.apothic_attributes`.

## Corrección importante sobre el roadmap anterior

La primera versión de `ROADMAP_ASCENDANT_ATTRIBUTES.md` asumía que el jar traía embebidas clases de `dev.shadowsoffire.apotheosis.*` (sistema de afijos compartido) y que habría que decidir una dependencia con Ascendant Equipment antes de la Fase 3. **Eso era incorrecto** — verificado con `unzip -l` sobre el jar real: **cero clases, assets o data bajo el namespace `apotheosis`**. El mod es completamente autocontenido: su único mod dependency real es `placebo` (→ `common_toolkit`), más la integración opcional con `curios`. No hay ninguna dependencia pendiente con Ascendant Equipment que resolver — se elimina esa sección del roadmap.

(El error venía de contenido residual de una extracción anterior de `Apotheosis-26.1.2-9.0.3.jar` en el mismo directorio temporal de una sesión previa, mal atribuido a este jar. Verificado y corregido en esta sesión.)

## Librería repackaged identificada

`dev/shadowsoffire/apothic_attributes/repack/evalex/Expression.java` (+ 40 clases internas/anónimas `Expression$*`) es una copia repackaged de **EvalEx** (`com.udojava:EvalEx`, evaluador de expresiones matemáticas Java, API de una sola clase `Expression` con `Operator`/`Function`/`Tokenizer` internos — coincide con la EvalEx "clásica" de uklimaschewski, no con la reescritura moderna de ezylang). Se usa en `ALConfig` para evaluar fórmulas configurables (ej. curvas de reducción de daño). **No se porta manualmente** — se añade como dependencia real de Gradle (`implementation 'com.udojava:EvalEx:<version>'`, confirmar última versión estable en Maven Central antes de la Fase 1) en vez de mantener 41 clases de una librería de terceros bajo nuestro paquete.

## Dependencias externas confirmadas (todas con Gradle cache local ya poblada)

| Import original | Origen | Resolución |
|---|---|---|
| `dev.shadowsoffire.placebo.registry.DeferredHelper` | Placebo | **`com.skd.commontoolkit.registry.DeferredHelper`** — API 1:1 confirmada (`attribute`, `attachment`, `registry`, `component`, `effect`, `particle`, `sound`, `singlePotion`, `custom`, `customDH`, mismas firmas) |
| `dev.shadowsoffire.placebo.network.PayloadHelper`/`PayloadProvider` | Placebo | `com.skd.commontoolkit.network.PayloadHelper`/`PayloadProvider` |
| `dev.shadowsoffire.placebo.datagen.DataGenBuilder` | Placebo | `com.skd.commontoolkit.datagen.DataGenBuilder` |
| `dev.shadowsoffire.placebo.config.Configuration` | Placebo | `com.skd.commontoolkit.config.Configuration` |
| `dev.shadowsoffire.placebo.systems.mixes.JsonMix`/`MixRegistry` | Placebo | `com.skd.commontoolkit.systems.mixes.JsonMix`/`MixRegistry` |
| `dev.shadowsoffire.placebo.util.data.DynamicRegistryProvider` | Placebo | `com.skd.commontoolkit.util.data.DynamicRegistryProvider` |
| `dev.shadowsoffire.placebo.util.Offset` | Placebo | `com.skd.commontoolkit.util.Offset` |
| `dev.shadowsoffire.placebo.PlaceboClient` | Placebo | `com.skd.commontoolkit.CommonToolkitClient` |
| `top.theillusivec4.curios.api.*` | Curios API | Mismo paquete en el jar de Curios API Updated (`lib_ext/curios-neoforge-15.0.0-beta.2+26.2.jar`, 84 clases bajo `top/theillusivec4/curios/api/`) — **sin cambios de import**, es compat opcional igual que en el original |
| `com.llamalad7.mixinextras.*` | MixinExtras | Dependencia Maven estándar, ya en caché de Gradle (`io.github.llamalad7:mixinextras-neoforge`) — se añade tal cual, no es residuo del mod original |
| `org.jspecify.annotations.Nullable` | JSpecify | Dependencia Maven estándar, ya en caché de Gradle — se añade tal cual |
| `it.unimi.dsi.fastutil.*`, `org.joml.*` | fastutil, JOML | Vienen con Minecraft/NeoForge, sin acción |

**Conclusión de la Fase 0**: no hay bloqueos de dependencias. Common Toolkit ya cubre el 100% de la superficie de Placebo que este mod usa. El único trabajo de "traducción de dependencia" es EvalEx (repack → artefacto Maven real) y Curios (ya resuelto desde el scaffold).

## Convención de renombrado por archivo

| Original (`dev.shadowsoffire.apothic_attributes.*`) | Ascendant Attributes (`com.skd.ascendantattributes.*`) | Notas |
|---|---|---|
| `ApothicAttributes` (raíz, `@Mod`) | `AscendantAttributes` | Ya existe el stub del scaffold — Fase 1 lo reemplaza |
| `ALConfig` | `AttributesConfig` | `AL` = "Apothic Lib", sin sentido fuera del original |
| `api.ALObjects` | `api.AscendantAttributesObjects` | Contenedor de registro (attributes, mob effects, attachments, etc.) — equivalente a `AscEq` en Ascendant Equipment |
| `api.AbilityCooldowns` | `api.AbilityCooldowns` | Sin cambio (nombre de dominio) |
| `api.ALCombatRules` | `api.CombatRules` | |
| `api.AttributeHelper` | `api.AttributeHelper` | Sin cambio |
| `api.CooldownTracker` | `api.CooldownTracker` | Sin cambio |
| `client.AttributeModifierComponent` | `client.AttributeModifierComponent` | Sin cambio |
| `client.AttributesGui` | `client.AttributesGui` | Sin cambio |
| `client.AttributesLibClient` | `client.AscendantAttributesClient` | Colisiona de nombre con la clase raíz cliente del scaffold (`AscendantAttributesClient.java` ya existe) — Fase 1 decide si se fusiona con el stub o se renombra a `AttributesClientHandler` |
| `client.ButtonPlacement` | `client.ButtonPlacement` | Sin cambio |
| `client.ModifierSource` / `ModifierSourceType` | sin cambio | |
| `commands.BonusModifierCommand` | `commands.BonusModifierCommand` | Sin cambio |
| `compat.CurioEquipmentSlot` / `CuriosCompat` | sin cambio | |
| `data.MixProvider` | `data.MixProvider` | Sin cambio |
| `event.ApotheosisCommandEvent` | `event.AttributesCommandEvent` | Nombre heredado de cuando formaba parte de Apotheosis — quitar la referencia |
| `impl.AttributeEvents` | `impl.AttributeEvents` | Sin cambio |
| `mixin.*` (7 clases, incl. `mixin/client/AbstractContainerScreenMixin`) | sin cambio de nombre, solo paquete raíz | Revisar antes de portar: Common Toolkit ya tiene su propio `mixin/client/AbstractContainerScreenMixin.java` — comprobar colisión/solapamiento de mixin target en Fase 1, no asumir que son intercambiables |
| `mob_effect.*` (7 efectos: Bleeding, Detonation, Flying, Grievous, Knowledge, Sundering, Vitality) | sin cambio de nombre, solo paquete raíz | |
| `modifiers.*` (EntityEquipmentSlot, EntitySlotGroup, EquipmentSlotCompat, StackAttributeModifiers, StackAttributeModifiersEvent, VanillaEquipmentSlot) | sin cambio de nombre, solo paquete raíz | |
| `payload.ConfigPayload` / `CritParticlePayload` | sin cambio | |
| `repack.evalex.*` | — | **No se porta**: se reemplaza por dependencia Maven `com.udojava:EvalEx` (ver arriba) |
| `util.AttributesUtil` / `AuxDmgTracker` / `Comparators` / `LEInvoker` | sin cambio de nombre, solo paquete raíz | `LEInvoker` invoca EvalEx — revisar su implementación al integrar la dependencia real, puede necesitar ajustar el import de `Expression` |

## Namespace de assets/data

`apothic_attributes:` → `ascendant_attributes:` (ya configurado en el scaffold, `MODID` del stub actual). Sin residuos pendientes: el jar original no trae ningún asset bajo namespace `apotheosis:`.

## Estado

Fase 0 completa. Próximo paso: Fase 1 (núcleo — raíz, `util`, `event`) según `docs/ROADMAP_ASCENDANT_ATTRIBUTES.md`.
