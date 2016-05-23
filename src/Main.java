import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import config.Config;
import parsing.WikiParser;

public class Main {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		Config.initConfig(args[0]);
		
		String srcFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".html";
		String dstFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".json";
		
		WikiParser wikiParser = new WikiParser();
		JSONObject page = wikiParser.parseAndClean(srcFileName);
		
		File file = new File(dstFileName);
		if(!file.exists())
			file.createNewFile();
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(page.toString());
		bufferedWriter.close();
	}

}
