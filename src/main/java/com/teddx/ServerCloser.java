package com.teddx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/close")
public class ServerCloser extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		
		int numb = Integer.parseInt(req.getParameter("sid"));
		
		ServerSocket server = (ServerSocket)session.getAttribute("server" + numb);
		ArrayList<ServerDispatch> clients = (ArrayList<ServerDispatch>)session.getAttribute("clients" + numb);
		
		if(req.getParameter("nousers") == null) {
			if(server != null) {
				for(ServerDispatch s: clients){
					PrintWriter sender = new PrintWriter(s.getSocket().getOutputStream(), true);
					sender.println("Server Closing.");
					s.getSocket().close();
				}
			}
			
			try {
				server.close();
			} catch(NullPointerException e) {
			}
		} else {
			try {
				server.close();
			} catch(NullPointerException e) {
			}
		}
	}
}
