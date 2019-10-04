package assignment3.ai;

import static assignment3.GameConstants.COMPUTER;
import static assignment3.GameConstants.EMPTY;
import static assignment3.GameConstants.HUMAN;

import java.util.List;

public class AlphaBetaAIOpponent extends AIOpponent {
	
	// depth is how many moves ahead we want to search
	private int depth = 2;
	
	// Alpha starts at max value
	private int alpha = Integer.MIN_VALUE;
	
	// Beta starts at min value
	private int beta = Integer.MAX_VALUE;

	public AlphaBetaAIOpponent() {
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
		int[] result = minMaxAlphaBetaPruning(this.depth, COMPUTER, this.alpha, this.beta);
		return new int[] { result[1], result[2] }; // row, col
	}
	
	/**
	 * Recursive MinMax with Alpha Beta pruning at level of depth for either maximizing or minimizing the
	 * provided player.
	 * 
	 * @param depth - int representing how many moves ahead we want to search
	 * @param player - int representing the player
	 * @return int[] - representing the [bestScore, row, col] of the best move
	 */
	
	private int[] minMaxAlphaBetaPruning(int depth, int player, int alpha, int beta) {
		List<int[]> nextMoves = getAvailableMoves();

		int currentScore = 0;
		int bestRow = -1;
		int bestCol = -1;

		if (nextMoves.isEmpty() || depth == 0) {
			currentScore = scoreGameBoard(); // Game over or depth reached.
			return new int[] { currentScore, bestRow, bestCol };
		} else {
			for (int[] move : nextMoves) {
				// Try this move for the current player
				int row = move[0];
				int col = move[1];
				board[row][col] = player;
				if (player == COMPUTER) {
					// maximize the human
					int[] minMaxAlphaBetaPruning = minMaxAlphaBetaPruning(depth - 1, HUMAN, alpha, beta);
					currentScore = minMaxAlphaBetaPruning[0];
					if (currentScore > alpha) {
						alpha = currentScore;
						bestRow = row;
						bestCol = col;
					}
				} else {
					// minimize the computer
					int[] minMaxAlphaBetaPruning = minMaxAlphaBetaPruning(depth - 1, COMPUTER, alpha, beta);
					currentScore = minMaxAlphaBetaPruning[0];
					if (currentScore < beta) {
						beta = currentScore;
						bestRow = row;
						bestCol = col;
					}
				}

				// Undo the move
				board[row][col] = EMPTY;

				// check for cutoff
				if (alpha >= beta) {
					break;
				}
			}

			if (player == COMPUTER) {
				currentScore = alpha;
			} else {
				currentScore = beta;
			}

			return new int[] { currentScore, bestRow, bestCol };
		}
	}
	
}
