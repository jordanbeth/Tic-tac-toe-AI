package assignment3;

import static assignment3.GameConstants.COMPUTER;
import static assignment3.GameConstants.EMPTY;
import static assignment3.GameConstants.HUMAN;

import java.util.Random;

import assignment3.ai.AIOpponent;
import assignment3.ai.AlphaBetaAIOpponent;
import assignment3.ai.MinMaxAIOpponent;

public class TicTacToeGame {

	private int[][] BOARD = new int[3][3];

	/**
	 * Indexes of rows in the board
	 */
	
	private static final int FIRST_ROW = 0;
	private static final int SECOND_ROW = 1;
	private static final int THIRD_ROW = 2;
	
	private final Random random = new Random();
	
	private boolean humanFirst = false;
	
	@SuppressWarnings("unused")
	private GameMode gameMode;
	
	private AIOpponent computer;
	

	/**
	 * Resets the game, switching the starting player  
	 */
	
	public void reset() {
		this.BOARD = new int[3][3];
		
		this.humanFirst = !this.humanFirst;
		if(!this.humanFirst) {
			makeRandomMove();
		}
	}
	
	/**
	 * Get the player that is in the current space
	 * @param row - int representing the row number
	 * @param col - int representing the column number
	 * @return int - the player in the current row, column space
	 */
	
	public int getPlayerInSpace(int row, int col) {
		return BOARD[row][col];
	}

	/**
	 * Move the player.
	 * @param location - int representing the location to move the player to
	 * @return boolean - whether or not the move was valid
	 */
	
	public boolean moveHuman(int location) {
		boolean validMove = false;
		if (location >= 0 && location <= 2) { // first row

			int space = BOARD[0][location];
			if (space == EMPTY) {
				validMove = true;
				BOARD[FIRST_ROW][location] = HUMAN;
			}

		} else if (location >= 3 && location <= 5) { // second row

			int idx = location - 3;

			int space = BOARD[1][idx];
			if (space == EMPTY) {
				validMove = true;
				BOARD[SECOND_ROW][idx] = HUMAN;
			}

		} else if (location >= 6 && location <= 8) { // third row

			int idx = location - 6;

			int space = BOARD[2][idx];
			if (space == EMPTY) {
				validMove = true;
				BOARD[THIRD_ROW][idx] = HUMAN;
			}

		}

		return validMove;
	}
	
	/**
	 * Computer will always make a legal move.
	 * @return true;
	 */
	
	public boolean isComputerMoveLegal() {
	
		moveAIComputer();
		
		return true;
	}
	
	/**
	 * Move computer using the MiniMax algorithm to determine the best move.
	 */
	
	private void moveAIComputer() {
		
		int[] bestMove = this.computer.getBestMove(this.BOARD);
		
		// System.out.println(Arrays.toString(bestMove));
		int row = bestMove[0];
		int col = bestMove[1];
		
		if(this.BOARD[row][col] == EMPTY) {
			this.BOARD[row][col] = COMPUTER;
		} else {
			// should never happen
			System.err.println("Could not make move as the space is not empty: " + this.BOARD[row][col]);
		}
	}
	
	/**
	 * Get the status of the current game.
	 * @return int representing the game status
	 */
	
	public int getGameStatus() {
		int status = TicTacToeApplet.OK;
		if (didHumanWin()) {
			status = TicTacToeApplet.WIN;
		} else if (didComputerWin()) {
			status = TicTacToeApplet.LOSE;
		} else if (isBoardFull()) {
			status = TicTacToeApplet.STALEMATE;
		}
		return status;

	}
	
	/**
	 * Make a random legal move.
	 */
	
	private void makeRandomMove() {
		while(true) {
			int nextRow = random.nextInt(BOARD.length);
			int nextCol = random.nextInt(BOARD.length);

			if(BOARD[nextRow][nextCol] == EMPTY) {
				BOARD[nextRow][nextCol] = COMPUTER;
				return;
			}
		}
	}

	/**
	 * Check if the board is full. This means the game is over.
	 * 
	 * @return boolean - true if the game is full else false
	 */
	
	private boolean isBoardFull() {
		boolean isFull = true;

		outer: for (int[] row : BOARD) {
			for (int i = 0; i < row.length; i++) {
				if (row[i] == EMPTY) {
					isFull = false;
					break outer;
				}
			}
		}
		return isFull;
	}

	private boolean didHumanWin() {
		return isWinForPlayerOrComputer(HUMAN);
	}

	
	private boolean didComputerWin() {
		return isWinForPlayerOrComputer(COMPUTER);
	}

	/**
	 * Check if there is a win for the player or the computer.
	 * @param playerOrComputer - int representing a player or computer
	 * @return
	 */
	
	private boolean isWinForPlayerOrComputer(int playerOrComputer) {
		return isRowCrossed(playerOrComputer) || isColumnCrossed(playerOrComputer) || isDiagonalCrossed(playerOrComputer);
	}
	
	/**
	 * Check if a row is crossed.
	 * 
	 * @param playerOrComputer - int representing the player or the computer
	 * @return true if row is crossed else false
	 */
	
	private boolean isRowCrossed(int playerOrComputer) {
		boolean isRowCrossed = false;
		for (int row = 0; row < BOARD.length; row++) {
			int[] boardRow = BOARD[row];

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
			if (BOARD[0][col] == playerOrComputer && BOARD[1][col] == playerOrComputer && BOARD[2][col] == playerOrComputer) {
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
		
		if ((player == BOARD[0][0]) && (player == BOARD[1][1]) && (player == BOARD[2][2])) {
			isCrossed = true;
		} else if ((player == BOARD[0][2]) && (player == BOARD[1][1]) && (player == BOARD[2][0])) {
			isCrossed = true;
		}

		return isCrossed;
	}

	/**
	 * Set the game mode and initialize the computer player.
	 * @param gameMode
	 */
	
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
		switch (gameMode) {
		case MIN_MAX:
			this.computer = new MinMaxAIOpponent();
			break;
		case ALPHA_BETA:
			this.computer = new AlphaBetaAIOpponent();
		default:
			break;
		}
	}
	
}
