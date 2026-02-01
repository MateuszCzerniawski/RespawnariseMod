package respawnarisemod.item.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
public record SoulVisageComponent(String typeKey) {
    public static final Codec<SoulVisageComponent> CODEC = Codec.STRING.xmap(SoulVisageComponent::new,SoulVisageComponent::typeKey);
    public static final StreamCodec<ByteBuf, SoulVisageComponent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map( SoulVisageComponent::new,SoulVisageComponent::typeKey);

    /*public void spawn(Level level, int x, int y, int z) {
        if (!level.isClientSide()) {
            EntityType.byString(typeKey).get().spawn((ServerLevel) level,new net.minecraft.core.BlockPos(x, y, z), MobSpawnType.SPAWNER);
        }
    }*/
    public static SoulVisageComponent empty(){
        return new SoulVisageComponent(null);
    }
}
