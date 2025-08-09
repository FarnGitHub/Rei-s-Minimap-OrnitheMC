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
			boolean b = i >= this.x && i < this.x + this.width && j >= this.y && j < this.y + this.height;
			this.drawString(minecraft.textRenderer, this.labelText, this.x, this.y + 1, b ? -1 : -4144960);
			String text = this.buttonText;
			if(this == this.parrent.getEditKeyConfig()) {
				text = ">" + text + "<";
			}

			b = i >= this.x + this.width - this.buttonWidth && i < this.x + this.width && j >= this.y && j < this.y + this.height;
			int color = b ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			this.fill(this.x + this.width - this.buttonWidth, this.y, this.x + this.width, this.y + this.height, color);
			this.drawCenteredString(minecraft.textRenderer, text, this.x + this.width - this.buttonWidth / 2, this.y + 1, -1);
		}
	}

	public boolean isMouseOver(Minecraft minecraft, int i, int j) {
		return i >= this.x + this.width - this.buttonWidth && i < this.x + this.width && j >= this.y && j < this.y + this.height;
	}

	void setBounds(int xpos, int ypos, int label, int button) {
		this.x = xpos;
		this.y = ypos;
		this.labelWidth = label;
		this.buttonWidth = button;
		this.width = label + button + 2;
	}

	KeyInput getKeyInput() {
		return this.keyInput;
	}
}
