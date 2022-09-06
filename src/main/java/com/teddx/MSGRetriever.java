package com.teddx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/retrieve")
public class MSGRetriever extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
			
		if(req.getParameter("cid") != null) {
			try {
				int numb = Integer.parseInt(req.getParameter("cid"));
				
				Socket client = (Socket)session.getAttribute("client" + numb);
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = res.getWriter();
				
				String msg = reader.readLine();
				
				if(msg != null) {
					out.print(msg);
				}
			}catch(SocketException | NullPointerException e) {
			}
		}
	}
}
