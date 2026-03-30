package com.dikiytechies.ger.init;

import com.dikiytechies.ger.action.BeamAction;
import com.dikiytechies.ger.util.JavaUtil;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.init.power.JojoCustomRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Set;

public class AddonInits {
    public static void vanillaRegistries(IEventBus modEventBus) {
        InitEntities.ENTITIES.register(modEventBus);
    }

    public static void addonRegistries(IEventBus modEventBus) {
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitStandEffects.STAND_EFFECTS.register(modEventBus);
    }
    // adding the most powerful abilities to the set
    public static Set<Class<? extends StandAction>> BANNED_ABILITIES = JavaUtil.modifiableSetOf(
            TimeStop.class, TheWorldTimeStop.class, TimeStopInstant.class, TheWorldTSHeavyAttack.class, MagiciansRedCrossfireHurricane.class,
            SilverChariotTakeOffArmor.class, GoldExperienceLifeshotPunch.class, HierophantGreenBarrier.class, HierophantGreenGrapple.class, CrazyDiamondBlockBullet.class,
            CrazyDiamondBloodCutter.class, BeamAction.class);

    public static void initCommunityAddonsBanList() {
        // hello there
        addIfPresent("rotp_kingcrimson", "kingcrimson_timeskip");
        addIfPresent("rotp_kingcrimson", "kingcrimson_timeerase");
        addIfPresent("rotp_kingcrimson", "kingcrimson_epitaph");

        addIfPresent("rotp_cream", "cream_void_ball");

        addIfPresent("rotp_pw", "planet_waves_vertical");
        addIfPresent("rotp_pw", "planet_waves_horizontal");
        addIfPresent("rotp_pw", "planet_waves_diagonal");
        addIfPresent("rotp_pw", "planet_waves_vertical_shower");
        addIfPresent("rotp_pw", "planet_waves_horizontal_shower");
        addIfPresent("rotp_pw", "planet_waves_diagonal_shower");

        addIfPresent("rotp_littlefeet", "little_feet_finisher_punch");
        addIfPresent("rotp_littlefeet", "little_feet_shrink_slash");

        // makutazeml
        addIfPresent("rotp_zkq", "kq_entity_explo");
        addIfPresent("rotp_zkq", "kq_block_explode");

        addIfPresent("rotp_zgd", "gd_mold");

        addIfPresent("rotp_zemperor", "barrage_shoot_entity");

        addIfPresent("rotp_zwa", "wa_freeze");
        addIfPresent("rotp_zwa", "wa_reflec");

        addIfPresent("rotp_zcs", "cs_entity_fill");

        addIfPresent("rotp_zbc", "shoot");
        addIfPresent("rotp_zbc", "put_mines");

        addIfPresent("rotp_zhp", "hp_grab");
        addIfPresent("rotp_zhp", "hp_grab_od");
        addIfPresent("rotp_zhp", "hp_grab_sc");
        addIfPresent("rotp_zhp", "hp_vine_entity");

        // tilto
        addIfPresent("rotp_dd", "diver_down_steal_item");
        addIfPresent("rotp_dd", "diver_down_spring_legs");

        addIfPresent("rotp_stfn", "sticky_fingers_place_zipper");

        addIfPresent("rotp_wr", "weather_report_fugu_rain");
        addIfPresent("rotp_wr", "weather_report_lightning");

        // evermore
        addIfPresent("rotp_whitesnake", "melt_your_heart");
        addIfPresent("rotp_whitesnake", "whitesnake_remove_stand_disc");
        addIfPresent("rotp_whitesnake", "whitesnake_removing_stand_disc");

        // weever
        addIfPresent("rotp_mandom", "time_rewind"); //trollge

        addIfPresent("rotp_lovers", "into_entity");

        addIfPresent("rotp_cm", "cm_heart_inversion");
        addIfPresent("rotp_cm", "cm_gravity_counter");
        addIfPresent("rotp_cm", "cm_center_of_gravity");
        addIfPresent("rotp_cm", "cm_gravity_shift");
        addIfPresent("rotp_cm", "cm_destroy");

        addIfPresent("rotp_mih", "mih_universe_reset");
        addIfPresent("rotp_mih", "mih_dash");

        // yarick
        addIfPresent("rotp_hd", "heavens_door_tear_out_a_page");
        addIfPresent("rotp_hd", "heavens_door_open_book");

        // danielgamer
        addIfPresent("rotp_th", "the_hand_erase");
        addIfPresent("rotp_th", "the_hand_erasure_barrage");
        addIfPresent("rotp_th", "the_hand_erase_item");

        addIfPresent("rotp_sf", "stone_free_attack_binding");
        addIfPresent("rotp_sf", "stone_free_user_attack_binding");
        addIfPresent("rotp_sf", "stone_free_extended_punch");
        addIfPresent("rotp_sf", "stone_free_user_grapple_entity");
        addIfPresent("rotp_sf", "stone_free_grapple_entity");

        addIfPresent("rotp_kw", "kraft_work_lock_target");

        addIfPresent("rotp_an", "aqua_necklace_brain_attack");
        addIfPresent("rotp_an", "aqua_necklace_get_into_the_lungs");

        // doggy
        addIfPresent("rotp_sg", "spice_girl_bounce_up");
        addIfPresent("rotp_sg", "spice_girl_bounce");

        addIfPresent("rotp_cr", "chariot_requiem_aura");
        addIfPresent("rotp_cr", "chariot_requiem_virus");

        addIfPresent("rotp_7su", "cardigans_anesthesia");

        // archlunatic123
        addIfPresent("rotp_ctr", "catch_the_rainbow_rain_blink");
        addIfPresent("rotp_ctr", "catch_the_rainbow_rain_merge");
        addIfPresent("rotp_ctr", "catch_the_rainbow_rain_redemption");

        // unclestalin
        addIfPresent("rotp_metallica", "invisibility");
        addIfPresent("rotp_metallica", "create_blades_in_victim");

        // dikiytechies
        addIfPresent("rejojo", "star_platinum_time_stop");
        addIfPresent("rejojo", "star_platinum_ts_blink");
        addIfPresent("rejojo", "star_platinum_heavy_punch_ts");
        addIfPresent("rejojo", "star_platinum_uppercut_ts");
        addIfPresent("rejojo", "the_world_time_stop");
        addIfPresent("rejojo", "the_world_ts_punch");

        // chainik
        addIfPresent("rotp_ph", "purple_haze_viral_punch");
        addIfPresent("rotp_ph", "purple_haze_capsule_shot");
        addIfPresent("rotp_ph", "purple_haze_virus_aura");
    }

    private static void addIfPresent(String modId, String registry) {
        Action<?> action = JojoCustomRegistries.ACTIONS.getValue(new ResourceLocation(modId, registry));
        if (action != null) {
            Class<? extends StandAction> clazz = (Class<? extends StandAction>) action.getClass();
            BANNED_ABILITIES.add(clazz);
        }
    }
}
