package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class WaypointEntity extends Entity {
	private final Minecraft mc;

	public WaypointEntity(Minecraft minecraft1) {
		super(minecraft1.world);
		this.mc = minecraft1;
		this.ignoreFrustumCull = true;
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

	public boolean shouldRender(double distance) {
		return true;
	}
}
