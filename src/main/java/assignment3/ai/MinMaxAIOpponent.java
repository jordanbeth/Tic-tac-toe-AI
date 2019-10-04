package assignment3.ai;

import static assignment3.GameConstants.COMPUTER;
import static assignment3.GameConstants.EMPTY;
import static assignment3.GameConstants.HUMAN;

import java.util.List;

public class MinMaxAIOpponent extends AIOpponent {
	
	// depth is how many moves ahead we want to search
	private int depth = 2;

	public MinMaxAIOpponent() {
	}

	/**
	 * Make a move for the computer using the MiniMax algorithm.
	 * 
	 * @param board
	 * @return int [] - representing the { row, col } of the computer's move
	 */
	@Override
	public int[] getBestMove(int[][] board) {
		this.board = getBoardDeepCopy(board);
		// depth is how many moves ahead we want to search
		int[] result = minMax(this.depth, COMPUTER);
		return new int[] { result[1], result[2] }; // row, col
	}
		
	/**
	 * Recursive MiniMax at level of depth for either maximizing or minimizing the
	 * provided player.
	 * 
	 * @param depth - int representing how many moves ahead we want to search
	 * @param player - int representing the player
	 * @return int[] - representing the [bestScore, row, col] of the best move
	 */
	
	private int[] minMax(int depth, int player) {
		List<int[]> nextMoves = getAvailableMoves();

		int bestScore;
		if (player == COMPUTER) { // we will maximize
			bestScore = Integer.MIN_VALUE;
		} else { // we will minimize
			bestScore = Integer.MAX_VALUE;
		}

		int currentScore;
		int bestRow = -1;
		int bestCol = -1;

		if (nextMoves.isEmpty() || depth == 0) {
			bestScore = scoreGameBoard(); // Game over or depth reached.
		} else {
			for (int[] move : nextMoves) {
				// Try this move for the current player
				int row = move[0];
				int col = move[1];
				board[row][col] = player;
				if (player == COMPUTER) { 
					// maximize the human
					currentScore = minMax(depth - 1, HUMAN)[0];
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestRow = row;
						bestCol = col;
					}
				} else {
					// minimize the computer
					currentScore = minMax(depth - 1, COMPUTER)[0];
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestRow = row;
						bestCol = col;
					}
				}
			
				// Undo the move
				board[row][col] = EMPTY;
			}
		}
		return new int[] { bestScore, bestRow, bestCol };
	}

}
