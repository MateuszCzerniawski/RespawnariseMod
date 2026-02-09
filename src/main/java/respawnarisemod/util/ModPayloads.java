package respawnarisemod.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import respawnarisemod.RespawnariseMod;

@Mod(value = RespawnariseMod.MOD_ID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = RespawnariseMod.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ModPayloads {

    @SubscribeEvent // on the mod event bus
    public static void register(RegisterPayloadHandlersEvent event) {
        RespawnariseMod.log("registering");
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SoulMatrixSetMaimedPayload.TYPE,
                SoulMatrixSetMaimedPayload.STREAM_CODEC,
                SoulMatrixSetMaimedPayload.HANDLER);
        registrar.playToServer(SoulMatrixSetDistancePayload.TYPE,
                SoulMatrixSetDistancePayload.STREAM_CODEC,
                SoulMatrixSetDistancePayload.HANDLER);
    }

}
