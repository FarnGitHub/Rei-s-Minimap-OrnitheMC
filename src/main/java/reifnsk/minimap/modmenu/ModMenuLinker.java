package reifnsk.minimap.modmenu;

import net.danygames2014.modmenu.api.ConfigScreenFactory;
import net.danygames2014.modmenu.api.ModMenuApi;
import reifnsk.minimap.main.gui.screen.GuiOptionScreen;

public class ModMenuLinker implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen1 -> new GuiOptionScreen(screen1);
    }
}
