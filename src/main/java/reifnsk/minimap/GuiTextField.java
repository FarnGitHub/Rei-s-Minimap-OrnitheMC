package reifnsk.minimap;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.input.Keyboard;

public class GuiTextField extends ButtonWidget {
	private static GuiTextField active;
	private int inputType;
	private GuiTextField prev;
	private GuiTextField next;
	private int norm = 0;

	public GuiTextField(String string1) {
		super(0, 0, 0, 0, 0, string1);
	}

	public GuiTextField() {
		super(0, 0, 0, 0, 0, "");
	}

	public void render(Minecraft minecraft1, int i2, int i3) {
		int i4 = active == this ? -2134851392 : -2141167520;
		this.fill(this.x, this.y, this.x + this.width, this.y + this.height, i4);
		if(this.inputType == 0) {
			this.drawCenteredTextWithShadow(minecraft1.textRenderer, this.text, this.x + this.width / 2, this.y + 1, -1);
		} else {
			int i5 = minecraft1.textRenderer.getWidth(this.text);
			this.drawTextWithShadow(minecraft1.textRenderer, this.text, this.x + this.width - i5 - 1, this.y + 1, -1);
		}

	}

	public boolean isMouseOver(Minecraft minecraft1, int i2, int i3) {
		if(i2 >= this.x && i2 < this.x + this.width && i3 >= this.y && i3 < this.y + this.height) {
			this.active();
		}

		return false;
	}

	public void active() {
		if(active != null) {
			active.norm();
		}

		active = this;
	}

	static void a(Minecraft minecraft0, char c1, int i2) {
		if(active != null) {
			active.kt(minecraft0, c1, i2);
		}

	}

	private void kt(Minecraft minecraft1, char c2, int i3) {
		String string4;
		int i5;
		if(this.inputType == 0 && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) && i3 == 47) {
			string4 = Screen.getClipboard();
			if(string4 == null) {
				return;
			}

			i5 = 0;

			for(int i6 = string4.length(); i5 < i6; ++i5) {
				char c7 = string4.charAt(i5);
				if(c7 != 13 && c7 != 10) {
					if(c7 == 58) {
						c7 = 59;
					}

					String string8 = this.text + c7;
					if(minecraft1.textRenderer.getWidth(string8) >= this.width - 2) {
						break;
					}

					this.text = string8;
				}
			}
		}

		if(i3 != 14 && i3 != 211) {
			if(i3 == 15) {
				if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					next();
				} else {
					prev();
				}
			}

			if(i3 == 28) {
				next();
			}

			if(this.checkInput(c2)) {
				string4 = this.text + c2;
				if(minecraft1.textRenderer.getWidth(string4) < this.width - 2) {
					try {
						if(this.inputType == 1) {
							i5 = Integer.parseInt(string4);
							string4 = i5 < -32000000 ? "-32000000" : (i5 >= 32000000 ? "31999999" : Integer.toString(i5));
						}

						if(this.inputType == 2) {
							i5 = Integer.parseInt(string4);
							string4 = i5 < 0 ? "0" : (i5 > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(i5));
						}
					} catch (NumberFormatException numberFormatException9) {
					}

					this.text = string4;
				}
			}

		} else {
			if(!this.text.isEmpty()) {
				this.text = this.text.substring(0, this.text.length() - 1);
			}

		}
	}

	boolean checkInput(char c1) {
		switch(this.inputType) {
		case 0:
			if(" !\"#$%&\'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c1) != -1) {
				return true;
			}

			return false;
		case 1:
			if((this.text.isEmpty() ? "-0123456789" : "0123456789").indexOf(c1) != -1) {
				return true;
			}

			return false;
		case 2:
			if("0123456789".indexOf(c1) != -1) {
				return true;
			}

			return false;
		default:
			return false;
		}
	}

	void norm() {
		String string1 = this.text;

		try {
			int i2;
			if(this.inputType == 1) {
				i2 = Integer.parseInt(string1);
				string1 = i2 < -32000000 ? "-32000000" : (i2 >= 32000000 ? "31999999" : Integer.toString(i2));
			}

			if(this.inputType == 2) {
				i2 = Integer.parseInt(string1);
				string1 = i2 < 0 ? "0" : (i2 > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(i2));
			}
		} catch (NumberFormatException numberFormatException3) {
			string1 = Integer.toString(this.norm);
		}

		this.text = string1;
	}

	void setInputType(int i1) {
		this.inputType = i1;
	}

	void setPosition(int i1, int i2) {
		this.x = i1;
		this.y = i2;
	}

	void setSize(int i1, int i2) {
		this.width = i1;
		this.height = i2;
	}

	void setBounds(int i1, int i2, int i3, int i4) {
		this.x = i1;
		this.y = i2;
		this.width = i3;
		this.height = i4;
	}

	void setNext(GuiTextField guiTextField1) {
		this.next = guiTextField1;
	}

	void setPrev(GuiTextField guiTextField1) {
		this.prev = guiTextField1;
	}

	static void next() {
		if(active != null) {
			active.norm();
			active = active.next;
		}

	}

	static void prev() {
		if(active != null) {
			active.norm();
			active = active.prev;
		}

	}

	static GuiTextField getActive() {
		return active;
	}

	void setNorm(int i1) {
		this.norm = i1;
	}
}
