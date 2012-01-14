package de.wortopia.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import de.wortopia.exceptions.*;

public class Words {
	private static Collection<Word> allWordsList = null;
	
	private static final String MEMCACHE_KEY = "words";
	
	public static Collection<Word> getAllWords() {
		if (allWordsList == null) {
			MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
			if (cache.contains(MEMCACHE_KEY)) 
				allWordsList = (Collection<Word>)cache.get(MEMCACHE_KEY);
			else 
				throw new NoWordListInMemcache();
		}
		return allWordsList;
	}
	
	public static Collection<Word> getMockWords(String ... values) {
		Collection<Word> list = new ArrayList<Word>();
		for (String value: values)
			list.add(new Word(value.toUpperCase()));
		return list;
	}
	
	public static void recache() {
		Collection<Word> list = new ArrayList<Word>();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//FetchOptions options = FetchOptions.Builder.withDefaults().prefetchSize(10000).chunkSize(10000);
		Query query = new Query("Word");
		
		Iterator<Entity> iterator = datastore.prepare(query).asIterator();
		Entity entity;
		try {
			while((entity = iterator.next()) != null) {
				list.add(new Word(entity.getKey().getName()));
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}	
	
		//Object[] array = list.toArray();
		
		System.out.println(list.size());
		
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		//cache.put(MEMCACHE_KEY, "sdfsdf", Expiration.byDeltaSeconds(60 * 60));
		//list.clear();
		cache.put(MEMCACHE_KEY, list, Expiration.byDeltaSeconds(60 * 60));
	}	
}
