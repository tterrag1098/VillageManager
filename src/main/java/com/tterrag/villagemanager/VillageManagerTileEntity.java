package com.tterrag.villagemanager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestManager.Status;
import net.minecraft.world.server.ServerWorld;

public class VillageManagerTileEntity extends TileEntity implements ITickableTileEntity {
	
	public static final Set<PointOfInterest> POIS = Sets.newConcurrentHashSet();
	public static final Set<VillagerEntity> VILLAGERS = Sets.newConcurrentHashSet();

	public VillageManagerTileEntity() {
		super(VillageManager.TILE_TYPE);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (getWorld().isRemote) return;
		POIS.clear();
		
		PointOfInterestManager pois = ((ServerWorld)getWorld()).getPointOfInterestManager();
		POIS.addAll(pois.func_219146_b(Predicates.alwaysTrue(), getPos(), 256, Status.ANY).collect(Collectors.toSet()));
	}
	
	@Override
	public void tick() {
	    if (getWorld().isRemote) return;
	    if (world.getGameTime() % 100 == 0) {
	        updateNearby();
	    }
	}
	
	private void updateNearby() {
	    VILLAGERS.clear();
	    VILLAGERS.addAll(getWorld().getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(getPos()).grow(256)));
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}