package de.wortopia.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Used to create and check signatures
 * Only works during the same memcache session
 * @author marek
 *
 */
public class Signer {	
	// Keep salt secret
	private String salt = null;
	
	private String getSalt() {
		// No salt? 
		if (salt == null) {
			
			// We store the salt in memcache
			MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
			
			String key = "signerSalt";			
			
			// Is there a salt in memcache?
			if (cache.contains(key)) {
				
				// Yes? Get it then
				salt = (String)cache.get(key);
			}
			else
			{
				// No? No worries, we create a new one...
				Random randomGenerator = new Random();
				salt = String.valueOf(randomGenerator.nextLong());
				
				// Write to memcache
				cache.put(key, salt);
			}
		}
		
		return String.valueOf(salt);
	}
	
	
	private String hash(String value) {
		try {			
			// Generate a SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] mdbytes = md.digest(value.getBytes());
			
			// Convert byte array to String
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	        	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        // We're done
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	
	public String sign(String ... values) {
		// String builder is faster than String
		StringBuilder payload = new StringBuilder();
		
		// Loop over values
		for (String value: values) {
			// We hash every value to make sure that ["abc", "d"] generates something different than ["ab", "cd"]
			payload.append(hash(value));
		}
		
		// Add salt
		payload.append(getSalt());
		
		// Hash again
		return hash(payload.toString());
	}
	
	public boolean check(String signature, String ... values) {
		// Sign values again and compare to signature
		return sign(values).equals(signature);
	}
}