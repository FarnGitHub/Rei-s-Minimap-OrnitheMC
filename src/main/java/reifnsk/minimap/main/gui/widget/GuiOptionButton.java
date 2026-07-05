package reifnsk.minimap.main.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import reifnsk.minimap.main.option.EnumOption;
import reifnsk.minimap.main.option.EnumOptionValue;
import reifnsk.minimap.main.ReiMinimap;

public class GuiOptionButton extends ButtonWidget {
	private static int NAME_WIDTH;
	private static int VALUE_WIDTH;
	private static int WIDTH;
	private EnumOption option;
	private EnumOptionValue value;

	public GuiOptionButton(TextRenderer fontRenderer1, EnumOption enumOption2) {
		super(0, 0, 0, 0, 10, "");
		this.option = enumOption2;
		this.value = this.option.getValue(0);

		for(int i3 = 0; i3 < enumOption2.getValueNum(); ++i3) {
			String string4 = enumOption2.getValue(i3).text();
			int i5 = fontRenderer1.getWidth(string4) + 4;
			VALUE_WIDTH = Math.max(VALUE_WIDTH, i5);
		}

		NAME_WIDTH = Math.max(NAME_WIDTH, fontRenderer1.getWidth(enumOption2.getText() + ": "));
		WIDTH = VALUE_WIDTH + 8 + NAME_WIDTH;
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		if(this.visible) {
			this.value = ReiMinimap.instance.getOption(this.option);
			TextRenderer fontRenderer4 = minecraft1.textRenderer;
			boolean z5 = i2 >= this.x && i3 >= this.y && i2 < this.x + getWidth() && i3 < this.y + getHeight();
			int i6 = z5 ? -1 : -4144960;
			int i7 = z5 ? 1728053247 : this.value.color;
			this.drawTextWithShadow(fontRenderer4, this.option.getText(), this.x, this.y + 1, i6);
			int i8 = this.x + NAME_WIDTH + 8;
			int i9 = i8 + VALUE_WIDTH;
			this.fill(i8, this.y, i9, this.y + getHeight() - 1, i7);
			this.drawCenteredTextWithShadow(fontRenderer4, this.value.text(), i8 + VALUE_WIDTH / 2, this.y + 1, -1);
		}
	}

	public boolean isMouseOver(Minecraft minecraft1, int i2, int i3) {
		if(this.active && i2 >= this.x && i3 >= this.y && i2 < this.x + getWidth() && i3 < this.y + getHeight()) {
			this.nextValue();
			return true;
		} else {
			return false;
		}
	}

	public EnumOption getOption() {
		return this.option;
	}

	public EnumOptionValue getValue() {
		return this.value;
	}

	public void setValue(EnumOptionValue enumOptionValue1) {
		if(this.option.getValue(enumOptionValue1) != -1) {
			this.value = enumOptionValue1;
		}

	}

	public void nextValue() {
		this.value = this.option.getValue((this.option.getValue(this.value) + 1) % this.option.getValueNum());
		if(!ReiMinimap.instance.getAllowCavemap() && this.option == EnumOption.RENDER_TYPE && this.value == EnumOptionValue.CAVE) {
			this.nextValue();
		}

	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return 10;
	}
}
