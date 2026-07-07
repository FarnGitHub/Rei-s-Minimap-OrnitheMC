package reifnsk.minimap.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reifnsk.minimap.main.waypoint.WaypointRenderer;


@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method="renderFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntities(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/Culler;F)V", shift = At.Shift.AFTER))
    public void reiminimap_renderWaypoint(float tick, long time, CallbackInfo ci) {
        WaypointRenderer.render();
    }

}
