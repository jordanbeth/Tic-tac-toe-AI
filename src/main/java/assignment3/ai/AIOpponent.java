package assignment3.ai;

import static assignment3.GameConstants.COMPUTER;
import static assignment3.GameConstants.EMPTY;
import static assignment3.GameConstants.HUMAN;

import java.util.ArrayList;
import java.util.List;

public abstract class AIOpponent {

	protected int[][] board = new int[3][3];
	
	/**
	 * Make a deep copy of the game board.
	 * @param board - int[][] game board to copy
	 * @return int[][] - a deep copy of the game board
	 */
	
	static int[][] getBoardDeepCopy(int[][] board) {
		int[][] boardDeepCopy = new int[3][3];
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				boardDeepCopy[i][j] = board[i][j];
			}
		}
		
		return boardDeepCopy;
	}
	
	public abstract int[] getBestMove(int[][] board);
	
	
	/**
	 * Find all of the valid next moves given current state of the board.
	 * 
	 * @return List<int[]> of moves where each int[] contains the { row, col } of
	 *         the move or an empty list if the game is over.
	 */
	
	protected List<int[]> getAvailableMoves() {
		List<int[]> nextMoves = new ArrayList<>(); // allocate List

		// If game over there will be no next moves
		if (didComputerWin() || didHumanWin()) {
			return nextMoves;
		}

		// Search for empty cells and add to the List
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; ++col) {
				if (board[row][col] == EMPTY) {
					nextMoves.add(new int[] { row, col });
				}
			}
		}
		return nextMoves;
	}

	
	/**
	 * A method to evaluate the current score given the state of the board.
	 * 
	 * @Return +100  -> 3 in-a-line (computer) 
	 * 			+10, -> 2 in-a-line (computer)
	 * 			 +1  -> 1 in-a-line (computer)
	 * 
	 *         -100  -> 3 in-a-line (human) 
	 * 			-10, -> 2 in-a-line (human)
	 * 			 -1  -> 1 in-a-line (human)
	 * 
	 *         	  0 otherwise
	 */
	
	protected int scoreGameBoard() {
		int score = 0;
		
		// Evaluate score for each of 3 rows and 3 columns
		for(int i = 0; i <= 2; i++) {
			score += evaluateRow(i); // row i	
			score += evaluateCol(i); // column i
		}
		
		// Evaluate the score for each of the 2 diagonals
		score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
		score += evaluateLine(0, 2, 1, 1, 2, 0); // alternate diagonal
		
		return score;
	}
	
	
	private int evaluateRow(int row){
		return evaluateLine(row, 0, row, 1, row, 2);
	}
	
	
	private int evaluateCol(int col) {
		return evaluateLine(0, col, 1, col, 2, col);	
	}

	
	/**
	 * A method to evaluate the current score of a line in the game board.
	 * 
	 * @Return +100  -> 3 in-a-line (computer) 
	 * 			+10, -> 2 in-a-line (computer)
	 * 			 +1  -> 1 in-a-line (computer)
	 * 
	 *         -100  -> 3 in-a-line (human) 
	 * 			-10, -> 2 in-a-line (human)
	 * 			 -1  -> 1 in-a-line (human)
	 * 
	 *         	  0 otherwise
	 */
	
	private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
		int score = 0;
		
		// First cell
		if (board[row1][col1] == COMPUTER) {
			score = 1;
		} else if (board[row1][col1] == HUMAN) {
			score = -1;
		}

		// Second cell
		if (board[row2][col2] == COMPUTER) {
			
			if (score == 1) { // Computer is in cell 1
				score = 10;
			} else if (score == -1) { // Human is in cell 1
				return 0;
			} else { // cell 1 is empty
				score = 1;
			}
		} else if (board[row2][col2] == HUMAN) {
			if (score == -1) { // Human is in cell 1
				score = -10;
			} else if (score == 1) { // Computer is in cell 1
				return 0;
			} else { // cell 1 is empty
				score = -1;
			}
		}

		// Third cell
		if (board[row3][col3] == COMPUTER) {
			if (score > 0) { // Computer is in cell 1 and / or cell 2 
				score *= 10;
			} else if (score < 0) { // Human is in cell 1 and / or cell 2
				return 0;
			} else { // cell 1 and cell 2 are empty
				score = 1;
			}
		} else if (board[row3][col3] == HUMAN) {
			if (score < 0) { // Human is in cell 1 and / or cell 2
				score *= 10;
			} else if (score > 1) { // Computer is in cell 1 and / or cell 2s
				return 0;
			} else { // cell 1 and cell 2 are empty
				score = -1;
			}
		}
		return score;
	}

	private boolean didHumanWin() {
		return isWinForPlayerOrComputer(HUMAN);
	}

	private boolean didComputerWin() {
		return isWinForPlayerOrComputer(COMPUTER);
	}

	/**
	 * Check if there is a win for the player or the computer.
	 * 
	 * @param playerOrComputer - int representing a player or computer
	 * @return
	 */

	private boolean isWinForPlayerOrComputer(int playerOrComputer) {
		boolean isWin = isRowCrossed(playerOrComputer) || isColumnCrossed(playerOrComputer) || isDiagonalCrossed(playerOrComputer);
		return isWin;
	}

	/**
	 * Check if a row is crossed.
	 * 
	 * @param playerOrComputer - int representing the player or the computer
	 * @return true if row is crossed else false
	 */

	private boolean isRowCrossed(int playerOrComputer) {
		boolean isRowCrossed = false;
		for (int row = 0; row < board.length; row++) {
			int[] boardRow = board[row];

			if (boardRow[0] == playerOrComputer && boardRow[1] == playerOrComputer && boardRow[2] == playerOrComputer) {
				isRowCrossed = true;
			}
		}
		return isRowCrossed;
	}

	/**
	 * Check if a column is crossed.
	 * 
	 * @param playerOrComputer - int representing the player or the computer
	 * @return true if column is crossed else false
	 */

	private boolean isColumnCrossed(int playerOrComputer) {
		boolean isColCrossed = false;

		int col = 0;
		while (col < 3) {
			if (board[0][col] == playerOrComputer && board[1][col] == playerOrComputer && board[2][col] == playerOrComputer) {
				isColCrossed = true;
			}
			col++;
		}

		return isColCrossed;
	}

	/**
	 * Check if a diagonal is crossed.
	 * 
	 * @param playerOrComputer - int representing the player or the computer
	 * @return true if diagonal is crossed else false
	 */

	private boolean isDiagonalCrossed(int player) {

		boolean isCrossed = false;
		if ((player == board[0][0]) && (player == board[1][1]) && (player == board[2][2])) {
			isCrossed = true;
		} else if ((player == board[0][2]) && (player == board[1][1]) && (player == board[2][0])) {
			isCrossed = true;
		}

		return isCrossed;
	}
}
