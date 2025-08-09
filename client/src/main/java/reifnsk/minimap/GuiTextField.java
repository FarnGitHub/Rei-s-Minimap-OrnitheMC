package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

import org.lwjgl.input.Keyboard;

public class GuiTextField extends ButtonWidget {
	private static GuiTextField active;
	private int inputType;
	private GuiTextField prev;
	private GuiTextField next;

	public GuiTextField(String s) {
		super(0, 0, 0, 0, 0, s);
	}

	public GuiTextField() {
		super(0, 0, 0, 0, 0, "");
	}

	public void render(Minecraft mc, int mx, int my) {
		int color = active == this ? -2134851392 : -2141167520;
		this.fill(this.x, this.y, this.x + this.width, this.y + this.height, color);
		if(this.inputType == 0) {
			this.drawCenteredString(mc.textRenderer, this.message, this.x + this.width / 2, this.y + 1, -1);
		} else {
			int w = mc.textRenderer.getWidth(this.message);
			this.drawString(mc.textRenderer, this.message, this.x + this.width - w - 1, this.y + 1, -1);
		}

	}

	public boolean isMouseOver(Minecraft mc, int mx, int my) {
		if(mx >= this.x && mx < this.x + this.width && my >= this.y && my < this.y + this.height) {
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

	static void keyType(Minecraft mc, char c, int i) {
		if(active != null) {
			active.kt(mc, c, i);
		}

	}

	private void kt(Minecraft mc, char c, int i) {
		if(i != 14 && i != 211) {
			if(i == 15) {
				if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					next();
				} else {
					prev();
				}
			}

			if(i == 28) {
				next();
			}

			if(this.checkInput(c)) {
				String newString = this.message + c;
				if(mc.textRenderer.getWidth(newString) < this.width - 2) {
					try {
						int e;
						if(this.inputType == 1) {
							e = Integer.parseInt(newString);
							newString = e < -32000000 ? "-32000000" : (e >= 32000000 ? "31999999" : Integer.toString(e));
						}

						if(this.inputType == 2) {
							e = Integer.parseInt(newString);
							newString = e < 0 ? "0" : (e > 129 ? "129" : Integer.toString(e));
						}
					} catch (NumberFormatException numberFormatException6) {
					}

					this.message = newString;
				}
			}

		} else {
			if(!this.message.isEmpty()) {
				this.message = this.message.substring(0, this.message.length() - 1);
			}

		}
	}

	boolean checkInput(char c) {
		switch(this.inputType) {
		case 0:
			return " !\"#$%&\'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c) != -1;
		case 1:
			return (this.message.isEmpty() ? "-0123456789" : "0123456789").indexOf(c) != -1;
		case 2:
			return "0123456789".indexOf(c) != -1;
		default:
			return false;
		}
	}

	void norm() {
		String newString = this.message;

		try {
			int e;
			if(this.inputType == 1) {
				e = Integer.parseInt(newString);
				newString = e < -32000000 ? "-32000000" : (e >= 32000000 ? "31999999" : Integer.toString(e));
			}

			if(this.inputType == 2) {
				e = Integer.parseInt(newString);
				newString = e < 0 ? "0" : (e > 129 ? "129" : Integer.toString(e));
			}
		} catch (NumberFormatException numberFormatException3) {
			newString = "0";
		}

		this.message = newString;
	}

	void setInputType(int i) {
		this.inputType = i;
	}

	void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}

	void setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	void setNext(GuiTextField next) {
		this.next = next;
	}

	void setPrev(GuiTextField prev) {
		this.prev = prev;
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
}
