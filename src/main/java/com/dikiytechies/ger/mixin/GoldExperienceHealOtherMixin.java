package com.dikiytechies.ger.mixin;

import com.dikiytechies.ger.init.InitStands;
import com.dikiytechies.ger.init.Stands;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.GoldExperienceHealOther;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GoldExperienceHealOther.class, remap = false)
public abstract class GoldExperienceHealOtherMixin {
    @Inject(method = "replaceAction(Lcom/github/standobyte/jojo/power/impl/stand/IStandPower;Lcom/github/standobyte/jojo/action/ActionTarget;)Lcom/github/standobyte/jojo/action/Action;",
    at = @At("HEAD"), cancellable = true)
    protected void fixTissue(IStandPower power, ActionTarget target, CallbackInfoReturnable<Action<IStandPower>> cir) {
        if ((target.getType() != ActionTarget.TargetType.ENTITY || target.getEntity() == power.getUser()) && power.getType() == Stands.GER.getStandType()) {
            System.out.println(power.getType() == Stands.GER.getStandType());
            cir.setReturnValue(InitStands.GOLD_EXPERIENCE_HEALING_ITEM.get());
            cir.cancel();
        }
    }
}
