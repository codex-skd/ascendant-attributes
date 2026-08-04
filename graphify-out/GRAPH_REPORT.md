# Graph Report - 26.2  (2026-08-04)

## Corpus Check
- 39 files · ~17,475 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 382 nodes · 737 edges · 28 communities (24 shown, 4 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 15 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `60de732a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Item Management
- Configuration Management
- Client Initialization
- Build Tools
- Event Handling
- Server Startup
- Project Metadata
- Creative Mode Content
- Common Setup
- Project Branding
- Entry
- AttributesConfig
- CooldownTracker
- AttributeHelper.java
- Flujo de trabajo — Ascendant Attributes (NeoForge)
- CurseForge — Variables del proyecto
- Roadmap — Ascendant Attributes (port de Apothic Attributes)
- Mapa de renombrado — Ascendant Attributes (Fase 0)
- VanillaEquipmentSlot.java
- Ascendant Attributes
- Comparators
- CLAUDE.md — ascendant_attributes (26.2)
- Changelog — Ascendant Attributes
- LEInvoker

## God Nodes (most connected - your core abstractions)
1. `EntitySlotGroup` - 24 edges
2. `AuxDmgTracker` - 21 edges
3. `StackAttributeModifiers` - 19 edges
4. `Entry` - 19 edges
5. `AscendantAttributes` - 18 edges
6. `StackAttributeModifiersEvent` - 18 edges
7. `CooldownTracker` - 14 edges
8. `EntityEquipmentSlot` - 14 edges
9. `StackAttributeModifiersBuilder` - 13 edges
10. `AttributesConfig` - 11 edges

## Surprising Connections (you probably didn't know these)
- `Mod Icon` ----> `Project Variables`  [EXTRACTED]
  src/main/resources/assets/ascendant_attributes/icon.png → docs/curseforge/project_vars.md
- `Attachments` --references--> `CooldownTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/api/CooldownTracker.java
- `Attachments` --references--> `AuxDmgTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/util/AuxDmgTracker.java
- `Components` --references--> `StackAttributeModifiers`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/StackAttributeModifiers.java
- `EquipmentSlots` --references--> `EntityEquipmentSlot`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/EntityEquipmentSlot.java

## Import Cycles
- None detected.

## Communities (28 total, 4 thin omitted)

### Community 0 - "Item Management"
Cohesion: 0.09
Nodes (20): AttackEntityEvent, Client, DeferredHelper, Entity, EntityAttributeModificationEvent, EntityType, FMLCommonSetupEvent, Logger (+12 more)

### Community 1 - "Configuration Management"
Cohesion: 0.07
Nodes (29): AttachmentType, DataComponentType, Experimental, Potion, SimpleParticleType, SoundEvent, AscendantAttributesObjects, Attachments (+21 more)

### Community 2 - "Client Initialization"
Cohesion: 0.83
Nodes (3): EventBusSubscriber, AscendantAttributesClient, Mod

### Community 3 - "Build Tools"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 4 - "Event Handling"
Cohesion: 0.07
Nodes (18): AttributeMap, MobEffect, Identifier, Identifier, BleedingEffect, LivingEntity, Override, ServerLevel (+10 more)

### Community 5 - "Server Startup"
Cohesion: 0.13
Nodes (21): BiMap, EquipmentSlotGroup, BuiltInRegs, EquipmentSlotGroups, HolderSet, Registry, EntityEquipmentSlot, ItemStack (+13 more)

### Community 7 - "Creative Mode Content"
Cohesion: 0.19
Nodes (12): IdentityHashMap, Marker, AuxDmgTracker, Entry, Attribute, Codec, DamageSource, DamageType (+4 more)

### Community 8 - "Common Setup"
Cohesion: 0.19
Nodes (12): Event, Attribute, AttributeModifier, Entry, Holder, Identifier, Internal, ItemStack (+4 more)

### Community 12 - "Entry"
Cohesion: 0.18
Nodes (11): Builder, Entry, Attribute, AttributeModifier, Codec, Holder, Identifier, ItemAttributeModifiers (+3 more)

### Community 13 - "AttributesConfig"
Cohesion: 0.17
Nodes (9): Configuration, Expression, Offset, ResourceManagerReloadListener, CombatRules, DamageSource, LivingEntity, AttributesConfig (+1 more)

### Community 14 - "CooldownTracker"
Cohesion: 0.19
Nodes (9): MapCodec, Object2LongMap, AbilityCooldowns, Identifier, LivingEntity, CooldownTracker, Identifier, RegistryFriendlyByteBuf (+1 more)

### Community 15 - "AttributeHelper.java"
Cohesion: 0.33
Nodes (9): Operation, AttributeHelper, Attribute, Entry, Holder, Identifier, ItemAttributeModifiers, LivingEntity (+1 more)

### Community 16 - "Flujo de trabajo — Ascendant Attributes (NeoForge)"
Cohesion: 0.15
Nodes (12): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Ascendant Attributes (NeoForge), Flujo por tarea, Idioma (+4 more)

### Community 17 - "CurseForge — Variables del proyecto"
Cohesion: 0.18
Nodes (10): CurseForge — Variables del proyecto, Datos usados para el alta ("Submit a Project"), Icono / imagen del proyecto, Nota, Nota para revisores de CurseForge (validación del proyecto), Proyecto, Rama, Tag (+2 more)

### Community 18 - "Roadmap — Ascendant Attributes (port de Apothic Attributes)"
Cohesion: 0.20
Nodes (9): Base legal — obligatorio mantener siempre, Convención de renombrado, Cómo se alimenta a OpenCode, Dependencias externas — resueltas en Fase 0, Estado, Fase 0 — completada (ver `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` para el detalle completo), Fases, Naturaleza del proyecto (+1 more)

### Community 19 - "Mapa de renombrado — Ascendant Attributes (Fase 0)"
Cohesion: 0.25
Nodes (7): Convención de renombrado por archivo, Corrección importante sobre el roadmap anterior, Dependencias externas confirmadas (todas con Gradle cache local ya poblada), Estado, Librería repackaged identificada, Mapa de renombrado — Ascendant Attributes (Fase 0), Namespace de assets/data

### Community 20 - "VanillaEquipmentSlot.java"
Cohesion: 0.43
Nodes (5): EquipmentSlot, ItemStack, LivingEntity, Override, VanillaEquipmentSlot

### Community 21 - "Ascendant Attributes"
Cohesion: 0.33
Nodes (5): Ascendant Attributes, Installation, License, Requirements, Status

### Community 22 - "Comparators"
Cohesion: 0.40
Nodes (3): Comparators, Registry, SafeVarargs

### Community 23 - "CLAUDE.md — ascendant_attributes (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — ascendant_attributes (26.2), Prioridad de instrucciones, Workflow del mod

## Knowledge Gaps
- **42 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1`, `Status`, `Requirements` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `StackAttributeModifiers` connect `Entry` to `Common Setup`, `Configuration Management`?**
  _High betweenness centrality (0.090) - this node is a cross-community bridge._
- **Why does `EntitySlotGroup` connect `Server Startup` to `Common Setup`, `Configuration Management`, `Entry`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Why does `CooldownTracker` connect `CooldownTracker` to `Item Management`, `Configuration Management`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Item Management` be split into smaller, more focused modules?**
  _Cohesion score 0.09365079365079365 - nodes in this community are weakly interconnected._
- **Should `Configuration Management` be split into smaller, more focused modules?**
  _Cohesion score 0.0708245243128964 - nodes in this community are weakly interconnected._
- **Should `Event Handling` be split into smaller, more focused modules?**
  _Cohesion score 0.07422402159244265 - nodes in this community are weakly interconnected._