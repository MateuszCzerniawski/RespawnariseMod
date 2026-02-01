package respawnarisemod.item.components;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import respawnarisemod.util.ModDataComponents;

public record SacrifiseDaggerComponent(ItemStack content) {
    public static final Codec<SacrifiseDaggerComponent> CODEC =
            ItemStack.CODEC.xmap(
                    SacrifiseDaggerComponent::new,
                    SacrifiseDaggerComponent::content
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, SacrifiseDaggerComponent> STREAM_CODEC =
            ItemStack.STREAM_CODEC.map(
                    SacrifiseDaggerComponent::new,
                    SacrifiseDaggerComponent::content
            );

    public static SacrifiseDaggerComponent empty(){
        return new SacrifiseDaggerComponent(ItemStack.EMPTY);
    }
    public SacrifiseDaggerComponent copy(){
        return new SacrifiseDaggerComponent(this.content.copy());
    }

}
