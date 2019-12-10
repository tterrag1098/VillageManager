package com.tterrag.villagemanager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestManager.Status;
import net.minecraft.world.ServerWorld;

public class VillageManagerTileEntity extends TileEntity {
	
	public static final Set<PointOfInterest> POIS = new HashSet<>();

	public VillageManagerTileEntity() {
		super(VillageManager.TILE_TYPE);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (getWorld().isRemote) return;
		POIS.clear();
		
		PointOfInterestManager pois = ((ServerWorld)getWorld()).func_217443_B();
		POIS.addAll(pois.func_219146_b(Predicates.alwaysTrue(), getPos(), 256, Status.ANY).collect(Collectors.toSet()));
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}