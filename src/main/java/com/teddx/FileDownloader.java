package com.teddx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class FileDownloader extends HttpServlet{
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		File chatLogs = new File(System.getProperty("user.home") + "/Desktop/ChatLogs");
		if(!chatLogs.exists()) {
			chatLogs.mkdir();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.home") + "/Desktop/ChatLogs", java.time.LocalDateTime.now() + "_ChatLog.txt")));
		
		writer.write(req.getParameter("chatlog"));
		
		writer.close();
	}
}
