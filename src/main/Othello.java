package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public final class Othello extends JFrame {
	private static final byte[][] DEFAULT_GAMEBOARD = new byte[][] { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 2, 0, 0, 0 },
			{ 0, 0, 0, 2, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, };

	public class MouseHandler extends MouseAdapter {
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

	public final class GameBoard {

		private byte[][] gameboard = DEFAULT_GAMEBOARD;

		public GameBoard() {

		}

		public void reset() {
			gameboard = DEFAULT_GAMEBOARD;
		}

		public byte getWinner() {
			int blackCount = 0, whiteCount = 0;
			for (byte[] row : gameboard)
				for (byte b : row)
					if (b == 1)
						blackCount++;
					else if (b == 2)
						whiteCount++;
			if (whiteCount == blackCount)
				return 0;
			return (byte) (whiteCount > blackCount ? 2 : 1);
		}

		public boolean gameOver(byte player) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (validMove(x, y, player))
						return false;
				}
			}
			return true;
		}

		public byte get(int x, int y) {
			if (x > 7 || y > 7 || x < 0 || y < 0)
				throw new IllegalArgumentException("x and y must be within the doman of all real numbers [0, 7]");
			return gameboard[x][y];
		}

		private boolean validMove(int x, int y, byte player) {
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
			for (int x2 = x + 1, y2 = y - 1; x2 < 8 && y2 > 0; x2++, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int y2 = y - 1; y2 > 0; y2--) {
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
			for (int x2 = x - 1; x2 > 0; x2--) {
				if (gameboard[x2][y] == 0)
					break;
				if (gameboard[x2][y] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y + 1; x2 > 0 && y2 < 8; x2--, y2++) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			for (int x2 = x - 1, y2 = y - 1; x2 > 0 && y2 > 0; x2--, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x - 1)
					return true;
				else if (gameboard[x2][y2] == player && x2 == x - 1)
					break;
			}
			return false;
		}

		public boolean place(int x, int y, byte player) {
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
			for (int x2 = x + 1, y2 = y - 1; x2 < 8 && y2 > 0; x2++, y2--) {
				if (gameboard[x2][y2] == 0)
					break;
				if (gameboard[x2][y2] == player && x2 != x + 1) {
					for (; x2 > x && y2 < y; x2--, y2++) {
						gameboard[x2][y2] = player;
					}
					System.out.println("+X-Y");
					valid = true;
					break;
				} else if (gameboard[x2][y2] == player && x2 == x + 1)
					break;
			}
			for (int y2 = y - 1; y2 > 0; y2--) {
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
			for (int x2 = x - 1; x2 > 0; x2--) {
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
			for (int x2 = x - 1, y2 = y + 1; x2 > 0 && y2 < 8; x2--, y2++) {
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
			for (int x2 = x - 1, y2 = y - 1; x2 > 0 && y2 > 0; x2--, y2--) {
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
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3235755634104439327L;
	
	private int squareWidth = 0, squareHeight = 0;
	private final GameBoard gameBoard = new GameBoard();
	private final MouseHandler mouseHandler = new MouseHandler(this);
	private byte player = 1;

	public Othello() {
		this.setUndecorated(true);
		this.setBounds(100, 100, 160, 160);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(mouseHandler);
		this.start();
	}

	public void start() {
		while (true) {
			gameBoard.reset();
			do {
				System.out.println("Your turn, " + (player == 1 ? "black" : "white") + ".");
				do {
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
					}
				} while (!gameBoard.place((mouseHandler.getX()) / squareWidth,
						(mouseHandler.getY()) / squareHeight, player));
				repaint();
				player = (byte) (player == 1 ? 2 : 1);
			} while (!gameBoard.gameOver(player));
			System.out.println("Winner: "
					+ (gameBoard.getWinner() == 1 ? "Black" : gameBoard.getWinner() == 2 ? "White" : "Draw"));
		}
	}

	private BufferedImage drawImage;

	@Override
	public void paint(Graphics g) {
		if (drawImage == null) {
			drawImage = (BufferedImage) createImage(1000, 1000);
		}
		Graphics2D g2 = (Graphics2D) drawImage.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color((int) (0xFF * 0.75) << 8));
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		squareWidth = (this.getWidth()) / 8;
		squareHeight = (this.getHeight()) / 8;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				g2.setColor(new Color((0xFF / 2) << 8));
				g2.drawRect((x * squareWidth), (y * squareHeight), squareWidth, squareHeight);
				if (gameBoard.get(x, y) != 0) {
					switch (gameBoard.get(x, y)) {
					case 1:
						g2.setColor(Color.BLACK);
						g2.fillOval((int) (((x * squareWidth)) + (0.10f * squareWidth)),
								(int) (((y * squareHeight)) + (0.10f * squareHeight)),
								(int) (squareWidth * 0.8f), (int) (squareHeight * 0.8f));
						break;
					case 2:
						g2.setColor(Color.WHITE);
						g2.fillOval((int) (((x * squareWidth)) + (0.10f * squareWidth)),
								(int) (((y * squareHeight)) + (0.10f * squareHeight)),
								(int) (squareWidth * 0.8f), (int) (squareHeight * 0.8f));
						break;
					}
				}
			}
		}
		g.drawImage(drawImage, 0, 0, null);
	}

	public static void main(String[] args) throws InterruptedException {
		Othello test = new Othello();
		test.start();
	}
}
