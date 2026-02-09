package respawnarisemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import respawnarisemod.block.entity.SoulMatrixBlockEntity;
import respawnarisemod.screen.ModMenuTypes;
import respawnarisemod.screen.SoulMatrixScreen;
import respawnarisemod.util.SoulMatrixSetDistancePayload;
import respawnarisemod.util.SoulMatrixSetMaimedPayload;
import respawnarisemod.util.WingedSoulTouchedChestplateLayer;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = RespawnariseMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = RespawnariseMod.MOD_ID, value = Dist.CLIENT)
public class RespawnariseModClient {
    public RespawnariseModClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }
    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new WingedSoulTouchedChestplateLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event){
        event.register(ModMenuTypes.SOUL_MATRIX_MENU.get(), SoulMatrixScreen::new);
    }
}
