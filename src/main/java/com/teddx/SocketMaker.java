package com.teddx;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


@WebServlet("/socket")
public class SocketMaker extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		
		session.setMaxInactiveInterval(15*60);
		
		try {
			Socket client = new Socket("127.0.0.1", Integer.parseInt(req.getParameter("port")));
			
			int numb = -1;
			if(session.getAttribute("cid") == null) {
				numb = 1;
				session.setAttribute("cid", numb);
			} else {
				numb = Integer.parseInt(session.getAttribute("cid") + "");
				numb++;
				session.setAttribute("cid", numb);
			}
			
			
			session.setAttribute("client" + numb, client);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = res.getWriter();
			PrintWriter sender = new PrintWriter(client.getOutputStream(), true);
			String name = reader.readLine();
			out.println(name + "*" + numb);
			
			String username = req.getParameter("name").substring(0, (req.getParameter("name").length() - 2));
			sender.println(username + " has joined.");
		} catch(java.net.SocketException e) {
			PrintWriter out = res.getWriter();
			out.print("error");
		}

	
		
		
	}

}
