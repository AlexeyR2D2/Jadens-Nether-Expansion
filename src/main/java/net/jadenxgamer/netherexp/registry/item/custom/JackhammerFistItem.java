package net.jadenxgamer.netherexp.registry.item.custom;

import net.jadenxgamer.netherexp.NetherExp;
import net.jadenxgamer.netherexp.registry.enchantment.JNEEnchantments;
import net.jadenxgamer.netherexp.registry.item.JNEItemRenderer;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEDamageSources;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.util.NonEntityAnimationState;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class JackhammerFistItem extends ProjectileWeaponItem implements Vanishable, IShotgun {
    public final NonEntityAnimationState punchAnimationState = new NonEntityAnimationState();
    public final NonEntityAnimationState pullAnimationState = new NonEntityAnimationState();
    public final NonEntityAnimationState pullLoopAnimationState = new NonEntityAnimationState();

    private int pullTimeOut;
    private boolean pullFlag;
    private int punchTimeOut;
    private boolean punchFlag;

    public JackhammerFistItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (level.isClientSide()) {
            if (this.pullFlag) {
                playPullAnimation(player, player.tickCount);
            }
            if (this.punchFlag) {
                playFireAnimation(player, player.tickCount);
            }
            if (getPulled(stack)) {
                pullLoopAnimationState.startIfStopped(player.tickCount, player);
            } else {
                pullLoopAnimationState.stop(player);
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new JNEItemRenderer();
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getProjectile(stack).isEmpty() || player.getAbilities().instabuild) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int remainingUseDuration) {
        if (remainingUseDuration >= 30 && !getPulled(pStack)) {
            setPulled(pStack, true);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseDuration) {
        int recoil = EnchantmentHelper.getItemEnchantmentLevel(JNEEnchantments.RECOIL.get(), stack);
        int barrage = EnchantmentHelper.getItemEnchantmentLevel(JNEEnchantments.BARRAGE.get(), stack);

        if (remainingUseDuration < 30) {
            return;
        }

        setPulled(stack, false);
        this.punchFlag = true;
        useProjectile(stack, user);
        Vec3 look = user.getLookAngle();
        Vec3 pushBack = new Vec3(-look.x, -look.y, -look.z).normalize();
        double recoilPushBonus = (double) recoil / 16;

        Vec3 velocity = user.getDeltaMovement();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        float damage = (float) (5.0f + horizontalSpeed * 64);
        NetherExp.LOGGER.info("Calculated Damage: {} for Velocity: {}", damage, horizontalSpeed);

        Vec3 raycastStart = user.getEyePosition(1.0F);
        Vec3 raycastEnd = raycastStart.add(user.getViewVector(1.0F).scale(5));
        AABB aabb = new AABB(raycastStart, raycastEnd).inflate(1.3, 1.3, 0);
        List<Entity> entities = level.getEntities(user, aabb, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && !level.isClientSide()) {
                livingEntity.hurt(user.level().damageSources().source(JNEDamageSources.JACKHAMMER, user), damage);
            }
        }
//        user.push(pushBack.x * (0.5 + recoilPushBonus), pushBack.y * (0.5 + recoilPushBonus), pushBack.z * (0.5 + recoilPushBonus));
        level.playSound(null, user.getX(), user.getY(), user.getZ(), JNESoundEvents.SHOTGUN_USE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    private void useProjectile(ItemStack stack, LivingEntity user) {
        if (user instanceof Player player) {
            boolean creative = player.getAbilities().instabuild;
            ItemStack projectileStack = player.getProjectile(stack);
            if (!projectileStack.isEmpty() || creative) {
                if (projectileStack.isEmpty()) {
                    projectileStack = new ItemStack(JNEItems.WRAITHING_FLESH.get());
                }
                boolean bl = projectileStack.getItem() == JNEItems.WRAITHING_FLESH.get();
                if (bl && !creative) {
                    projectileStack.shrink(1);
                    if (projectileStack.isEmpty()) {
                        player.getInventory().removeItem(projectileStack);
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    private void playPullAnimation(Player player, int tickCount) {
        if (this.pullTimeOut == 30) {
            this.pullAnimationState.startIfStopped(tickCount, player);
        }
        if (this.pullTimeOut > 0) {
            --this.pullTimeOut;
        }
        if (this.pullTimeOut <= 0) {
            this.pullAnimationState.stop(player);
            this.pullTimeOut = 30;
            this.pullFlag = false;
        }
    }

    private void playFireAnimation(Player player, int tickCount) {
        if (this.punchTimeOut == 20) {
            this.punchAnimationState.startIfStopped(tickCount, player);
        }
        if (this.punchTimeOut > 0) {
            --this.punchTimeOut;
        }
        if (this.punchTimeOut <= 0) {
            this.punchAnimationState.stop(player);
            this.punchTimeOut = 20;
            this.punchFlag = false;
        }
    }
    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.is(JNEItems.WRAITHING_FLESH.get());
    }

    public static boolean getPulled(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.getBoolean("Pulled");
    }

    public static void setPulled(ItemStack stack, boolean bl) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("Pulled", bl);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(JNEItems.JACKHAMMER_FIST.get()) || !newStack.is(JNEItems.JACKHAMMER_FIST.get());
    }
}
