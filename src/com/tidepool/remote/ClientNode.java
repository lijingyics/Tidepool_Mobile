package com.tidepool.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.tidepool.entities.Data;
import com.tidepool.entities.User;

import android.util.Log;


public class ClientNode {
	private static final int SERVERPORT = 5555;
	//private static final String SERVER_IP = "192.168.1.205"; //Village Lake
	private static final String SERVER_IP = "10.0.0.20"; //D19
	
	private static ClientNode singleton = null;
	private static ClientThread client = null;
	
	private String status = null;
	private String feedback = "";
	private String email;
	private String pwd;
	private User user = null;
	private ArrayList<User> friends = new ArrayList<User>();
	private ArrayList<Data> data = new ArrayList<Data>();
	
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
		status = null;
		client.closeSession();
		client = null;
		singleton = null;
	}
	
	/**
	 * Get the message from server.
	 * Need to pause for while waiting for update
	 * @return the feedback from server
	 */
	public String getFeedback() { 
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feedback; 
	}
	
	/**
	 * Get the user from server.
	 * Need to pause for while waiting for update
	 * @return this user
	 */
	public User getUser() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// while(!feedback.equals("success"));
		return user; 
	}
	
	/**
	 * Used for signin
	 * @param email
	 * @param pwd
	 */
	public void signin(String email, String pwd) {
		feedback = "";
		this.email = email;
		this.pwd = pwd;
		status = "signin";
	}

	/**
	 * Used for register
	 * @param user
	 */
	public void register(User user) {
		feedback = "";
		this.user = user;
		status = "register";
	}
	
	/**
	 * Used for update user
	 * @param user
	 */
	public void updateUser(User user) {
		feedback = "";
		this.user = user;
		status = "updateUser";
	}
	
	/**
	 * Return all data relevant to the current user
	 * @return data
	 */
	public ArrayList<Data> getData() {
		feedback = "";
		status = "receiveData";
		
		while(!feedback.equals("success"));
		
		return data;
	}
	
	/**
	 * Return all friends relevant to the current user
	 * @return
	 */
	public ArrayList<User> getFriends() {
		feedback = "";
		status = "receiveFriends";
		
		while(!feedback.equals("success"));

		return friends;
	}
	
	
	private class ClientThread implements Runnable {
		/**
		 * Socket Thread
		 */
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
	    		writer.writeObject("signout");
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
					else
						continue;
					if(status.equalsIgnoreCase("signout")) break;
					
					if(status!=null && status.equalsIgnoreCase("signin")) signin();
					if(status!=null && status.equalsIgnoreCase("register")) register();
					if(status!=null && status.equalsIgnoreCase("receiveData")) receiveData();
					if(status!=null && status.equalsIgnoreCase("receiveFriends")) receiveFriends();
					if(status!=null && status.equalsIgnoreCase("updateUser")) updateUser();
					/*if(status!=null && status.equalsIgnoreCase("chat")) sendMsgProcess();
					if(status!=null && status.equalsIgnoreCase("addFriend")) addFriend();
					if(status!=null && status.equalsIgnoreCase("deleteFriend")) deleteFriend();*/
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
				status = null;
				writer.writeObject("signin");
				String tmp = (String) reader.readObject();
				
				// Respond to email
				if(!tmp.equalsIgnoreCase("email")) {
					writer.writeObject("should respond email");
					return;
				}
				writer.writeObject(email);
				
				// Respond to wrong email
				tmp = (String) reader.readObject();
				Log.d("From server", tmp);
				if(tmp.equalsIgnoreCase("No such user!")) {
					feedback = "No such user!";
					return;
				}
				
				// Respond to pwd
				writer.writeObject(pwd);
				Object obj = reader.readObject();
				if(obj instanceof String) {
					feedback = "Wrong Password!";
					return;
				}
				
				// Get User
				user = (User) obj;
				feedback = "success";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void register() {
			try {
				status = null;
				writer.writeObject("register");
				String tmp = (String) reader.readObject();
				
				// Respond to server
				if(!tmp.equalsIgnoreCase("user")) {
					writer.writeObject("should respond user");
					return;
				}
				
				// Send user to server
				writer.writeObject(user);
				
				// Respond to duplicate user or error
				tmp = (String) reader.readObject();
				if(tmp.equalsIgnoreCase("Duplicate User")) {
					feedback = "error";
					return;
				}
				
				// Create new User successfully
				user.setId(Integer.parseInt(tmp));
				feedback = "success";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void updateUser() {
			try {
				status = null;
				writer.writeObject("updateUser");
				String tmp = (String) reader.readObject();
				
				// Respond to server
				if(!tmp.equalsIgnoreCase("update user")) {
					writer.writeObject("should respond update user");
					return;
				}
				
				// Send user to server
				writer.writeObject(user);
				
				// Respond to duplicate error and failure
				int res = (Integer) reader.readObject();
				if(res!=1) {
					feedback = "error";
					return;
				}
				
				// Update the user successfully
				feedback = "success";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void receiveData() {
			try {
				status = null;
				writer.writeObject("sendData");
				
				data = (ArrayList<Data>) reader.readObject();
				
				// Receive Data successfully
				feedback = "success";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void receiveFriends() {
			try {
				status = null;
				writer.writeObject("sendFriends");
				
				friends = (ArrayList<User>) reader.readObject();
				
				// Receive Data successfully
				feedback = "success";
				
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
