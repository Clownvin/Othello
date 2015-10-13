package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class Othello extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3235755634104439327L;

	public static final byte BLACK = (byte) 1, WHITE = (byte) 2;

	private static final Color BOARD_COLOR = new Color((int) (0xFF * 0.65f) << 8),
			BOARD_SQUARE_COLOR = new Color((0xFF / 2) << 8);

	public static void main(String[] args) {
		new Othello(600, 600).run();
	}

	private final int squareWidth, squareHeight;
	private final GameBoard gameBoard = new GameBoard();

	private final MouseHandler mouseHandler = new MouseHandler(this);
	private byte player = (byte) 1;

	private BufferedImage drawImage;

	public Othello(int width, int height) {
		this.setUndecorated(true);
		this.setResizable(false);
		this.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (width / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (height / 2), width, height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(mouseHandler);
		this.addKeyListener(new KeyHandler());
		squareWidth = width / 8;
		squareHeight = height / 8;
	}

	@Override
	public void paint(Graphics g) {
		if (drawImage == null) {
			drawImage = (BufferedImage) createImage(this.getWidth(), this.getHeight());
		}
		Graphics2D g2 = (Graphics2D) drawImage.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(BOARD_COLOR);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				g2.setColor(BOARD_SQUARE_COLOR);
				for (int i = 0; i < 3; i++) { // Drawing borders
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
							this.wait();
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
