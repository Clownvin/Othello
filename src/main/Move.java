package main;

public final class Move {
	private final int x, y;
	private final byte player;
	
	public Move(int x, int y, byte player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public byte getPlayer() {
		return player;
	}
}
