package net.jadenxgamer.netherexp.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Shadow protected int sprintTriggerTime;

    @Shadow public Input input;

    @Shadow protected abstract boolean hasEnoughImpulseToStartSprinting();

    @Shadow protected abstract boolean hasEnoughFoodToStartSprinting();

    @Shadow protected abstract boolean vehicleCanSprint(Entity pVehicle);

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;leftImpulse:F", opcode = Opcodes.PUTFIELD)
    )
    private void netherexp$leftImpulse(Input instance, float value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            input.leftImpulse *= 1.0f;
        } else {
            original.call(instance, value);
        }
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD)
    )
    private void netherexp$forwardImpulse(Input instance, float value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            input.forwardImpulse *= 1.0f;
        } else {
            original.call(instance, value);
        }
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;sprintTriggerTime:I", opcode = Opcodes.PUTFIELD, ordinal = 1)
    )
    private void netherexp$sprintTriggerTime(LocalPlayer instance, int value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            this.sprintTriggerTime = 7;
        } else {
            original.call(instance, value);
        }
    }

    @Inject(
            method = "canStartSprinting",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void netherexp$allowSprintWhenUsing(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            if (!player.isSprinting() && this.hasEnoughImpulseToStartSprinting() && this.hasEnoughFoodToStartSprinting() && !player.hasEffect(MobEffects.BLINDNESS) && (!player.isPassenger() || this.vehicleCanSprint(player.getVehicle())) && !player.isFallFlying()) {
                cir.setReturnValue(true);
            }
        }
    }
}
