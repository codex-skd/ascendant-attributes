# Graph Report - 26.2  (2026-08-04)

## Corpus Check
- 43 files · ~19,169 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 476 nodes · 977 edges · 30 communities (27 shown, 3 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 22 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `d9263fc5`
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
- AttributesCommandEvent
- DetonationEffect.java

## God Nodes (most connected - your core abstractions)
1. `AttributeEvents` - 29 edges
2. `EntitySlotGroup` - 24 edges
3. `AuxDmgTracker` - 22 edges
4. `StackAttributeModifiersEvent` - 21 edges
5. `StackAttributeModifiers` - 19 edges
6. `Entry` - 19 edges
7. `AscendantAttributes` - 18 edges
8. `CooldownTracker` - 14 edges
9. `EntityEquipmentSlot` - 14 edges
10. `StackAttributeModifiersBuilder` - 13 edges

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

## Communities (30 total, 3 thin omitted)

### Community 0 - "Item Management"
Cohesion: 0.06
Nodes (30): AttackEntityEvent, AttributeMap, Client, DeferredHelper, Entity, EntityAttributeModificationEvent, EntityType, FMLCommonSetupEvent (+22 more)

### Community 1 - "Configuration Management"
Cohesion: 0.07
Nodes (32): AttachmentType, DataComponentType, Experimental, Potion, SimpleParticleType, SoundEvent, AscendantAttributesObjects, Attachments (+24 more)

### Community 2 - "Client Initialization"
Cohesion: 0.83
Nodes (3): EventBusSubscriber, AscendantAttributesClient, Mod

### Community 3 - "Build Tools"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 4 - "Event Handling"
Cohesion: 0.36
Nodes (4): BleedingEffect, LivingEntity, Override, ServerLevel

### Community 5 - "Server Startup"
Cohesion: 0.13
Nodes (21): BiMap, EquipmentSlotGroup, BuiltInRegs, EquipmentSlotGroups, HolderSet, Registry, EntityEquipmentSlot, ItemStack (+13 more)

### Community 7 - "Creative Mode Content"
Cohesion: 0.20
Nodes (12): IdentityHashMap, Marker, AuxDmgTracker, Entry, Attribute, Codec, DamageSource, DamageType (+4 more)

### Community 8 - "Common Setup"
Cohesion: 0.20
Nodes (11): Attribute, AttributeModifier, Entry, Holder, Identifier, Internal, ItemStack, Nullable (+3 more)

### Community 12 - "Entry"
Cohesion: 0.18
Nodes (11): Builder, Entry, Attribute, AttributeModifier, Codec, Holder, Identifier, ItemAttributeModifiers (+3 more)

### Community 13 - "AttributesConfig"
Cohesion: 0.16
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
Cohesion: 0.11
Nodes (22): CustomPacketPayload, PayloadProvider, ConfigPayload, ConnectionProtocol, FriendlyByteBuf, IPayloadContext, Override, PacketFlow (+14 more)

### Community 21 - "Ascendant Attributes"
Cohesion: 0.33
Nodes (5): Ascendant Attributes, Installation, License, Requirements, Status

### Community 22 - "Comparators"
Cohesion: 0.40
Nodes (3): Comparators, Registry, SafeVarargs

### Community 23 - "CLAUDE.md — ascendant_attributes (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — ascendant_attributes (26.2), Prioridad de instrucciones, Workflow del mod

### Community 25 - "LEInvoker"
Cohesion: 0.07
Nodes (22): AddServerReloadListenersEvent, BlockDropsEvent, CriticalHitEvent, EntityJoinLevelEvent, ItemAttributeModifierEvent, LivingExperienceDropEvent, LivingHealEvent, LivingIncomingDamageEvent (+14 more)

### Community 28 - "AttributesCommandEvent"
Cohesion: 0.50
Nodes (5): CommandBuildContext, CommandSourceStack, Event, LiteralArgumentBuilder, AttributesCommandEvent

### Community 29 - "DetonationEffect.java"
Cohesion: 0.36
Nodes (4): DetonationEffect, LivingEntity, Override, ServerLevel

## Knowledge Gaps
- **42 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1`, `Status`, `Requirements` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `CooldownTracker` connect `CooldownTracker` to `Item Management`, `Configuration Management`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **Why does `StackAttributeModifiersEvent` connect `Common Setup` to `LEInvoker`, `AttributesCommandEvent`, `Entry`?**
  _High betweenness centrality (0.063) - this node is a cross-community bridge._
- **Why does `AuxDmgTracker` connect `Creative Mode Content` to `Configuration Management`, `LEInvoker`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Item Management` be split into smaller, more focused modules?**
  _Cohesion score 0.05727644652250146 - nodes in this community are weakly interconnected._
- **Should `Configuration Management` be split into smaller, more focused modules?**
  _Cohesion score 0.06753006475485661 - nodes in this community are weakly interconnected._
- **Should `Server Startup` be split into smaller, more focused modules?**
  _Cohesion score 0.12903225806451613 - nodes in this community are weakly interconnected._