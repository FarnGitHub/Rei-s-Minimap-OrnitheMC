package reifnsk.minimap.mixin;

import net.minecraft.client.gui.ChatMessage;
import net.minecraft.client.gui.GameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GameGui.class)
public interface GameGuiGetter {

	@Accessor("chatMessages")
	public List<ChatMessage> getChatMessage();

}
