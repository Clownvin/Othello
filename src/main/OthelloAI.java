package main;

import java.util.ArrayList;

public final class OthelloAI {
	private final byte player;
	
	public OthelloAI(byte player) {
		this.player = player;
	}
	
	public byte getPlayer() {
		return player;
	}
	//int[0] = x
	//int[1] = y
	public Move getAIMove(GameBoard gameboard) {
		gameboard = (GameBoard) gameboard.clone();
		Move bestMove = null;
		float bestRatio = 0.0f;
		if (gameboard.getMovesLeft() < 15) {
		System.out.println("Powerhouse Mode");
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (gameboard.validMove(x, y, player)) {
					System.out.println("Trailing move ("+x+", "+y+")");
					Move possibleMove = new Move(x, y, player);
					float winRatio = getWinRatio(possibleMove, gameboard);
					if (winRatio > bestRatio || bestMove == null) {
						bestMove = possibleMove;
						bestRatio = winRatio;
					}
				}
			}
		}
		} else {
			System.out.println("Random Mode");
			ArrayList<Move> moves = new ArrayList<Move>();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (gameboard.validMove(x, y, player)) {
						moves.add(new Move(x, y, player));
				}
			}
			}
			bestMove = moves.get((int)Math.random() * moves.size());
		}
		return bestMove;
	}
	
	private float getWinRatio(Move move, GameBoard gameboard) {
		gameboard = (GameBoard) gameboard.clone();
		gameboard.place(move.getX(), move.getY(), move.getPlayer());
		if (gameboard.gameOver())
			return gameboard.getWinner() == player ? 1.0f : gameboard.getWinner() == 0 ? 0.5f : 0.0f;
		float totalMoves = 0.0f;
		float totalRatio = 0.0f;
		byte nextPlayer = Othello.getOppositePlayer(move.getPlayer());
		if (!gameboard.playerHasMove(nextPlayer)) {
			nextPlayer = move.getPlayer();
			totalRatio = 1.0f;
		}
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (gameboard.validMove(x, y, nextPlayer)) {
					totalRatio += getWinRatio(new Move(x, y, nextPlayer), gameboard);
					totalMoves++;
				}
			}
		}
		return totalRatio / totalMoves;
	}
}
