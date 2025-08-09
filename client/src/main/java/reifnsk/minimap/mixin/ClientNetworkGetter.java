package reifnsk.minimap.mixin;

import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.SocketAddress;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkGetter {

	@Accessor("connection")
	Connection getConnection();

}
