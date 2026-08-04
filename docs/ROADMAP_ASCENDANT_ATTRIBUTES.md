# Roadmap — Ascendant Attributes (port de Apothic Attributes)

> Documento de planificación. No es el workflow operativo (ese es `WORKFLOW_ASCENDANT_ATTRIBUTES_26-2.md`) — este archivo define **qué construir y en qué orden**, para ir alimentando el trabajo a OpenCode fase a fase.

## Naturaleza del proyecto

**Ascendant Attributes es un port declarado de [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) por Shadows_of_Fire**, de NeoForge 26.1.2 (v3.0.1) a NeoForge 26.2, con todos los identificadores (paquetes, clases, campos, mod id) renombrados a nuestra convención. No es un mod "inspirado en" — es un port funcional 1:1 del código.

### Base legal — obligatorio mantener siempre

- **Código**: licencia MIT del original. Podemos copiar, modificar y renombrar libremente, pero el aviso de copyright/atribución **debe** conservarse en `LICENSE`, `README.md`, `docs/curseforge/project_description.md` y el campo `credits` de `neoforge.mods.toml`. Frase fija a usar en los cuatro sitios: *"Ascendant Attributes is a port of [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) by Shadows_of_Fire, ported from NeoForge 26.1.2 to NeoForge 26.2."*
- **Assets**: el original es `All Rights Reserved` — **no se copia ni un archivo** de `assets/apothic_attributes/` (texturas, sonidos, partículas). Cada fase que necesite un asset lo sustituye por uno propio (placeholder al principio, arte final después).
- **Fuente de referencia**: `ApothicAttributes-26.1.2-3.0.1.jar` (en `~/Downloads/`, referencia de lectura, no se versiona en el repo) es solo bytecode compilado. Fase 0 lo decompiló con Vineflower a `temp/apothic_attributes-src/` (no versionado, gitignored) — nunca se commitea el código decompilado tal cual, se reescribe fase a fase dentro de `src/`.

## Fase 0 — completada (ver `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` para el detalle completo)

- Decompilado con Vineflower 1.12.0 (mismo decompilador del NeoForge MDK, ya en caché de Gradle): 47 archivos `.java` bajo `dev.shadowsoffire.apothic_attributes`.
- **Corrección sobre una asunción anterior de este documento**: se había afirmado que el jar traía embebidas clases de `dev.shadowsoffire.apotheosis.*` y que habría que decidir una dependencia con Ascendant Equipment. Verificado con `unzip -l` sobre el jar real: **eso era falso** (contenido residual mal atribuido de una sesión anterior). El mod es autocontenido: su única dependencia real es `placebo` (→ `common_toolkit`) más integración opcional con `curios`. **No hay decisión de dependencia con Ascendant Equipment pendiente.**
- `repack/evalex/` (41 clases) identificado como una copia repackaged de **EvalEx** (`com.udojava:EvalEx`, evaluador de expresiones). No se porta manualmente — se añade como dependencia Maven real en Fase 1.
- Confirmado que **Common Toolkit ya cubre el 100%** de la superficie de Placebo que usa este mod (`DeferredHelper`, `PayloadHelper`/`PayloadProvider`, `DataGenBuilder`, `Configuration`, `JsonMix`/`MixRegistry`, `DynamicRegistryProvider`, `Offset`, `CommonToolkitClient`) con API 1:1. Sin bloqueos.
- `MixinExtras` y `JSpecify` (dependencias Maven estándar del original) ya están en la caché de Gradle local — se añaden tal cual, no son residuo del mod original.

## Convención de renombrado

Ver `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` para el mapeo completo archivo por archivo. Resumen:

| Original | Ascendant Attributes |
|---|---|
| Paquete raíz `dev.shadowsoffire.apothic_attributes` | `com.skd.ascendantattributes` |
| MODID `apothic_attributes` | `ascendant_attributes` |
| Namespace de assets/data `apothic_attributes:` | `ascendant_attributes:` |
| `ALConfig`, `ALObjects`, `ALCombatRules` (prefijo "AL" = Apothic Lib) | `AttributesConfig`, `AscendantAttributesObjects`, `CombatRules` |
| `event.ApotheosisCommandEvent` (nombre heredado, sin relación funcional con Apotheosis) | `event.AttributesCommandEvent` |

Regla general: cada subpaquete (`api`, `client`, `mob_effect`, `modifiers`, `mixin`, `compat`, `payload`, `commands`, `data`, `impl`, `util`) se mantiene igual en minúsculas (son nombres de dominio, no de marca), solo cambia el paquete raíz y las clases que llevan el nombre del mod o del framework original.

## Dependencias externas — resueltas en Fase 0

- **`placebo` → `common_toolkit`**: requerida, API 1:1 confirmada. Ya cableada en `build.gradle`/`neoforge.mods.toml` desde el scaffold inicial.
- **`curios` → Curios API Updated**: opcional/compat, mismo paquete Java (`top.theillusivec4.curios.api`) — sin cambios de import. Ya cableada.
- **`repack/evalex` → `com.udojava:EvalEx`**: confirmar última versión estable en Maven Central en Fase 1 y añadir como `implementation` (con `jarJar` si hace falta bundlearla, ya que no es un mod de Minecraft con su propio jar de distribución).
- **MixinExtras, JSpecify**: dependencias Maven estándar, añadir tal cual.

## Fases

Cada fase = un encargo a OpenCode. Orden pensado por dependencia técnica y tamaño (nº de archivos `.java` del original entre paréntesis, ya contando clases internas fusionadas por el decompilador).

| Fase | Alcance | Paquetes origen (nº archivos) | Depende de |
|---|---|---|---|
| **0** | ✅ Setup: decompilar, identificar `repack/`, resolver dependencias, mapping completo | — | — |
| **1** | Núcleo: clase principal (`AscendantAttributes`), `AttributesConfig`, dependencia EvalEx real, utilidades base | raíz (2: `ApothicAttributes`, `ALConfig`), `util` (4) | Fase 0 |
| **2** | API pública: registro de objetos (attributes, mob effects, attachments, sonidos, damage types, potions, tags), helpers de atributos y cooldowns | `api` (5: `ALObjects`, `AbilityCooldowns`, `ALCombatRules`, `AttributeHelper`, `CooldownTracker`) | Fase 1 |
| **3** | Efectos de estado propios | `mob_effect` (7: Bleeding, Detonation, Flying, Grievous, Knowledge, Sundering, Vitality) | Fase 2 |
| **4** | Modificadores de atributos por stack/equipo | `modifiers` (7: EntityEquipmentSlot, EntitySlotGroup, EquipmentSlotCompat, StackAttributeModifiers(+Event), VanillaEquipmentSlot) | Fase 2 |
| **5** | Lógica de eventos e implementación central (aplica todo lo anterior al gameplay) | `impl` (1: `AttributeEvents`), `event` (1: `AttributesCommandEvent`) | Fases 2–4 |
| **6** | Red: payloads de sincronización cliente/servidor | `payload` (2: ConfigPayload, CritParticlePayload) | Fase 1 |
| **7** | Comandos | `commands` (1: BonusModifierCommand) | Fase 5 |
| **8** | Datos: brewing mixes, damage types, tags (JSON, equivalentes propios no copiados) | `data` (1: MixProvider) + JSONs de `data/apothic_attributes/` | Fase 3, 5 |
| **9** | Cliente: HUD/render de atributos, GUI | `client` (6: AttributeModifierComponent, AttributesGui, AttributesLibClient, ButtonPlacement, ModifierSource(Type)) | Fases 2–5 según feature |
| **10** | Mixins (al final: tocan clases vanilla, lo más frágil entre versiones de Minecraft) | `mixin` (7, incl. `mixin/client/AbstractContainerScreenMixin` — **revisar solapamiento** con el mixin homónimo ya existente en Common Toolkit antes de portar) | Todas las anteriores relevantes |
| **11** | Compat opcional: Curios (integración de slots de accesorio) | `compat` (2: CurioEquipmentSlot, CuriosCompat) | Fase 4, 5 |
| **12** | Arte propio: sustituir placeholders por texturas/sonidos/partículas originales | — (todo `assets/`) | Trabajo paralelo, no bloquea el resto |
| **13** | QA de paridad funcional: probar que el comportamiento replica el original fase por fase | — | Todas |

## Cómo se alimenta a OpenCode

1. Antes de cada fase: confirmar contigo el alcance exacto (qué subpaquete, qué clases del original) — no se abre una fase sin fase anterior mergeada y compilando.
2. El prompt a OpenCode por fase incluye: ruta al código decompilado de referencia en `temp/apothic_attributes-src/dev/shadowsoffire/apothic_attributes/<paquete>/`, el mapeo de `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md`, y el resultado esperado (`src/main/java/com/skd/ascendantattributes/<paquete>/...` compilando con `./gradlew.bat build`).
3. Al cerrar cada fase: build verde, commit (`feat[<paquete>]: port <subsistema> from Apothic Attributes`, versión bump beta), push, actualizar `CHANGELOG.md` y marcar la fase como hecha en este documento.
4. Graphify se actualiza tras cada fase para que el grafo de conocimiento no se quede desfasado.

## Estado

Fase 0 completa. Próximo paso: **Fase 1** (núcleo: clase principal, config, dependencia EvalEx, utilidades base).
