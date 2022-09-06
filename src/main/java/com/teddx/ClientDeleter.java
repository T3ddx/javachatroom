package com.teddx;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/delete")
public class ClientDeleter extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		
		int numb = Integer.parseInt(req.getParameter("cid"));
		
		Socket client = (Socket)session.getAttribute("client" + numb);
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch(NullPointerException | IllegalStateException e) {
			
		}
	}
}
