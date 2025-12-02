package reifnsk.minimap;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class GuiKeyConfigScreen extends Screen implements GuiScreenInterface {
	private int top;
	private int bottom;
	private int left;
	private int right;
	private GuiSimpleButton okButton;
	private GuiSimpleButton cancelButton;
	private GuiSimpleButton defaultButton;
	private GuiKeyConfigButton edit;
	private int[] currentKeyCode;

	GuiKeyConfigScreen() {
		KeyInput[] keyInput1 = KeyInput.values();
		this.currentKeyCode = new int[keyInput1.length];

		for(int i2 = 0; i2 < this.currentKeyCode.length; ++i2) {
			this.currentKeyCode[i2] = keyInput1[i2].getKey();
		}

	}

	public void initGui() {
		int i1 = this.calcLabelWidth();
		int i2 = this.calcButtonWidth();
		this.left = (this.width - i1 - i2 - 12) / 2;
		this.right = (this.width + i1 + i2 + 12) / 2;
		this.top = (this.height - KeyInput.values().length * 10) / 2;
		this.bottom = (this.height + KeyInput.values().length * 10) / 2;
		int i3 = this.top;
		KeyInput[] keyInput7;
		int i6 = (keyInput7 = KeyInput.values()).length;

		for(int i5 = 0; i5 < i6; ++i5) {
			KeyInput keyInput4 = keyInput7[i5];
			GuiKeyConfigButton guiKeyConfigButton8 = new GuiKeyConfigButton(this, 0, this.left, i3, i1, i2, keyInput4);
			this.buttons.add(guiKeyConfigButton8);
			i3 += 10;
		}

		int i9 = this.width / 2;
		this.okButton = new GuiSimpleButton(0, i9 - 74, this.bottom + 7, 46, 14, "OK");
		this.buttons.add(this.okButton);
		this.cancelButton = new GuiSimpleButton(0, i9 - 23, this.bottom + 7, 46, 14, "Cancel");
		this.buttons.add(this.cancelButton);
		this.defaultButton = new GuiSimpleButton(0, i9 + 28, this.bottom + 7, 46, 14, "Default");
		this.buttons.add(this.defaultButton);
	}

	private int calcLabelWidth() {
		TextRenderer fontRenderer1 = this.minecraft.textRenderer;
		int i2 = -1;
		KeyInput[] keyInput6;
		int i5 = (keyInput6 = KeyInput.values()).length;

		for(int i4 = 0; i4 < i5; ++i4) {
			KeyInput keyInput3 = keyInput6[i4];
			i2 = Math.max(i2, fontRenderer1.getWidth(keyInput3.name()));
		}

		return i2;
	}

	private int calcButtonWidth() {
		TextRenderer fontRenderer1 = this.textRenderer;
		int i2 = 30;
		KeyInput[] keyInput6;
		int i5 = (keyInput6 = KeyInput.values()).length;

		for(int i4 = 0; i4 < i5; ++i4) {
			KeyInput keyInput3 = keyInput6[i4];
			i2 = Math.max(i2, fontRenderer1.getWidth(">" + keyInput3.getKeyName() + "<"));
		}

		return i2 + 2;
	}

	public void render(int i1, int i2, float f3) {
		String string4 = "Key Config";
		int i5 = this.textRenderer.getWidth(string4);
		int i6 = this.width - i5 >> 1;
		int i7 = this.width + i5 >> 1;
		this.fill(i6 - 2, this.top - 22, i7 + 2, this.top - 8, -1610612736);
		this.drawCenteredTextWithShadow(this.textRenderer, string4, this.width / 2, this.top - 19, -1);
		this.fill(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.render(i1, i2, f3);
	}

	GuiKeyConfigButton getEditKeyConfig() {
		return this.edit;
	}

	protected void buttonClicked(ButtonWidget guiButton1) {
		if(guiButton1 instanceof GuiKeyConfigButton) {
			this.edit = (GuiKeyConfigButton)guiButton1;
		}

		if(guiButton1 == this.okButton) {
			if(KeyInput.saveKeyConfig()) {
				this.minecraft.inGameHud.addChatMessage("\u00a7E[Rei\'s Minimap] Keyconfig Saved.");
			} else {
				this.minecraft.inGameHud.addChatMessage("\u00a7E[Rei\'s Minimap] Error Keyconfig Saving.");
			}

			this.minecraft.setScreen(new GuiOptionScreen());
		}

		int i3;
		if(guiButton1 == this.defaultButton) {
			KeyInput[] keyInput5;
			int i4 = (keyInput5 = KeyInput.values()).length;

			for(i3 = 0; i3 < i4; ++i3) {
				KeyInput keyInput2 = keyInput5[i3];
				keyInput2.setDefault();
			}

			this.buttons.clear();
			this.initGui();
		}

		if(guiButton1 == this.cancelButton) {
			KeyInput[] keyInput6 = KeyInput.values();

			for(i3 = 0; i3 < this.currentKeyCode.length; ++i3) {
				keyInput6[i3].setKey(this.currentKeyCode[i3]);
			}

			this.minecraft.setScreen(new GuiOptionScreen());
		}

	}

	protected void keyPressed(char c1, int i2) {
		if(this.edit != null) {
			this.edit.getKeyInput().setKey(i2);
			this.edit = null;
			this.buttons.clear();
			this.initGui();
		} else if(i2 == 1) {
			KeyInput[] keyInput3 = KeyInput.values();

			for(int i4 = 0; i4 < this.currentKeyCode.length; ++i4) {
				keyInput3[i4].setKey(this.currentKeyCode[i4]);
			}

			this.minecraft.setScreen(null);
		}

	}
}
