package de.wortopia.model.user;

import java.util.Random;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class AnonUser extends User {
	public static AnonUser getNewAnonUser() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		Random randomGenerator = new Random();
		int randomId = randomGenerator.nextInt(9998)+1;
		
		while (cache.contains("guest:"+String.valueOf(randomId)))
			randomId = randomGenerator.nextInt(9998)+1;
		
		return getAnonUserById(randomId);
	}
	
	public static AnonUser getAnonUserById(long id) {
		if (id<0)
			id = -id;
		
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		String key = "guest:"+String.valueOf(id);
		
		AnonUser user;
		if (cache.contains(key)) {
			user = (AnonUser)cache.get(key);
		}
		else
		{
			user = new AnonUser(id);
			cache.put(key, user, Expiration.byDeltaSeconds(24*60*60));
		}
		return user;
	}
	
	public AnonUser() {
		super(0);
		Random randomGenerator = new Random();
		int randomId = randomGenerator.nextInt(9998)+1;
		id = (long)randomId*(-1);
		username = "Gast "+String.valueOf(randomId);
	}
	
	public AnonUser(long id) {
		super(id);
		username = "Gast "+String.valueOf(-id);
	}

	public boolean setPassword(String password) {
		return false;
	}
	
	public boolean checkPassword(String password) {
		return false;
	}
	
	public boolean isAuthenticated() {
		return false;
	}
	
	public boolean isAdmin() {
		return false;
	}
	
	public String getEmail() {
		return null;
	}
}
