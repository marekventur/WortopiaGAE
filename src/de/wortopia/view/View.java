package de.wortopia.view;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class View {
	private Map<String, Object> data;
	private Template temp;
	
	public View(String template) {
		try {
			// Config
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File("template/"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());  
			
			// Instantiate data
			data = new HashMap<String, Object>();
			
			// Template
			//temp = cfg.getTemplate(template);
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public void assign(String key, Object value) {
		data.put(key, value);
	}
	
	public String processAsString() {
		StringWriter sw = new StringWriter();
		try {
			temp.process(data, sw);
			sw.flush();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sw.toString();
	}
	
	public void process(Writer out) {
		try {
			temp.process(data, out);
			out.flush();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
