package reifnsk.minimap.main.waypoint;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class WaypointEntity extends Entity {
	private final Minecraft mc;

	public WaypointEntity(Minecraft mc) {
		super(mc.world);
		this.mc = mc;
		this.ignoreFrustumCull = true;
		this.isPersistent = true;
		this.onUpdate();
	}

	public void onUpdate() {
		this.setPosition(this.mc.player.x, this.mc.player.y, this.mc.player.z);
	}

	protected void initDataTracker() {
	}

	protected void readNbt(NbtCompound nBTTagCompound1) {
	}

	protected void writeNbt(NbtCompound nBTTagCompound1) {
	}

	@Environment(EnvType.CLIENT)
	public boolean shouldRender(Vec3d pos) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return true;
	}
}
