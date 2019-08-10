package com.DystryktZ.utils;

import net.minecraft.item.Item;

public class LotteryPack extends Vector2D<Item, Integer> implements Comparable<LotteryPack> {

	public LotteryPack(Item x, Integer y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(LotteryPack o) {
		// TODO Auto-generated method stub
		return o.getY() - this.getY();
	}

}
