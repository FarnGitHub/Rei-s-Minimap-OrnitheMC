package reifnsk.minimap.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reifnsk.minimap.main.ReiMinimap;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    private Minecraft minecraft;

    @Inject(method="render", at = @At("TAIL"))
    public void renderMinimap(float screenOpen, boolean mouseX, int mouseY, int par4, CallbackInfo ci) {
        ReiMinimap.instance.onTickInGame(minecraft);
    }
}
