package com.tterrag.villagemanager;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestType;

public class VillageManagerTileEntityRenderer extends TileEntityRenderer<VillageManagerTileEntity> {
	
	@Override
	public void render(VillageManagerTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
		BufferBuilder buf = Tessellator.getInstance().getBuffer();
		buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		buf.setTranslation(x, y, z);
		
		GlStateManager.disableTexture();
		GlStateManager.lineWidth(4);
		GlStateManager.disableLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240F, 240F);
		
		for (PointOfInterest poi : VillageManagerTileEntity.POIS) {
			BlockPos pos = poi.getPos().subtract(tileEntityIn.getPos());
			Vector4f color = new Vector4f(1, 1, 1, 1);
			if (poi.isOccupied()) {
				color.set(0, 1, 0, 1);
			} else {
				color.set(1, 1, 0, 1);
			}
			if (poi.getType() == PointOfInterestType.HOME) {
				color.set(0, 0, 1, 1);
			}
			buf.pos(0.5, 0.5, 0.5).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
			buf.pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).color(color.getX(), color.getY(), color.getZ(), color.getW()).endVertex();
		}
		Tessellator.getInstance().draw();
		buf.setTranslation(0, 0, 0);
		
		GlStateManager.enableTexture();
	}

	@Override
	public boolean isGlobalRenderer(VillageManagerTileEntity te) {
		return true;
	}
}
