package com.tterrag.villagemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestType;

public class VillageManagerTileEntityRenderer extends TileEntityRenderer<VillageManagerTileEntity> {
	
	@Override
	public void render(VillageManagerTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
		BufferBuilder buf = Tessellator.getInstance().getBuffer();

		GlStateManager.disableTexture();
		GlStateManager.lineWidth(4);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240F, 240F);
		
        for (int i = 0; i < 2; i++) {
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buf.setTranslation(x, y, z);
            
            if (i == 0) {
                GlStateManager.disableDepthTest();
            } else {
                GlStateManager.enableDepthTest();
            }
    		for (PointOfInterest poi : VillageManagerTileEntity.POIS) {
    			BlockPos pos = poi.getPos();
    			if (pos.distanceSq(tileEntityIn.getPos()) > 150 * 150) continue;
    			Vector4f color = new Vector4f(1, 1, 1, 1);
    			List<Vec3d> starts = new ArrayList<>();
    			if (poi.isOccupied()) {
    				color.set(0, 1, 0, 1);
    			} else {
                    color.set(1, 0.5f, 0, 1);
    			}
    			if (poi.getType() == PointOfInterestType.HOME) {
    			    starts = getStarts(pos, tileEntityIn.getPos(), partialTicks, MemoryModuleType.HOME);
    			    if (poi.isOccupied()) {
    			        color.set(0, 0, 1, 1);
    			    } else {
                        color.set(1, 0, 0, 1);
    			    }
    			} else {
                    starts = getStarts(pos, tileEntityIn.getPos(), partialTicks, MemoryModuleType.JOB_SITE);
                    if (starts.isEmpty()) {
                        starts = getStarts(pos, tileEntityIn.getPos(), partialTicks, MemoryModuleType.MEETING_POINT);
                        if (!starts.isEmpty()) {
                            color.set(1, 1, 0, 1);
                        }
                    }
    			}
    			if (starts.isEmpty()) {
                    starts.add(new Vec3d(0.5, 0.5, 0.5));
    			}
    			if (i == 0) {
    			    color.set(color.getX(), color.getY(), color.getZ(), color.getW() * 0.25f);;
    			}
                BlockPos renderPos = pos.subtract(tileEntityIn.getPos());
    			for (Vec3d start : starts) {
        			buf.pos(start.x, start.y, start.z).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
        			buf.pos(renderPos.getX() + 0.5, renderPos.getY() + 0.5, renderPos.getZ() + 0.5).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
    			}
    		}
    		Tessellator.getInstance().draw();
        }

		buf.setTranslation(0, 0, 0);
		
		GlStateManager.enableTexture();
		GlStateManager.enableDepthTest();
	}
	
	private List<Vec3d> getStarts(BlockPos poiPos, BlockPos tilePos, float partialTicks, MemoryModuleType<?> type) {
	    return VillageManagerTileEntity.VILLAGERS.stream().filter(v -> v.getBrain().getMemory(type).map(gp -> 
                gp.equals(GlobalPos.of(getWorld().getDimension().getType(), poiPos))).orElse(false))
                    .map(v -> 
                        new Vec3d(MathHelper.lerp(partialTicks, v.lastTickPosX, v.posX), MathHelper.lerp(partialTicks, v.lastTickPosY, v.posY), MathHelper.lerp(partialTicks, v.lastTickPosZ, v.posZ))
                        .add(0, v.getHeight() / 2, 0)
                        .subtract(new Vec3d(tilePos)))
                    .collect(Collectors.toList());
	}

	@Override
	public boolean isGlobalRenderer(VillageManagerTileEntity te) {
		return true;
	}
}
