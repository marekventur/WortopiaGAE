package de.wortopia.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.mortbay.util.ajax.JSON;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.marekventur.helper.PMF;
import de.wortopia.exceptions.UserNotFoundException;
import de.wortopia.exceptions.UsernameException;

@PersistenceCapable
public class User implements Serializable {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id;
    
	@Persistent
	protected String email = null;
	
	@Persistent
	protected String username = null;
	
	@Persistent
	protected String group = null;
	
	@Persistent
	protected boolean admin = false;
	
	@Persistent
	protected String autoLoginToken = null;
	
	@Persistent
	protected String passwordHash = null;
	
	// Used in the old version
	@Persistent
	protected String userId = null;
	
	@Persistent
	protected String serviceShortname = null;
	
	@Persistent
	protected String imageURL = null;
	
	@Persistent
	protected String link = null;
	
	@Persistent
	protected String bio = null;
	
	@Persistent
	protected String teamName = null;
	
	public String asJSON() {
	
		// Exclude passwordHash
		Gson gson = new GsonBuilder()
        .setExclusionStrategies(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipClass(Class<?> c) {
				return false;
			}

			@Override
			public boolean shouldSkipField(FieldAttributes a) {
				return a.getName().equals("passwordHash");
			}
        	
        })
        .serializeNulls()
        .create();
		
		return gson.toJson(this);
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public User(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}

	public void setUsername(String username) throws UsernameException {
		if (username.length() < 3)
			throw new UsernameException(UsernameException.Type.TOO_SHORT);
		if (username.length() > 20)
			throw new UsernameException(UsernameException.Type.TOO_LONG);
		if (username.matches("^[a..zA..Z01..9]$")) 
			throw new UsernameException(UsernameException.Type.INVALID_CHARACTER);
		if (!isUsernameAvailable(username)) 
			throw new UsernameException(UsernameException.Type.ALREADY_USED);

		this.username = username;
	}
	
	public void setUsernameNoCheck(String username) {
		this.username = username;
	}
	
	/**
	 * Set password
	 * @param password
	 * @return true if successful, false otherwise
	 */
	public boolean setPassword(String password) {
		if (password.length() < 6)
			return false;
		passwordHash = hashPassword(password);
		return true;
	}
	
	public boolean checkPassword(String password) {
		return passwordHash.equals(hashPassword(password));
	}
	
	public static String hashPassword(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			return new String(md.digest(password.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public String getService() {
		return null;
	}
	
	/**
	 * @return A short identifier, should match /^[a..z]{2}$/ and be unique 
	 */
	public String getServiceShortName() {
		return serviceShortname;
	}
	
	public boolean isAuthenticated() {
		return id > 0;
	}
	
	
	public boolean isAdmin() {
		return admin;
	}
	
	public String getAutoLoginToken() {
		return autoLoginToken;
	}

	public void setAutoLoginToken(String autoLoginToken) {
		this.autoLoginToken = autoLoginToken;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/** 
	 * Checks if a name is still available
	 * @param username
	 * @return true if available, false otherwise
	 */
	public static boolean isUsernameAvailable(String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(User.class);
	    query.setFilter("username == givenUsername");
	    query.declareParameters("String givenUsername");
	    query.setRange(0, 1);
	    
	    boolean result = false;
	    try {
	        List<User> list = (List<User>) query.execute(username);
	        result = list.isEmpty();
	    } finally {
	        query.closeAll();
	    }
	    
	    return result;
	}
	
	/** 
	 * Returns a user for a given email adress
	 * @param email
	 * @return User object or null if none is found
	 */
	public static User getUserByEmail(String email) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(User.class);
	    query.setFilter("email == givenEmail");
	    query.declareParameters("String givenEmail");
	    query.setRange(0, 1);
	    
	    User result = null;
	    try {
	        List<User> list = (List<User>) query.execute(email);
	        result = list.get(0);
	    } finally {
	        query.closeAll();
	    }
	    
	    return result;
	}
	
	/** 
	 * Returns a user for a given username
	 * @param username 
	 * @return User object or null if none is found
	 */
	public static User getUserByUsername(String username) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(User.class);
	    query.setFilter("username == givenUsername");
	    query.declareParameters("String givenUsername");
	    query.setRange(0, 1);
	    
	    User result = null;
	    try {
	        List<User> list = (List<User>) query.execute(username);
	        result = list.get(0);
	    } finally {
	        query.closeAll();
	    }
	    
	    return result;
	}
	
	public static User getUserById(long id) throws UserNotFoundException {
		// Guests have Id's below 0
		if (id < 0) { 
			// Guests are only stored in memcache
			MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
			
			// guestId is just the negatice value of id
			long guestId = -id;
			
			// Userids over 9999 are disallowed
			if (guestId > 9999)
				throw new UserNotFoundException();
			
			// This is the key for guest in memcache
			String key = "guest:"+String.valueOf(guestId);
			
			// Let's try to get one!
			if (cache.contains(key)) {
				
				// We're lucky, there is already one in cache
				return (User)cache.get(key);
			}
			else
			{
				
				// Oh, no cache? Don't worry, we create a new one with this id!
				User user = new User(id);
				
				// We also have to set a appropriate username.
				// Warning: Might need to be localized
				try {
					user.setUsername("Gast "+String.valueOf(guestId));
				} catch (UsernameException e) {
					e.printStackTrace();
				}
				
				// Put it in memcache for further use
				// Cache time: 1h
				cache.put(key, user, Expiration.byDeltaSeconds(24*60*60));
				
				return user;
			}
			
		} 
		else 
		{
			// This seems to be a normal user, so let's fetch it from the database
			PersistenceManager pm = PMF.get().getPersistenceManager();
		
			// TODO: Catch exception if user is not found
			return pm.getObjectById(User.class, id);
				
		}
	}
	
	/**
	 * Create a new anon user, make sure he is not currently used
	 * @return
	 * @throws  
	 */
	public static User getNewAnonUser() {
	
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		Random randomGenerator = new Random();
		
		// Pick a random number between 1 and 9999
		int guestId = randomGenerator.nextInt(9998)+1; 
		
		// Check if this number is still available, otherwise pick another one
		while (cache.contains("guest:"+String.valueOf(guestId)))
			guestId = randomGenerator.nextInt(9998)+1; 
		
		try {
			
			// Create this user
			return getUserById(-guestId);
		} catch (UserNotFoundException e) {
			 
			// Erm... We should never get here
			System.err.println("This should never have happend");
			e.printStackTrace();
			return null;
		}
		 
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceShortname() {
		return serviceShortname;
	}

	public void setServiceShortname(String serviceShortname) {
		this.serviceShortname = serviceShortname;
	}
	
	/*public String getChannelToken(int size) {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return channelService.createChannel(String.valueOf(userId)+"@"+String.valueOf(size));
	}*/

}