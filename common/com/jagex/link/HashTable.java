package com.jagex.link;

final class HashTable {

	HashTable() {
		int i = 1024;// was parameter
		this.size = i;
		this.cache = new Linkable[i];
		for (int k = 0; k < i; k++) {
			Linkable linkable = this.cache[k] = new Linkable();
			linkable.next = linkable;
			linkable.previous = linkable;
		}

	}

	Linkable get(long l) {
		Linkable linkable = this.cache[(int) (l & this.size - 1)];
		for (Linkable node_1 = linkable.next; node_1 != linkable; node_1 = node_1.next)
			if (node_1.key == l)
				return node_1;

		return null;
	}

	void put(long l, Linkable linkable) {
		if (linkable.previous != null)
			linkable.unlink();
		Linkable node_1 = this.cache[(int) (l & this.size - 1)];
		linkable.previous = node_1.previous;
		linkable.next = node_1;
		linkable.previous.next = linkable;
		linkable.next.previous = linkable;
		linkable.key = l;
	}

	private final int size;
	private final Linkable[] cache;
}
