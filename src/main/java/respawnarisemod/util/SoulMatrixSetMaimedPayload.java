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

public record SoulMatrixSetMaimedPayload(BlockPos pos, boolean value) implements CustomPacketPayload {

    public static final Type<SoulMatrixSetMaimedPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,"soul_matrix_maimed_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SoulMatrixSetMaimedPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, SoulMatrixSetMaimedPayload::pos,
                    ByteBufCodecs.BOOL, SoulMatrixSetMaimedPayload::value,
                    SoulMatrixSetMaimedPayload::new
            );
    public static final IPayloadHandler<SoulMatrixSetMaimedPayload> HANDLER = (payload, context)-> context.enqueueWork(()->{
        if(context.player().level() instanceof ServerLevel level){
            BlockEntity be = level.getBlockEntity(payload.pos());
            if (be instanceof SoulMatrixBlockEntity blockEntity) {
                blockEntity.setSpawnMaimed(payload.value());
            }
        }
    });
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
