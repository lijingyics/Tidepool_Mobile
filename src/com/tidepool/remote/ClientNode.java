package com.tidepool.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;


public class ClientNode {
	private static final int SERVERPORT = 5555;
	private static final String SERVER_IP = "192.168.1.205";
	
	private Socket socket;
	private ObjectInputStream reader = null;
    private ObjectOutputStream writer = null;

	public ClientNode() {
		new Thread(new ClientThread()).start();
	}
	
	public void closeSession(){
    	try {
    		writer = null;
    		reader = null;
    		socket.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

	public Object receiveMsg() {
		try {
			return reader.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void sendMsg(Object obj) {
		try {
			writer.writeObject(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ClientThread implements Runnable {
		
		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

				socket = new Socket(serverAddr, SERVERPORT);

				Log.d("Here:", "bug 3");
				reader = new ObjectInputStream( socket.getInputStream() );
				Log.d("Here:", "bug 4");
				writer = new ObjectOutputStream( socket.getOutputStream() );
				Log.d("Here:", "bug 5");
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
}
