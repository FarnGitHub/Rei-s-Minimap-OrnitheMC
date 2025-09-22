package reifnsk.minimap.optionscreen.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import reifnsk.minimap.optionscreen.screen.GuiOptionScreen;

import java.util.function.Function;

public class ModMenuLinker implements ModMenuApi {
    @Override
    public String getModId() {
        return "rei_minimap";
    }

    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return screen1 -> new GuiOptionScreen(screen1, 0);
    }
}
