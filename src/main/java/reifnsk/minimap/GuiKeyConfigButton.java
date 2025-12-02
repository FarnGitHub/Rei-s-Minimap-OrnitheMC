package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

public class GuiKeyConfigButton extends ButtonWidget {
	private GuiKeyConfigScreen parrent;
	private KeyInput keyInput;
	private String labelText;
	private String buttonText;
	private int labelWidth;
	private int buttonWidth;

	public GuiKeyConfigButton(GuiKeyConfigScreen guiKeyConfigScreen1, int i2, int i3, int i4, int i5, int i6, KeyInput keyInput7) {
		super(i2, i3, i4, i5 + 12 + i6, 9, "");
		this.parrent = guiKeyConfigScreen1;
		this.keyInput = keyInput7;
		this.labelWidth = i5;
		this.buttonWidth = i6;
		this.labelText = this.keyInput.label();
		this.buttonText = this.keyInput.getKeyName();
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		if(this.keyInput != null) {
			boolean z4 = i2 >= this.x && i2 < this.x + this.width && i3 >= this.y && i3 < this.y + this.height;
			this.drawTextWithShadow(minecraft1.textRenderer, this.labelText, this.x, this.y + 1, z4 ? -1 : -4144960);
			String string5 = this.buttonText;
			if(this == this.parrent.getEditKeyConfig()) {
				string5 = ">" + string5 + "<";
			}

			z4 = i2 >= this.x + this.width - this.buttonWidth && i2 < this.x + this.width && i3 >= this.y && i3 < this.y + this.height;
			int i6 = z4 ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			this.fill(this.x + this.width - this.buttonWidth, this.y, this.x + this.width, this.y + this.height, i6);
			this.drawCenteredTextWithShadow(minecraft1.textRenderer, string5, this.x + this.width - this.buttonWidth / 2, this.y + 1, -1);
		}
	}

	public boolean isMouseOver(Minecraft minecraft1, int i2, int i3) {
		return i2 >= this.x + this.width - this.buttonWidth && i2 < this.x + this.width && i3 >= this.y && i3 < this.y + this.height;
	}

	void setBounds(int i1, int i2, int i3, int i4) {
		this.x = i1;
		this.y = i2;
		this.labelWidth = i3;
		this.buttonWidth = i4;
		this.width = i3 + i4 + 2;
	}

	KeyInput getKeyInput() {
		return this.keyInput;
	}
}
