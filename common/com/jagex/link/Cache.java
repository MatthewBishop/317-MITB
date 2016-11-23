package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 



public final class Cache {

	public Cache(int i) {
		this.empty = new Cacheable();
		this.history = new Queue();
		this.initialCount = i;
		this.unused = i;
		this.table = new HashTable();
	}

	public Cacheable get(long l) {
		Cacheable cacheable = (Cacheable) this.table.get(l);
		if (cacheable != null) {
			this.history.push(cacheable);
		}
		return cacheable;
	}

	public void put(long l, Cacheable cacheable) {
		if (this.unused == 0) {
			Cacheable nodeSub_1 = this.history.pop();
			nodeSub_1.unlink();
			nodeSub_1.unlinkCacheable();
			if (nodeSub_1 == this.empty) {
				Cacheable nodeSub_2 = this.history.pop();
				nodeSub_2.unlink();
				nodeSub_2.unlinkCacheable();
			}
		} else {
			this.unused--;
		}
		this.table.put(l, cacheable);
		this.history.push(cacheable);
	}

	public void clear() {
		do {
			Cacheable cacheable = this.history.pop();
			if (cacheable != null) {
				cacheable.unlink();
				cacheable.unlinkCacheable();
			} else {
				this.unused = this.initialCount;
				return;
			}
		} while (true);
	}

	private final Cacheable empty;
	private final int initialCount;
	private int unused;
	private final HashTable table;
	private final Queue history;
}
