package com._idrae.towers_of_the_wild.world.structures;

import com._idrae.towers_of_the_wild.TowersOfTheWild;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Map;
import java.util.Random;

public abstract class AbstractTowerPiece extends TemplateStructurePiece {

    private static final ResourceLocation TOWER_CHEST = new ResourceLocation(TowersOfTheWild.MOD_ID, "chests/tower_chest");

    protected final Map<ResourceLocation, BlockPos> centerTopOffsets;
    protected final ResourceLocation structurePart;
    protected final Rotation rotation;

    public AbstractTowerPiece(TemplateManager templateManager, ResourceLocation structurePart, Rotation rotation, IStructurePieceType piece, Map<ResourceLocation, BlockPos> centerTopOffsets) {
        super(piece, 0);
        this.structurePart = structurePart;
        this.rotation = rotation;
        this.centerTopOffsets = centerTopOffsets;
    }

    public AbstractTowerPiece(TemplateManager templateManager, IStructurePieceType structurePieceTypeIn, CompoundNBT nbt, Map<ResourceLocation, BlockPos> centerTopOffsets) {
        super(structurePieceTypeIn, nbt);
        this.structurePart = new ResourceLocation(nbt.getString("Template"));
        this.rotation = Rotation.valueOf(nbt.getString("Rot"));
        this.centerTopOffsets = centerTopOffsets;
        this.func_207614_a(templateManager);
    }

    protected void func_207614_a(TemplateManager templateManager) {
        Template template = templateManager.getTemplateDefaulted(this.structurePart);
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setCenterOffset(centerTopOffsets.get(this.structurePart));
        this.setup(template, this.templatePosition, placementsettings);
    }

    protected void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putString("Template", this.structurePart.toString());
        tagCompound.putString("Rot", this.rotation.name());
    }

    protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
        if ("chest".equals(function)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            TileEntity tileentity = worldIn.getTileEntity(pos.down());
            if (tileentity instanceof ChestTileEntity) {
                ((ChestTileEntity) tileentity).setLootTable(TOWER_CHEST, rand.nextLong());
            }
        }
    }
}
