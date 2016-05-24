import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import config.Config;
import parsing.WikiParser;

public class Main {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		Config.initConfig(args[0]);
		
		String srcFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".html";
		String dstJsonFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".json";
		String dstCsvFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".csv";
		
		WikiParser wikiParser = new WikiParser();
		JSONObject page = wikiParser.parseAndClean(srcFileName);
		
		//Write to JSON file
		File jsonFile = new File(dstJsonFileName);
		if(!jsonFile.exists())
			jsonFile.createNewFile();
		
		FileWriter fileWriter = new FileWriter(jsonFile.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(page.toString());
		bufferedWriter.close();
		
		//Write to CSV file
		File csvFile = new File(dstCsvFileName);
		if(!csvFile.exists())
			csvFile.createNewFile();
		
		PrintWriter printWriter = new PrintWriter(csvFile);
		Iterator<String> categories = page.keys();
		
		while(categories.hasNext()) {
			String category = categories.next();
			Iterator<String> topics = page.getJSONObject(category).keys();
			
			while(topics.hasNext()) {
				String topic = topics.next();
				if(page.getJSONObject(category).get(topic) instanceof JSONObject)
					printWriter.println(args[1] + "," + category + "," + topic);
			}
		}
		printWriter.close();
	}

}
