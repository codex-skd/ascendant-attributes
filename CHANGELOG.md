# Changelog — Ascendant Attributes

## 0.0.0-beta.1

- Scaffold inicial desde el esqueleto `codex-docs/mod_template/neoforge/26.2-26.2.0.32-beta` (NeoForge 26.2 / NeoForge 26.2.0.32-beta).
- Repo creado en `stalking-dragons/minecraft/ascendant-attributes`.
- Dependencias de compilación cableadas en `lib_ext/`: Common Toolkit (fork de Placebo, requerido) y Curios API Updated (fork de Curios API, compat opcional).
- Dependencia con Ascendant Equipment (API de afijos/attributes) pendiente: `ascendant_equipment` aún es solo scaffold, sin esa API todavía — ver `docs/ROADMAP_ASCENDANT_ATTRIBUTES.md`.
- Fix: `generateModMetadata` (heredado de `codex-docs/mod_template`) buscaba la plantilla en `src/main/templates/` pero el archivo vivía en `src/main/resources/templates/...`, así que `META-INF/neoforge.mods.toml` nunca se expandía ni llegaba al JAR final (el mod no habría cargado en el juego). Movido a `src/main/templates/META-INF/neoforge.mods.toml`. Mismo bug detectado en el JAR ya construido de `ascendant_equipment` — pendiente de corregir allí también.
- Icono propio integrado (`assets/ascendant_attributes/icon.png`, `logoFile` activo en `neoforge.mods.toml`).
- Proyecto CurseForge creado: ID `1638518`.
- JAR: `ascendant_attributes-26.2-neoforge-0.0.0-beta.1.jar` (naming alineado con el resto de mods del workspace, `archivesName` incluye versión de Minecraft y framework).
