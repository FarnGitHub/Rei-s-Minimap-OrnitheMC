package reifnsk.minimap.stationapi;

import net.danygames2014.unitweaks.util.ModOptions;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;
import reifnsk.minimap.WaypointEntity;
import reifnsk.minimap.WaypointEntityRender;
import reifnsk.minimap.mixin.accessor.MinecraftGetter;


public class ReiMinimapStationAPI {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    public static boolean uniTweak = false;

    @EventListener
    public void addRenderer(EntityRendererRegisterEvent event) {
        event.renderers.put(WaypointEntity.class, new WaypointEntityRender(MinecraftGetter.getInstance()));
        if(FabricLoader.getInstance().isModLoaded("unitweaks")) {
            try {
                uniTweak = ModOptions.class.getDeclaredField("frontView") != null;
            } catch (NoSuchFieldException e) {
                uniTweak = false;
            }
        }
    }

}
