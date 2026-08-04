# Graph Report - .  (2026-08-04)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 50 nodes · 69 edges · 12 communities (6 shown, 6 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 321 input · 108 output

## Graph Freshness
- Built from commit: `b0703b9b`
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

## God Nodes (most connected - your core abstractions)
1. `AscendantAttributes` - 17 edges
2. `Config` - 7 edges
3. `AscendantAttributesClient` - 5 edges
4. `Mod Icon` - 1 edges
5. `Project Variables` - 1 edges
6. `Project Logo` - 0 edges

## Surprising Connections (you probably didn't know these)
- `Mod Icon` ----> `Project Variables`  [EXTRACTED]
  src/main/resources/assets/ascendant_attributes/icon.png → docs/curseforge/project_vars.md

## Import Cycles
- None detected.

## Communities (12 total, 6 thin omitted)

### Community 0 - "Item Management"
Cohesion: 0.28
Nodes (12): Block, BlockItem, Blocks, CreativeModeTab, DeferredBlock, DeferredHolder, DeferredItem, DeferredRegister (+4 more)

### Community 1 - "Configuration Management"
Cohesion: 0.25
Nodes (7): BooleanValue, Builder, ConfigValue, IntValue, Item, ModConfigSpec, Config

### Community 2 - "Client Initialization"
Cohesion: 0.36
Nodes (6): EventBusSubscriber, FMLClientSetupEvent, AscendantAttributesClient, Mod, ModContainer, SubscribeEvent

### Community 3 - "Build Tools"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **3 isolated node(s):** `Project Logo`, `Mod Icon`, `Project Variables`
  These have ≤1 connection - possible missing edges or undocumented components.
- **6 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AscendantAttributes` connect `Item Management` to `Configuration Management`, `Event Handling`, `Server Startup`, `Creative Mode Content`, `Common Setup`?**
  _High betweenness centrality (0.240) - this node is a cross-community bridge._
- **What connects `Project Logo`, `Mod Icon`, `Project Variables` to the rest of the system?**
  _3 weakly-connected nodes found - possible documentation gaps or missing edges._