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
	
	private static ClientNode singleton = null;
	private static ClientThread client = null;
	
	private String status = null;
	private String feedback = null;
	private String email;
	private String pwd;
	
	private ClientNode() {
		client = new ClientThread();
		new Thread(client).start();
	}
	
	public static ClientNode getInstance() {
		if(singleton==null) {
			singleton = new ClientNode();
		}
		return singleton;
	}
	
	public void close() {
		client = null;
		singleton = null;
	}
	
	/**
	 * Get the message from server.
	 * Need to pause for while waiting for update
	 * @return the feedback from server
	 */
	public String getFeedback() { return feedback; }
	
	public void signin(String email, String pwd) {
		status = "signin";
		this.email = email;
		this.pwd = pwd;
	}

	private class ClientThread implements Runnable {
		private Socket socket;
		private ObjectInputStream reader = null;
	    private ObjectOutputStream writer = null;
	    
	    public void startSession() throws IOException {
	    	InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			socket = new Socket(serverAddr, SERVERPORT);

			reader = new ObjectInputStream( socket.getInputStream() );
			writer = new ObjectOutputStream( socket.getOutputStream() );
	    	
	    }
	    
	    public void closeSession() {
	    	try {
	    		writer = null;
	    		reader = null;
	    		socket.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    }
	    
		@Override
		public void run() {

			try {
				startSession();
				
				while (!Thread.currentThread().isInterrupted()) {
					if(status!=null)
						Log.d("From client", status);
					if(status.equalsIgnoreCase("signout")) break;
					
					if(status.equalsIgnoreCase("signin")) signin();
					if(status.equalsIgnoreCase("register")) register();
					if(status.equalsIgnoreCase("sendData")) sendData();
					if(status.equalsIgnoreCase("sendFriends")) sendFriends();
					if(status.equalsIgnoreCase("chat")) sendMsgProcess();
					if(status.equalsIgnoreCase("addFriend")) addFriend();
					if(status.equalsIgnoreCase("deleteFriend")) deleteFriend();
				}
				
				Log.d("Close communication", "signout");
				closeSession();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		
		public void signin() {
			try {
				writer.writeObject("signin");
				String tmp = (String) reader.readObject();
				
				// Respond to email
				if(!tmp.equalsIgnoreCase("email")) {
					writer.writeObject("should respond email");
					status = null;
					return;
				}
				writer.writeObject(email);
				
				// Respond to wrong email
				tmp = (String) reader.readObject();
				if(tmp.equalsIgnoreCase("No such user!")) {
					feedback = "No such user!";
					status = null;
					return;
				}
				
				// Respond to pwd
				writer.writeObject(pwd);
				Object read = reader.readObject();
				if(read instanceof String) {
					feedback = "Wrong Password!";
					status = null;
					return;
				}
				
				// Get User
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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

	}
}
