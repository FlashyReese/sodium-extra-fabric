package me.flashyreese.mods.sodiumextra.mixin.particle;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Inject(method = "addBlockBreakParticles", at = @At(value = "HEAD"), cancellable = true)
    public void addBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().particleSettings.particles || !SodiumExtraClientMod.options().particleSettings.blockBreak) {
            ci.cancel();
        }
    }

    @Inject(method = "addBlockBreakingParticles", at = @At(value = "HEAD"), cancellable = true)
    public void addBlockBreakingParticles(BlockPos pos, Direction direction, CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().particleSettings.particles || !SodiumExtraClientMod.options().particleSettings.blockBreaking) {
            ci.cancel();
        }
    }

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At(value = "HEAD"), cancellable = true)
    public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (SodiumExtraClientMod.options().particleSettings.particles) {
            Identifier particleTypeId = Registry.PARTICLE_TYPE.getId(parameters.getType());
            if (!SodiumExtraClientMod.options().particleSettings.otherMap.getOrDefault(particleTypeId, true)) {
                cir.setReturnValue(null);
            }
        } else {
            cir.setReturnValue(null);
        }
    }
}
