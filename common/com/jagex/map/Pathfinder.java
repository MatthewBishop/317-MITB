package com.jagex.map;

import com.jagex.window.RSBase;

public class Pathfinder {

	private int[][] anIntArrayArray901;

	private int[][] pathDistance;
	private int[] waypointX;
	private int[] waypointY;
	public int anInt1264;

	public Pathfinder() {
        this.waypointX = new int[4000];
        this.waypointY = new int[4000];
		this.anIntArrayArray901 = new int[104][104];

		this.pathDistance = new int[104][104];
	}

	public void kill() {
        this.waypointX = null;
        this.waypointY = null;
		this.anIntArrayArray901 = null;
		this.pathDistance = null;
	}

	public boolean doWalkTo(RSBase base, int i, int rotation, int height, int type, int initialY, int width, int l1, int finalY, int initialX, boolean flag, int finalX, CollisionMap map) {
		byte mapWidth = 104;
		byte mapLength = 104;
		for (int l2 = 0; l2 < mapWidth; l2++) {
			for (int i3 = 0; i3 < mapLength; i3++) {
				this.anIntArrayArray901[l2][i3] = 0;
				this.pathDistance[l2][i3] = 0x5f5e0ff;
			}

		}

		int currentX = initialX;
		int currentY = initialY;
		this.anIntArrayArray901[initialX][initialY] = 99;
		this.pathDistance[initialX][initialY] = 0;
		int nextIndex = 0;
		int currentIndex = 0;
		this.waypointX[nextIndex] = initialX;
		this.waypointY[nextIndex++] = initialY;
		boolean reached = false;
		int waypoints = this.waypointX.length;
		int flags[][] = map.flags;
		while (currentIndex != nextIndex) {
			currentX = this.waypointX[currentIndex];
			currentY = this.waypointY[currentIndex];
			currentIndex = (currentIndex + 1) % waypoints;
			if (currentX == finalX && currentY == finalY) {
				reached = true;
				break;
			}
			if (type != 0) {
				if ((type < 5 || type == 10) && map.reachedWall(currentX, currentY, finalX, finalY, rotation, type - 1)) {
					reached = true;
					break;
				}
				if (type < 10 && map.reachedDecoration(currentX, currentY, finalX, finalY, rotation, type - 1)) {
					reached = true;
					break;
				}
			}
			if (width != 0 && height != 0 && map.reachedObject(currentX, currentY, finalX, finalY, width, height, l1)) {
				reached = true;
				break;
			}
			int l4 = this.pathDistance[currentX][currentY] + 1;
			if (currentX > 0 && this.anIntArrayArray901[currentX - 1][currentY] == 0 && (flags[currentX - 1][currentY] & 0x1280108) == 0) {
				this.waypointX[nextIndex] = currentX - 1;
				this.waypointY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX - 1][currentY] = 2;
				this.pathDistance[currentX - 1][currentY] = l4;
			}
			if (currentX < mapWidth - 1 && this.anIntArrayArray901[currentX + 1][currentY] == 0 && (flags[currentX + 1][currentY] & 0x1280180) == 0) {
				this.waypointX[nextIndex] = currentX + 1;
				this.waypointY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX + 1][currentY] = 8;
				this.pathDistance[currentX + 1][currentY] = l4;
			}
			if (currentY > 0 && this.anIntArrayArray901[currentX][currentY - 1] == 0 && (flags[currentX][currentY - 1] & 0x1280102) == 0) {
				this.waypointX[nextIndex] = currentX;
				this.waypointY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX][currentY - 1] = 1;
				this.pathDistance[currentX][currentY - 1] = l4;
			}
			if (currentY < mapLength - 1 && this.anIntArrayArray901[currentX][currentY + 1] == 0 && (flags[currentX][currentY + 1] & 0x1280120) == 0) {
				this.waypointX[nextIndex] = currentX;
				this.waypointY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX][currentY + 1] = 4;
				this.pathDistance[currentX][currentY + 1] = l4;
			}
			if (currentX > 0 && currentY > 0 && this.anIntArrayArray901[currentX - 1][currentY - 1] == 0
					&& (flags[currentX - 1][currentY - 1] & 0x128010e) == 0 && (flags[currentX - 1][currentY] & 0x1280108) == 0
					&& (flags[currentX][currentY - 1] & 0x1280102) == 0) {
				this.waypointX[nextIndex] = currentX - 1;
				this.waypointY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX - 1][currentY - 1] = 3;
				this.pathDistance[currentX - 1][currentY - 1] = l4;
			}
			if (currentX < mapWidth - 1 && currentY > 0 && this.anIntArrayArray901[currentX + 1][currentY - 1] == 0
					&& (flags[currentX + 1][currentY - 1] & 0x1280183) == 0 && (flags[currentX + 1][currentY] & 0x1280180) == 0
					&& (flags[currentX][currentY - 1] & 0x1280102) == 0) {
				this.waypointX[nextIndex] = currentX + 1;
				this.waypointY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX + 1][currentY - 1] = 9;
				this.pathDistance[currentX + 1][currentY - 1] = l4;
			}
			if (currentX > 0 && currentY < mapLength - 1 && this.anIntArrayArray901[currentX - 1][currentY + 1] == 0
					&& (flags[currentX - 1][currentY + 1] & 0x1280138) == 0 && (flags[currentX - 1][currentY] & 0x1280108) == 0
					&& (flags[currentX][currentY + 1] & 0x1280120) == 0) {
				this.waypointX[nextIndex] = currentX - 1;
				this.waypointY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX - 1][currentY + 1] = 6;
				this.pathDistance[currentX - 1][currentY + 1] = l4;
			}
			if (currentX < mapWidth - 1 && currentY < mapLength - 1 && this.anIntArrayArray901[currentX + 1][currentY + 1] == 0
					&& (flags[currentX + 1][currentY + 1] & 0x12801e0) == 0 && (flags[currentX + 1][currentY] & 0x1280180) == 0
					&& (flags[currentX][currentY + 1] & 0x1280120) == 0) {
				this.waypointX[nextIndex] = currentX + 1;
				this.waypointY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % waypoints;
				this.anIntArrayArray901[currentX + 1][currentY + 1] = 12;
				this.pathDistance[currentX + 1][currentY + 1] = l4;
			}

		}
		this.anInt1264 = 0;
		if (!reached) {
			if (flag) {
				int maxDistance = 100;
				for (int n = 1; n < 2; n++) {
					for (int dx = finalX - n; dx <= finalX + n; dx++) {
						for (int dy = finalY - n; dy <= finalY + n; dy++)
							if (dx >= 0 && dy >= 0 && dx < 104 && dy < 104 && this.pathDistance[dx][dy] < maxDistance) {
								maxDistance = this.pathDistance[dx][dy];
								currentX = dx;
								currentY = dy;
								this.anInt1264 = 1;
								reached = true;
							}

					}

					if (reached)
						break;
				}

			}
			if (!reached)
				return false;
		}
		currentIndex = 0;
		this.waypointX[currentIndex] = currentX;
		this.waypointY[currentIndex++] = currentY;
		int lastWaypoint;
		for (int waypoint = lastWaypoint = this.anIntArrayArray901[currentX][currentY]; currentX != initialX
				|| currentY != initialY; waypoint = this.anIntArrayArray901[currentX][currentY]) {
			if (waypoint != lastWaypoint) {
				lastWaypoint = waypoint;
				this.waypointX[currentIndex] = currentX;
				this.waypointY[currentIndex++] = currentY;
			}
			if ((waypoint & 2) != 0)
				currentX++;
			else if ((waypoint & 8) != 0)
				currentX--;
			if ((waypoint & 1) != 0)
				currentY++;
			else if ((waypoint & 4) != 0)
				currentY--;
		}
		// if(cancelWalk) { return i4 > 0; }

		if (currentIndex > 0) {
			base.writepath(currentIndex, i, waypointX, waypointY);
			return true;
		}
		return i != 1;
	}
}
