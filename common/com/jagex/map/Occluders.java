package com.jagex.map;

public class Occluders {

	private static int activeOccluderCount;
	private static final Occluder[] activeOccluders = new Occluder[500];
	private static int[] levelOccluderCount;
	private static Occluder[][] levelOccluders;

	private static final int MAX_OCCLUDER_LEVELS;

	static {
		MAX_OCCLUDER_LEVELS = 4;
		Occluders.levelOccluderCount = new int[Occluders.MAX_OCCLUDER_LEVELS];
		Occluders.levelOccluders = new Occluder[Occluders.MAX_OCCLUDER_LEVELS][500];
	}

	public static void clear() {
		for (int l = 0; l < Occluders.MAX_OCCLUDER_LEVELS; l++) {
			for (int j1 = 0; j1 < Occluders.levelOccluderCount[l]; j1++)
				Occluders.levelOccluders[l][j1] = null;

			Occluders.levelOccluderCount[l] = 0;
		}
	}
	
	public static void destroy() {
		Occluders.levelOccluderCount = null;
		Occluders.levelOccluders = null;

	}
	
	public static boolean isOccluded(int x, int y, int z) {
		for (int n = 0; n < Occluders.activeOccluderCount; n++) {
			Occluder o = Occluders.activeOccluders[n];
			if (o.testDirection == 1) {
				int dx = o.minX - x;
				if (dx > 0) {
					int minZ = o.minZ + (o.minNormalZ * dx >> 8);
					int maxZ = o.maxZ + (o.maxNormalZ * dx >> 8);
					int minY = o.minY + (o.minNormalY * dx >> 8);
					int maxY = o.maxY + (o.maxNormalY * dx >> 8);
					if (z >= minZ && z <= maxZ && y >= minY && y <= maxY)
						return true;
				}
			} else if (o.testDirection == 2) {
				int dx = x - o.minX;
				if (dx > 0) {
					int minZ = o.minZ + (o.minNormalZ * dx >> 8);
					int maxZ = o.maxZ + (o.maxNormalZ * dx >> 8);
					int minY = o.minY + (o.minNormalY * dx >> 8);
					int maxY = o.maxY + (o.maxNormalY * dx >> 8);
					if (z >= minZ && z <= maxZ && y >= minY && y <= maxY)
						return true;
				}
			} else if (o.testDirection == 3) {
				int dz = o.minZ - z;
				if (dz > 0) {
					int minX = o.minX + (o.minNormalX * dz >> 8);
					int maxX = o.maxX + (o.maxNormalX * dz >> 8);
					int minY = o.minY + (o.minNormalY * dz >> 8);
					int maxY = o.maxY + (o.maxNormalY * dz >> 8);
					if (x >= minX && x <= maxX && y >= minY && y <= maxY)
						return true;
				}
			} else if (o.testDirection == 4) {
				int dz = z - o.minZ;
				if (dz > 0) {
					int minX = o.minX + (o.minNormalX * dz >> 8);
					int maxX = o.maxX + (o.maxNormalX * dz >> 8);
					int minY = o.minY + (o.minNormalY * dz >> 8);
					int maxY = o.maxY + (o.maxNormalY * dz >> 8);
					if (x >= minX && x <= maxX && y >= minY && y <= maxY)
						return true;
				}
			} else if (o.testDirection == 5) {
				int dy = y - o.minY;
				if (dy > 0) {
					int minX = o.minX + (o.minNormalX * dy >> 8);
					int maxX = o.maxX + (o.maxNormalX * dy >> 8);
					int minZ = o.minZ + (o.minNormalZ * dy >> 8);
					int maxZ = o.maxZ + (o.maxNormalZ * dy >> 8);
					if (x >= minX && x <= maxX && z >= minZ && z <= maxZ)
						return true;
				}
			}
		}

		return false;
	}

	public static void update(boolean[][] visibilityMap, int activeLevel, int cameraTileX, int cameraTileZ, int cameraX,
			int cameraY, int cameraZ) {
		int count = Occluders.levelOccluderCount[activeLevel];
		Occluder occluders[] = Occluders.levelOccluders[activeLevel];
		Occluders.activeOccluderCount = 0;
		while_175_: 
		for (int n = 0; n < count; n++) {
			Occluder o = occluders[n];
		/*	if (Class21.anIntArray437 != null) {
				for (int pos = 0; pos < Class21.anIntArray437.length; pos++) {
					if (Class21.anIntArray437[pos] != -1000000
							&& (o.minY <= Class21.anIntArray437[pos]
									|| (o.maxY <= Class21.anIntArray437[pos]))
							&& (o.minX <= Static2.anIntArray1790[pos]
									|| (o.maxX <= Static2.anIntArray1790[pos]))
							&& (o.minX >= Class98.anIntArray1660[pos]
									|| (o.maxX >= Class98.anIntArray1660[pos]))
							&& (o.minZ <= Class142.anIntArray2284[pos]
									|| (o.maxZ <= Class142.anIntArray2284[pos]))
							&& (o.minZ >= Class104.anIntArray1737[pos]
									|| (o.maxZ >= Class104.anIntArray1737[pos])))
						continue while_175_;
				}
			}*/
			if (o.type == 1) {
				int x = (o.minTileX - cameraTileX) + 25;
				if (x < 0 || x > 50)
					continue;
				int minZ = (o.minTileZ - cameraTileZ) + 25;
				if (minZ < 0)
					minZ = 0;
				int maxZ = (o.maxTileZ - cameraTileZ) + 25;
				if (maxZ > 50)
					maxZ = 50;
				boolean visible = false;
				while (minZ <= maxZ)
					if (visibilityMap[x][minZ++]) {
						visible = true;
						break;
					}
				if (!visible)
					continue;
				int dx = cameraX - o.minX;
				if (dx > 32) {
					o.testDirection = 1;
				} else {
					if (dx >= -32)
						continue;
					o.testDirection = 2;
					dx = -dx;
				}
				o.minNormalZ = (o.minZ - cameraZ << 8) / dx;
				o.maxNormalZ = (o.maxZ - cameraZ << 8) / dx;
				o.minNormalY = (o.minY - cameraY << 8) / dx;
				o.maxNormalY = (o.maxY - cameraY << 8) / dx;
				Occluders.activeOccluders[Occluders.activeOccluderCount++] = o;
				continue;
			}
			if (o.type == 2) {
				int z = (o.minTileZ - cameraTileZ) + 25;
				if (z < 0 || z > 50)
					continue;
				int minX = (o.minTileX - cameraTileX) + 25;
				if (minX < 0)
					minX = 0;
				int maxX = (o.maxTileX - cameraTileX) + 25;
				if (maxX > 50)
					maxX = 50;
				boolean visible = false;
				while (minX <= maxX)
					if (visibilityMap[minX++][z]) {
						visible = true;
						break;
					}
				if (!visible)
					continue;
				int dz = cameraZ - o.minZ;
				if (dz > 32) {
					o.testDirection = 3;
				} else {
					if (dz >= -32)
						continue;
					o.testDirection = 4;
					dz = -dz;
				}
				o.minNormalX = (o.minX - cameraX << 8) / dz;
				o.maxNormalX = (o.maxX - cameraX << 8) / dz;
				o.minNormalY = (o.minY - cameraY << 8) / dz;
				o.maxNormalY = (o.maxY - cameraY << 8) / dz;
				Occluders.activeOccluders[Occluders.activeOccluderCount++] = o;
			} else if (o.type == 4) {
				int y = o.minY - cameraY;
				if (y > 128) {
					int minZ = (o.minTileZ - cameraTileZ) + 25;
					if (minZ < 0)
						minZ = 0;
					int maxZ = (o.maxTileZ - cameraTileZ) + 25;
					if (maxZ > 50)
						maxZ = 50;
					if (minZ <= maxZ) {
						int minX = (o.minTileX - cameraTileX) + 25;
						if (minX < 0)
							minX = 0;
						int maxX = (o.maxTileX - cameraTileX) + 25;
						if (maxX > 50)
							maxX = 50;
						boolean visible = false;
						visibilityCheck: for (int x = minX; x <= maxX; x++) {
							for (int z = minZ; z <= maxZ; z++) {
								if (!visibilityMap[x][z])
									continue;
								visible = true;
								break visibilityCheck;
							}

						}

						if (visible) {
							o.testDirection = 5;
							o.minNormalX = (o.minX - cameraX << 8) / y;
							o.maxNormalX = (o.maxX - cameraX << 8) / y;
							o.minNormalZ = (o.minZ - cameraZ << 8) / y;
							o.maxNormalZ = (o.maxZ - cameraZ << 8) / y;
							Occluders.activeOccluders[Occluders.activeOccluderCount++] = o;
						}
					}
				}
			}
		}
	}

	public static void add(int level, int minX, int maxY, int maxX, int maxZ, int minY, int minZ, int type) {
		Occluder sceneCluster = new Occluder();
		sceneCluster.minTileX = minX / 128;
		sceneCluster.maxTileX = maxX / 128;
		sceneCluster.minTileZ = minZ / 128;
		sceneCluster.maxTileZ = maxZ / 128;
		sceneCluster.type = type;
		sceneCluster.minX = minX;
		sceneCluster.maxX = maxX;
		sceneCluster.minZ = minZ;
		sceneCluster.maxZ = maxZ;
		sceneCluster.minY = minY;
		sceneCluster.maxY = maxY;
		Occluders.levelOccluders[level][Occluders.levelOccluderCount[level]++] = sceneCluster;
	}

	private static final class Occluder {
		int minTileX;
		int maxTileX;
		int minTileZ;
		int maxTileZ;
		int type;
		int minX;
		int maxX;
		int minZ;
		int maxZ;
		int minY;
		int maxY;
		int testDirection;
		int minNormalX;
		int maxNormalX;
		int minNormalZ;
		int maxNormalZ;
		int minNormalY;
		int maxNormalY;
	}
	
}
