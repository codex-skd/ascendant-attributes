# Roadmap — Ascendant Attributes (port de Apothic Attributes)

> Documento de planificación. No es el workflow operativo (ese es `WORKFLOW_ASCENDANT_ATTRIBUTES_26-2.md`) — este archivo define **qué construir y en qué orden**, para ir alimentando el trabajo a OpenCode fase a fase.

## Naturaleza del proyecto

**Ascendant Attributes es un port declarado de [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) por Shadows_of_Fire**, de NeoForge 26.1.2 (v3.0.1) a NeoForge 26.2, con todos los identificadores (paquetes, clases, campos, mod id) renombrados a nuestra convención. No es un mod "inspirado en" — es un port funcional 1:1 del código.

### Base legal — obligatorio mantener siempre

- **Código**: licencia MIT del original. Podemos copiar, modificar y renombrar libremente, pero el aviso de copyright/atribución **debe** conservarse en `LICENSE`, `README.md`, `docs/curseforge/project_description.md` y el campo `credits` de `neoforge.mods.toml`. Frase fija a usar en los cuatro sitios: *"Ascendant Attributes is a port of [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) by Shadows_of_Fire, ported from NeoForge 26.1.2 to NeoForge 26.2."*
- **Assets**: el original es `All Rights Reserved` — **no se copia ni un archivo** de `assets/apothic_attributes/` (texturas, sonidos, partículas, páginas del libro Patchouli). Cada fase que necesite un asset lo sustituye por uno propio (placeholder al principio, arte final después).
- **Fuente de referencia**: `ApothicAttributes-26.1.2-3.0.1.jar` (en `~/Downloads/`, referencia de lectura, no se versiona en el repo) es solo bytecode compilado. Fase 0 lo decompila a `temp/apothic_attributes-src/` (no versionado) — nunca se commitea el código decompilado tal cual, se reescribe fase a fase dentro de `src/`.
- **Detalle importante encontrado al inspeccionar el jar**: además de las clases bajo `dev/shadowsoffire/apothic_attributes/`, el jar trae compiladas directamente clases bajo `dev/shadowsoffire/apotheosis/` (sistema de afijos compartido) — el original **no** declara `apotheosis` como dependencia en su `neoforge.mods.toml`, solo `placebo`. Es decir: Apothic Attributes no depende en runtime del mod Apotheosis, compila su propia copia del código de afijos que necesita. Replicamos esa misma estrategia (ver más abajo, "Dependencia con Ascendant Equipment").

## Convención de renombrado

| Original | Ascendant Attributes |
|---|---|
| Paquete raíz `dev.shadowsoffire.apothic_attributes` | `com.skd.ascendantattributes` |
| Clases compartidas `dev.shadowsoffire.apotheosis.*` (afijos, compiladas dentro del jar) | `com.skd.ascendantattributes.affix.*` (o el subpaquete que corresponda — se decide en Fase 0 al mapear, evitando namespace `apotheosis` que ya usa Ascendant Equipment) |
| MODID `apothic_attributes` | `ascendant_attributes` |
| Namespace de assets/data `apothic_attributes:` | `ascendant_attributes:` |

Regla general: cada subpaquete (`api`, `client`, `mob_effect`, `modifiers`...) se mantiene igual en minúsculas (son nombres de dominio, no de marca), solo cambia el paquete raíz y las clases que llevan el nombre del mod.

## Dependencias externas — decisión tomada

- **`placebo` → `common_toolkit`** (fork de Placebo de este workspace). Requerida, igual que en el original. Ya cableada en `build.gradle`/`neoforge.mods.toml` desde el scaffold inicial.
- **`curios` → Curios API Updated** (fork, [CurseForge 1579340](https://www.curseforge.com/minecraft/mc-mods/curios-api-updated)). Integración opcional/compat, igual que en el original (paquete `compat`, 4 clases). Ya cableada.
- **`apotheosis` → Ascendant Equipment**: sin dependencia dura en el toml (igual que el original con Apotheosis), pero conceptualmente el sistema de afijos que este mod porta es el mismo que Ascendant Equipment también necesita portar (su propio roadmap, Fase 3 — "Sistema de rareza y afijos"). **Antes de la Fase 3 de este roadmap, confirmar con el usuario** si:
  - Opción A (recomendada si Ascendant Equipment ya avanzó): consumir la API de afijos de Ascendant Equipment como dependencia real (`lib_ext/`), evitando duplicar el sistema.
  - Opción B (si Ascendant Equipment sigue en scaffold): portar aquí una copia propia del subsistema de afijos que Apothic Attributes necesita, igual que hizo el original, y no bloquear este mod en el avance de Ascendant Equipment.
  A fecha de este documento, Ascendant Equipment está en `0.0.0-beta.1` (solo scaffold) → Opción B es la asunción de trabajo hasta que se confirme lo contrario.
- **`repack/` (41 clases)**: el original trae una librería de terceros repackaged dentro de su propio namespace. Se identifica en Fase 0 qué librería es y si existe una versión NeoForge 26.2 compatible como dependencia real, en vez de portar clase por clase.

## Fases

Cada fase = un encargo a OpenCode. Orden pensado por dependencia técnica y tamaño (nº de clases del original entre paréntesis).

| Fase | Alcance | Paquetes origen (nº clases) | Depende de |
|---|---|---|---|
| **0** | Setup: decompilar jar a `temp/apothic_attributes-src/`, identificar la librería de `repack/` y resolver si hay dependencia real disponible, definir mapping de paquetes/clases en `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` | — | — |
| **1** | Núcleo: clase principal, config, utilidades base, evento común | raíz (3), `util` (6), `event` (1) | Fase 0 |
| **2** | API pública: contratos que expondrá el mod (attributes, modifiers builders) | `api` (17) | Fase 1 |
| **3** | Sistema de afijos/atributos compartido con Apotheosis (decisión de dependencia — ver sección anterior) | clases `dev.shadowsoffire.apotheosis.*` embebidas en el jar original | Fase 2 + decisión de dependencia con Ascendant Equipment |
| **4** | Modificadores de atributos e implementación interna | `modifiers` (11), `impl` (1) | Fase 3 |
| **5** | Efectos de estado (mob effects) propios del mod | `mob_effect` (7) | Fase 3 |
| **6** | Red: payloads de sincronización cliente/servidor | `payload` (4) | Fase 1 |
| **7** | Comandos | `commands` (1) | Fase 2 |
| **8** | Datos: brewing mixes, damage types, tags | `data/apothic_attributes/` (brewing_mixes, damage_type, tags) | Fase 3, 5 |
| **9** | Cliente: HUD/render de atributos | `client` (16) | Fases 2–5 según feature |
| **10** | Mixins (al final: tocan clases vanilla, lo más frágil entre versiones de Minecraft) | `mixin` (7) | Todas las anteriores relevantes |
| **11** | Compat opcional: Curios (paquete de 4 clases, integración de slots de accesorio) | `compat` (4) | Fase 4, 5 |
| **12** | Arte propio: sustituir placeholders por texturas/sonidos/partículas/libro de guía originales | — (todo `assets/`) | Trabajo paralelo, no bloquea el resto |
| **13** | QA de paridad funcional: probar que el comportamiento replica el original fase por fase | — | Todas |

## Cómo se alimenta a OpenCode

1. Antes de cada fase: confirmar contigo el alcance exacto (qué subpaquete, qué clases del original) — no se abre una fase sin fase anterior mergeada y compilando.
2. El prompt a OpenCode por fase incluye: ruta al código decompilado de referencia en `temp/apothic_attributes-src/<paquete>/`, la convención de renombrado de este documento, y el resultado esperado (`src/main/java/com/skd/ascendantattributes/<paquete>/...` compilando con `./gradlew.bat build`).
3. Al cerrar cada fase: build verde, commit (`feat[<paquete>]: port <subsistema> from Apothic Attributes`, versión bump beta), push, actualizar `CHANGELOG.md` y marcar la fase como hecha en este documento.
4. Graphify se actualiza tras cada fase para que el grafo de conocimiento no se quede desfasado.

## Estado

Ninguna fase iniciada. Próximo paso: **Fase 0** (decompilar + identificar `repack/` + mapping de paquetes).
