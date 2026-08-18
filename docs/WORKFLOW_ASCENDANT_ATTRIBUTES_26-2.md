# Flujo de trabajo — Ascendant Attributes (NeoForge)

> **Versión del workflow**: 1.16.0 (codex-docs)
> Este archivo pertenece al proyecto **Ascendant Attributes**. Cambios aquí solo afectan a este proyecto.
> **Trabaja directamente con este archivo**: es el workflow operativo del mod, autocontenido. No leas `codex-docs/WORKFLOW_AGENT.md` ni `WORKFLOW_GENERIC.md` de forma rutinaria.
> On-demand (solo si la tarea lo necesita): `codex-docs/reference/CURSEFORGE.md` (formato HTML al publicar), `codex-docs/reference/GRAPHIFY.md` (backend LLM de Graphify), `codex-docs/reference/REPO_SETUP.md` (setup único de repo).

## Específico del mod

| Dato | Valor |
|---|---|
| Mod ID (`gradle.properties`) | `ascendant_attributes` |
| Clase principal | `AscendantAttributes` |
| Display name (Title Case) | `Ascendant Attributes` |
| Versiones de Minecraft | `26.2` |
| Rama | `minecraft/26.2/neoforge-26.2.0.45-beta/production` |
| Última versión publicada | `1.0.0` (CurseForge file ID 8617339) |
| Estado | ✅ Release estable 1.0.0 |

### Notas específicas de este mod

- **Fork de**: [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) por Shadows_of_Fire (v3.0.1 para MC 26.1.2/NeoForge 26.1.2.70-beta). Completamente portado a Ascendant Attributes (v0.0.0-beta.4+ para MC 26.2/NeoForge 26.2.0.37-beta). Roadmap de 13 fases completado. ✅
- **package**: `com.skd.ascendantattributes`
- **Minecraft / NeoForge**: `26.2` / `26.2.0.37-beta` (actualizado desde `26.2.0.32-beta` en beta.3)
- **Conceptualmente módulo de [Ascendant Equipment](https://gitlab.com/stalking-dragons/minecraft/ascendant-equipment)** (fork de Apotheosis), pero sin dependencia técnica real: verificado en Fase 0 (decompilando el jar original) que Apothic Attributes es **completamente autocontenido** — no embebe ni depende de código de Apotheosis, solo de Placebo (→ Common Toolkit) y opcionalmente Curios. Ver `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` para el detalle. No hay ninguna dependencia con Ascendant Equipment que resolver.
- **Dependencias reemplazadas** (a petición del usuario, no son las originales del mod):
  - `placebo` (requerida en el original) → **`common_toolkit`** (fork de Placebo de este workspace). Cableada en `build.gradle` (`compileOnly`/`localRuntime` desde `lib_ext/common_toolkit-26.2-neoforge-0.0.0-beta.1.jar`) y declarada `required` en `neoforge.mods.toml`.
  - `curios` (309927, integración opcional/compat en el original, no dependencia dura) → **Curios API Updated** (fork, [CurseForge 1579340](https://www.curseforge.com/minecraft/mc-mods/curios-api-updated)). Cableada igual (`lib_ext/curios-neoforge-15.0.0-beta.2+26.2.jar`) y declarada `optional` en `neoforge.mods.toml`, igual que en el original.
- **Sin residuos del original**: el mod original usa el package `dev.shadowsoffire.apothic_attributes` (y comparte clases bajo `dev.shadowsoffire.apotheosis`) y namespace de recursos `apothic_attributes:`/`apotheosis:` — todo el código, assets y datos portados deben quedar bajo `com.skd.ascendantattributes` / `ascendant_attributes:`, sin nombres de clases, métodos ni variables calcados del original.
- **Icono pendiente**: `assets/ascendant_attributes/icon.png` aún no existe (línea `logoFile` comentada en `neoforge.mods.toml`) — diseñar uno propio antes de la primera subida a CurseForge, no reutilizar el logo de Apothic Attributes ni el de Ascendant Equipment.
- **Estado de desarrollo**: `0.0.0-beta.4` completa el roadmap entero (13 fases). Todos los sistemas portados y verificados:
  - ✅ Núcleo: 20 atributos personalizados, 7 efectos de estado
  - ✅ Eventos: 20+ handlers de eventos de combate/atributos
  - ✅ Cliente: GUI de atributos en inventario, overlays, tooltips
  - ✅ Mixins: 7 mixins verificados en runtime (`runServer`)
  - ✅ Compat: integración Curios API, opcional
  - ✅ Assets: arte propio completo (iconos, texturas, sonidos)
  - ✅ QA: `clean build` + `runServer` sin errores propios del mod
  - Próximo paso: RELEASE 1.0.0 (cuando se haya jugado en partida real)

## Convenciones de nomenclatura

| Convención | Uso | Ejemplo |
|---|---|---|
| **snake_case** | `mod_id`, assets/, packages Java | `ascendant_attributes` |
| **PascalCase** | Clases Java principales | `AscendantAttributes` |
| **camelCase** | Variables, métodos, config keys | `ascendantAttributesConfig` |
| **Title Case** | Display name (README, CHANGELOG, docs, CurseForge) | `Ascendant Attributes` |

## Organización y ramas

- Un repo GitLab por mod, una rama `minecraft/<mc>/neoforge-<neo>/production` por versión. Este clon local trabaja en la rama `production` de esta versión.
- Carpetas: `<mod_id>/<framework>/<mc-version>/` — este clon vive en `<mod_id>/neoforge/<mc-version>/`.
- Remoto: `https://gitlab.com/stalking-dragons/minecraft/ascendant-attributes.git`
- `*/main` y CI/CD: setup único al crear el repo (`codex-docs/reference/REPO_SETUP.md`) — no releer ni modificar.

## Estructura del proyecto

`build.gradle` · `gradle.properties` (mod_id, mod_version, mod_group_id, mod_framework) · `settings.gradle` · `src/main/java/<package>/` · `src/main/resources/assets/<mod_id>/` · `META-INF/neoforge.mods.toml` · `lib_ext/` (dependencias reales de compilación: Common Toolkit + Curios API Updated, no versionado) · `temp/` (no versionado) · `docs/` (WORKFLOW + ROADMAP + curseforge/) · `CHANGELOG.md` · `README.md`.

## Versionado

- Beta `0.0.0-beta.X` · Release `X.Y.Z` (SemVer: MAJOR breaking / MINOR feature / PATCH fix)
- `mod_version` y `mod_framework` en `gradle.properties`. JAR: `<mod_id>-<mc>-<framework>-<loader>-<version>.jar`

## Commits (Conventional Commits)

`<tipo>[<ámbito>]: <descripción>` · tipos `feat fix refactor docs chore style perf test` · el mensaje incluye la versión (`v<version>`).

## Tags

Cada subida a CurseForge crea tag: beta `<mc>-neoforge-beta.X` · release `<mc>-neoforge-X.Y.Z`.

## Flujo por tarea

**0. Alcance** — si el mod tiene varias versiones, preguntar con la herramienta `question`: **"Todas"** o una versión. No asumir.

**1. Desarrollo**

```bash
git checkout minecraft/26.2/neoforge-26.2.0.32-beta/production
./gradlew.bat build
git add -A
git commit -m "feat: <descripción>

v<version>"
git push
```

**2. CurseForge** — solo si el usuario confirma:
- Bump `mod_version` en gradle.properties → `./gradlew.bat clean build`
- Release notes `docs/curseforge/versions/<version>.md` (HTML) + actualizar `CHANGELOG.md`
- Commit `chore: bump version to <version>` → tag `<mc>-neoforge-<version>` → push
- La **primera subida es manual** (proyecto de CurseForge aún no creado) — ver `docs/curseforge/project_vars.md`. A partir de la segunda: `powershell -File ../../codex-docs/scripts/curseforge-upload.ps1` (desde este repo)
- Formato HTML de descripciones/changelog: `codex-docs/reference/CURSEFORGE.md`

**3. Release estable** — bump `X.Y.Z` + tag.

**4. Graphify** — tras cada push a remoto. Versión 0.9.12: **`build` no existe**, usar `extract` (1ª vez) o `update . --force` (tras cambios):

```bash
GRAPHIFY="C:\Users\llagu\AppData\Local\Packages\PythonSoftwareFoundation.Python.3.13_qbz5n2kfra8p0\LocalCache\local-packages\Python313\Scripts\graphify.exe"
"$GRAPHIFY" update . --force
git add graphify-out/ && git commit -m "chore: update knowledge graph" && git push
```

Leer siempre `GRAPH_REPORT.md`, nunca `graph.json`/`graph.html` (pesan >1MB). Sin copias fechadas de `graphify-out/`. Backend LLM: `codex-docs/reference/GRAPHIFY.md`.

## Buenas prácticas

- Un commit por cambio lógico · commit+push tras cada cambio funcional y de docs
- `clean build` antes del JAR final · versionar antes de CurseForge · CHANGELOG al día
- Graphify actualizado tras cada release · nomenclatura consistente · sin basura en repo (`nul`, `*_errors.txt`, `TEMPLATE_LICENSE.txt`) · `.gitignore` excluye `temp/` y `lib_ext/`
- README en inglés siempre actualizado · sin residuos de mod original (paquetes, clases, toml, lang, assets) · atribución de fork explícita (README, project_description, credits)

## Idioma

| Ámbito | Idioma |
|---|---|
| código, logs, commits | en-US |
| README.md | en-US |
| docs internas (docs/, CHANGELOG, este archivo) | es-ES |
| CurseForge | en-US |

