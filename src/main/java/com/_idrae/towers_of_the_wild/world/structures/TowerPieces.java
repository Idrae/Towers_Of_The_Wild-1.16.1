package com._idrae.towers_of_the_wild.world.structures;

import com._idrae.towers_of_the_wild.TowersOfTheWild;
import com._idrae.towers_of_the_wild.config.TowersOfTheWildConfig;
import com._idrae.towers_of_the_wild.util.RegistryHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class TowerPieces {

    // private static final ResourceLocation TOWER_TOP = new ResourceLocation(TowersOfTheWild.MOD_ID, "tower_top");
    private static final ResourceLocation TOWER_TOP = ModList.get().isLoaded("waystones") && TowersOfTheWildConfig.waystonesCompat ? new ResourceLocation(TowersOfTheWild.MOD_ID, "waystone_tower_top") : new ResourceLocation(TowersOfTheWild.MOD_ID, "tower_top");
    private static final ResourceLocation TOWER_BOTTOM = new ResourceLocation(TowersOfTheWild.MOD_ID, "tower_bottom");

    private static final ResourceLocation TOWER_CHEST = new ResourceLocation(TowersOfTheWild.MOD_ID, "chests/tower_chest");
    private static final Map<ResourceLocation, BlockPos> CENTER_TOP_OFFSETS = ImmutableMap.of(TOWER_TOP, new BlockPos(6, 28, 6), TOWER_BOTTOM, new BlockPos(3, 31, 3));
    private static final Map<ResourceLocation, BlockPos> CORNER_RELATIVE_POSITIONS = ImmutableMap.of(TOWER_TOP, new BlockPos(-3, 31, -3), TOWER_BOTTOM, BlockPos.ZERO);


    public static void addPieces(TemplateManager templateManager, BlockPos absolutePos, Rotation rotation, List<StructurePiece> pieces, Random random, NoFeatureConfig config) {
        pieces.add(new Piece(templateManager, TOWER_BOTTOM, absolutePos, rotation));
        pieces.add(new Piece(templateManager, TOWER_TOP, absolutePos, rotation));

    }

    public static class Piece extends AbstractTowerPiece {

        public Piece(TemplateManager templateManager, ResourceLocation structurePart, BlockPos absolutePos, Rotation rotation) {
            super(templateManager, structurePart, rotation, RegistryHandler.TOWER_PIECE, CENTER_TOP_OFFSETS);
            BlockPos relativePos = CORNER_RELATIVE_POSITIONS.get(structurePart);
            this.templatePosition = absolutePos.add(relativePos.getX(), relativePos.getY(), relativePos.getZ());
            this.func_207614_a(templateManager);
        }

        public Piece(TemplateManager p_i50566_1_, CompoundNBT p_i50566_2_) {
            super(p_i50566_1_, RegistryHandler.TOWER_PIECE, p_i50566_2_, CENTER_TOP_OFFSETS);
        }

        @Override
        public boolean func_230383_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGenerator, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn, BlockPos blockPosIn) {

            if (this.structurePart.equals(TOWER_BOTTOM)) {
                BlockPos blockpos1 = this.templatePosition;
                // BlockPos blockpos1 = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(- relativePos.getX(), 0, - relativePos.getZ())));*

                // setting spawn height
                int height;
                int minHeight = Integer.MAX_VALUE;
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        height = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + i, blockpos1.getZ() + j);
                        if (height < minHeight) {
                            minHeight = height;
                        }
                    }
                }

                // replacing dirt or water blocks beneath tower by grass
                for (int i = -1; i < 6; ++i) {
                    for (int j = -1; j < 6; ++j) {
                        BlockPos grassPos = new BlockPos(blockpos1.getX() + i, minHeight - 1, blockpos1.getZ() + j);
                        BlockState blockstate = seedReader.getBlockState(grassPos);
                        if (blockstate.getBlock() == Blocks.DIRT) {
                            seedReader.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                        }

                        if (!((i == -1 || i == 5) && (j == -1 || j == 5))) {
                            if (blockstate.getBlock() == Blocks.WATER) {
                                seedReader.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                            }
                        }
                    }
                }
                this.templatePosition = this.templatePosition.add(0, minHeight - 90, 0);

            } else if (this.structurePart.equals(TOWER_TOP)) {
                BlockPos blockpos1 = this.templatePosition;
                // BlockPos blockpos1 = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(- relativePos.getX(), 0, - relativePos.getZ())));
                int height;
                int minHeight = Integer.MAX_VALUE;
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        height = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + 3 + i, blockpos1.getZ() + 3 + j);
                        if (height < minHeight) {
                            minHeight = height;
                        }
                    }
                }
                this.templatePosition = this.templatePosition.add(0, minHeight - 90, 0);
            }
            return super.func_230383_a_(seedReader, structureManager, chunkGenerator, randomIn, mutableBoundingBoxIn, chunkPosIn, blockPosIn);
        }
    }
}
