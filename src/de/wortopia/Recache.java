package de.wortopia;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wortopia.model.Words;

@SuppressWarnings("serial")
public class Recache extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		
		//resp.getWriter().println(Words.getInstance().size());
		
		Words.recache();
		
		//resp.getWriter().println(Words.getAllWords().size());
		
		resp.getWriter().println("done");
	}
}
	

