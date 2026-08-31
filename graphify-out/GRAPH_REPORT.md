# Graph Report - 1.21.1  (2026-08-31)

## Corpus Check
- 79 files · ~29,922 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 771 nodes · 1564 edges · 47 communities (46 shown, 1 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 26 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5ce202ca`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- AttributeEvents.java
- AscendantAttributesClientHandler.java
- AttributesGui
- StackAttributeModifiersEvent
- ModifierSource
- AscendantAttributesObjects.java
- EntitySlotGroup
- ConfigPayload.java
- LivingEntityMixin.java
- Port Report — Ascendant Attributes 1.21.1 / NeoForge 21.1.249
- AuxDmgTracker
- CooldownTracker
- AscendantAttributes.java
- AttributeHelper.java
- MobEffect
- Flujo de trabajo — Ascendant Attributes (NeoForge)
- MixProvider.java
- NearestAttackableTargetGoalMixin.java
- PlayerMixin.java
- CurseForge — Variables del proyecto
- Delegation brief — Ascendant Attributes: finish the 1.21.1 / NeoForge 21.1.249 port
- Roadmap — Ascendant Attributes (port de Apothic Attributes)
- ThrownTridentMixin.java
- AbstractContainerScreenMixin.java
- Mapa de renombrado — Ascendant Attributes (Fase 0)
- Ascendant Attributes
- BleedingEffect
- DetonationEffect
- FlyingEffect.java
- .init
- BonusModifierCommand.java
- [0.0.0-beta.1] - 2026-08-31
- AscendantAttributesClient.java
- gradlew
- .getTooltipFlag

## God Nodes (most connected - your core abstractions)
1. `AttributesGui` - 39 edges
2. `AttributeEvents` - 30 edges
3. `EntitySlotGroup` - 24 edges
4. `StackAttributeModifiersEvent` - 23 edges
5. `AuxDmgTracker` - 22 edges
6. `EntityEquipmentSlot` - 19 edges
7. `StackAttributeModifiers` - 19 edges
8. `Entry` - 19 edges
9. `AscendantAttributes` - 18 edges
10. `CooldownTracker` - 14 edges

## Surprising Connections (you probably didn't know these)
- `BuiltInRegs` --references--> `EntityEquipmentSlot`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/EntityEquipmentSlot.java
- `BuiltInRegs` --references--> `EntitySlotGroup`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/EntitySlotGroup.java
- `Components` --references--> `StackAttributeModifiers`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/modifiers/StackAttributeModifiers.java
- `Attachments` --references--> `CooldownTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/api/CooldownTracker.java
- `Attachments` --references--> `AuxDmgTracker`  [EXTRACTED]
  src/main/java/com/skd/ascendantattributes/api/AscendantAttributesObjects.java → src/main/java/com/skd/ascendantattributes/util/AuxDmgTracker.java

## Import Cycles
- None detected.

## Communities (47 total, 1 thin omitted)

### Community 0 - "AttributeEvents.java"
Cohesion: 0.07
Nodes (28): AddReloadListenerEvent, BlockDropsEvent, BreakSpeed, CommandBuildContext, CriticalHitEvent, EntityJoinLevelEvent, Event, ItemAttributeModifierEvent (+20 more)

### Community 1 - "AscendantAttributesClientHandler.java"
Cohesion: 0.07
Nodes (30): AnchorPoint, ClientLevel, Configuration, CritParticle, Expression, FMLClientSetupEvent, GatherEffectScreenTooltipsEvent, ItemTooltipEvent (+22 more)

### Community 2 - "AttributesGui"
Cohesion: 0.08
Nodes (27): AbstractButton, AttributeInstance, Box, ChatFormatting, DecimalFormat, GuiEventListener, InventoryScreen, NarrationElementOutput (+19 more)

### Community 3 - "StackAttributeModifiersEvent"
Cohesion: 0.10
Nodes (22): Builder, Entry, Attribute, AttributeModifier, Codec, Holder, ItemAttributeModifiers, RegistryFriendlyByteBuf (+14 more)

### Community 4 - "ModifierSource"
Cohesion: 0.07
Nodes (28): BufferSource, ClientTooltipComponent, FormattedCharSequence, FormattedText, Matrix4f, SafeVarargs, AttributeModifierComponent, Font (+20 more)

### Community 5 - "AscendantAttributesObjects.java"
Cohesion: 0.07
Nodes (36): AttachmentType, DataComponentType, Experimental, SoundEvent, AscendantAttributesObjects, Attachments, Attributes, BuiltInRegs (+28 more)

### Community 6 - "EntitySlotGroup"
Cohesion: 0.10
Nodes (28): BiMap, CurioAttributeModifierEvent, EquipmentSlotGroup, IDynamicStackHandler, HolderSet, CurioEquipmentSlot, CurioStackIterator, ItemStack (+20 more)

### Community 7 - "ConfigPayload.java"
Cohesion: 0.11
Nodes (22): CustomPacketPayload, PayloadProvider, ConfigPayload, ConnectionProtocol, FriendlyByteBuf, IPayloadContext, Override, PacketFlow (+14 more)

### Community 8 - "LivingEntityMixin.java"
Cohesion: 0.12
Nodes (18): DamageContainer, Entity, ModifyVariable, Redirect, Shadow, EntityMixin, Mixin, DamageSource (+10 more)

### Community 9 - "Port Report — Ascendant Attributes 1.21.1 / NeoForge 21.1.249"
Cohesion: 0.07
Nodes (27): A.1: Deleted `compat/AttributesJEIPlugin.java`, A.2: Deleted `client/CuriosClientCompat.java`, A.3: `client/AttributesGui.java:312` — `renderTooltipInternal`, A.4: `impl/AttributeEvents.java:344-347` — `piercingIgnoreEntityIds`, A.5: AT file updated with all upstream entries, `accesstransformer.cfg`, API Decisions & Notes, `ascendant_attributes.mixins.json` (+19 more)

### Community 10 - "AuxDmgTracker"
Cohesion: 0.20
Nodes (12): IdentityHashMap, Marker, AuxDmgTracker, Entry, Attribute, Codec, DamageSource, DamageType (+4 more)

### Community 11 - "CooldownTracker"
Cohesion: 0.16
Nodes (10): Object2LongMap, PlayerLoggedInEvent, AbilityCooldowns, LivingEntity, ResourceLocation, CooldownTracker, Codec, RegistryFriendlyByteBuf (+2 more)

### Community 12 - "AscendantAttributes.java"
Cohesion: 0.14
Nodes (13): AttackEntityEvent, DeferredHelper, EntityAttributeModificationEvent, Logger, AscendantAttributes, Attribute, Entity, EntityType (+5 more)

### Community 13 - "AttributeHelper.java"
Cohesion: 0.25
Nodes (12): Deprecated, Multimap, AttributeHelper, Attribute, AttributeModifier, Entry, Holder, ItemAttributeModifiers (+4 more)

### Community 14 - "MobEffect"
Cohesion: 0.19
Nodes (6): MobEffect, ResourceLocation, GrievousEffect, KnowledgeEffect, SunderingEffect, VitalityEffect

### Community 15 - "Flujo de trabajo — Ascendant Attributes (NeoForge)"
Cohesion: 0.15
Nodes (12): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Ascendant Attributes (NeoForge), Flujo por tarea, Idioma (+4 more)

### Community 16 - "MixProvider.java"
Cohesion: 0.27
Nodes (8): DynamicRegistryProvider, Item, JsonMix, PackOutput, Provider, Holder, Potion, MixProvider

### Community 17 - "NearestAttackableTargetGoalMixin.java"
Cohesion: 0.33
Nodes (8): CallbackInfo, Mob, Inject, LivingEntity, Mixin, NearestAttackableTargetGoalMixin, TargetGoal, TargetingConditions

### Community 18 - "PlayerMixin.java"
Cohesion: 0.35
Nodes (8): DamageSource, Entity, LivingEntity, Mixin, Operation, Player, PlayerMixin, WrapOperation

### Community 19 - "CurseForge — Variables del proyecto"
Cohesion: 0.18
Nodes (10): CurseForge — Variables del proyecto, Datos usados para el alta ("Submit a Project"), Icono / imagen del proyecto, Nota, Nota para revisores de CurseForge (validación del proyecto), Proyecto, Rama, Tag (+2 more)

### Community 20 - "Delegation brief — Ascendant Attributes: finish the 1.21.1 / NeoForge 21.1.249 port"
Cohesion: 0.20
Nodes (9): Delegation brief — Ascendant Attributes: finish the 1.21.1 / NeoForge 21.1.249 port, Deliverable, HARD CONSTRAINTS, Mission, Paths (all inside the work dir — sandbox blocks outside reads), TASK A — make it compile (34 errors, ~5 clusters), TASK B — carry over the 26.2 fork's changes, TASK C — metadata (+1 more)

### Community 21 - "Roadmap — Ascendant Attributes (port de Apothic Attributes)"
Cohesion: 0.20
Nodes (9): Base legal — obligatorio mantener siempre, Convención de renombrado, Cómo se alimenta a OpenCode, Dependencias externas — resueltas en Fase 0, Estado, Fase 0 — completada (ver `docs/ASCENDANT_ATTRIBUTES_RENAME_MAP.md` para el detalle completo), Fases, Naturaleza del proyecto (+1 more)

### Community 22 - "ThrownTridentMixin.java"
Cohesion: 0.39
Nodes (6): AbstractArrow, ModifyConstant, EntityType, Level, Mixin, ThrownTridentMixin

### Community 23 - "AbstractContainerScreenMixin.java"
Cohesion: 0.36
Nodes (6): CallbackInfoReturnable, Screen, AbstractContainerScreenMixin, Component, Inject, Mixin

### Community 24 - "Mapa de renombrado — Ascendant Attributes (Fase 0)"
Cohesion: 0.25
Nodes (7): Convención de renombrado por archivo, Corrección importante sobre el roadmap anterior, Dependencias externas confirmadas (todas con Gradle cache local ya poblada), Estado, Librería repackaged identificada, Mapa de renombrado — Ascendant Attributes (Fase 0), Namespace de assets/data

### Community 25 - "Ascendant Attributes"
Cohesion: 0.29
Nodes (6): Ascendant Attributes, Features, Installation, License, Requirements, Status

### Community 26 - "BleedingEffect"
Cohesion: 0.38
Nodes (3): BleedingEffect, LivingEntity, Override

### Community 27 - "DetonationEffect"
Cohesion: 0.38
Nodes (3): DetonationEffect, LivingEntity, Override

### Community 28 - "FlyingEffect.java"
Cohesion: 0.40
Nodes (3): AttributeMap, FlyingEffect, Override

### Community 29 - ".init"
Cohesion: 0.40
Nodes (3): FMLCommonSetupEvent, GatherDataEvent, SubscribeEvent

### Community 30 - "BonusModifierCommand.java"
Cohesion: 0.60
Nodes (4): BonusModifierCommand, CommandSourceStack, LiteralArgumentBuilder, SuggestionProvider

### Community 31 - "[0.0.0-beta.1] - 2026-08-31"
Cohesion: 0.40
Nodes (4): [0.0.0-beta.1] - 2026-08-31, Added, Ascendant Attributes (1.21.1) — Changelog, Technical

### Community 32 - "AscendantAttributesClient.java"
Cohesion: 0.83
Nodes (3): EventBusSubscriber, AscendantAttributesClient, Mod

### Community 33 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **68 isolated node(s):** `Added`, `Technical`, `Status`, `Features`, `Requirements` (+63 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `StackAttributeModifiersEvent` connect `StackAttributeModifiersEvent` to `AttributeEvents.java`, `EntitySlotGroup`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **Why does `CooldownTracker` connect `CooldownTracker` to `AscendantAttributes.java`, `AscendantAttributesObjects.java`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Why does `AttributesGui` connect `AttributesGui` to `AscendantAttributesClientHandler.java`?**
  _High betweenness centrality (0.047) - this node is a cross-community bridge._
- **What connects `Added`, `Technical`, `Status` to the rest of the system?**
  _68 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `AttributeEvents.java` be split into smaller, more focused modules?**
  _Cohesion score 0.06656426011264721 - nodes in this community are weakly interconnected._
- **Should `AscendantAttributesClientHandler.java` be split into smaller, more focused modules?**
  _Cohesion score 0.06604324956165984 - nodes in this community are weakly interconnected._
- **Should `AttributesGui` be split into smaller, more focused modules?**
  _Cohesion score 0.0780399274047187 - nodes in this community are weakly interconnected._