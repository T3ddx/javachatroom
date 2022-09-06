package com.teddx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/accept")
public class ClientAccepter extends HttpServlet{	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws UnknownHostException, IOException {
		HttpSession session = req.getSession();
		
		int numb = Integer.parseInt(req.getParameter("sid"));
		
		ServerSocket server = (ServerSocket)session.getAttribute("server" + numb);
		ArrayList<ServerDispatch> sockets = (ArrayList<ServerDispatch>)session.getAttribute("clients" + numb);
		Queue<String> unreadMSG = (Queue<String>)session.getAttribute("unreadMSG" + numb);
		ArrayList<Thread> clientThreads = new ArrayList<Thread>();
		
		
		while(true) {
			
			try {
				Socket client = server.accept();
				sockets.add(new ServerDispatch(client, sockets, req.getParameter("name"), unreadMSG, server));
				clientThreads.add(new Thread(sockets.get(sockets.size()-1)));
				clientThreads.get(clientThreads.size()-1).start();
				
			} catch (IOException | NullPointerException e) {
				break;
			}
		}
	}
}
