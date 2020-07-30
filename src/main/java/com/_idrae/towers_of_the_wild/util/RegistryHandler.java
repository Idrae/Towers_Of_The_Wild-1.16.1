package com._idrae.towers_of_the_wild.util;

import com._idrae.towers_of_the_wild.TowersOfTheWild;
import com._idrae.towers_of_the_wild.world.structures.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.SnowyTundraBiome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    // Structures

    public static final DeferredRegister<Structure<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, TowersOfTheWild.MOD_ID);
    public static final RegistryObject<Structure<NoFeatureConfig>> TOWER = registerStructure("tower", new TowerStructure(NoFeatureConfig.field_236558_a_));

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, T structure) {
        // Structure.func_236394_a_("towers_of_the_wild:" + name, new TowerStructure(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.SURFACE_STRUCTURES);
        Structure.field_236365_a_.put(TowersOfTheWild.MOD_ID + ":" + name, structure);
        Structure.field_236385_u_.put(structure, GenerationStage.Decoration.SURFACE_STRUCTURES);
        TowersOfTheWild.LOGGER.info(name + " structure registered");
        return STRUCTURE_FEATURES.register(name, () -> structure);
    }

    /*
    public static Structure<NoFeatureConfig> TOWER;
    public static void registerStructureVanilla() {
        TOWER =
    }
     */

    public static void registerPieces() {
        registerPiece("tower", TOWER_PIECE);
        registerPiece("jungle_tower", JUNGLE_TOWER_PIECE);
        registerPiece("ice_tower", ICE_TOWER_PIECE);
        registerPiece("derelict_tower", DERELICT_TOWER_PIECE);
        registerPiece("derelict_tower_grass", DERELICT_TOWER_GRASS_PIECE);
    }

    // Structure Pieces
    public static final IStructurePieceType TOWER_PIECE = TowerPieces.Piece::new;
    public static final IStructurePieceType JUNGLE_TOWER_PIECE = JungleTowerPieces.Piece::new;
    public static final IStructurePieceType ICE_TOWER_PIECE = IceTowerPieces.Piece::new;
    public static final IStructurePieceType DERELICT_TOWER_PIECE = DerelictTowerPieces.Piece::new;
    public static final IStructurePieceType DERELICT_TOWER_GRASS_PIECE = DerelictTowerGrassPieces.Piece::new;

    private static void registerPiece(String key, IStructurePieceType type) {
        TowersOfTheWild.LOGGER.info(key + " structure piece registered");
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(TowersOfTheWild.MOD_ID, key), type);
    }
}
