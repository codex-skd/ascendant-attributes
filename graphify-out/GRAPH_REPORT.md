# Graph Report - 26.2  (2026-08-04)

## Corpus Check
- 95 files · ~23,921 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 631 nodes · 1333 edges · 35 communities (32 shown, 3 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 29 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2ad2bdf5`
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
- VanillaEquipmentSlot.java
- EquipmentSlots
- EquipmentSlotCompat
- EntityEquipmentSlot
- .positionGuiButton

## God Nodes (most connected - your core abstractions)
1. `AttributesGui` - 38 edges
2. `AttributeEvents` - 29 edges
3. `EntitySlotGroup` - 24 edges
4. `AuxDmgTracker` - 22 edges
5. `StackAttributeModifiersEvent` - 21 edges
6. `StackAttributeModifiers` - 19 edges
7. `Entry` - 19 edges
8. `AscendantAttributes` - 18 edges
9. `CooldownTracker` - 14 edges
10. `EntityEquipmentSlot` - 14 edges

## Surprising Connections (you probably didn't know these)
- `Mod Icon` ----> `Project Variables`  [EXTRACTED]
  src/main/resources/assets/ascendant_attributes/icon.png → docs/curseforge/project_vars.md
- `Attachments` --references--> `CooldownTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/api/CooldownTracker.java
- `Attachments` --references--> `AuxDmgTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/util/AuxDmgTracker.java
- `BuiltInRegs` --references--> `EntitySlotGroup`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/EntitySlotGroup.java
- `Components` --references--> `StackAttributeModifiers`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/StackAttributeModifiers.java

## Import Cycles
- None detected.

## Communities (35 total, 3 thin omitted)

### Community 0 - "Item Management"
Cohesion: 0.05
Nodes (34): AttackEntityEvent, AttributeMap, Client, DeferredHelper, Entity, EntityAttributeModificationEvent, EntityType, FMLCommonSetupEvent (+26 more)

### Community 1 - "Configuration Management"
Cohesion: 0.10
Nodes (21): AttachmentType, DataComponentType, SoundEvent, AscendantAttributesObjects, Attachments, Attributes, Components, DamageTypes (+13 more)

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
Cohesion: 0.27
Nodes (8): DynamicRegistryProvider, Item, JsonMix, PackOutput, Provider, Holder, Potion, MixProvider

### Community 7 - "Creative Mode Content"
Cohesion: 0.19
Nodes (12): IdentityHashMap, Marker, AuxDmgTracker, Entry, Attribute, Codec, DamageSource, DamageType (+4 more)

### Community 8 - "Common Setup"
Cohesion: 0.10
Nodes (22): Builder, Entry, Attribute, AttributeModifier, Codec, Holder, Identifier, ItemAttributeModifiers (+14 more)

### Community 12 - "Entry"
Cohesion: 0.05
Nodes (43): AbstractButton, AddClientReloadListenersEvent, AttributeInstance, ChatFormatting, ClientLevel, Component, CritParticle, DecimalFormat (+35 more)

### Community 13 - "AttributesConfig"
Cohesion: 0.15
Nodes (10): AnchorPoint, Configuration, Expression, ResourceManagerReloadListener, CombatRules, DamageSource, LivingEntity, AttributesConfig (+2 more)

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
Cohesion: 0.08
Nodes (24): ClientTooltipComponent, FormattedCharSequence, FormattedText, AttributeModifierComponent, Font, GuiGraphicsExtractor, Identifier, EffectModifierSource (+16 more)

### Community 23 - "CLAUDE.md — ascendant_attributes (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — ascendant_attributes (26.2), Prioridad de instrucciones, Workflow del mod

### Community 25 - "LEInvoker"
Cohesion: 0.08
Nodes (21): AddServerReloadListenersEvent, BlockDropsEvent, CriticalHitEvent, EntityJoinLevelEvent, ItemAttributeModifierEvent, LivingExperienceDropEvent, LivingHealEvent, LivingIncomingDamageEvent (+13 more)

### Community 28 - "AttributesCommandEvent"
Cohesion: 0.23
Nodes (10): CommandBuildContext, Event, RegisterCommandsEvent, BonusModifierCommand, CommandSourceStack, LiteralArgumentBuilder, AttributesCommandEvent, CommandSourceStack (+2 more)

### Community 29 - "DetonationEffect.java"
Cohesion: 0.23
Nodes (10): EquipmentSlotGroups, HolderSet, EntitySlotGroup, Codec, Holder, HolderSet, Identifier, Override (+2 more)

### Community 30 - "VanillaEquipmentSlot.java"
Cohesion: 0.43
Nodes (5): EquipmentSlot, ItemStack, LivingEntity, Override, VanillaEquipmentSlot

### Community 31 - "EquipmentSlots"
Cohesion: 0.24
Nodes (6): Experimental, EquipmentSlots, EquipmentSlot, Holder, MobEffect, MobEffects

### Community 32 - "EquipmentSlotCompat"
Cohesion: 0.56
Nodes (6): BiMap, EquipmentSlotGroup, EquipmentSlotCompat, EquipmentSlot, Holder, Nullable

### Community 33 - "EntityEquipmentSlot"
Cohesion: 0.32
Nodes (5): BuiltInRegs, Registry, EntityEquipmentSlot, ItemStack, LivingEntity

### Community 34 - ".positionGuiButton"
Cohesion: 0.53
Nodes (4): Box, ButtonPlacement, ImageButton, Offset

## Knowledge Gaps
- **42 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1`, `Status`, `Requirements` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `CooldownTracker` connect `CooldownTracker` to `Item Management`, `Configuration Management`?**
  _High betweenness centrality (0.056) - this node is a cross-community bridge._
- **Why does `StackAttributeModifiersEvent` connect `Common Setup` to `LEInvoker`, `AttributesCommandEvent`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `0.0.0-beta.1` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Item Management` be split into smaller, more focused modules?**
  _Cohesion score 0.05081585081585081 - nodes in this community are weakly interconnected._
- **Should `Configuration Management` be split into smaller, more focused modules?**
  _Cohesion score 0.09885057471264368 - nodes in this community are weakly interconnected._
- **Should `Common Setup` be split into smaller, more focused modules?**
  _Cohesion score 0.09831649831649832 - nodes in this community are weakly interconnected._
- **Should `Entry` be split into smaller, more focused modules?**
  _Cohesion score 0.05070028011204482 - nodes in this community are weakly interconnected._