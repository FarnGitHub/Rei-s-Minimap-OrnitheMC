package reifnsk.minimap.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reifnsk.minimap.ReiMinimap;

@Mixin(GameGui.class)
public abstract class GameGuiMixin {

	@Shadow
	Minecraft minecraft;

	@Inject(
		method = "render",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/options/GameOptions;debugProfilerEnabled:Z",
			ordinal = 0
		)
	)
	private void injectBeforeDebugOverlay(CallbackInfo ci) {
		// Your custom rendering code here
		ReiMinimap.instance.onTickInGame(minecraft);
	}

}
