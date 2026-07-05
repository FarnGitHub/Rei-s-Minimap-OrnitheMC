package reifnsk.minimap.main.waypoint;

import java.util.ArrayList;

import net.danygames2014.unitweaks.util.ModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;

import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import reifnsk.minimap.main.ReiMinimap;
import reifnsk.minimap.stationapi.ReiMinimapStationAPI;

public class WaypointEntityRender extends EntityRenderer {
	double far = 1.0D;
	double _d = 1.0D;

	public void render(Entity ent, double x, double y, double z, float pitch, float yaw) {
		ReiMinimap rm = ReiMinimap.instance;
		this.far = (double)(512 >> Minecraft.INSTANCE.options.viewDistance) * 0.9D;
		this._d = 1.0D / (double)(256 >> Minecraft.INSTANCE.options.viewDistance);
		double dmScale = rm.getVisibleDimensionScale();
		ArrayList<ViewWaypoint> waypoints = new ArrayList<>();
		if(rm.getMarker()) {

            for (Waypoint wp : rm.getWaypoints()) {
                if (wp.enable)
                    waypoints.add(new ViewWaypoint(wp, dmScale));
            }

			if(!waypoints.isEmpty()) {
				waypoints.sort(null);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_FOG);

                for (ViewWaypoint waypoint : waypoints) {
                    this.draw(waypoint);
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

	private void draw(ViewWaypoint waypoint) {
		ReiMinimap rm = ReiMinimap.instance;
		float distance = (float)Math.max(0.0D, 1.0D - waypoint.distance * this._d);
		TextRenderer textRenderer = this.getTextRenderer();
		GL11.glPushMatrix();
		StringBuilder builder = new StringBuilder();
		if(rm.getMarkerLabel() && waypoint.name != null) {
			builder.append(waypoint.name);
		}

		if(rm.getMarkerDistance()) {
			if(!builder.isEmpty()) {
				builder.append(" ");
			}

			builder.append(String.format("[%1.2fm]", waypoint.distance));
		}

		String finalText = builder.toString();
		double scale = (waypoint.dl * 0.1D + 1.0D) * 0.02666666666666667D;
		int markedTexCoord = rm.getMarkerIcon() ? -16 : 0;
		GL11.glTranslated(waypoint.dx, waypoint.dy, waypoint.dz);
		GL11.glRotatef(-(this.dispatcher.yaw + (isUniTweakThirdPersonView() ? 180.0F : 0F)), 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(isUniTweakThirdPersonView() ? -this.dispatcher.pitch: this.dispatcher.pitch, 1.0F, 0.0F, 0.0F);
		GL11.glScaled(-scale, -scale, scale);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tess = Tessellator.INSTANCE;
		if(rm.getMarkerIcon()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			Waypoint.FILE[waypoint.type].bind();
			tess.startQuads();
			tess.color(waypoint.red, waypoint.green, waypoint.blue, 0.4F);
			tess.vertex(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tess.vertex(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tess.vertex(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tess.vertex(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tess.draw();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			tess.startQuads();
			tess.color(waypoint.red, waypoint.green, waypoint.blue, distance);
			tess.vertex(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tess.vertex(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tess.vertex(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tess.vertex(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tess.draw();
		}

		int textWidth = textRenderer.getWidth(finalText) >> 1;
		if(textWidth != 0) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			tess.startQuads();
			tess.color(0.0F, 0.0F, 0.0F, 0.6275F);
			tess.vertex(-textWidth - 1, markedTexCoord - 1, 0.0D);
			tess.vertex(-textWidth - 1, markedTexCoord + 8, 0.0D);
			tess.vertex(textWidth + 1, markedTexCoord + 8, 0.0D);
			tess.vertex(textWidth + 1, markedTexCoord - 1, 0.0D);
			tess.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			textRenderer.drawWithShadow(finalText, -textWidth, markedTexCoord, waypoint.type == 0 ? 1627389951 : 1627324416);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			int dis = (int)(255.0F * distance);
			if(dis > 5) {
				textRenderer.drawWithShadow(finalText, -textWidth, markedTexCoord, (waypoint.type == 0 ? 0xFFFFFF : 16711680) | dis << 24);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public class ViewWaypoint extends Waypoint implements Comparable<ViewWaypoint> {
		double dx;
		double dy;
		double dz;
		double dl;
		double distance;

		ViewWaypoint(Waypoint wp, double distance) {
			super(wp);
			this.dx = (double)wp.x * distance - EntityRenderDispatcher.offsetX + 0.5D;
			this.dy = (double)wp.y - EntityRenderDispatcher.offsetY + 0.5D;
			this.dz = (double)wp.z * distance - EntityRenderDispatcher.offsetZ + 0.5D;
			this.dl = this.distance = Math.sqrt(this.dx * this.dx + this.dy * this.dy + this.dz * this.dz);
			if(this.dl > WaypointEntityRender.this.far) {
				double d5 = WaypointEntityRender.this.far / this.dl;
				this.dx *= d5;
				this.dy *= d5;
				this.dz *= d5;
				this.dl = WaypointEntityRender.this.far;
			}

		}

		public int compareTo(ViewWaypoint waypoint) {
			return Double.compare(waypoint.distance, this.distance);
		}

	}
}
