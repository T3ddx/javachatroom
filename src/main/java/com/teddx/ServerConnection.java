package com.teddx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

@WebServlet("/server")
public class ServerConnection extends HttpServlet{
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		PrintWriter out = res.getWriter();
		
		session.setMaxInactiveInterval(15*60);
		
		try {
			ServerSocket server = new ServerSocket(Integer.parseInt(req.getParameter("port")));
			ArrayList<ServerDispatch> clients = new ArrayList<ServerDispatch>();
			Queue<String> unreadMSG = new LinkedList<>();
			
			int numb = -1;
			if(session.getAttribute("sid") == null) {
				session.setAttribute("sid", 1);
				numb = 1;
			} else {
				numb = Integer.parseInt(session.getAttribute("sid") + "");
				numb++;
				session.setAttribute("sid", numb);
			}
			
			session.setAttribute("clients" + numb, clients);
			session.setAttribute("server" + numb, server);	
			session.setAttribute("unreadMSG" + numb, unreadMSG);
			out.print(numb);
		} catch(java.net.SocketException e) {
			out.print("error");
		}
		
	}
}
