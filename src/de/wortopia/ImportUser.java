package de.wortopia;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import de.marekventur.helper.PMF;
import de.wortopia.model.user.User;

@SuppressWarnings("serial")
public class ImportUser extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			FileInputStream fstream = new FileInputStream("csv/user.csv");
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
			Collection<User> users = new ArrayList<User>();
			while ((strLine = br.readLine()) != null) {
				String[] elements = strLine.split(",");
				for (int i=0; i<elements.length; i++)
					elements[i] = elements[i].replace("\"", "");
				//resp.getWriter().println(strLine);
				
				try {
					User user = new User(Integer.parseInt(elements[0]));
					user.setUserId(elements[1]);
					user.setServiceShortname(elements[1].substring(0,2));
					
					user.setEmail(elements[2]);
					user.setLink(elements[3]);
					user.setImageURL(elements[4]);
					user.setUsernameNoCheck(elements[4]);
					user.setPasswordHash(elements[6]);
					user.setTeamName(elements[7]);
					user.setAdmin(elements[8].equals("1"));
					user.setBio(elements[9]);
					
					users.add(user);
				} catch (Exception e) {
					
				}
				
				//Word word = new Word(elements[0].toUpperCase(), elements[1].equals("1"), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
				
				//Entity wordEntity = new Entity("Word", word.getWord());
				//wordEntity.setProperty("accept", word.isAccept());
				//wordEntity.setProperty("userAsk", word.getUserAskId());
				//wordEntity.setProperty("userCheck", word.getUserCheckId());
				
				//datastore.put(wordEntity);
				
				if(users.size() == 20) {
					pm.makePersistentAll(users);
					users.clear();
					if(req.getParameter("test")!=null)
						System.exit(0);
					resp.getWriter().println(j++);
					
				}
				
				
			}
			pm.makePersistentAll(users);
			
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}
}
	

