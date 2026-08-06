# CurseForge — Variables del proyecto

## Proyecto

| Variable | Valor |
|----------|-------|
| `curseforge_project_id` | `1638518` |
| `mod_id` | `ascendant_attributes` |
| `display_name` | `Ascendant Attributes` |

## Tokens

| API | Token | Uso |
|-----|-------|-----|
| Upload | `ee776b0a-ee95-4850-b554-06be02a8657f` | Subir archivos JAR |
| Core (GET) | `$2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO` | Consultar datos del mod |

Autenticación Upload: cabecera `X-Api-Token`
Autenticación Core: cabecera `x-api-key`

> Token de cuenta (mismo para todos los mods, ver `ageforged_armor/neoforge/26.2/docs/curseforge/project_vars.md` u otros).

## Variables para script (lectura automática)

project_id = 1638518
api_token = ee776b0a-ee95-4850-b554-06be02a8657f
release_type = beta
game_versions = 9638, 9639, 16498, 10150
relations =

El script lee `project_id`, `api_token` y `game_versions` de este archivo, y `mod_id`, `mod_name`, `minecraft_version`, `mod_version` de `gradle.properties`. Sube automáticamente el JAR desde `build/libs/` con el changelog de `docs/curseforge/versions/<version>.md`.

**Relations**: el intento de declarar `common-toolkit:requiredDependency,curios-api-updated:optionalDependency` en la subida de `0.0.0-beta.1` falló con `errorCode 1018` ("Invalid slug... does not exist, is not accessible, or belongs to an unrelated root category") — probablemente porque el proyecto `common-toolkit` (o `curios-api-updated`) no estaba aprobado/público en CurseForge en ese momento. Se subió sin `relations` por API. **Añadidas manualmente desde la web** (Relations → Required/Optional Dependency) por el usuario — ya no está pendiente.

## Nota

La **primera subida a CurseForge se hace manual** (proyecto recién creado, sin archivos previos que verificar por API). A partir de la segunda subida se puede usar el script `codex-docs/scripts/curseforge-upload.ps1`. `game_versions` reutilizado de `ascendant_equipment`/`common_toolkit` (mismo target Minecraft 26.2/NeoForge).

## Datos usados para el alta ("Submit a Project")

| Campo del formulario | Valor |
|---|---|
| Project name | `Ascendant Attributes` |
| Slug / URL | `ascendant-attributes` |
| Summary (corto, <150 car.) | `A library mod providing attributes and affixes for equipment. Fork of Apothic Attributes — module of the Ascendant Equipment family. Requires Common Toolkit.` |
| Project type | Mod |
| Game | Minecraft |
| Mod loader / categories | NeoForge · Library / API |
| Client/Server side | Both |
| License | MIT |
| Description | Contenido de `docs/curseforge/project_description.md` (HTML) |
| Relations — Required Dependency | Common Toolkit (`common-toolkit`, CurseForge ID `1638419`) |
| Relations — Optional Dependency | [Curios API Updated](https://www.curseforge.com/minecraft/mc-mods/curios-api-updated) (CurseForge ID `1579340`) |

## Nota para revisores de CurseForge (validación del proyecto)

CurseForge suele pedir contexto extra cuando un proyecto nuevo es un fork/port declarado. Texto sugerido a pegar en el campo de notas al staff si el formulario lo pide:

> This project is an open-source port of "Apothic Attributes" by Shadows_of_Fire (https://www.curseforge.com/minecraft/mc-mods/apothic-attributes), originally MIT licensed. The port updates it from NeoForge 26.1.2 to NeoForge 26.2, with all package/class identifiers renamed. Attribution is kept in LICENSE, README, and the mod's `credits` field. Not affiliated with or endorsed by the original author.

## Icono / imagen del proyecto

Icono aún pendiente de diseñar (`assets/ascendant_attributes/icon.png`, `logoFile` comentado en `neoforge.mods.toml`). Prompt propuesto para generarlo, manteniendo la identidad visual ya usada en `ascendant_equipment`/`ascendant_spawners` (pieza flotante dorada con aura violeta radiante sobre fondo oscuro) pero con motivo de atributo/afijo en vez de equipo:

```
Fantasy RPG game item icon, a glowing rune-etched crystal shard floating and
rotating slightly, radiant violet-purple magical aura swirling around it,
warm golden light core at its center, dark vignette background, dramatic
rim lighting, painterly digital art style matching World of Warcraft /
Diablo loot icon aesthetics, square composition, centered subject, no text,
no border, high detail, 1:1 aspect ratio
```

Generar en alta resolución (1024x1024 recomendado) y luego exportar dos tamaños: 64x64 para `assets/ascendant_attributes/icon.png` (icono in-game) y una versión cuadrada (mínimo 256x256, PNG con fondo) para el logo del proyecto en CurseForge. No reutilizar el icono de `ascendant_equipment` ni el de `ascendant_spawners` — cada mod necesita uno propio aunque compartan estilo de familia.

## Rama

```
minecraft/26.2/neoforge-26.2.0.32-beta/production
```

## Tag

Formato: `<mc-version>-<framework>-<version>`
Ejemplo: `26.2-neoforge-0.0.0-beta.1`
