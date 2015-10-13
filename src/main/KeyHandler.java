package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class KeyHandler extends KeyAdapter {
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}
}