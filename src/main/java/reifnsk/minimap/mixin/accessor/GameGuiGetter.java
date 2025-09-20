package reifnsk.minimap.mixin.accessor;

import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(InGameHud.class)
public interface GameGuiGetter {

	@Accessor("messages")
	public List<ChatHudLine> getChatMessage();

}
