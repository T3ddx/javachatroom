package com.teddx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

import javax.servlet.http.HttpSession;

public class ServerDispatch implements Runnable{
	private Socket client;
	private ArrayList<ServerDispatch> clients;
	private String name;
	private Queue<String> unreadMSG;
	private BufferedReader reader;
	private PrintWriter sender;
	private ServerSocket server;
	
	public ServerDispatch(Socket s, ArrayList<ServerDispatch> sd, String n, Queue<String> urm, ServerSocket ss) throws IOException {
		client = s;
		clients = sd;
		name = n;
		unreadMSG = urm;
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		sender = new PrintWriter(client.getOutputStream(), true);
		server = ss;
	}
	
	public Socket getSocket() {
		return client;
	}
	
	public void removeBySocket(Socket s) throws IOException {
		for(ServerDispatch d: clients) {
			if(d.getSocket() == s) {
				d.getSocket().close();
				clients.remove(d);
				break;
			}
		}
	}

	public void sendToOthers(String send) throws IOException {
		for(ServerDispatch s: clients){
			if(s.getSocket() != client) {
				PrintWriter sender = new PrintWriter(s.getSocket().getOutputStream(), true);
				sender.println(send);
			}
		}
	}
	
	@Override
	public void run(){
		sender.println(name);
		
		while(true) {
			try {
				String msg = reader.readLine();
				
				if(msg.substring(0,1).equals("*")) {
					msg = msg.substring(1);
					sendToOthers(msg);
					unreadMSG.add(msg);
					if(clients.size() == 1) {
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						server.close();
					}
					removeBySocket(client);
					break;
				} else {
					sendToOthers(msg);
					unreadMSG.add(msg);
				}
			} catch(IOException | NullPointerException e) {
				break;
			}
		}
		
	}

}
