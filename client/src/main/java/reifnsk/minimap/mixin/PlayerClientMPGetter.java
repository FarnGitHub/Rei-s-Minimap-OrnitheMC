package reifnsk.minimap.mixin;

import net.minecraft.client.entity.living.player.LocalPlayerEntity;
import net.minecraft.client.gui.ChatMessage;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LocalPlayerEntity.class)
public interface PlayerClientMPGetter {

	@Accessor("networkHandler")
	ClientNetworkHandler getNetworkHandler();

}
