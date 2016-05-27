/**
 * 
 */
package wikipedia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.json.JSONObject;

/**
 * @author Harshit
 *
 */
public class WikiWriter {
	
	private String sanitizeText(String title) {
		return title.replaceAll("[^a-zA-Z ]", "").replaceAll("\\s+", " ").trim().toLowerCase();
	}
	
	public void writeAsJson(String outputFileName, JSONObject json, String politician) throws IOException {
		
		JSONObject jsonWithTitle = new JSONObject();
		jsonWithTitle.put(politician, json);
		File jsonFile = new File(outputFileName);
		if(!jsonFile.exists())
			jsonFile.createNewFile();
		
		FileWriter fileWriter = new FileWriter(jsonFile.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(jsonWithTitle.toString());
		bufferedWriter.close();
		
	}
	
	public void writeAsCsv(String outputFileName, JSONObject json, String politician) throws IOException {
		
		File csvFile = new File(outputFileName);
		if(!csvFile.exists())
			csvFile.createNewFile();
		
		PrintWriter printWriter = new PrintWriter(csvFile);
		Iterator<String> categories = json.keys();
		
		while(categories.hasNext()) {
			String category = categories.next();
			Iterator<String> topics = json.getJSONObject(category).keys();
			
			while(topics.hasNext()) {
				String topic = topics.next();
				if(json.getJSONObject(category).get(topic) instanceof JSONObject)
					printWriter.println(politician + "," + category + "," + topic);
			}
		}
		printWriter.close();
		
	}
	
	public void writeAsSanitizedContent(String outputFileName, JSONObject json, String politician) throws IOException {
		
		File sanitizedFile = new File(outputFileName);
		if(!sanitizedFile.exists())
			sanitizedFile.createNewFile();
		
		PrintWriter printWriter = new PrintWriter(sanitizedFile);
		Iterator<String> categories = json.keys();
		
		while(categories.hasNext()) {
			String category = categories.next();
			Iterator<String> topics = json.getJSONObject(category).keys();
			
			while(topics.hasNext()) {
				String topic = topics.next();
				if(json.getJSONObject(category).get(topic) instanceof JSONObject) {
					String sanitizedText = sanitizeText(json.getJSONObject(category).getJSONObject(topic).getString("text"));
					printWriter.println(sanitizedText);
				}
			}
		}
		printWriter.close();
		
	}

}
