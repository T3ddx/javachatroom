package com.teddx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/send")
public class MSGSender extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		
		int numb = Integer.parseInt(req.getParameter("cid"));
		if(session.getAttribute("client" + numb) != null) {
			Socket client = (Socket)session.getAttribute("client" + numb);
			PrintWriter serverSender = new PrintWriter(client.getOutputStream(), true);
			
			serverSender.println(req.getParameter("msg"));
		} else {
			PrintWriter out = res.getWriter();
			out.print("error");
		}
	}
}
