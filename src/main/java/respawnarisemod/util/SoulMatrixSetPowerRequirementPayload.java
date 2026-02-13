package respawnarisemod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.block.entity.SoulMatrixBlockEntity;

public record SoulMatrixSetPowerRequirementPayload(BlockPos pos, boolean value) implements CustomPacketPayload {

    public static final Type<SoulMatrixSetPowerRequirementPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,"soul_matrix_power_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulMatrixSetPowerRequirementPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, SoulMatrixSetPowerRequirementPayload::pos,
                    ByteBufCodecs.BOOL, SoulMatrixSetPowerRequirementPayload::value,
                    SoulMatrixSetPowerRequirementPayload::new
            );

    public static final IPayloadHandler<SoulMatrixSetPowerRequirementPayload> HANDLER = (payload, context)-> context.enqueueWork(()->{
        if(context.player().level() instanceof ServerLevel level){
            BlockEntity be = level.getBlockEntity(payload.pos());
            if (be instanceof SoulMatrixBlockEntity blockEntity) {
                blockEntity.setRequireSignal(payload.value());
            }
        }
    });
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
