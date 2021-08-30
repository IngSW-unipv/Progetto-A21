package Model.Logic;

import Model.Items.Item;
import Model.Items.Paddle;

public class Player {
	/*
	 * 
	 * classe che gestisce le funzionalità di un player, 
	 * ogni player possiede il suo paddle e lo mantiene 
	 * per tutta la durata del gioco
	 */
	private Paddle objPaddle;
	private InputAdapter inputHandler;


	public Player() {
	
		objPaddle = (Paddle)ScreenItemFactory.getInstance().getScreenItem(Item.PADDLE);
		this.inputHandler = new InputAdapter(objPaddle);
	}

	public InputAdapter getInputHandler() {
		return inputHandler;
	}

	public void setInputHandler(InputAdapter inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public Paddle getObjPaddle() {	
		return objPaddle;
	}
}
