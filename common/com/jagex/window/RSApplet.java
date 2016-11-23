package com.jagex.window;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import com.jagex.Utils;

public class RSApplet extends Applet
		implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener, WindowListener {

	private boolean isApplet;

	private RSBase client;

	public RSApplet(RSBase client) {
		this.client = client;
	}

	@Override
	public void init() {
		this.client.init();
	}
	
	public final void initClientFrame(int i, int j, boolean applet) {
		this.isApplet = applet;
		if (!applet) {
			this.gameFrame = new RSFrame(this, j, i, RSBase.frameMode == RSBase.ScreenMode.RESIZABLE,
					RSBase.frameMode == RSBase.ScreenMode.FULLSCREEN);
			this.gameFrame.setFocusTraversalKeysEnabled(false);
		}
		this.graphics = this.getGameComponent().getGraphics();
		Utils.startRunnable(this, 1);
	}

	public void refreshFrameSize(boolean undecorated, int width, int height, boolean resizable, boolean full) {
		boolean createdByApplet = (this.isApplet && !full);
		if (this.gameFrame != null) {
			this.gameFrame.dispose();
		}
		if (!createdByApplet) {
			this.gameFrame = new RSFrame(this, width, height, resizable, undecorated);
			this.gameFrame.addWindowListener(this);
		}
		this.graphics = (createdByApplet ? this : this.gameFrame).getGraphics();
		if (!createdByApplet) {
			// getGameComponent().addMouseWheelListener(this);
			this.getGameComponent().addMouseListener(this);
			this.getGameComponent().addMouseMotionListener(this);
			this.getGameComponent().addKeyListener(this);
			this.getGameComponent().addFocusListener(this);
		}
	}

	public boolean appletClient() {
		return this.gameFrame == null && this.isApplet == true;
	}

	private int getW() {
		return (this.appletClient() ? this.getGameComponent().getWidth() : this.gameFrame.getFrameWidth());
	}

	private int getH() {
		return (this.appletClient() ? this.getGameComponent().getHeight() : this.gameFrame.getFrameHeight());
	}

	@Override
	public void run() {
		this.getGameComponent().addMouseListener(this);
		this.getGameComponent().addMouseMotionListener(this);
		this.getGameComponent().addKeyListener(this);
		this.getGameComponent().addFocusListener(this);
		if (this.gameFrame != null)
			this.gameFrame.addWindowListener(this);
		this.drawLoadingText(0, "Loading...");
		this.client.startUp();
		int i = 0;
		int j = 256;
		int k = 1;
		int i1 = 0;
		int j1 = 0;
		for (int k1 = 0; k1 < 10; k1++)
			this.aLongArray7[k1] = System.currentTimeMillis();

		long l = System.currentTimeMillis();
		while (this.anInt4 >= 0) {
			if (this.anInt4 > 0) {
				this.anInt4--;
				if (this.anInt4 == 0) {
					this.exit();
					return;
				}
			}
			int i2 = j;
			int j2 = k;
			j = 300;
			k = 1;
			long l1 = System.currentTimeMillis();
			if (this.aLongArray7[i] == 0L) {
				j = i2;
				k = j2;
			} else if (l1 > this.aLongArray7[i])
				j = (int) (2560 * this.delayTime / (l1 - this.aLongArray7[i]));
			if (j < 25)
				j = 25;
			if (j > 256) {
				j = 256;
				k = (int) (this.delayTime - (l1 - this.aLongArray7[i]) / 10L);
			}
			if (k > this.delayTime)
				k = this.delayTime;
			this.aLongArray7[i] = l1;
			i = (i + 1) % 10;
			if (k > 1) {
				for (int k2 = 0; k2 < 10; k2++)
					if (this.aLongArray7[k2] != 0L)
						this.aLongArray7[k2] += k;

			}
			if (k < this.minDelay)
				k = this.minDelay;
			try {
				Thread.sleep(k);
			} catch (InterruptedException _ex) {
				j1++;
			}
			for (; i1 < 256; i1 += j) {
				this.clickMode3 = this.clickMode1;
				this.saveClickX = this.clickX;
				this.saveClickY = this.clickY;
				this.aLong29 = this.clickTime;
				this.clickMode1 = 0;
				this.client.processGameLoop();
				this.readIndex = this.writeIndex;
			}

			i1 &= 0xff;
			if (this.delayTime > 0)
				this.fps = (1000 * j) / (this.delayTime * 256);
			this.client.processDrawing();
			if (this.shouldDebug) {
				System.out.println("ntime:" + l1);
				for (int l2 = 0; l2 < 10; l2++) {
					int i3 = ((i - l2 - 1) + 20) % 10;
					System.out.println("otim" + i3 + ":" + this.aLongArray7[i3]);
				}

				System.out.println("fps:" + this.fps + " ratio:" + j + " count:" + i1);
				System.out.println("del:" + k + " deltime:" + this.delayTime + " mindel:" + this.minDelay);
				System.out.println("intex:" + j1 + " opos:" + i);
				this.shouldDebug = false;
				j1 = 0;
			}
		}
		if (this.anInt4 == -1)
			this.exit();
	}

	public String aString1049;

	public int anInt1079;

	private void exit() {
		this.anInt4 = -2;
		this.client.cleanUpForQuit();
		if (this.gameFrame != null) {
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
			try {
				System.exit(0);
			} catch (Throwable _ex) {
			}
		}
	}

	public final void method4(int i) {
		this.delayTime = 1000 / i;
	}

	@Override
	public final void start() {
		if (this.anInt4 >= 0)
			this.anInt4 = 0;
	}

	@Override
	public final void stop() {
		if (this.anInt4 >= 0)
			this.anInt4 = 4000 / this.delayTime;
	}

	@Override
	public final void destroy() {
		this.anInt4 = -1;
		try {
			Thread.sleep(5000L);
		} catch (Exception _ex) {
		}
		if (this.anInt4 == -1)
			this.exit();
	}

	@Override
	public final void update(Graphics g) {
		if (this.graphics == null)
			this.graphics = g;
		this.shouldClearScreen = true;
		this.client.raiseWelcomeScreen();
	}

	@Override
	public final void paint(Graphics g) {
		if (this.graphics == null)
			this.graphics = g;
		this.shouldClearScreen = true;
		this.client.raiseWelcomeScreen();
	}

	@Override
	public final void mousePressed(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (this.gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		this.idleTime = 0;
		this.clickX = i;
		this.clickY = j;
		this.clickTime = System.currentTimeMillis();
		if (mouseevent.isMetaDown()) {
			this.clickMode1 = 2;
			this.clickMode2 = 2;
		} else {
			this.clickMode1 = 1;
			this.clickMode2 = 1;
		}
	}

	@Override
	public final void mouseReleased(MouseEvent mouseevent) {
		this.idleTime = 0;
		this.clickMode2 = 0;
	}

	@Override
	public final void mouseClicked(MouseEvent mouseevent) {
	}

	@Override
	public final void mouseEntered(MouseEvent mouseevent) {
	}

	@Override
	public final void mouseExited(MouseEvent mouseevent) {
		this.idleTime = 0;
		this.mouseX = -1;
		this.mouseY = -1;
	}

	@Override
	public final void mouseDragged(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (this.gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		this.idleTime = 0;
		this.mouseX = i;
		this.mouseY = j;
	}

	@Override
	public final void mouseMoved(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (this.gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		this.idleTime = 0;
		this.mouseX = i;
		this.mouseY = j;
	}

	@Override
	public final void keyPressed(KeyEvent keyevent) {
		this.idleTime = 0;
		int i = keyevent.getKeyCode();
		int j = keyevent.getKeyChar();
		if (j < 30)
			j = 0;
		if (i == 37)
			j = 1;
		if (i == 39)
			j = 2;
		if (i == 38)
			j = 3;
		if (i == 40)
			j = 4;
		if (i == 17)
			j = 5;
		if (i == 8)
			j = 8;
		if (i == 127)
			j = 8;
		if (i == 9)
			j = 9;
		if (i == 10)
			j = 10;
		if (i >= 112 && i <= 123)
			j = (1008 + i) - 112;
		if (i == 36)
			j = 1000;
		if (i == 35)
			j = 1001;
		if (i == 33)
			j = 1002;
		if (i == 34)
			j = 1003;
		if (j > 0 && j < 128)
			this.keyArray[j] = 1;
		if (j > 4) {
			this.charQueue[this.writeIndex] = j;
			this.writeIndex = this.writeIndex + 1 & 0x7f;
		}
	}

	@Override
	public final void keyReleased(KeyEvent keyevent) {
		this.idleTime = 0;
		int i = keyevent.getKeyCode();
		char c = keyevent.getKeyChar();
		if (c < '\036')
			c = '\0';
		if (i == 37)
			c = '\001';
		if (i == 39)
			c = '\002';
		if (i == 38)
			c = '\003';
		if (i == 40)
			c = '\004';
		if (i == 17)
			c = '\005';
		if (i == 8)
			c = '\b';
		if (i == 127)
			c = '\b';
		if (i == 9)
			c = '\t';
		if (i == 10)
			c = '\n';
		if (c > 0 && c < '\200')
			this.keyArray[c] = 0;
	}

	@Override
	public final void keyTyped(KeyEvent keyevent) {
	}

	public final int readChar(int dummy) {
		while (dummy >= 0) {
			for (int j = 1; j > 0; j++)
				;
		}
		int k = -1;
		if (this.writeIndex != this.readIndex) {
			k = this.charQueue[this.readIndex];
			this.readIndex = this.readIndex + 1 & 0x7f;
		}
		return k;
	}

	@Override
	public final void focusGained(FocusEvent focusevent) {
		this.awtFocus = true;
		this.shouldClearScreen = true;
		this.client.raiseWelcomeScreen();
	}

	@Override
	public final void focusLost(FocusEvent focusevent) {
		this.awtFocus = false;
		for (int i = 0; i < 128; i++)
			this.keyArray[i] = 0;

	}

	@Override
	public final void windowActivated(WindowEvent windowevent) {
	}

	@Override
	public final void windowClosed(WindowEvent windowevent) {
	}

	@Override
	public final void windowClosing(WindowEvent windowevent) {
		this.destroy();
	}

	@Override
	public final void windowDeactivated(WindowEvent windowevent) {
	}

	@Override
	public final void windowDeiconified(WindowEvent windowevent) {
	}

	@Override
	public final void windowIconified(WindowEvent windowevent) {
	}

	@Override
	public final void windowOpened(WindowEvent windowevent) {
	}

	public Component getGameComponent() {
		if (this.gameFrame != null)
			return this.gameFrame;
		else
			return this;
	}

	@Override
	public URL getCodeBase() {
		try {
			if (this.gameFrame != null)
				return new URL("http://127.0.0.1:" + (80 + RSBase.portOff));
		} catch (Exception _ex) {
		}
		return super.getCodeBase();
	}

	public void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			this.getAppletContext().showDocument(new URL(this.getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	public void drawLoadingText(int i, String s) {
		this.anInt1079 = i;
		this.aString1049 = s;
		while (this.graphics == null) {
			this.graphics = this.getGameComponent().getGraphics();
			try {
				this.getGameComponent().repaint();
			} catch (Exception _ex) {
			}
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		}
		int myWidth = this.getW();
		int myHeight = this.getH();
		Font font = new Font("Helvetica", 1, 13);
		FontMetrics fontmetrics = this.getGameComponent().getFontMetrics(font);
		Font font1 = new Font("Helvetica", 0, 13);
		this.getGameComponent().getFontMetrics(font1);
		if (this.shouldClearScreen) {
			this.graphics.setColor(Color.black);
			this.graphics.fillRect(0, 0, myWidth, myHeight);
			this.shouldClearScreen = false;
		}
		Color color = new Color(140, 17, 17);
		int j = myHeight / 2 - 18;
		this.graphics.setColor(color);
		this.graphics.drawRect(myWidth / 2 - 152, j, 304, 34);
		this.graphics.fillRect(myWidth / 2 - 150, j + 2, i * 3, 30);
		this.graphics.setColor(Color.black);
		this.graphics.fillRect((myWidth / 2 - 150) + i * 3, j + 2, 300 - i * 3, 30);
		this.graphics.setFont(font);
		this.graphics.setColor(Color.white);
		this.graphics.drawString(s, (myWidth - fontmetrics.stringWidth(s)) / 2, j + 22);
	}

	private int anInt4;
	private int delayTime = 20;
	public int minDelay = 1;
	private final long[] aLongArray7 = new long[10];
	public int fps;
	public boolean shouldDebug = false;
	public Graphics graphics;
	public RSFrame gameFrame;
	private boolean shouldClearScreen = true;
	public boolean awtFocus = true;
	public int idleTime;
	public int clickMode2;
	public int mouseX;
	public int mouseY;
	private int clickMode1;
	private int clickX;
	private int clickY;
	private long clickTime;
	public int clickMode3;
	public int saveClickX;
	public int saveClickY;
	public long aLong29;
	public final int[] keyArray = new int[128];
	private final int[] charQueue = new int[128];
	private int readIndex;
	private int writeIndex;
	public static int anInt34;
}
