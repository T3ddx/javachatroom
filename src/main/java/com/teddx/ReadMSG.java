package com.teddx;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/getmsg")
public class ReadMSG extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession session = req.getSession();
		PrintWriter out = res.getWriter();
		
		int numb = Integer.parseInt(req.getParameter("sid"));
		
		Queue<String> unreadMSG = (Queue<String>)session.getAttribute("unreadMSG" + numb);
			
		if(unreadMSG.size() > 0) {
			while(unreadMSG.size() > 0) {
				out.print(unreadMSG.poll());
			}
		} else {
			out.print("***");
		}
	}
}
