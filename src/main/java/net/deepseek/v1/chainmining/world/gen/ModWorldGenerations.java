package net.deepseek.v1.chainmining.world.gen;

import net.deepseek.v1.chainmining.world.ore.ModOreGeneration;

public class ModWorldGenerations {
    public static void register() {
        ModOreGeneration.generateOres();
    }
}
