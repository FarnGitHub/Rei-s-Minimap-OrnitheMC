package reifnsk.minimap.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import reifnsk.minimap.main.gui.screen.GuiOptionScreen;

public class ModMenuLinker implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GuiOptionScreen::new;
    }
}
