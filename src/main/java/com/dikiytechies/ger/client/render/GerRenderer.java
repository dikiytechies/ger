package com.dikiytechies.ger.client.render;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.client.render.entity.model.stand.GoldExperienceModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class GerRenderer extends StandEntityRenderer<GerStandEntity, StandEntityModel<GerStandEntity>> {
    public GerRenderer(EntityRendererManager manager) {
        super(manager,
                StandModelRegistry.registerModel(new ResourceLocation(GerMain.MOD_ID, "ger"), GerModel::new),
                new ResourceLocation(GerMain.MOD_ID, "textures/entity/stand/ger.png"), 0);
    }
}
