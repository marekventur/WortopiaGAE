package de.wortopia;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import de.wortopia.model.Word;


@SuppressWarnings("serial")
public class InitialLoading extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		try {
			FileInputStream fstream = new FileInputStream("csv/words.csv");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			/*Query query = new Query("Word");
			List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
			for(Entity entity: entities) {
				resp.getWriter().println(entity.getKey().getName());
				datastore.delete(entity.getKey());
			}
			
			
			datastore.delete();
			
			System.exit(0);
	          */
			
			int j = 0; 
			
			br.readLine();
			while ((strLine = br.readLine()) != null) {
				String[] elements = strLine.split(",");
				for (int i=0; i<elements.length; i++)
					elements[i] = elements[i].replace("\"", "");
				resp.getWriter().println(strLine);
				
				Word word = new Word(elements[0].toUpperCase(), elements[1].equals("1"), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
				
				Entity wordEntity = new Entity("Word", word.getWord());
				wordEntity.setProperty("accept", word.isAccept());
				wordEntity.setProperty("userAsk", word.getUserAskId());
				wordEntity.setProperty("userCheck", word.getUserCheckId());
				
				datastore.put(wordEntity);
				
				if(j++==1000 && (req.getParameter("test")!=null))
					System.exit(0);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
	

