package Model.Core.Multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;

import Model.BreakoutGame;
import Model.Core.MultiplayerScreen;

import Model.Core.Screen;


public class Client {
	
	private DatagramSocket datagramSocket;
    static String serverIP = "35.246.143.53";
    //
    //78.134.15.159
    //
	private int serverPort;
	private ClientThread thread;
	private InetAddress address;
	private int portNewPlayer, numberOfMissingPlayer, numberOfPlayer, playerIndex, numberLevel;
		
	
	/**
	 * @param BreakoutGame game, è host?, gameCode, nome player, numero giocatore
	 */
	public void join(BreakoutGame game, Boolean isHost, String gameCode, String playerName, int playerNumber) {		
		
			// connessione con il server ed inzializzazione giocatore
			try {
				address = InetAddress.getByName(serverIP);
				
			//202.61.250.68
			
			boolean waitingToSend = true;
			
			while(waitingToSend) {
				// attesa per l'invioo di tutte le info per inzializzare una partita
				
				String playerData = new String(isHost + " " + playerName + " " + gameCode + " " + playerNumber);
				
				byte[] b = playerData.getBytes();
	            datagramSocket = new DatagramSocket();
	            DatagramPacket packet = new DatagramPacket(b, b.length, address, 2001);
	            datagramSocket.send(packet);
	            
	            
	            boolean waitingForReply = true;
	           // ciclo di attesa di riposta dal server
	            while (waitingForReply) {
	           
	            	//
		            byte[] c = new byte[1024];
	                DatagramPacket packet1 = new DatagramPacket(c, c.length);
	                datagramSocket.receive(packet1);
	                String AllInfo = new String(packet1.getData(), 0, packet1.getLength());
	                
	                System.out.println(AllInfo);
	                String AllInfos[] = AllInfo.split(" ");
	                
	                String isJoined = AllInfos[0];
	               
	                portNewPlayer = Integer.parseInt(AllInfos[1]);
	                
	                numberOfMissingPlayer = Integer.parseInt(AllInfos[2]);
	                
	                numberOfPlayer = Integer.parseInt(AllInfos[3]);
	                
	                playerIndex = Integer.parseInt(AllInfos[4]);
	                
	                numberLevel = Integer.parseInt(AllInfos[5]);
	                
	                System.out.println(AllInfo);
	                

	                if (isJoined.equals("false")) {
	                	
	                	waitingToSend = false;
	                	waitingForReply = false;
	                	
	                	game.multiplayerError(); 
	                }
	                
	                
	               if (isJoined.equals("true")) {
	                	
	            	   waitingToSend = false;
	            	   waitingForReply = false;
	            	   
	            	   if(numberOfMissingPlayer == 0) {
	            		   game.setNumberOfPlayer(numberOfPlayer);
	            		   game.setPlayerIndex(playerIndex);
	            		   game.setNumberOfMissingPlayer(numberOfMissingPlayer);
	            		   game.setNumberLevel(numberLevel);
	            		   game.startGame();
	            	   }
	            	   
	            	   else {
	            		   
	            		  
	            		   game.setNumberOfMissingPlayer(numberOfMissingPlayer);
    		       	      
    		       	       game.waitingMissingPlayer(); //apre menu di attesa attraverso il controller
    		       	       
    		       	       game.updateMissing();
    		       	       
    		       	       // thread che permette di attendere che tutti i giocatori siano entrati nella lobby per poi poter avviare la partita 
    		       	       
	            		   new Thread(new Runnable(){
	            				@Override
	            				public synchronized void run(){
	            					
	            					while(numberOfMissingPlayer != 0) {
	            						System.out.println("MANCANO: " + numberOfMissingPlayer);
	    		    	                
	    		       	                game.setNumberOfMissingPlayer(numberOfMissingPlayer);
	    		       	       
	    		       	                game.updateMissing();
	    		       	             
	            						byte[] d = new byte[1024];
	    		       	                DatagramPacket packet2 = new DatagramPacket(d, d.length);
	    		       	                try {
											datagramSocket.receive(packet2);
										} catch (IOException e) {
											e.printStackTrace();
										}
	    		       	                String numberOfMissingPlayerString = new String(packet2.getData(), 0, packet2.getLength());
	    		       	                
	    		    	                numberOfMissingPlayer = Integer.parseInt(numberOfMissingPlayerString);

	    		    	                
	    		       	                
	    		       	                try {
											thread.sleep(300);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
	            						
	            					}
	            					
	            					
	            				   game.setNumberOfPlayer(numberOfPlayer);
	      	            		   game.setPlayerIndex(playerIndex);
	      	            		   game.setNumberOfMissingPlayer(numberOfMissingPlayer);
	      	            		   game.setNumberLevel(numberLevel);
	      	            		   game.startGame();
	      	            		   
	            					
	      	            		   
	            				}
	            			}).start();
	            	   }
	            	               	
	                }
	                	
	            }
			}

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
	        
       
	}
	
	/**
	 * creazione client thread per la ricezione e aggiornamento dei dati durante la partita
	 * @param multiplayerScreen
	 */
	
	public void startThread(MultiplayerScreen multiplayerScreen) {
		
		thread = new ClientThread(address, portNewPlayer, datagramSocket, multiplayerScreen);
        thread.start();
	}
	
	/**
	 * chiusura connessione e stop thread
	 */
	public void stopConnection() {
		
		thread.close();
		datagramSocket.close();
	
	}
	

}