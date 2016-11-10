package com.jagex.window;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import com.jagex.Client;
import com.jagex.Utils;

public class RSApplet extends Applet
		implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener, WindowListener {

	private boolean isApplet;

	private RSBase client;

	public RSApplet(Client client) {
		this.client = client;
	}

	public final void initClientFrame(int i, int j, boolean applet) {
		isApplet = applet;
		if (!applet) {
			gameFrame = new RSFrame(this, j, i, Client.frameMode == Client.ScreenMode.RESIZABLE,
					Client.frameMode == Client.ScreenMode.FULLSCREEN);
			gameFrame.setFocusTraversalKeysEnabled(false);
		}
		graphics = getGameComponent().getGraphics();
		Utils.startRunnable(this, 1);
	}

	public void refreshFrameSize(boolean undecorated, int width, int height, boolean resizable, boolean full) {
		boolean createdByApplet = (isApplet && !full);
		if (gameFrame != null) {
			gameFrame.dispose();
		}
		if (!createdByApplet) {
			gameFrame = new RSFrame(this, width, height, resizable, undecorated);
			gameFrame.addWindowListener(this);
		}
		graphics = (createdByApplet ? this : gameFrame).getGraphics();
		if (!createdByApplet) {
			// getGameComponent().addMouseWheelListener(this);
			getGameComponent().addMouseListener(this);
			getGameComponent().addMouseMotionListener(this);
			getGameComponent().addKeyListener(this);
			getGameComponent().addFocusListener(this);
		}
	}

	public boolean appletClient() {
		return gameFrame == null && isApplet == true;
	}

	private int getW() {
		return (appletClient() ? getGameComponent().getWidth() : gameFrame.getFrameWidth());
	}

	private int getH() {
		return (appletClient() ? getGameComponent().getHeight() : gameFrame.getFrameHeight());
	}

	public void run() {
		getGameComponent().addMouseListener(this);
		getGameComponent().addMouseMotionListener(this);
		getGameComponent().addKeyListener(this);
		getGameComponent().addFocusListener(this);
		if (gameFrame != null)
			gameFrame.addWindowListener(this);
		drawLoadingText(0, "Loading...");
		client.startUp();
		int i = 0;
		int j = 256;
		int k = 1;
		int i1 = 0;
		int j1 = 0;
		for (int k1 = 0; k1 < 10; k1++)
			aLongArray7[k1] = System.currentTimeMillis();

		long l = System.currentTimeMillis();
		while (anInt4 >= 0) {
			if (anInt4 > 0) {
				anInt4--;
				if (anInt4 == 0) {
					exit();
					return;
				}
			}
			int i2 = j;
			int j2 = k;
			j = 300;
			k = 1;
			long l1 = System.currentTimeMillis();
			if (aLongArray7[i] == 0L) {
				j = i2;
				k = j2;
			} else if (l1 > aLongArray7[i])
				j = (int) ((long) (2560 * delayTime) / (l1 - aLongArray7[i]));
			if (j < 25)
				j = 25;
			if (j > 256) {
				j = 256;
				k = (int) ((long) delayTime - (l1 - aLongArray7[i]) / 10L);
			}
			if (k > delayTime)
				k = delayTime;
			aLongArray7[i] = l1;
			i = (i + 1) % 10;
			if (k > 1) {
				for (int k2 = 0; k2 < 10; k2++)
					if (aLongArray7[k2] != 0L)
						aLongArray7[k2] += k;

			}
			if (k < minDelay)
				k = minDelay;
			try {
				Thread.sleep(k);
			} catch (InterruptedException _ex) {
				j1++;
			}
			for (; i1 < 256; i1 += j) {
				clickMode3 = clickMode1;
				saveClickX = clickX;
				saveClickY = clickY;
				aLong29 = clickTime;
				clickMode1 = 0;
				client.processGameLoop();
				readIndex = writeIndex;
			}

			i1 &= 0xff;
			if (delayTime > 0)
				fps = (1000 * j) / (delayTime * 256);
			client.processDrawing();
			if (shouldDebug) {
				System.out.println("ntime:" + l1);
				for (int l2 = 0; l2 < 10; l2++) {
					int i3 = ((i - l2 - 1) + 20) % 10;
					System.out.println("otim" + i3 + ":" + aLongArray7[i3]);
				}

				System.out.println("fps:" + fps + " ratio:" + j + " count:" + i1);
				System.out.println("del:" + k + " deltime:" + delayTime + " mindel:" + minDelay);
				System.out.println("intex:" + j1 + " opos:" + i);
				shouldDebug = false;
				j1 = 0;
			}
		}
		if (anInt4 == -1)
			exit();
	}

	public String aString1049;

	public int anInt1079;

	private void exit() {
		anInt4 = -2;
		client.cleanUpForQuit();
		if (gameFrame != null) {
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
		delayTime = 1000 / i;
	}

	public final void start() {
		if (anInt4 >= 0)
			anInt4 = 0;
	}

	public final void stop() {
		if (anInt4 >= 0)
			anInt4 = 4000 / delayTime;
	}

	public final void destroy() {
		anInt4 = -1;
		try {
			Thread.sleep(5000L);
		} catch (Exception _ex) {
		}
		if (anInt4 == -1)
			exit();
	}

	public final void update(Graphics g) {
		if (graphics == null)
			graphics = g;
		shouldClearScreen = true;
		client.raiseWelcomeScreen();
	}

	public final void paint(Graphics g) {
		if (graphics == null)
			graphics = g;
		shouldClearScreen = true;
		client.raiseWelcomeScreen();
	}

	public final void mousePressed(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		idleTime = 0;
		clickX = i;
		clickY = j;
		clickTime = System.currentTimeMillis();
		if (mouseevent.isMetaDown()) {
			clickMode1 = 2;
			clickMode2 = 2;
		} else {
			clickMode1 = 1;
			clickMode2 = 1;
		}
	}

	public final void mouseReleased(MouseEvent mouseevent) {
		idleTime = 0;
		clickMode2 = 0;
	}

	public final void mouseClicked(MouseEvent mouseevent) {
	}

	public final void mouseEntered(MouseEvent mouseevent) {
	}

	public final void mouseExited(MouseEvent mouseevent) {
		idleTime = 0;
		mouseX = -1;
		mouseY = -1;
	}

	public final void mouseDragged(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		idleTime = 0;
		mouseX = i;
		mouseY = j;
	}

	public final void mouseMoved(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (gameFrame != null) {
			i -= 4;
			j -= 22;
		}
		idleTime = 0;
		mouseX = i;
		mouseY = j;
	}

	public final void keyPressed(KeyEvent keyevent) {
		idleTime = 0;
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
			keyArray[j] = 1;
		if (j > 4) {
			charQueue[writeIndex] = j;
			writeIndex = writeIndex + 1 & 0x7f;
		}
	}

	public final void keyReleased(KeyEvent keyevent) {
		idleTime = 0;
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
			keyArray[c] = 0;
	}

	public final void keyTyped(KeyEvent keyevent) {
	}

	public final int readChar(int dummy) {
		while (dummy >= 0) {
			for (int j = 1; j > 0; j++)
				;
		}
		int k = -1;
		if (writeIndex != readIndex) {
			k = charQueue[readIndex];
			readIndex = readIndex + 1 & 0x7f;
		}
		return k;
	}

	public final void focusGained(FocusEvent focusevent) {
		awtFocus = true;
		shouldClearScreen = true;
		client.raiseWelcomeScreen();
	}

	public final void focusLost(FocusEvent focusevent) {
		awtFocus = false;
		for (int i = 0; i < 128; i++)
			keyArray[i] = 0;

	}

	public final void windowActivated(WindowEvent windowevent) {
	}

	public final void windowClosed(WindowEvent windowevent) {
	}

	public final void windowClosing(WindowEvent windowevent) {
		destroy();
	}

	public final void windowDeactivated(WindowEvent windowevent) {
	}

	public final void windowDeiconified(WindowEvent windowevent) {
	}

	public final void windowIconified(WindowEvent windowevent) {
	}

	public final void windowOpened(WindowEvent windowevent) {
	}

	public Component getGameComponent() {
		if (gameFrame != null)
			return gameFrame;
		else
			return this;
	}

	@Override
	public URL getCodeBase() {
		try {
			if (gameFrame != null)
				return new URL("http://127.0.0.1:" + (80 + RSBase.portOff));
		} catch (Exception _ex) {
		}
		return super.getCodeBase();
	}

	public void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
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
		anInt1079 = i;
		aString1049 = s;
		while (graphics == null) {
			graphics = getGameComponent().getGraphics();
			try {
				getGameComponent().repaint();
			} catch (Exception _ex) {
			}
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		}
		int myWidth = getW();
		int myHeight = getH();
		Font font = new Font("Helvetica", 1, 13);
		FontMetrics fontmetrics = getGameComponent().getFontMetrics(font);
		Font font1 = new Font("Helvetica", 0, 13);
		getGameComponent().getFontMetrics(font1);
		if (shouldClearScreen) {
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, myWidth, myHeight);
			shouldClearScreen = false;
		}
		Color color = new Color(140, 17, 17);
		int j = myHeight / 2 - 18;
		graphics.setColor(color);
		graphics.drawRect(myWidth / 2 - 152, j, 304, 34);
		graphics.fillRect(myWidth / 2 - 150, j + 2, i * 3, 30);
		graphics.setColor(Color.black);
		graphics.fillRect((myWidth / 2 - 150) + i * 3, j + 2, 300 - i * 3, 30);
		graphics.setFont(font);
		graphics.setColor(Color.white);
		graphics.drawString(s, (myWidth - fontmetrics.stringWidth(s)) / 2, j + 22);
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
