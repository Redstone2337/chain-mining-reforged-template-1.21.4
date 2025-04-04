package net.deepseek.v1.chainmining.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.deepseek.v1.chainmining.core.keys.ModKeys;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Range;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Config(name = "chainminingreforged_config")
public class ModConfig implements ConfigData {
    private String keys;


    @Comment("是否启用连锁采集")
    public boolean enabled = true;

//    @Comment("需要按住的键（空字符表示不需要按键）")
//    @ConfigEntry.Gui.Excluded // 从 GUI 中排除，使用自定义的按键绑定 UI
//    public String requireKey = getKeys();

    @Comment("最大连锁数量（0为无限制）")
    public int maxChainCount = 10;

    @Comment("每次连锁消耗的耐久倍数")
    public float durabilityMultiplier = 1.0f;

    @Comment("允许使用的工具类型 [pickaxe, axe, shovel, hoe]")
    public String[] allowedTools = {"pickaxe", "axe", "shovel"};

    @Comment("方块过滤模式：true=白名单，false=黑名单")
    public boolean useBlockWhitelist = true;

    @Comment("默认允许连锁的方块列表（白名单模式）")
    public List<String> defaultAllowedBlocks = Arrays.asList(
            "minecraft:oak_log",
            "minecraft:stone",
            "minecraft:dirt",
            "minecraft:coal_ore"
    );

    @Comment("默认禁止连锁的方块列表（黑名单模式）")
    public List<String> defaultDeniedBlocks = Arrays.asList(
            "minecraft:bedrock",
            "minecraft:spawner",
            "minecraft:air"
    );

    @Comment("自定义允许连锁的方块列表（白名单模式）")
    public Set<String> customAllowedBlocks = new HashSet<>();

    @Comment("自定义禁止连锁的方块列表（黑名单模式）")
    public Set<String> customDeniedBlocks = new HashSet<>();

    @Comment("最大信号传输距离")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
    public int maxPowerDistance = 32;

    @Comment("当前信号传输距离")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 128)
    public int currentPowerDistance = 15;

    @Comment("最大生成数量")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int spawnMaxCount = 20;

    @Comment("默认生成数量")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int spawnNormalCount = 10;


    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}