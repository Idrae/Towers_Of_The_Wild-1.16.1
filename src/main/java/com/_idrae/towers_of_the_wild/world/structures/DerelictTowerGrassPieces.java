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
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class DerelictTowerGrassPieces {
    private static final ResourceLocation DERELICT_TOWER_TOP = ModList.get().isLoaded("waystones") && TowersOfTheWildConfig.waystonesCompat ? new ResourceLocation(TowersOfTheWild.MOD_ID, "waystone_derelict_tower_top_grass") : new ResourceLocation(TowersOfTheWild.MOD_ID, "derelict_tower_top_grass");
    private static final ResourceLocation DERELICT_TOWER_BOTTOM = new ResourceLocation(TowersOfTheWild.MOD_ID, "derelict_tower_bottom");

    private static final Map<ResourceLocation, BlockPos> CENTER_TOP_OFFSETS = ImmutableMap.of(DERELICT_TOWER_TOP, new BlockPos(6, 5, 6), DERELICT_TOWER_BOTTOM, new BlockPos(3, 31, 3));
    private static final Map<ResourceLocation, BlockPos> CORNER_RELATIVE_POSITIONS = ImmutableMap.of(DERELICT_TOWER_TOP, new BlockPos(4, 0, 4), DERELICT_TOWER_BOTTOM, BlockPos.ZERO);


    public static void addPieces(TemplateManager templateManager, BlockPos absolutePos, Rotation rotation, List<StructurePiece> pieces, Random random, NoFeatureConfig config) {
        pieces.add(new DerelictTowerGrassPieces.Piece(templateManager, DERELICT_TOWER_BOTTOM, absolutePos, rotation));
        pieces.add(new DerelictTowerGrassPieces.Piece(templateManager, DERELICT_TOWER_TOP, absolutePos, rotation));
    }

    public static class Piece extends AbstractTowerPiece {

        public Piece(TemplateManager templateManager, ResourceLocation structurePart, BlockPos absolutePos, Rotation rotation) {
            super(templateManager, structurePart, rotation, RegistryHandler.DERELICT_TOWER_GRASS_PIECE, CENTER_TOP_OFFSETS);
            BlockPos relativePos = CORNER_RELATIVE_POSITIONS.get(structurePart);
            this.templatePosition = absolutePos.add(relativePos.getX(), relativePos.getY(), relativePos.getZ());
            this.func_207614_a(templateManager);
        }

        public Piece(TemplateManager p_i50566_1_, CompoundNBT p_i50566_2_) {
            super(p_i50566_1_, RegistryHandler.DERELICT_TOWER_GRASS_PIECE, p_i50566_2_, CENTER_TOP_OFFSETS);
        }

        @Override
        public boolean func_230383_a_(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {

            if (this.structurePart.equals(DERELICT_TOWER_BOTTOM)) {
                BlockPos blockpos1 = this.templatePosition;
                // BlockPos blockpos1 = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(- relativePos.getX(), 0, - relativePos.getZ())));*

                // setting spawn height
                int height;
                int minHeight = Integer.MAX_VALUE;
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        height = p_230383_1_.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + i, blockpos1.getZ() + j);
                        if (height < minHeight) {
                            minHeight = height;
                        }
                    }
                }

                // replacing dirt and water blocks beneath tower by grass
                for (int i = -1; i < 6; ++i) {
                    for (int j = -1; j < 6; ++j) {
                        BlockPos grassPos = new BlockPos(blockpos1.getX() + i, minHeight - 1, blockpos1.getZ() + j);
                        BlockState blockstate = p_230383_1_.getBlockState(grassPos);
                        if (blockstate.getBlock() == Blocks.DIRT) {
                            p_230383_1_.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                        }

                        if (!((i == -1 || i == 5) && (j == -1 || j == 5))) {
                            if (blockstate.getBlock() == Blocks.WATER) {
                                p_230383_1_.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                            }
                        }
                    }
                }
                this.templatePosition = this.templatePosition.add(0, minHeight - 90, 0);

            } else if (this.structurePart.equals(DERELICT_TOWER_TOP)) {
                BlockPos blockpos1 = this.templatePosition;
                // BlockPos blockpos1 = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(- relativePos.getX(), 0, - relativePos.getZ())));
                int height;
                int minHeight = Integer.MAX_VALUE;
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 5; ++j) {
                        height = p_230383_1_.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + 2 + i, blockpos1.getZ() + 2 + j);
                        if (height < minHeight) {
                            minHeight = height;
                        }
                    }
                }

                // replacing dirt blocks beneath tower by grass
                for (int i = 0; i < 11; ++i) {
                    for (int j = 0; j < 11; ++j) {
                        BlockPos grassPos = new BlockPos(blockpos1.getX() + i, minHeight - 1, blockpos1.getZ() + j);
                        BlockState blockstate = p_230383_1_.getBlockState(grassPos);
                        if (blockstate.getBlock() == Blocks.DIRT) {
                            p_230383_1_.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
                        }
                    }
                }
                this.templatePosition = this.templatePosition.add(0, minHeight - 90, 0);
            }

            return super.func_230383_a_(p_230383_1_, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
        }
    }
}
