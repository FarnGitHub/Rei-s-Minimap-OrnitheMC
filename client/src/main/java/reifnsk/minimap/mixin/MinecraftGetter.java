package reifnsk.minimap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public interface MinecraftGetter {

	@Accessor("INSTANCE")
	public static Minecraft getInstance() {
		throw new AssertionError();
	}

}
