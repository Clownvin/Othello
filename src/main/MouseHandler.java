package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class MouseHandler extends MouseAdapter {
	private final Othello game;
	private volatile int x, y;

	public MouseHandler(final Othello game) {
		this.game = game;
	}

	public synchronized int getX() {
		return x;
	}

	public synchronized int getY() {
		return y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		synchronized (game) {
			game.notifyAll();
		}
	}
}