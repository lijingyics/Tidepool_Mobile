package com.tidepool.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.tidepool.entities.Data;
import com.tidepool.entities.User;

import android.util.Log;


public class ClientNode {
	private static final int SERVERPORT = 5555;
	//private static final String SERVER_IP = "10.0.23.122"; //Bldg 19 
	private static final String SERVER_IP = "192.168.1.205";
	
	private static ClientNode singleton = null;
	private static ClientThread client = null;
	
	private String status = null;
	private String feedback = "";
	private String email;
	private String pwd;
	private long uid;
	private String theRespond;
	private User user = null;
	private long friend_id = -1;
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
		user = null;
		friend_id = -1;
		friends = null;
		data = null;
		feedback = "";
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
			e.printStackTrace();
		}
		return user; 
	}
	
	public User getUser(long uid) {
		feedback = "";
		this.uid = uid;
		status = "receiveUser";
		
		while(!feedback.equals("success"));
		
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
	 * Return the data of the user
	 * @param user_id
	 * @return user data
	 */
	public ArrayList<Data> getData(long id) {
		feedback = "";
		status = "receiveData";
		friend_id = id;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	/**
	 * Return all friends relevant to the current user
	 * @return
	 */
	public ArrayList<User> getFriends() {
		feedback = "";
		status = "receiveFriends";
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * User send "add friend" request
	 * @param email
	 * @return whether success
	 */
	public String sendRequest(String email) {
		feedback = "";
		status = "sendRequest";
		this.email = email;
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return feedback;
	}
	
	/**
	 * User get "add friend" request
	 * @return senders
	 */
	public ArrayList<User> receiveRequest() {
		feedback = "";
		friends = new ArrayList<User>();
		status = "receiveRequest";
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * The user respond to the "add friend" request
	 * @param user
	 * @param r - respond, should be "admit" or "refuse"
	 * @return whether success
	 */
	public String sendRespond(long fId, String r) {
		feedback = "";
		friend_id = fId;
		theRespond = r;
		status = "sendRespond";
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return feedback;
	}
	
	/**
	 * The user get "add friend" respond
	 * @return friend
	 * return all column if "admit"
	 * return only username if "refuse"
	 */
	public ArrayList<User> receiveRespond() {
		feedback = "";
		friends = new ArrayList<User>();
		status = "receiveRespond";
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * Delete friend of the user
	 * @param friend email
	 * @return whether success
	 */
	public String deleteFriend(String email) {
		feedback = "";
		this.email = email;
		status = "deleteFriend";
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return feedback;
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
					if(status!=null && status.equalsIgnoreCase("receiveUser")) receiveUser();
					if(status!=null && status.equalsIgnoreCase("receiveFriends")) receiveFriends();
					
					// For account tab
					if(status!=null && status.equalsIgnoreCase("updateUser")) updateUser();
					
					// For chat room
					//if(status!=null && status.equalsIgnoreCase("chat")) sendMsgProcess();
					
					// For add contact
					if(status!=null && status.equalsIgnoreCase("sendRequest")) sendRequest();
					if(status!=null && status.equalsIgnoreCase("receiveRequest")) receiveRequest();
					if(status!=null && status.equalsIgnoreCase("sendRespond")) sendRespond();
					if(status!=null && status.equalsIgnoreCase("receiveRespond")) receiveRespond();
					if(status!=null && status.equalsIgnoreCase("deleteFriend")) deleteFriend();
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
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unchecked")
		public void receiveData() {
			try {
				status = null;
				writer.writeObject("sendData");
				
				writer.writeObject(friend_id);
				data = (ArrayList<Data>) reader.readObject();
				
				// Receive Data successfully
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public void receiveUser() {
			try {
				status = null;
				writer.writeObject("sendUser");
				String tmp = (String) reader.readObject();
				
				// Respond to server
				if(!tmp.equalsIgnoreCase("get uid")) {
					writer.writeObject("should respond get uid");
					return;
				}
				
				// Send user to server
				writer.writeObject(uid);
				
				// Respond to duplicate error and failure
				user = (User) reader.readObject();
				if(user == null) {
					feedback = "error";
					return;
				}
				
				// Update the user successfully
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unchecked")
		public void receiveFriends() {
			try {
				status = null;
				writer.writeObject("sendFriends");
				
				friends = (ArrayList<User>) reader.readObject();
				
				// Receive Data successfully
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public void sendRequest() {
			try {
				status = null;
				writer.writeObject("sendRequest");
				String tmp = (String) reader.readObject();
				
				if(!tmp.equalsIgnoreCase("friend email")) {
					writer.writeObject("should respond friend email");
					return;
				}
				
				// Send receiver email to server
				writer.writeObject(email);
				Object obj = reader.readObject();
				if(obj instanceof String) {
					feedback = "No such user!";
					return;
				}
				
				// Check whether send request successfully
				int res = (Integer) obj;
				if(res!=1) {
					feedback = "error";
					return;
				}
				
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unchecked")
		public void receiveRequest() {
			try {
				status = null;
				writer.writeObject("receiveRequest");
				
				friends = (ArrayList<User>) reader.readObject();

				// Receive Data successfully
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public void sendRespond() {
			try {
				status = null;
				writer.writeObject("sendRespond");
				String tmp = (String) reader.readObject();
				
				if(!tmp.equalsIgnoreCase("sender id")) {
					writer.writeObject("should respond sender id");
					return;
				}
				
				// Send sender id to server
				writer.writeObject(friend_id);
				tmp = (String) reader.readObject();
				
				if(!tmp.equalsIgnoreCase("respond")) {
					writer.writeObject("should respond respond");
					return;
				}
				
				// Get result
				writer.writeObject(theRespond);
				int res = (Integer) reader.readObject();
				if(res!=1) {
					feedback = "error";
					return;
				}
				
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unchecked")
		public void receiveRespond() {
			try {
				status = null;
				writer.writeObject("receiveRespond");
				
				friends = (ArrayList<User>) reader.readObject();
				
				// Receive Data successfully
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public void deleteFriend() {
			try {
				status = null;
				writer.writeObject("deleteFriend");
				String tmp = (String) reader.readObject();
				
				if(!tmp.equalsIgnoreCase("friend email")) {
					writer.writeObject("should respond friend email");
					tmp = (String) reader.readObject();
					return;
				}
				writer.writeObject(email);
				
				// Get respond
				tmp = (String) reader.readObject();
				feedback = "success";
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
