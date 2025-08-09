package reifnsk.minimap.mixin;

import net.minecraft.client.entity.living.player.LocalPlayerEntity;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.SocketAddress;

@Mixin(Connection.class)
public interface ConnectionGetter {

	@Accessor("address")
	SocketAddress getSocketAdress();

}
