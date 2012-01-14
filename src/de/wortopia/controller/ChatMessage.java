package de.wortopia.controller;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wortopia.model.Channel;
import de.wortopia.view.View;

@SuppressWarnings("serial")
public class ChatMessage extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("application/json");
		
		int size = 4;
		
		Channel channel = new Channel(size);
		channel.sendMessage("{status:1, message:'"+req.getParameter("message")+"'}");
		
		resp.getWriter().println("{done:true}");
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
	
		resp.setContentType("application/json");
		
		int size = 4;
		
		Channel channel = new Channel(size);
		channel.sendMessage("{status:1, message:'"+req.getParameter("message")+"'}");
		
		resp.getWriter().println("{done:true}");
	
	}
}
