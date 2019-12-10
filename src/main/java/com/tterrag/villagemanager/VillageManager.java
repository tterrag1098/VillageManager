package com.tterrag.villagemanager;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

@Mod("villagemanager")
@ObjectHolder("villagemanager")
public class VillageManager {
	
	public VillageManager() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		modBus.addGenericListener(Block.class, this::registerBlocks);
		modBus.addGenericListener(Item.class, this::registerItems);
		modBus.addGenericListener(TileEntityType.class, this::registerTileEntities);
		
		modBus.addListener(this::clientSetup);
	}
	
	@ObjectHolder("villagemanager")
	public static final VillageManagerBlock BLOCK = null;
	
	@ObjectHolder("villagemanager")
	public static final TileEntityType<VillageManagerTileEntity> TILE_TYPE = null;

	private void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(new VillageManagerBlock(Block.Properties.create(Material.IRON)).setRegistryName("villagemanager"));
	}
	
	private void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new BlockItem(BLOCK, new Item.Properties()).setRegistryName("villagemanager"));
	}
	
	private void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().registerAll(TileEntityType.Builder.create(VillageManagerTileEntity::new, BLOCK).build(null).setRegistryName("villagemanager"));
	}
	
	private void clientSetup(FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(VillageManagerTileEntity.class, new VillageManagerTileEntityRenderer());
	}
}
