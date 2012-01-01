package de.wortopia;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.wortopia.model.Field;
import de.wortopia.model.Word;
import de.wortopia.model.Words;

@SuppressWarnings("serial")
public class WortopiaGAEServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		Collection<Word> words = Words.getAllWords();
		resp.getWriter().println(words.size());
		
		Field field = Field.fetchField(1, 4);
		
		field.processWords(words);
		
		for(Word word: field.getWords()) {
			resp.getWriter().println(word);
		}
	}
}
