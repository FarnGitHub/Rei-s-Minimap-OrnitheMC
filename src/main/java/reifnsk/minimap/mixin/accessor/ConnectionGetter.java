package reifnsk.minimap.mixin.accessor;

import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.SocketAddress;

@Mixin(Connection.class)
public interface ConnectionGetter {

	@Accessor("address")
	SocketAddress getSocketAdress();

}
