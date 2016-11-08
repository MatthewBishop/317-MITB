package com.jagex;

import com.jagex.cache.graphics.IndexedImage;
import com.jagex.cache.graphics.Sprite;
import com.jagex.draw.DrawingArea;
import com.jagex.draw.RSImageProducer;
import com.jagex.draw.SpriteRenderer;

public class LoadingScreen {

	public static void destroy() {
		aBackground_966 = null;
		aBackground_967 = null;
		aRSImageProducer_1109 = null;
	}

	static RSImageProducer aRSImageProducer_1109;

	static IndexedImage aBackground_966;
	static IndexedImage aBackground_967;

	public static boolean isLoaded() {
		return aRSImageProducer_1109 != null;
	}

	public static void prepare(Client client) {
		load(client);
		aRSImageProducer_1109 = new RSImageProducer(360, 200);
		DrawingArea.setAllPixelsToZero();

	}

	public static void load(Client client) {
		if (client.titleStreamLoader != null) {
			aBackground_966 = new IndexedImage(client.titleStreamLoader, "titlebox", 0);
			aBackground_967 = new IndexedImage(client.titleStreamLoader, "titlebutton", 0);
			
		}
	}
}
