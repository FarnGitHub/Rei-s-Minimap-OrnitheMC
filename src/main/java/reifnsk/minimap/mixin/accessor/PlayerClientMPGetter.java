package reifnsk.minimap.mixin.accessor;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(MultiplayerClientPlayerEntity.class)
public interface PlayerClientMPGetter {

	@Accessor("networkHandler")
	ClientNetworkHandler getNetworkHandler();

}
