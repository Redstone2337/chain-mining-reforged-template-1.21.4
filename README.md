# A commands mod for fabric 1.21.4 minecraft.

### General Section:
1. **Added Commands**:
   - `1) /chainmining or /cmr command, used to modify chain mining configurations.`
   - `2) /sethome command, used to set the nearest home.`
   - `3) /back command, used to return to the last set home.`
   - `4) /health command, mainly for modifying health values.`

2. **About Format**:
   - `/chainmining <allow|denied> [<block>]
        ---[<block : Block> ]`, - If the block parameter is empty, the target will be set to the block the player's cursor is pointing at.
   - `/sethome [<HomePos>]
        ---[<HomePos : Vec3>] `, - If the HomePos parameter is empty, the location will be set to the position the player's cursor is pointing at.
   - `/back [<HomePos>]
    ---[<HomePos : Vec3>] `, - The effect is similar to /sethome, but this one returns to the last set home location.

# Important Screenshots
<div align="center">
  <img src="Require.png" width="30%" alt="GeckoLib4">
</div>

# About Health Command:
### Command Descriptions:
1. **Basic Commands**:
   - `/health add <target> <value>` - Increase the target's current health.
   - `/health remove <target> <value>` - Decrease the target's current health.
   - `/health set <target> <value>` - Directly set the target's current health.

2. **Maximum Health Adjustment**:
   - `/health limit <target> add <value>` - Increase the maximum health.
   - `/health limit <target> remove <value>` - Decrease the maximum health.
   - `/health limit <target> set <value>` - Set the maximum health.

3. **Condition Filtering (modify/distance)**:
   - `/health modify <target> <operation> <value>` - Perform a health operation on the target.
   - `/health modify <target> <operation> <value> distance <x> <y> <z> <radius>` - Perform the operation within a specified area.

### Examples:
1. **Area Healing**:
   ```
   /health modify @e add 10 distance 100 64 100 15
   ```
   - Heal all entities within a 15-block radius centered at (100,64,100) by 10 health points.

2. **Boss Fight Health Lock**:
   ```
   /health limit @e[type=example:boss] set 500
   ```
   - Force set the boss's maximum health to 500 points.

3. **Danger Zone Health Drain**:
   ```
   /execute as @a at @s run health modify @s remove 2 distance ~ ~ ~ 5
   ```
   - Continuously drain health from each player within a 5-block radius (requires a looping command block).
