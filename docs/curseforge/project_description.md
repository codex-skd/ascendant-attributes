<h1 align="center">&#128142; Ascendant Attributes</h1>

<p align="center"><strong>20 custom attributes, 7 status effects, and a full in-game attribute-inspection GUI.</strong></p>

<p align="center">
<img src="https://img.shields.io/badge/loader-NeoForge-orange?style=plastic&logo=curseforge" alt="NeoForge">
<img src="https://img.shields.io/badge/minecraft-26.2%20%7C%201.21.1-blue?style=plastic" alt="Minecraft 26.2 and 1.21.1">
<img src="https://img.shields.io/badge/type-library-brightgreen?style=plastic" alt="Library">
<img src="https://img.shields.io/badge/license-MIT-lightgrey?style=plastic" alt="MIT License">
</p>

<br>

---

<br>

<h2>&#10024; Overview</h2>

<table>
<tr>
<td width="65%">
<p>Ascendant Attributes is a <strong>library</strong> mod that adds a rich set of custom player attributes and status effects, plus a GUI to inspect exactly how each attribute's final value is built up from its base value, modifiers and formulas. It is a foundation other mods and modpacks can build affixes, equipment bonuses and combat mechanics on top of &mdash; it adds no content of its own.</p>

<p>A fork of <a href="https://www.curseforge.com/minecraft/mc-mods/apothic-attributes"><strong>Apothic Attributes</strong></a> by <em>Shadows_of_Fire</em> / Stormraven Studios. Not affiliated with or endorsed by the original author.</p>
</td>
<td width="35%" align="center">
<a href="https://codex.skdragons.com/" target="_blank"><img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="160"></a>
</td>
</tr>
</table>

<br>

<h2>&#9876;&#65039; Attributes</h2>

<p>20 custom attributes covering offense, defense and utility:</p>

<ul>
<li><strong>Offense</strong>: Crit Chance, Crit Damage, Cold Damage, Fire Damage, Life Steal, Current HP Damage, Arrow Damage, Arrow Velocity, Draw Speed, Projectile Damage, Armor Pierce, Armor Shred, Protection Pierce, Protection Shred.</li>
<li><strong>Defense &amp; Utility</strong>: Overheal, Dodge Chance, Healing Received, Experience Gained, Elytra Flight, Creative Flight, Cooldown Reduction.</li>
</ul>

<br>

<h2>&#128171; Status Effects</h2>

<p>7 custom status effects tied into the attribute system: Ancient Knowledge, Bursting Vitality, Grievous Wounds, Flying, Sundering, Bleeding, and Flaming Detonation.</p>

<br>

<h2>&#128421;&#65039; Attribute GUI</h2>

<p>Hold Shift while viewing your attributes for a full breakdown per attribute &mdash; base value, minimum/maximum, every modifier applied (flat addition, multiply base, multiply total), and the formula used to combine them.</p>

<br>

<h2>&#128295; Configurable Combat Formulas</h2>

<p>Combat math (crit damage, life steal, armor/protection pierce and shred, and more) is driven by expressions evaluated at runtime via a bundled expression engine (EvalEx), so server owners can retune balance without recompiling.</p>

<br>

<h2>&#129513; Regalia Slots Compatibility</h2>

<p>When <a href="https://www.curseforge.com/minecraft/mc-mods/regalia-slots-api">Regalia Slots API</a> (our fork of Curios API) is installed, attribute modifiers on items equipped in accessory slots are recognized by both the combat logic and the attribute GUI.</p>

<br>

<h2>&#129521; Mod Structure</h2>

<table>
<tr><th align="left">Area</th><th align="left">What it provides</th></tr>
<tr><td><code>api</code></td><td>The public surface: the attribute registry objects, the attribute-value helper, the configurable combat-rules engine, and the ability-cooldown tracker.</td></tr>
<tr><td><code>mob_effect</code></td><td>The 7 custom status effects and their per-tick behaviour.</td></tr>
<tr><td><code>modifiers</code></td><td>Equipment-slot helpers for reading attribute modifiers off equipped/held/accessory items.</td></tr>
<tr><td><code>impl</code> / <code>event</code></td><td>The event handlers that apply the attributes to real combat, movement and healing.</td></tr>
<tr><td><code>client</code></td><td>The Shift-to-inspect attribute GUI and its components.</td></tr>
<tr><td><code>compat</code></td><td>Regalia Slots API (Curios) integration &mdash; optional, only active when that mod is present.</td></tr>
<tr><td><code>payload</code> / <code>commands</code> / <code>data</code></td><td>Config sync + crit-particle packets, the debug commands, and the data-generation providers.</td></tr>
</table>

<br>

<h2>&#128203; Requirements</h2>

<table>
<tr><td><strong>Minecraft / NeoForge</strong></td><td>see <em>Available Versions</em> below</td></tr>
<tr><td><strong>Common Toolkit</strong></td><td>Required</td></tr>
<tr><td><strong>Regalia Slots API</strong></td><td>Optional</td></tr>
<tr><td><strong>Side</strong></td><td>Client and Server (required on both)</td></tr>
</table>

<br>

<h2>&#128230; Available Versions</h2>

<table>
<tr><th align="left">Minecraft</th><th align="left">NeoForge</th><th align="left">Java</th><th align="left">Latest build</th><th align="left">Status</th></tr>
<tr><td>26.2</td><td>26.2.0.37-beta+</td><td>25</td><td><code>1.1.0</code></td><td>Stable</td></tr>
<tr><td>1.21.1</td><td>21.1.249+</td><td>21</td><td><code>0.0.0-beta.1</code></td><td>Beta &mdash; re-fork port from upstream Apothic Attributes 1.21</td></tr>
</table>

<p><em>Both versions share this CurseForge project. Pick the file that matches your Minecraft version.</em></p>

<br>

---

<br>

<h2>&#128591; Credits &amp; License</h2>

<p>Ascendant Attributes is a fork of <a href="https://www.curseforge.com/minecraft/mc-mods/apothic-attributes">Apothic Attributes</a> by <strong>Shadows_of_Fire</strong> / <strong>Stormraven Studios, LLC</strong>, rebranded and ported to NeoForge by <strong>Stalking Dragons</strong>. The <code>1.21.1</code> build is a re-fork from the upstream Apothic Attributes 1.21 sources.</p>

<p><strong>License:</strong> <strong>MIT</strong>, same as upstream (the original <code>Copyright (c) 2023-2025 Stormraven Studios, LLC</code> notice is kept in the jar and repository <code>LICENSE</code>). Bundled / required dependencies keep their own licenses: <strong>Common Toolkit</strong> (required) is LGPL-2.1-or-later, <strong>Regalia Slots API</strong> (optional) is LGPL-3.0-or-later, and the embedded <strong>EvalEx</strong> expression engine is MIT.</p>

<br>
<br>

<p align="center">
  <a href="https://codex.skdragons.com/" target="_blank">
    <img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="200">
  </a>
  <br>
  <a href="https://codex.skdragons.com/">https://codex.skdragons.com/</a>
  <br>
  <em>Codex Stalking Dragons &mdash; Minecraft Modding</em>
</p>
