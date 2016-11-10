package com.jagex;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.zip.CRC32;

import com.jagex.cache.Archive;
import com.jagex.io.Buffer;
import com.jagex.sign.signlink;

public class Jaggrab {

	private static int[] completion = { 25, 30, 35, 40, 60, 45, 50, 55 };
	private static String[] displayedName = { "title screen", "config", "interface", "2d graphics", "update list", "textures", "chat system", "sound effects" };
	private static String[] names = { "title", "config", "interface", "media", "versionlist", "textures", "wordenc", "sounds"};
	
	private static CRC32 indexCrc = new CRC32();

	private Client client;

    public final int[] expectedCRCs;
	private Socket jaggrab;

	private boolean useJaggrab = false;

	public Jaggrab(Client client) {
		this.client = client;
		expectedCRCs = new int[9];
		requestCrcs();
	}

	public boolean check(byte[] abyte0, int j) {
		if (abyte0 != null) {
			indexCrc.reset();
			indexCrc.update(abyte0);
			int i1 = (int) indexCrc.getValue();
			if (i1 != expectedCRCs[j])
				return false;
		}
		return true;
	}
		
	private void requestCrcs() {
		int j = 5;
		expectedCRCs[8] = 0;
		int k = 0;
		while (expectedCRCs[8] == 0) {
			String s = "Unknown problem";
			client.drawLoadingText(20, "Connecting to web server");
			try {
				DataInputStream datainputstream = requestCacheIndex(
						"crc" + (int) (Math.random() * 99999999D) + "-" + 317);
				Buffer class30_sub2_sub2 = new Buffer(new byte[40]);
				datainputstream.readFully(class30_sub2_sub2.payload, 0, 40);
				datainputstream.close();
				for (int i1 = 0; i1 < 9; i1++)
					expectedCRCs[i1] = class30_sub2_sub2.readInt();

				int j1 = class30_sub2_sub2.readInt();
				int k1 = 1234;
				for (int l1 = 0; l1 < 9; l1++)
					k1 = (k1 << 1) + expectedCRCs[l1];

				if (j1 != k1) {
					s = "checksum problem";
					expectedCRCs[8] = 0;
				}
			} catch (EOFException _ex) {
				s = "EOF problem";
				expectedCRCs[8] = 0;
			} catch (IOException _ex) {
				s = "connection problem";
				expectedCRCs[8] = 0;
			} catch (Exception _ex) {
				s = "logic problem";
				expectedCRCs[8] = 0;
				if (!signlink.reporterror)
					return;
			}
			if (expectedCRCs[8] == 0) {
				k++;
				for (int l = j; l > 0; l--) {
					if (k >= 10) {
						client.drawLoadingText(10, "Game updated - please reload page");
						l = 10;
					} else {
						client.drawLoadingText(10, s + " - Will retry in " + l + " secs.");
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				j *= 2;
				if (j > 60)
					j = 60;
				useJaggrab = !useJaggrab;
			}
		}

	}
	public Archive get(int i) {
		int reconnectionDelay = 5;
		byte[] abyte0 = null;
		int errors = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			client.drawLoadingText(completion[i - 1], "Requesting " + displayedName[i - 1]);
			try {
				int k1 = 0;
				DataInputStream datainputstream = requestCacheIndex(names[i - 1] + expectedCRCs[i]);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Buffer buffer = new Buffer(abyte1);
				buffer.position = 3;
				int i2 = buffer.readUTriByte() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1)
						client.drawLoadingText(completion[i - 1], "Loading " + displayedName[i - 1] + " - " + k3 + "%");
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (client.indexs[0] != null)
						client.indexs[0].put(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					client.indexs[0] = null;
				}
				if (abyte0 != null) {
					indexCrc.reset();
					indexCrc.update(abyte0);
					int i3 = (int) indexCrc.getValue();
					if (i3 != expectedCRCs[i]) {
						abyte0 = null;
						errors++;
						s2 = "Checksum error: " + i3;
					}
				}

			} catch (IOException ioexception) {
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			}
			if (abyte0 == null) {
				for (int l1 = reconnectionDelay; l1 > 0; l1--) {
					if (errors >= 3) {
						client.drawLoadingText(completion[i - 1], "Game updated - please reload page");
						l1 = 10;
					} else {
						client.drawLoadingText(completion[i - 1], s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				reconnectionDelay *= 2;
				if (reconnectionDelay > 60)
					reconnectionDelay = 60;
				useJaggrab = !useJaggrab;
			}

		}

		Archive streamLoader_1 = new Archive(abyte0);
		return streamLoader_1;
	}
	private DataInputStream requestCacheIndex(String s) throws IOException {
		if (!useJaggrab)
			return new DataInputStream((new URL(client.applet.getCodeBase(), s)).openStream());
		if (jaggrab != null) {
			try {
				jaggrab.close();
			} catch (Exception _ex) {
			}
			jaggrab = null;
		}
		jaggrab = client.openSocket(43595);
		jaggrab.setSoTimeout(10000);
		java.io.InputStream inputstream = jaggrab.getInputStream();
		OutputStream outputstream = jaggrab.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}	
}
