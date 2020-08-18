package com._idrae.towers_of_the_wild.world;

import com._idrae.towers_of_the_wild.TowersOfTheWild;
import com._idrae.towers_of_the_wild.config.TowersOfTheWildConfig;
import com._idrae.towers_of_the_wild.util.RegistryHandler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.SnowyTundraBiome;
import net.minecraft.world.chunk.storage.ChunkSerializer;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IglooPieces;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class WorldInit {

    public static void setup() {

        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!TowersOfTheWildConfig.allModBiomesBlackList.contains(biome.getRegistryName().getNamespace())) {
                if (!TowersOfTheWildConfig.biomeBlackList.contains(biome.getRegistryName().toString())) {
                    addSurfaceStructure(biome, RegistryHandler.TOWER.get());
                }
            }
        }
    }

    private static void addSurfaceStructure(Biome biome, Structure<NoFeatureConfig> structure) {
        biome.func_235063_a_(structure.func_236391_a_(NoFeatureConfig.field_236559_b_));
    }
}
