package Model.Core.Multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Model.Core.MultiplayerScreen;
import Model.Core.Screen;
import Model.Logic.Player;

public class ClientThread extends Thread {
    private DatagramSocket socket;
    private byte[] buf=new byte[4096];
    private String message;
   //private Player player1;
    private int serverPort;
    private DatagramSocket datagramSocket;
    private MultiplayerScreen screen;

    public ClientThread(InetAddress address, int port,  DatagramSocket socket, MultiplayerScreen screen) {
        message = "";
        this.socket = socket;
        this.serverPort=port;
        socket.connect(address, serverPort);
        //this.player1=player1;
        this.datagramSocket=socket;
        this.screen=screen;
        
    }

    /**
     * Metodo che viene chiamato ogni volta che parte il Thread
     */
    synchronized public void run() {
    	
        while (true) {
        	byte[] b = new byte[1024];
        	b = (((Integer) screen.getPaddleXPosition()).toString()+" "+((Integer) screen.getPaddleYPosition()).toString()).getBytes();
        	
        	DatagramPacket packetBack;
			try {
				packetBack = new DatagramPacket(b, b.length, InetAddress.getByName("202.61.250.68"), serverPort);
				datagramSocket.send(packetBack);
				wait(10);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public String getMessage() {
        return message;
    }


}