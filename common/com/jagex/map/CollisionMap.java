package com.jagex.map;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class CollisionMap {

	public CollisionMap(int xOffset, int yOffset, int width, int height) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.flags = new int[this.width][this.height];
		this.reset();
	}

	public CollisionMap() {
		this.xOffset = 0;
		this.yOffset = 0;
		this.width = 104;
		this.height = 104;
		this.flags = new int[this.width][this.height];
		this.reset();
	}

	public void reset() {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++)
				if (x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1)
					this.flags[x][y] = 0xffffff;
				else
					this.flags[x][y] = 0x1000000;

		}

	}

	public void setWall(int x, int y, int rotation, int type, boolean solid) {
		x -= this.xOffset;
		y -= this.yOffset;
		if (type == 0) {
			if (rotation == 0) {
				this.add(x, y, 128);
				this.add(x - 1, y, 8);
			}
			if (rotation == 1) {
				this.add(x, y, 2);
				this.add(x, y + 1, 32);
			}
			if (rotation == 2) {
				this.add(x, y, 8);
				this.add(x + 1, y, 128);
			}
			if (rotation == 3) {
				this.add(x, y, 32);
				this.add(x, y - 1, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (rotation == 0) {
				this.add(x, y, 1);
				this.add(x - 1, y + 1, 16);
			}
			if (rotation == 1) {
				this.add(x, y, 4);
				this.add(x + 1, y + 1, 64);
			}
			if (rotation == 2) {
				this.add(x, y, 16);
				this.add(x + 1, y - 1, 1);
			}
			if (rotation == 3) {
				this.add(x, y, 64);
				this.add(x - 1, y - 1, 4);
			}
		}
		if (type == 2) {
			if (rotation == 0) {
				this.add(x, y, 130);
				this.add(x - 1, y, 8);
				this.add(x, y + 1, 32);
			}
			if (rotation == 1) {
				this.add(x, y, 10);
				this.add(x, y + 1, 32);
				this.add(x + 1, y, 128);
			}
			if (rotation == 2) {
				this.add(x, y, 40);
				this.add(x + 1, y, 128);
				this.add(x, y - 1, 2);
			}
			if (rotation == 3) {
				this.add(x, y, 160);
				this.add(x, y - 1, 2);
				this.add(x - 1, y, 8);
			}
		}
		if (solid) {
			if (type == 0) {
				if (rotation == 0) {
					this.add(x, y, 0x10000);
					this.add(x - 1, y, 4096);
				}
				if (rotation == 1) {
					this.add(x, y, 1024);
					this.add(x, y + 1, 16384);
				}
				if (rotation == 2) {
					this.add(x, y, 4096);
					this.add(x + 1, y, 0x10000);
				}
				if (rotation == 3) {
					this.add(x, y, 16384);
					this.add(x, y - 1, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (rotation == 0) {
					this.add(x, y, 512);
					this.add(x - 1, y + 1, 8192);
				}
				if (rotation == 1) {
					this.add(x, y, 2048);
					this.add(x + 1, y + 1, 32768);
				}
				if (rotation == 2) {
					this.add(x, y, 8192);
					this.add(x + 1, y - 1, 512);
				}
				if (rotation == 3) {
					this.add(x, y, 32768);
					this.add(x - 1, y - 1, 2048);
				}
			}
			if (type == 2) {
				if (rotation == 0) {
					this.add(x, y, 0x10400);
					this.add(x - 1, y, 4096);
					this.add(x, y + 1, 16384);
				}
				if (rotation == 1) {
					this.add(x, y, 5120);
					this.add(x, y + 1, 16384);
					this.add(x + 1, y, 0x10000);
				}
				if (rotation == 2) {
					this.add(x, y, 20480);
					this.add(x + 1, y, 0x10000);
					this.add(x, y - 1, 1024);
				}
				if (rotation == 3) {
					this.add(x, y, 0x14000);
					this.add(x, y - 1, 1024);
					this.add(x - 1, y, 4096);
				}
			}
		}
	}

	public void setLoc(int tileX, int tileY, int sizeX, int sizeY, int rotation, boolean solid) {
		int flag = 256;
		if (solid)
			flag += 0x20000;
		tileX -= this.xOffset;
		tileY -= this.yOffset;
		if (rotation == 1 || rotation == 3) {
			int y_ = sizeX;
			sizeX = sizeY;
			sizeY = y_;
		}
		for (int x = tileX; x < tileX + sizeX; x++)
			if (x >= 0 && x < this.width) {
				for (int y = tileY; y < tileY + sizeY; y++)
					if (y >= 0 && y < this.height)
						this.add(x, y, flag);

			}

	}

	public void block(int x, int y) {
		x -= this.xOffset;
		y -= this.yOffset;
		this.flags[x][y] |= 0x200000;
	}

	private void add(int x, int y, int flag) {
		this.flags[x][y] |= flag;
	}

	public void removeWall(int tileX, int tileZ, int rotation, int type, boolean solid) {
		tileX -= this.xOffset;
		tileZ -= this.yOffset;
		if (type == 0) {
			if (rotation == 0) {
				this.remove(tileX, tileZ, 128);
				this.remove(tileX - 1, tileZ, 8);
			}
			if (rotation == 1) {
				this.remove(tileX, tileZ, 2);
				this.remove(tileX, tileZ + 1, 32);
			}
			if (rotation == 2) {
				this.remove(tileX, tileZ, 8);
				this.remove(tileX + 1, tileZ, 128);
			}
			if (rotation == 3) {
				this.remove(tileX, tileZ, 32);
				this.remove(tileX, tileZ - 1, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (rotation == 0) {
				this.remove(tileX, tileZ, 1);
				this.remove(tileX - 1, tileZ + 1, 16);
			}
			if (rotation == 1) {
				this.remove(tileX, tileZ, 4);
				this.remove(tileX + 1, tileZ + 1, 64);
			}
			if (rotation == 2) {
				this.remove(tileX, tileZ, 16);
				this.remove(tileX + 1, tileZ - 1, 1);
			}
			if (rotation == 3) {
				this.remove(tileX, tileZ, 64);
				this.remove(tileX - 1, tileZ - 1, 4);
			}
		}
		if (type == 2) {
			if (rotation == 0) {
				this.remove(tileX, tileZ, 130);
				this.remove(tileX - 1, tileZ, 8);
				this.remove(tileX, tileZ + 1, 32);
			}
			if (rotation == 1) {
				this.remove(tileX, tileZ, 10);
				this.remove(tileX, tileZ + 1, 32);
				this.remove(tileX + 1, tileZ, 128);
			}
			if (rotation == 2) {
				this.remove(tileX, tileZ, 40);
				this.remove(tileX + 1, tileZ, 128);
				this.remove(tileX, tileZ - 1, 2);
			}
			if (rotation == 3) {
				this.remove(tileX, tileZ, 160);
				this.remove(tileX, tileZ - 1, 2);
				this.remove(tileX - 1, tileZ, 8);
			}
		}
		if (solid) {
			if (type == 0) {
				if (rotation == 0) {
					this.remove(tileX, tileZ, 0x10000);
					this.remove(tileX - 1, tileZ, 4096);
				}
				if (rotation == 1) {
					this.remove(tileX, tileZ, 1024);
					this.remove(tileX, tileZ + 1, 16384);
				}
				if (rotation == 2) {
					this.remove(tileX, tileZ, 4096);
					this.remove(tileX + 1, tileZ, 0x10000);
				}
				if (rotation == 3) {
					this.remove(tileX, tileZ, 16384);
					this.remove(tileX, tileZ - 1, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (rotation == 0) {
					this.remove(tileX, tileZ, 512);
					this.remove(tileX - 1, tileZ + 1, 8192);
				}
				if (rotation == 1) {
					this.remove(tileX, tileZ, 2048);
					this.remove(tileX + 1, tileZ + 1, 32768);
				}
				if (rotation == 2) {
					this.remove(tileX, tileZ, 8192);
					this.remove(tileX + 1, tileZ - 1, 512);
				}
				if (rotation == 3) {
					this.remove(tileX, tileZ, 32768);
					this.remove(tileX - 1, tileZ - 1, 2048);
				}
			}
			if (type == 2) {
				if (rotation == 0) {
					this.remove(tileX, tileZ, 0x10400);
					this.remove(tileX - 1, tileZ, 4096);
					this.remove(tileX, tileZ + 1, 16384);
				}
				if (rotation == 1) {
					this.remove(tileX, tileZ, 5120);
					this.remove(tileX, tileZ + 1, 16384);
					this.remove(tileX + 1, tileZ, 0x10000);
				}
				if (rotation == 2) {
					this.remove(tileX, tileZ, 20480);
					this.remove(tileX + 1, tileZ, 0x10000);
					this.remove(tileX, tileZ - 1, 1024);
				}
				if (rotation == 3) {
					this.remove(tileX, tileZ, 0x14000);
					this.remove(tileX, tileZ - 1, 1024);
					this.remove(tileX - 1, tileZ, 4096);
				}
			}
		}
	}

	public void removeLoc(int tileX, int tileY, int sizeX, int sizeY, int rotation, boolean solid) {
		int flag = 256;
		if (solid)
			flag += 0x20000;
		tileX -= this.xOffset;
		tileY -= this.yOffset;
		if (rotation == 1 || rotation == 3) {
			int w = sizeX;
			sizeX = sizeY;
			sizeY = w;
		}
		for (int x = tileX; x < tileX + sizeX; x++)
			if (x >= 0 && x < this.width) {
				for (int y = tileY; y < tileY + sizeY; y++)
					if (y >= 0 && y < this.height)
						this.remove(x, y, flag);

			}

	}

	private void remove(int x, int y, int flag) {
		this.flags[x][y] &= 0xffffff - flag;
	}

	public void removeFloorDecoration(int x, int y) {
		x -= this.xOffset;
		y -= this.yOffset;
		this.flags[x][y] &= 0xdfffff;
	}

	public boolean reachedWall(int initialX, int initialY, int finalX, int finalY, int rotation, int type) {
		if (initialX == finalX && initialY == finalY)
			return true;
		initialX -= this.xOffset;
		initialY -= this.yOffset;
		finalX -= this.xOffset;
		finalY -= this.yOffset;
		if (type == 0)
			if (rotation == 0) {
				if (initialX == finalX - 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x1280120) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 0x1280102) == 0)
					return true;
			} else if (rotation == 1) {
				if (initialX == finalX && initialY == finalY + 1)
					return true;
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280108) == 0)
					return true;
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280180) == 0)
					return true;
			} else if (rotation == 2) {
				if (initialX == finalX + 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x1280120) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 0x1280102) == 0)
					return true;
			} else if (rotation == 3) {
				if (initialX == finalX && initialY == finalY - 1)
					return true;
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280108) == 0)
					return true;
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280180) == 0)
					return true;
			}
		if (type == 2)
			if (rotation == 0) {
				if (initialX == finalX - 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY + 1)
					return true;
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280180) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 0x1280102) == 0)
					return true;
			} else if (rotation == 1) {
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280108) == 0)
					return true;
				if (initialX == finalX && initialY == finalY + 1)
					return true;
				if (initialX == finalX + 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 0x1280102) == 0)
					return true;
			} else if (rotation == 2) {
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280108) == 0)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x1280120) == 0)
					return true;
				if (initialX == finalX + 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY - 1)
					return true;
			} else if (rotation == 3) {
				if (initialX == finalX - 1 && initialY == finalY)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x1280120) == 0)
					return true;
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x1280180) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1)
					return true;
			}
		if (type == 9) {
			if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x20) == 0)
				return true;
			if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 2) == 0)
				return true;
			if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 8) == 0)
				return true;
			if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x80) == 0)
				return true;
		}
		return false;
	}

	public boolean reachedDecoration(int initialX, int initialY, int finalX, int finalY, int rotation, int type) {
		if (initialX == finalX && initialY == finalY)
			return true;
		initialX -= this.xOffset;
		initialY -= this.yOffset;
		finalX -= this.xOffset;
		finalY -= this.yOffset;
		if (type == 6 || type == 7) {
			if (type == 7)
				rotation = rotation + 2 & 3;
			if (rotation == 0) {
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x80) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 2) == 0)
					return true;
			} else if (rotation == 1) {
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 8) == 0)
					return true;
				if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 2) == 0)
					return true;
			} else if (rotation == 2) {
				if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 8) == 0)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x20) == 0)
					return true;
			} else if (rotation == 3) {
				if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x80) == 0)
					return true;
				if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x20) == 0)
					return true;
			}
		}
		if (type == 8) {
			if (initialX == finalX && initialY == finalY + 1 && (this.flags[initialX][initialY] & 0x20) == 0)
				return true;
			if (initialX == finalX && initialY == finalY - 1 && (this.flags[initialX][initialY] & 2) == 0)
				return true;
			if (initialX == finalX - 1 && initialY == finalY && (this.flags[initialX][initialY] & 8) == 0)
				return true;
			if (initialX == finalX + 1 && initialY == finalY && (this.flags[initialX][initialY] & 0x80) == 0)
				return true;
		}
		return false;
	}

	public boolean reachedObject(int x, int y, int finalX, int finalY, int width, int height, int surroundings) {
		int maxX = (finalX + width) - 1;
		int maxY = (finalY + height) - 1;
		if (x >= finalX && x <= maxX && y >= finalY && y <= maxY)
			return true;
		if (x == finalX - 1 && y >= finalY && y <= maxY && (this.flags[x - this.xOffset][y - this.yOffset] & 8) == 0
				&& (surroundings & 8) == 0)
			return true;
		if (x == maxX + 1 && y >= finalY && y <= maxY && (this.flags[x - this.xOffset][y - this.yOffset] & 0x80) == 0
				&& (surroundings & 2) == 0)
			return true;
		return y == finalY - 1 && x >= finalX && x <= maxX && (this.flags[x - this.xOffset][y - this.yOffset] & 2) == 0
				&& (surroundings & 4) == 0
				|| y == maxY + 1 && x >= finalX && x <= maxX
						&& (this.flags[x - this.xOffset][y - this.yOffset] & 0x20) == 0 && (surroundings & 1) == 0;
	}

	private final int xOffset;
	private final int yOffset;
	private final int width;
	private final int height;
	public final int[][] flags;
}
