package com.m4thg33k.pipedream.client.render.tile;

import com.m4thg33k.pipedream.PipeDream;
import com.m4thg33k.pipedream.client.handler.ClientTickHandler;
import com.m4thg33k.pipedream.client.render.models.ModelSphere;
import com.m4thg33k.pipedream.client.render.models.ModelTankValve;
import com.m4thg33k.pipedream.client.render.models.SphereModels;
import com.m4thg33k.pipedream.core.util.LogHelper;
import com.m4thg33k.pipedream.particles.ParticleFluidOrb;
import com.m4thg33k.pipedream.tiles.TileTank;
import javafx.scene.shape.Sphere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class RenderTileTank extends RenderTileSymmetricBase<TileTank>{

    ModelTankValve tankValve;
    ModelSphere center = null;
    ModelSphere sphere = null;
    double filledPercentage = 0;

    @Override
    public void renderTileEntityAt(TileTank te, double x, double y, double z, float partialTicks, int destroyStage) {


        if (te == null || !te.getWorld().isBlockLoaded(te.getPos(), false))
        {
            return;
        }

//        LogHelper.info("spawning particle\t" + partialTicks);
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFluidOrb(te.getWorld(), te.getPos().getX()+0.5, te.getPos().getY()+1.5, te.getPos().getZ()+0.5, 0, 0.1, 0, "water", 100));

        if (tankValve == null)
        {
            tankValve = new ModelTankValve();
        }

        if (center == null)
        {
            center = new ModelSphere(new ResourceLocation(PipeDream.MODID, "blocks/tankSphere"));
        }

        int connections = te.getFluidConnectionsAsInteger();//(te == null) ? 3 : te.getFluidConnectionsAsInteger();

        filledPercentage = 0.65*te.getPercentage();
        sphere = filledPercentage > 0 ? SphereModels.getSphereFromFluid(te.getFluid()) : null;
        filledPercentage += 0.1;


        double worldTime = (double)(ClientTickHandler.ticksInGame + partialTicks) + new Random(te.getPos().hashCode()).nextInt(360);

        // get matrix set to the correct location
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();

        GlStateManager.color(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.1,0.1,0.1);
        center.renderModel();
        GlStateManager.popMatrix();


        renderValves(te, connections);

        if (sphere != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(filledPercentage,filledPercentage,filledPercentage);
            GlStateManager.rotate((float)worldTime, 0f, 1f, 0f);
//            sphere = new ModelSphere(FluidRegistry.WATER.getStill());
            sphere.renderModel();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();

    }

    private void renderValves(TileTank tank, int connections)
    {
        int[] cTypes = tank.getFluidConnectionTypes();
        for (int i=0; i<6; i++)
        {
            if (((connections>>i) & 1) == 1)
            {
                GlStateManager.pushMatrix();
                rotate(facings[i]);
                tankValve.renderModel(cTypes[i]);
                GlStateManager.popMatrix();
            }
        }
    }
}
