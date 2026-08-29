package reifnsk.minimap.compat;

import net.fabricmc.loader.api.FabricLoader;

public class CompatHelper {
	public static final CompatImpl GET;

	static {
		if(FabricLoader.getInstance().isModLoaded("stationapi"))
			GET = new StapiCompatImpl();
		else
			GET = new VanillaCompatImpl();
	}

}
