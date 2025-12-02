package reifnsk.minimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import net.danygames2014.unitweaks.util.ModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;

import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import reifnsk.minimap.stationapi.ReiMinimapStationAPI;

public class WaypointEntityRender extends EntityRenderer {
	static final ReiMinimap rm = ReiMinimap.instance;
	final Minecraft mc;
	double far = 1.0D;
	double _d = 1.0D;

	public WaypointEntityRender(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public void render(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.far = (double)(512 >> this.mc.options.viewDistance) * 0.9D;
		this._d = 1.0D / (double)(256 >> this.mc.options.viewDistance);
		double d10 = rm.getVisibleDimensionScale();
		ArrayList arrayList12 = new ArrayList();
		if(rm.getMarker()) {
			Iterator iterator14 = rm.getWaypoints().iterator();

			while(iterator14.hasNext()) {
				Waypoint waypoint13 = (Waypoint)iterator14.next();
				if(waypoint13.enable) {
					arrayList12.add(new ViewWaypoint(waypoint13, d10));
				}
			}

			if(!arrayList12.isEmpty()) {
				Collections.sort(arrayList12);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_FOG);
				iterator14 = arrayList12.iterator();

				while(iterator14.hasNext()) {
					ViewWaypoint waypointEntityRender$ViewWaypoint15 = (ViewWaypoint)iterator14.next();
					this.draw(waypointEntityRender$ViewWaypoint15, f8, f9);
				}

				GL11.glEnable(GL11.GL_FOG);
				GL11.glEnable(GL11.GL_LIGHTING);
				this.shadowRadius = 0.0F;
			}
		}
	}

	private static boolean isUniTweakThirdPersonView() {
		if(ReiMinimapStationAPI.uniTweak) {
			return ModOptions.frontView;
		}
		return false;
	}

	void draw(ViewWaypoint waypointEntityRender$ViewWaypoint1, float f2, float f3) {
		float f4 = (float)Math.max(0.0D, 1.0D - waypointEntityRender$ViewWaypoint1.distance * this._d);
		TextRenderer fontRenderer5 = this.getTextRenderer();
		GL11.glPushMatrix();
		StringBuilder stringBuilder6 = new StringBuilder();
		if(rm.getMarkerLabel() && waypointEntityRender$ViewWaypoint1.name != null) {
			stringBuilder6.append(waypointEntityRender$ViewWaypoint1.name);
		}

		if(rm.getMarkerDistance()) {
			if(!stringBuilder6.isEmpty()) {
				stringBuilder6.append(" ");
			}

			stringBuilder6.append(String.format("[%1.2fm]", new Object[]{waypointEntityRender$ViewWaypoint1.distance}));
		}

		String string7 = stringBuilder6.toString();
		double d8 = (waypointEntityRender$ViewWaypoint1.dl * 0.1D + 1.0D) * 0.02666666666666667D;
		int i10 = rm.getMarkerIcon() ? -16 : 0;
		GL11.glTranslated(waypointEntityRender$ViewWaypoint1.dx, waypointEntityRender$ViewWaypoint1.dy, waypointEntityRender$ViewWaypoint1.dz);
		GL11.glRotatef(-(this.dispatcher.yaw + (isUniTweakThirdPersonView() ? 180.0F : 0F)), 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(isUniTweakThirdPersonView() ? -this.dispatcher.pitch: this.dispatcher.pitch, 1.0F, 0.0F, 0.0F);
		GL11.glScaled(-d8, -d8, d8);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator11 = Tessellator.INSTANCE;
		if(rm.getMarkerIcon()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			Waypoint.FILE[waypointEntityRender$ViewWaypoint1.type].bind();
			tessellator11.startQuads();
			tessellator11.color(waypointEntityRender$ViewWaypoint1.red, waypointEntityRender$ViewWaypoint1.green, waypointEntityRender$ViewWaypoint1.blue, 0.4F);
			tessellator11.vertex(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tessellator11.vertex(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tessellator11.vertex(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tessellator11.vertex(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tessellator11.draw();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			tessellator11.startQuads();
			tessellator11.color(waypointEntityRender$ViewWaypoint1.red, waypointEntityRender$ViewWaypoint1.green, waypointEntityRender$ViewWaypoint1.blue, f4);
			tessellator11.vertex(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tessellator11.vertex(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tessellator11.vertex(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tessellator11.vertex(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tessellator11.draw();
		}

		int i12 = fontRenderer5.getWidth(string7) >> 1;
		if(i12 != 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			tessellator11.startQuads();
			tessellator11.color(0.0F, 0.0F, 0.0F, 0.6275F);
			tessellator11.vertex((double)(-i12 - 1), (double)(i10 - 1), 0.0D);
			tessellator11.vertex((double)(-i12 - 1), (double)(i10 + 8), 0.0D);
			tessellator11.vertex((double)(i12 + 1), (double)(i10 + 8), 0.0D);
			tessellator11.vertex((double)(i12 + 1), (double)(i10 - 1), 0.0D);
			tessellator11.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			fontRenderer5.drawWithShadow(string7, -i12, i10, waypointEntityRender$ViewWaypoint1.type == 0 ? 1627389951 : 1627324416);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			int i13 = (int)(255.0F * f4);
			if(i13 > 5) {
				fontRenderer5.drawWithShadow(string7, -i12, i10, (waypointEntityRender$ViewWaypoint1.type == 0 ? 0xFFFFFF : 16711680) | i13 << 24);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	class ViewWaypoint extends Waypoint implements Comparable {
		double dx;
		double dy;
		double dz;
		double dl;
		double distance;

		ViewWaypoint(Waypoint waypoint2, double d3) {
			super(waypoint2);
			this.dx = (double)waypoint2.x * d3 - EntityRenderDispatcher.offsetX + 0.5D;
			this.dy = (double)waypoint2.y - EntityRenderDispatcher.offsetY + 0.5D;
			this.dz = (double)waypoint2.z * d3 - EntityRenderDispatcher.offsetZ + 0.5D;
			this.dl = this.distance = Math.sqrt(this.dx * this.dx + this.dy * this.dy + this.dz * this.dz);
			if(this.dl > WaypointEntityRender.this.far) {
				double d5 = WaypointEntityRender.this.far / this.dl;
				this.dx *= d5;
				this.dy *= d5;
				this.dz *= d5;
				this.dl = WaypointEntityRender.this.far;
			}

		}

		public int compareTo(ViewWaypoint waypointEntityRender$ViewWaypoint1) {
			return waypointEntityRender$ViewWaypoint1.distance < this.distance ? -1 : (waypointEntityRender$ViewWaypoint1.distance > this.distance ? 1 : 0);
		}

		public int compareTo(Object object1) {
			return this.compareTo((ViewWaypoint)object1);
		}
	}
}
