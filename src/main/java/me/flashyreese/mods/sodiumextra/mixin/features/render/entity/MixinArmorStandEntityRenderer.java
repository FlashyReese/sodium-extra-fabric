package me.flashyreese.mods.sodiumextra.mixin.features.render.entity;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class MixinArmorStandEntityRenderer extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {

    public MixinArmorStandEntityRenderer(EntityRendererFactory.Context ctx, ArmorStandArmorEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Override
    public void render(ArmorStandEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (SodiumExtraClientMod.options().renderSettings.armorStand) {
            super.render(entity, f, g, matrices, vertexConsumers, light);
        } else {
            if (this.hasLabel(entity)) {
                this.renderLabelIfPresent(entity, entity.getDisplayName(), matrices, vertexConsumers, light);
            }
        }
    }
}
