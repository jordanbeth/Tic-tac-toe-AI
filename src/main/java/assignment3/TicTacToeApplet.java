package assignment3;

import static assignment3.GameConstants.COMPUTER;
import static assignment3.GameConstants.HUMAN;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TicTacToeApplet extends Applet implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;

	boolean hasSelectedGameMode = false;

	GameMode gameMode;

	final TicTacToeGame game = new TicTacToeGame();

	// States of the game
	static final int OK = 0;
	static final int WIN = 1;
	static final int LOSE = 2;
	static final int STALEMATE = 3;

	/**
	 * User move.
	 * 
	 * @return true if legal
	 */
	boolean humanMove(int m) {
		return this.game.moveHuman(m);
	}

	/**
	 * Computer move.
	 * 
	 * @return true if legal
	 */
	boolean computerMove() {
		return this.game.isComputerMoveLegal();
	}

	/**
	 * Figure what the status of the game is.
	 */
	int status() {
		return this.game.getGameStatus();
	}

	/**
	 * The image for white.
	 */
	Image notImage;

	/**
	 * The image for black.
	 */
	Image crossImage;

	Button minMaxButton;

	Button alphaBetaButton;
	
	/**
	 * Initialize the applet. Resize and load images.
	 */
	public void init() {

		this.minMaxButton = new Button("Play Min-Max");
		this.minMaxButton.addActionListener(this);
		add(this.minMaxButton);

		this.alphaBetaButton = new Button("Play Alpha-Beta");
		this.alphaBetaButton.addActionListener(this);
		add(this.alphaBetaButton);
		

		notImage = getImage(getCodeBase(), "not.gif");
		crossImage = getImage(getCodeBase(), "cross.gif");

		setSize(500, 500);
		addMouseListener(this);
	}

	public void destroy() {
		removeMouseListener(this);
	}

	/**
	 * Paint it.
	 */
	public void paint(Graphics g) {

		if (this.hasSelectedGameMode) {
			Dimension d = getSize();
			g.setColor(Color.black);
			int xoff = d.width / 3;
			int yoff = d.height / 3;
			g.drawLine(xoff, 0, xoff, d.height);
			g.drawLine(2 * xoff, 0, 2 * xoff, d.height);
			g.drawLine(0, yoff, d.width, yoff);
			g.drawLine(0, 2 * yoff, d.width, 2 * yoff);

			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {

					int playerInSpace = this.game.getPlayerInSpace(row, col);
					if (playerInSpace == HUMAN) {
						g.drawImage(crossImage, col * xoff + 1, row * yoff + 1, this);
					} else if (playerInSpace == COMPUTER) {
						g.drawImage(notImage, col * xoff + 1, row * yoff + 1, this);
					}

				}
			}
		}
	}

	/**
	 * The user has clicked in the applet. Figure out where and see if a legal move
	 * is possible. If it is a legal move, respond with a legal move (if possible).
	 */
	public void mouseReleased(MouseEvent e) {
		if (this.hasSelectedGameMode) {
			int x = e.getX();
			int y = e.getY();
			int status = status();
			switch (status) {
			case WIN:
			case LOSE:
			case STALEMATE:
				play(getCodeBase(), "audio/return.au");
				this.hasSelectedGameMode = false;
				setButtonsVisible(true);
				this.game.reset();
				repaint();
				return;
			}

			// Figure out the row/column
			Dimension d = getSize();
			int col = (x * 3) / d.width;
			int row = (y * 3) / d.height;

			boolean moveIsLegal = humanMove(col + row * 3);

			if (moveIsLegal) {
				repaint();
				switch (status()) {
				case WIN:
					play(getCodeBase(), "audio/yahoo1.au");
					break;
				case LOSE:
					play(getCodeBase(), "audio/yahoo2.au");
					break;
				case STALEMATE:
					break;
				default:
					if (computerMove()) {
						repaint();
						switch (status()) {
						case WIN:
							play(getCodeBase(), "audio/yahoo1.au");
							break;
						case LOSE:
							play(getCodeBase(), "audio/yahoo2.au");
							break;
						case STALEMATE:
							break;
						default:
							play(getCodeBase(), "audio/ding.au");
						}
					} else {
						play(getCodeBase(), "audio/beep.au");
					}
				}
			} else {
				play(getCodeBase(), "audio/beep.au");
			}
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		boolean selected = false;
		if (e.getSource() == this.minMaxButton) {
			System.out.println("playing mix-max... ");
			this.gameMode = GameMode.MIN_MAX;
			selected = true;
		} else if (e.getSource() == this.alphaBetaButton) {
			System.out.println("playing alpha beta pruning...");
			this.gameMode = GameMode.ALPHA_BETA;
			selected = true;
		}
		
		if(selected) {
			setButtonsVisible(false);
			this.hasSelectedGameMode = true;
			this.game.setGameMode(this.gameMode);	
			repaint();
		}
		
	}
	
	private void setButtonsVisible(boolean setVisible) {
		this.minMaxButton.setVisible(setVisible);
		this.alphaBetaButton.setVisible(setVisible);
	}
}