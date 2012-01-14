package de.wortopia.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wortopia.model.Field;
import de.wortopia.model.Word;
import de.wortopia.model.Words;
import de.wortopia.view.View;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

@SuppressWarnings("serial")
public class Index extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		View view = new View("index.html");
		
		view.process(resp.getWriter());
		
		/*Collection<Word> words = Words.getAllWords();
		resp.getWriter().println(words.size());
		
		Field field = Field.fetchField(1, 4);
		
		field.processWords(words);
		
		for(Word word: field.getWords()) {
			resp.getWriter().println(word);
		}*/
		
		
	}
}
