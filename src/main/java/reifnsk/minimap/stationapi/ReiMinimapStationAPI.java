package reifnsk.minimap.stationapi;

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

    @EventListener
    public void addRenderer(EntityRendererRegisterEvent event) {
        event.renderers.put(WaypointEntity.class, new WaypointEntityRender(MinecraftGetter.getInstance()));
    }

}
