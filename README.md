# Ascendant Attributes

Ascendant Attributes is a library mod adding 20 custom attributes, 7 status effects, and a full attribute-inspection GUI to Minecraft 1.21.1 (NeoForge). Conceptually a module of [Ascendant Equipment](https://gitlab.com/stalking-dragons/minecraft/ascendant-equipment).

> This mod is a fork of [Apothic Attributes](https://www.curseforge.com/minecraft/mc-mods/apothic-attributes) by Shadows_of_Fire. Not affiliated with or endorsed by the original author.

## Status

Stable release (`1.0.0`). The full port from Apothic Attributes is complete: attributes, effects, GUI, event handling, mixins, and Curios compatibility are all implemented and verified.

## Features

- **20 custom attributes**: Draw Speed, Crit Chance, Crit Damage, Cold/Fire Damage, Life Steal, Current HP Damage, Overheal, Arrow Damage/Velocity, Experience Gained, Healing Received, Armor Pierce/Shred, Projectile Damage, Protection Pierce/Shred, Dodge Chance, Elytra Flight, Creative Flight, Cooldown Reduction.
- **7 status effects**: Ancient Knowledge, Bursting Vitality, Grievous Wounds, Flying, Sundering, Bleeding, Flaming Detonation.
- **In-game attribute inspection GUI**: hold-to-view breakdown of base values, modifiers, and formulas for every attribute on your character.
- **Configurable combat formulas** via a bundled expression evaluator (EvalEx) — server owners can tune the math without recompiling.
- **Curios API compatibility**: attribute modifiers on Curios-slotted items are recognized by both combat logic and the GUI.
- Fully localized (English, Spanish).

## Requirements

| Component | Version |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.249+ |
| Java | 21+ |
| [Common Toolkit](https://gitlab.com/stalking-dragons/minecraft/common-toolkit) | Required (fork of Placebo) |
| [Regalia Slots API](https://gitlab.com/stalking-dragons/minecraft/regalia-slots-api) | Optional (our fork of Curios API, compat only) |

## Installation

1. Install [NeoForge](https://neoforge.net/) for Minecraft 1.21.1.
2. Install Common Toolkit.
3. Download the mod jar and place it in your `mods/` folder.

## License

MIT — see [LICENSE](LICENSE).
