package reifnsk.minimap.main.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;

public class GuiSimpleButton extends ButtonWidget {
	public GuiSimpleButton(int i1, int i2, int i3, int i4, int i5, String string6) {
		super(i1, i2, i3, i4, i5, string6);
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		if(this.visible) {
			TextRenderer fontRenderer4 = minecraft1.textRenderer;
			boolean z5 = i2 >= this.x && i3 >= this.y && i2 < this.x + this.width && i3 < this.y + this.height;
			int i6 = z5 && this.active ? -932813210 : -1610612736;
			this.fill(this.x, this.y, this.x + this.width, this.y + this.height, i6);
			this.drawCenteredTextWithShadow(fontRenderer4, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, this.active ? -1 : -8355712);
		}
	}
}
