package reifnsk.minimap.mixin.accessor;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkGetter {

	@Accessor("connection")
	Connection getConnection();

}
