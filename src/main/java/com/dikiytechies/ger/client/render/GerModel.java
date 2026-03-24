package com.dikiytechies.ger.client.render;

import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.GoldExperienceModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class GerModel extends HumanoidStandModel<GerStandEntity> {
    private ModelRenderer theThing;
    private ModelRenderer rightString;
    private ModelRenderer leftString;
    private ModelRenderer loincloth;
    private ModelRenderer leftPartLoincloth;
    private ModelRenderer rightPartLoincloth;

    public GerModel() {
        super();
    }

    @Override
    protected void initOpposites() {
        super.initOpposites();
        oppositeHandside.put(leftPartLoincloth, rightPartLoincloth);
        oppositeHandside.put(leftString, rightString);
    }
}
