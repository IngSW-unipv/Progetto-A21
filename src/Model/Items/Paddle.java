package Model.Items;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import GUI.ImagesLoader;

public class Paddle extends ScreenItem {
	
	// classe che definisce un paddle e le sue funzioni 
	
	// velocità paddle 
	private static final int VELOCITA = 3;
	
	// direzione paddle
    private int dr, leftLimit, rightLimit;
    private boolean isSwitched;
    private int switchConstant;
	private boolean oneTimeMulti;


    public Paddle(int width, int height, int[] position) {
    	super(width, height, position);
    	this.image = ImagesLoader.getInstace().uploadImage("/Images/paddle.png");
    	isSwitched = false;
    	switchConstant = 1;
    	leftLimit = 0;
    	rightLimit = 495;
    	oneTimeMulti=true;
    }
    
    public void setLimits(int leftLimit, int rightLimit) {
    	this.leftLimit=leftLimit;
    	this.rightLimit=rightLimit;
    }
    
    // richiamato da update, verifica che non si vada fuori dai bordi dx e sx
    public void move() {
    	
        position[0] += dr * switchConstant;
        
        if (position[0] <= leftLimit) {
        	position[0] = leftLimit;
        }

        if (position[0] + imageWidth >= rightLimit) {
        	position[0] = rightLimit - imageWidth;
        }
    }
    
    public void move(int positionXBall, int positionYBall, int ballImageWidth) {
    	if (positionYBall >= 81+Math.random()*10) {
    		if (positionXBall+ballImageWidth/2 >= position[0]+getImageWidth()/2) {
        		position[0] += VELOCITA;
        	}
        	else position[0] += -VELOCITA;
    	}
    	
    	if (position[0] <= leftLimit) {
        	position[0] = leftLimit;
        }

        if (position[0] + imageWidth >= rightLimit) {
        	position[0] = rightLimit - imageWidth;
        }
    }
    
    // appena si preme il paddle si aggiorna ad ogni frame in base alla velocità impostata
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

        	dr = -VELOCITA;
        }

        if (key == KeyEvent.VK_RIGHT) {

        	dr = VELOCITA;
        }
    }

    // al rilascio del stato il paddle si ferma, controllo da tastiera utente
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

        	dr = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

        	dr = 0;
        }
    }
    
    public void switchDir() {
    	isSwitched =!isSwitched;
    	switchConstant = -switchConstant;
    }
    
    public void switchDirMultiplayer(boolean active) {
    	if(!active&&!oneTimeMulti) switchDir();
    	if (oneTimeMulti) {
	    	if (active) {
	    		switchDir();
	    		oneTimeMulti = false;
	    	}
    	}
	if (!active) oneTimeMulti = true;
    }
    
    public void setImageMainPaddle() {
    	image = ImagesLoader.getInstace().uploadImage("/Images/paddleMain.png");
    }
}
