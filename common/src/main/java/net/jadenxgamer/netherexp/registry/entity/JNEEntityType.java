package net.jadenxgamer.netherexp.registry.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.entity.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class JNEEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(NetherExp.MOD_ID, Registries.ENTITY_TYPE);

    // LIVING ENTITIES

    public static final RegistrySupplier<EntityType<Apparition>> APPARITION = ENTITY_TYPES.register("apparition", () ->
            EntityType.Builder.of(Apparition::new, MobCategory.MONSTER)
                    .sized(1.0f, 2.2f).fireImmune().build("apparition"));

    public static final RegistrySupplier<EntityType<Wisp>> WISP = ENTITY_TYPES.register("wisp", () ->
            EntityType.Builder.of(Wisp::new, MobCategory.AMBIENT)
                    .sized(0.6f, 0.6f).fireImmune().build("wisp"));

    public static final RegistrySupplier<EntityType<Vessel>> VESSEL = ENTITY_TYPES.register("vessel", () ->
            EntityType.Builder.of(Vessel::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.99F).fireImmune().build("vessel"));

    public static final RegistrySupplier<EntityType<EctoSlab>> ECTO_SLAB = ENTITY_TYPES.register("ecto_slab", () ->
            EntityType.Builder.of(EctoSlab::new, MobCategory.MONSTER)
                    .sized(2.04F, 2.04F).fireImmune().build("ecto_slab"));

    public static final RegistrySupplier<EntityType<Banshee>> BANSHEE = ENTITY_TYPES.register("banshee", () ->
            EntityType.Builder.of(Banshee::new, MobCategory.MONSTER)
                    .sized(1.2F, 2.55F).fireImmune().build("banshee"));

    public static final RegistrySupplier<EntityType<Stampede>> STAMPEDE = ENTITY_TYPES.register("stampede", () ->
            EntityType.Builder.of(Stampede::new, MobCategory.MONSTER)
                    .sized(1.8F, 4.0F).fireImmune().build("stampede"));

    public static final RegistrySupplier<EntityType<Carcass>> CARCASS = ENTITY_TYPES.register("carcass", () ->
            EntityType.Builder.of(Carcass::new, MobCategory.MISC)
                    .sized(1.4F, 1.4F).fireImmune().build("carcass"));

    // OTHER

    public static final RegistrySupplier<EntityType<SoulBullet>> SOUL_BULLET = ENTITY_TYPES.register("soul_bullet", () ->
            EntityType.Builder.<SoulBullet>of(SoulBullet::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("soul_bullet"));

    public static final RegistrySupplier<EntityType<BloodDrop>> BLOOD_DROP = ENTITY_TYPES.register("blood_drop", () ->
            EntityType.Builder.<BloodDrop>of(BloodDrop::new, MobCategory.MISC)
                    .sized(0.8f, 0.8f).build("blood_drop"));

    public static final RegistrySupplier<EntityType<PhasmoArrow>> PHASMO_ARROW = ENTITY_TYPES.register("phasmo_arrow", () ->
            EntityType.Builder.<PhasmoArrow>of(PhasmoArrow::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("phasmo_arrow"));

    public static final RegistrySupplier<EntityType<MistCharge>> MIST_CHARGE = ENTITY_TYPES.register("mist_charge", () ->
            EntityType.Builder.<MistCharge>of(MistCharge::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("mist_charge"));

    public static final RegistrySupplier<EntityType<GraveCloud>> GRAVE_CLOUD = ENTITY_TYPES.register("grave_cloud", () ->
            EntityType.Builder.of(GraveCloud::new, MobCategory.MISC)
                    .sized(3.0F, 2.4F).fireImmune().build("grave_cloud"));

    public static final RegistrySupplier<EntityType<WillOWisp>> WILL_O_WISP = ENTITY_TYPES.register("will_o_wisp", () ->
            EntityType.Builder.<WillOWisp>of(WillOWisp::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).build("will_o_wisp"));

    public static final RegistrySupplier<EntityType<ThrownAntidote>> ANTIDOTE = ENTITY_TYPES.register("antidote", () ->
            EntityType.Builder.<ThrownAntidote>of(ThrownAntidote::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).build("antidote"));

    public static final RegistrySupplier<EntityType<GrenadeEffectCloud>> GRENADE_EFFECT_CLOUD = ENTITY_TYPES.register("grenade_effect_cloud", () ->
            EntityType.Builder.<GrenadeEffectCloud>of(GrenadeEffectCloud::new, MobCategory.MISC)
                    .sized(10.0F, 6.5F).fireImmune().build("grenade_effect_cloud"));

    public static void init() {
        ENTITY_TYPES.register();
    }
}
