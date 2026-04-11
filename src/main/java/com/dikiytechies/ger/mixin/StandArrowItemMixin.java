package com.dikiytechies.ger.mixin;

import com.dikiytechies.ger.config.GlobalConfig;
import com.dikiytechies.ger.init.Stands;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.stand.ModStands;
import com.github.standobyte.jojo.item.StandArrowItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StandArrowItem.class)
public abstract class StandArrowItemMixin extends ArrowItem {

    public StandArrowItemMixin(Properties properties) { super(properties); }

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (!world.isClientSide()) {
            IStandPower power = IStandPower.getPlayerStandPower(player);
            ItemStack stack = player.getItemInHand(hand);
            if (power.getType() == ModStands.GOLD_EXPERIENCE.getStandType() && power.getResolveLevel() >= 4) {
                power.clear();
                power.givePower(Stands.GER.getStandType());
                if (GlobalConfig.isTemporal(world.isClientSide())) {
                    int duration = GlobalConfig.getResolveDuration(world.isClientSide());
                    int resolveLevel = GlobalConfig.getResolveAmplifier(world.isClientSide());
                    player.addEffect(new EffectInstance(ModStatusEffects.RESOLVE.get(),
                            duration, resolveLevel, false,
                            false, true));
                    player.getCooldowns().addCooldown(stack.getItem(), (int) (duration * 2.2f));
                }
                power.toggleSummon();

                player.hurt(DamageSource.playerAttack(player), Math.min(1.0F, Math.max(player.getHealth() - 1.0F, 0)));
                stack.hurtAndBreak(1, player, pl -> {});
                cir.setReturnValue(ActionResult.success(stack));
                cir.cancel();
            }
        }
    }
}
