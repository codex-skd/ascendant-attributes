# CurseForge — Variables del proyecto

> **Proyecto aún no creado en CurseForge.** Esta tabla recoge los datos para rellenar el formulario de alta manualmente (submit-mod) — completar `curseforge_project_id` y los tokens en cuanto exista.

## Datos para el formulario de alta ("Submit a Project")

| Campo del formulario | Valor sugerido |
|---|---|
| Project name | `Ascendant Attributes` |
| Slug / URL | `ascendant-attributes` |
| Summary (corto, <150 car.) | `A library mod providing attributes and related things. Fork of Apothic Attributes, module of the Ascendant Equipment family.` |
| Project type | Mod |
| Game | Minecraft |
| Mod loader / categories | NeoForge · Library / API |
| Client/Server side | Both |
| License | MIT |
| Description | Contenido de `docs/curseforge/project_description.md` (HTML) |
| Relations — Required Dependency | Common Toolkit (fork de Placebo de este workspace, aún sin publicar en CurseForge — usar el proyecto correspondiente cuando exista) |
| Relations — Optional Dependency | [Curios API Updated](https://www.curseforge.com/minecraft/mc-mods/curios-api-updated) (CurseForge ID `1579340`) |

## Proyecto

| Variable | Valor |
|----------|-------|
| `curseforge_project_id` | *(pendiente — rellenar tras crear el proyecto)* |
| `mod_id` | `ascendant_attributes` |
| `display_name` | `Ascendant Attributes` |

## Tokens

| API | Token | Uso |
|-----|-------|-----|
| Upload | *(pendiente)* | Subir archivos JAR |
| Core (GET) | *(pendiente)* | Consultar datos del mod |

Autenticación Upload: cabecera `X-Api-Token`
Autenticación Core: cabecera `x-api-key`

> Token de cuenta (mismo para todos los mods, ver `ageforged_armor/neoforge/26.2/docs/curseforge/project_vars.md` u otros).

## Variables para script (lectura automática)

project_id =
api_token =
release_type = release
game_versions =
relations =

El script lee `project_id`, `api_token` y `game_versions` de este archivo, y `mod_id`, `mod_name`, `minecraft_version`, `mod_version` de `gradle.properties`. Sube automáticamente el JAR desde `build/libs/` con el changelog de `docs/curseforge/versions/<version>.md`.

## Nota

La **primera subida a CurseForge se hace manual** (proyecto recién creado, sin archivos previos que verificar por API). A partir de la segunda subida se puede usar el script `codex-docs/scripts/curseforge-upload.ps1`. `game_versions` se rellena con los IDs que CurseForge asigne al crear el proyecto (comparar con `ascendant_equipment/neoforge/26.2/docs/curseforge/project_vars.md`, mismo target de Minecraft 26.2/NeoForge).

## Rama

```
minecraft/26.2/neoforge-26.2.0.32-beta/production
```

## Tag

Formato: `<mc-version>-<framework>-<version>`
Ejemplo: `26.2-neoforge-0.0.0-beta.1`
