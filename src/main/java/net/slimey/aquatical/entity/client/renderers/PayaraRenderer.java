package net.slimey.aquatical.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.slimey.aquatical.Aquatical;
import net.slimey.aquatical.entity.client.models.PayaraModel;
import net.slimey.aquatical.entity.custom.PayaraEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PayaraRenderer extends GeoEntityRenderer<PayaraEntity> {
    public PayaraRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PayaraModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(PayaraEntity instance) {
        return new ResourceLocation(Aquatical.MOD_ID, "textures/entity/payara/payara.png");
    }

    @Override
    public RenderType getRenderType(PayaraEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.2F, 1.2F, 1.2F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}

