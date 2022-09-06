package com.teddx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/leaving")
public class ClientCloser extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		
		int numb = Integer.parseInt(req.getParameter("cid"));
		
		Socket client = (Socket)session.getAttribute("client" + numb);
		try {
			PrintWriter sender = new PrintWriter(client.getOutputStream(), true);
			String username = req.getParameter("name").substring(0, (req.getParameter("name").length() - 2));
				
			sender.println("*" + username + " has left.");
		} catch(NullPointerException e) {
			
		}
	}
}
