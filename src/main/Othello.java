package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class Othello extends JFrame implements Runnable {

	private final static class GameBoard {
		private static final byte[][] DEFAULT_GAMEBOARD = new byte[][] { { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 2, 0, 0, 0 },
				{ 0, 0, 0, 2, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, };

		private byte[][] gameboard = new byte[8][8];

		public GameBoard() {

		}

		private GameBoard(byte[][] gameboard) {
			this.gameboard = gameboard.clone();
		}

		@Override
		public Object clone() {
			return new GameBoard(gameboard);
		}

		public boolean gameOver() {
			return !playerHasMove(BLACK) && !playerHasMove(WHITE);
		}

		public byte get(int x, int y) {
			if (x > 7 || y > 7 || x < 0 || y < 0)
				throw new IllegalArgumentException("x and y must be within the doman of all real numbers [0, 7]");
			return gameboard[x][y];
		}

		public byte getWinner() {
			int blackCount = 0, whiteCount = 0;
			for (byte[] row : gameboard)
				for (byte b : row)
					if (b == BLACK)
						blackCount++;
					else if (b == WHITE)
						whiteCount++;
			if (whiteCount == blackCount)
				return 0;
			return whiteCount > blackCount ? WHITE : BLACK;
		}

		public boolean place(int x, int y, byte player) {
			if (x > 7 || y > 7 || x < 0 || y < 0)
				throw new IllegalArgumentException("x and y must be within the doman of all real numbers [0, 7]");
			if (gameboard[x][y] != 0)
				return false;
			boolean valid = false;
			for (int x2 = x + 1, y2 = y + 1; x2 < 8 && y2 < 8; x2++, y2++) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1) {
					for (; x2 > x && y2 > y; x2--, y2--) {
						gameboard[x2][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int x2 = x + 1; x2 < 8; x2++) {
				if (gameboard[x2][y] == 0)
					break;
				if (gameboard[x2][y] == player && x2 != x + 1) {
					for (; x2 > x; x2--) {
						gameboard[x2][y] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y] == player && x2 == x + 1)
					break;
			}
			for (int x2 = x + 1, y2 = y - 1; x2 < 8 && y2 > -1; x2++, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1) {
					for (; x2 > x && y2 < y; x2--, y2++) {
						gameboard[x2][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int y2 = y - 1; y2 > -1; y2--) {
				if (gameboard[x][y2] == 0)
					break;
				if (gameboard[x][y2] == player && y2 != y - 1) {
					for (; y2 < y; y2++) {
						gameboard[x][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x][y2] == player && y2 == y - 1)
					break;
			}
			for (int y2 = y + 1; y2 < 8; y2++) {
				if (gameboard[x][y2] == 0)
					break;
				if (gameboard[x][y2] == player && y2 != y + 1) {
					for (; y2 > y; y2--) {
						gameboard[x][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x][y2] == player && y2 == y + 1)
					break;
			}
			for (int x2 = x - 1; x2 > -1; x2--) {
				if (gameboard[x2][y] == 0)
					break;
				if (gameboard[x2][y] == player && x2 != x - 1) {
					for (; x2 < x; x2++) {
						gameboard[x2][y] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y + 1; x2 > -1 && y2 < 8; x2--, y2++) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1) {
					for (; x2 < x && y2 > y; x2++, y2--) {
						gameboard[x2][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y - 1; x2 > -1 && y2 > -1; x2--, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1) {
					for (; x2 < x && y2 < y; x2++, y2++) {
						gameboard[x2][y2] = player;
					}
					valid = true;
					break;
				} else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			if (valid)
				gameboard[x][y] = player;
			return valid;
		}

		public boolean playerHasMove(byte player) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (validMove(x, y, player)) {
						return true;
					}
				}
			}
			return false;
		}

		public void reset() {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					gameboard[x][y] = DEFAULT_GAMEBOARD[x][y];
				}
			}
		}

		private boolean validMove(int x, int y, byte player) {
			if (x > 7 || y > 7 || x < 0 || y < 0)
				throw new IllegalArgumentException("x and y must be within the doman of all real numbers [0, 7]");
			if (gameboard[x][y] != 0)
				return false;
			for (int x2 = x + 1, y2 = y + 1; x2 < 8 && y2 < 8; x2++, y2++) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int x2 = x + 1; x2 < 8; x2++) {
				if (gameboard[x2][y] == 0)
					break;
				if (gameboard[x2][y] == player && x2 != x + 1)
					return true;
				else if (gameboard[x2][y] == player && x2 == x + 1)
					break;
			}
			for (int x2 = x + 1, y2 = y - 1; x2 < 8 && y2 > -1; x2++, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int y2 = y - 1; y2 > -1; y2--) {
				if (gameboard[x][y2] == 0)
					break;
				if (gameboard[x][y2] == player && y2 != y - 1)
					return true;
				else if (gameboard[x][y2] == player && y2 == y - 1)
					break;
			}
			for (int y2 = y + 1; y2 < 8; y2++) {
				if (gameboard[x][y2] == 0)
					break;
				if (gameboard[x][y2] == player && y2 != y + 1)
					return true;
				else if (gameboard[x][y2] == player && y2 == y + 1)
					break;
			}
			for (int x2 = x - 1; x2 > -1; x2--) {
				if (gameboard[x2][y] == 0)
					break;
				if (gameboard[x2][y] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y + 1; x2 > -1 && y2 < 8; x2--, y2++) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y - 1; x2 > -1 && y2 > -1; x2--, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			return false;
		}
	}

	private final class KeyHandler extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
		}
	}

	private final class MouseHandler extends MouseAdapter {
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -3235755634104439327L;

	private static final byte BLACK = 1, WHITE = 2;

	public static void main(String[] args) {
		new Othello().run();
	}
	private int squareWidth = 0, squareHeight = 0;
	private final GameBoard gameBoard = new GameBoard();

	private final MouseHandler mouseHandler = new MouseHandler(this);
	private byte player = 1;

	private BufferedImage drawImage;

	public Othello() {
		this.setUndecorated(true);
		this.setResizable(false);
		this.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - 300,
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - 300, 600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(mouseHandler);
		this.addKeyListener(new KeyHandler());
	}

	@Override
	public void paint(Graphics g) {
		if (drawImage == null) {
			drawImage = (BufferedImage) createImage(this.getWidth(), this.getHeight());
		}
		Graphics2D g2 = (Graphics2D) drawImage.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color((int) (0xFF * 0.65) << 8));
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		squareWidth = (this.getWidth()) / 8;
		squareHeight = (this.getHeight()) / 8;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				for (int i = 0; i < 3; i++) {
					g2.setColor(new Color((0xFF / 2) << 8));
					g2.drawRect((x * squareWidth) - i, (y * squareHeight) - i, squareWidth + (i * 2),
							squareHeight + (i * 2));
				}
				if (gameBoard.get(x, y) != 0) {
					switch (gameBoard.get(x, y)) {
					case BLACK:
						g2.setColor(Color.BLACK);
						g2.fillOval((int) (((x * squareWidth)) + (0.10f * squareWidth)),
								(int) (((y * squareHeight)) + (0.10f * squareHeight)), (int) (squareWidth * 0.8f),
								(int) (squareHeight * 0.8f));
						break;
					case WHITE:
						g2.setColor(Color.WHITE);
						g2.fillOval((int) (((x * squareWidth)) + (0.10f * squareWidth)),
								(int) (((y * squareHeight)) + (0.10f * squareHeight)), (int) (squareWidth * 0.8f),
								(int) (squareHeight * 0.8f));
						break;
					}
				}
				if (gameBoard.validMove(x, y, player)) {
					switch (player) {
					case BLACK:
						g2.setColor(Color.BLACK);
						g2.drawOval((int) (((x * squareWidth)) + (0.20f * squareWidth)),
								(int) (((y * squareHeight)) + (0.20f * squareHeight)), (int) (squareWidth * 0.6f),
								(int) (squareHeight * 0.6f));
						break;
					case WHITE:
						g2.setColor(Color.WHITE);
						g2.drawOval((int) (((x * squareWidth)) + (0.20f * squareWidth)),
								(int) (((y * squareHeight)) + (0.20f * squareHeight)), (int) (squareWidth * 0.6f),
								(int) (squareHeight * 0.6f));
						break;
					}
				}
			}
		}
		g.drawImage(drawImage, 0, 0, null);
	}

	@Override
	public void run() {
		setVisible(true);
		gameBoard.reset();
		repaint();
		JOptionPane.showMessageDialog(this, "Press [ESC] to close.");
		while (true) {
			gameBoard.reset();
			player = BLACK;
			repaint();
			do {
				do {
					try {
						synchronized (this) {
							this.wait(); // Waiting for mouse handler to notify
											// main thread of a release event
						}
					} catch (InterruptedException e) {
					}
				} while (!gameBoard.place((mouseHandler.getX()) / squareWidth, (mouseHandler.getY()) / squareHeight,
						player));
				player = player == BLACK ? WHITE : BLACK;
				if (!gameBoard.playerHasMove(player)) {
					player = player == BLACK ? WHITE : BLACK;
				}
				repaint();
			} while (!gameBoard.gameOver());
			JOptionPane.showMessageDialog(this, ("Winner: "
					+ (gameBoard.getWinner() == BLACK ? "Black" : gameBoard.getWinner() == WHITE ? "White" : "Draw")));
		}
	}
}
