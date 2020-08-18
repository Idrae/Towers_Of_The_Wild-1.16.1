package com._idrae.towers_of_the_wild.world.structures;

import com._idrae.towers_of_the_wild.TowersOfTheWild;
import com._idrae.towers_of_the_wild.config.TowersOfTheWildConfig;
import com._idrae.towers_of_the_wild.util.RegistryHandler;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;


public class TowerStructure extends Structure<NoFeatureConfig> {

    public static final String NAME = TowersOfTheWild.MOD_ID +  ":tower";
    private static final int SEPARATION = 5;
    private static final int SEED_MODIFIER = 16897777;

    public TowerStructure(Codec<NoFeatureConfig> p_i231965_1_) {
        super(p_i231965_1_);
    }

    @Override
    public String getStructureName() {
        return NAME;
    }

    public int getSize() {
        return 1;
    }

    public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean func_230365_b_() {
        return false;
    }

    protected int getSeedModifier() {
        return SEED_MODIFIER;
    }

    @Nullable
    @Override
    public BlockPos func_236388_a_(IWorldReader p_236388_1_, StructureManager p_236388_2_, BlockPos p_236388_3_, int p_236388_4_, boolean p_236388_5_, long p_236388_6_, StructureSeparationSettings p_236388_8_) {
        return super.func_236388_a_(p_236388_1_, p_236388_2_, p_236388_3_, p_236388_4_, p_236388_5_, p_236388_6_, new StructureSeparationSettings(TowersOfTheWildConfig.rarity, SEPARATION, getSeedModifier()));
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator p_230363_1_, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_) {
        return isSurfaceFlat(p_230363_1_, p_230363_6_, p_230363_7_);
    }

    protected boolean isSurfaceFlat(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
        // Size of the area to check.
        int offset = getSize() * 16;

        int xStart = (chunkX << 4);
        int zStart = (chunkZ << 4);

        int i1 = generator.func_222531_c(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
        int j1 = generator.func_222531_c(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
        int k1 = generator.func_222531_c(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
        int l1 = generator.func_222531_c(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
        int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
        int maxHeight = Math.max(Math.max(i1, j1), Math.max(k1, l1));
        return Math.abs(maxHeight - minHeight) <= 20;
    }


    @Override
    public ChunkPos func_236392_a_(StructureSeparationSettings p_236392_1_, long p_236392_2_, SharedSeedRandom p_236392_4_, int p_236392_5_, int p_236392_6_) {
        int spacing = TowersOfTheWildConfig.rarity;
        int gridX = ((p_236392_5_ / spacing) * spacing);
        int gridZ = ((p_236392_6_ / spacing) * spacing);

        int offset = SEPARATION + 1;
        p_236392_4_.setLargeFeatureSeedWithSalt(p_236392_2_, gridX, gridZ, this.getSeedModifier());
        int offsetX = p_236392_4_.nextInt(offset);
        int offsetZ = p_236392_4_.nextInt(offset);

        int gridOffsetX = gridX + offsetX;
        int gridOffsetZ = gridZ + offsetZ;

        return new ChunkPos(gridOffsetX, gridOffsetZ);
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> p_i225806_1_, int p_i225806_2_, int p_i225806_3_, MutableBoundingBox p_i225806_4_, int p_i225806_5_, long p_i225806_6_) {
            super(p_i225806_1_, p_i225806_2_, p_i225806_3_, p_i225806_4_, p_i225806_5_, p_i225806_6_);
        }

        @Override
        public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            int i = chunkX * 16;
            int j = chunkZ * 16;
            BlockPos blockpos = new BlockPos(i + 3, 90, j + 3);
            // Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            Rotation rotation = Rotation.NONE;

            if (biomeIn.getCategory() == Biome.Category.JUNGLE) {
                JungleTowerPieces.addPieces(templateManagerIn, blockpos, rotation, this.components, this.rand, (NoFeatureConfig) config);
            } else if (biomeIn.getCategory() == Biome.Category.ICY) {
                // blockpos.add(-2, 0, -2);
                IceTowerPieces.addPieces(templateManagerIn, blockpos, rotation, this.components, this.rand, (NoFeatureConfig) config);
            } else {
                if (this.rand.nextInt(100) < TowersOfTheWildConfig.derelictTowerProportion) {
                    // blockpos.add(-2, 0, -2);
                    blockpos = new BlockPos(i, 90, j);
                    if (biomeIn.getCategory() == Biome.Category.PLAINS
                            || biomeIn.getCategory() == Biome.Category.FOREST
                            || biomeIn.getCategory() == Biome.Category.TAIGA
                            || biomeIn.getCategory() == Biome.Category.SAVANNA
                            || biomeIn.getCategory() == Biome.Category.EXTREME_HILLS) {
                        DerelictTowerGrassPieces.addPieces(templateManagerIn, blockpos, rotation, this.components, this.rand, (NoFeatureConfig) config);
                    } else {
                        DerelictTowerPieces.addPieces(templateManagerIn, blockpos, rotation, this.components, this.rand, (NoFeatureConfig) config);
                    }
                } else {
                    TowerPieces.addPieces(templateManagerIn, blockpos, rotation, this.components, this.rand, (NoFeatureConfig) config);
                }
            }
            this.recalculateStructureSize();
        }
    }
}
