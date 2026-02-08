package respawnarisemod.util;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.ModItems;

@OnlyIn(Dist.CLIENT)
public class WingedSoulTouchedChestplateLayer extends ElytraLayer {
    public WingedSoulTouchedChestplateLayer(RenderLayerParent renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, LivingEntity entity) {
        return stack.getItem() == ModItems.WINGED_SOUL_TOUCHED_CHESTPLATE.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, LivingEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,"textures/entity/wings.png");
    }
}
