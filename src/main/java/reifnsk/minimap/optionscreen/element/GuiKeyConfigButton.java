package reifnsk.minimap.optionscreen.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.opengl.GL11;
import reifnsk.minimap.optionscreen.enums.KeyInput;
import reifnsk.minimap.optionscreen.screen.GuiKeyConfigScreen;

public class GuiKeyConfigButton extends ButtonWidget {
	private GuiKeyConfigScreen parrent;
	private KeyInput keyInput;
	private String labelText;
	private String buttonText;
	private int labelWidth;
	private int buttonWidth;

	public GuiKeyConfigButton(GuiKeyConfigScreen parrent, int id, int x, int y, int label, int button, KeyInput key) {
		super(id, x, y, label + 12 + button, 9, "");
		this.parrent = parrent;
		this.keyInput = key;
		this.labelWidth = label;
		this.buttonWidth = button;
		this.labelText = this.keyInput.label();
		this.buttonText = this.keyInput.getKeyName();
	}

	public void render(Minecraft minecraft, int i, int j) {
		if(this.keyInput != null) {
			boolean bet = i >= this.x && i < this.x + this.width && j >= this.y && j < this.y + this.height;
			this.drawTextWithShadow(minecraft.textRenderer, this.labelText, this.x, this.y + 1, bet ? -1 : -4144960);
			String text = this.buttonText;
			if(this == this.parrent.getEditKeyConfig()) {
				text = ">" + text + "<";
			}

			bet = i >= this.x + this.width - this.buttonWidth && i < this.x + this.width && j >= this.y && j < this.y + this.height;
			int color = bet ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			this.fill(this.x + this.width - this.buttonWidth, this.y, this.x + this.width, this.y + this.height, color);
			//this.fillButton(minecraft, this.x + this.width - this.buttonWidth, this.y, this.x + this.width, this.y + this.height, color);
			this.drawCenteredTextWithShadow(minecraft.textRenderer, text, this.x + this.width - this.buttonWidth / 2, this.y + 1, -1);
		}
	}

	public boolean isMouseOver(Minecraft minecraft, int i, int j) {
		return i >= this.x + this.width - this.buttonWidth && i < this.x + this.width && j >= this.y && j < this.y + this.height;
	}

	public void setBounds(int xpos, int ypos, int label, int button) {
		this.x = xpos;
		this.y = ypos;
		this.labelWidth = label;
		this.buttonWidth = button;
		this.width = label + button + 2;
	}

	public KeyInput getKeyInput() {
		return this.keyInput;
	}

	public void fillButton(Minecraft mc, int x1, int y1, int x2, int y2, int color) {
		int width  = x2 - x1;
		int height = y2 - y1;

		float a = (color >> 24 & 255) / 255.0F;
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8  & 255) / 255.0F;
		float b = (color       & 255) / 255.0F;

		// Draw the button using the default gui texture
		GL11.glBindTexture(3553, mc.textureManager.getTextureId("/gui/gui.png"));
		GL11.glColor4f(r, g, b, a);

		// left half
		this.drawTexture(x1, y1, 0, 46 + 1 * 20, width / 2, height);
		// right half
		this.drawTexture(x1 + width / 2, y1, 200 - width / 2, 46 + 1 * 20, width / 2, height);
	}
}
