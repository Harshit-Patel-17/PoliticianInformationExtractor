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
import wikipedia.WikiParser;
import wikipedia.WikiWriter;

public class Main {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		Config.initConfig(args[0]);
		
		String srcFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".html";
		String dstJsonFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".json";
		String dstCsvFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + ".csv";
		String dstSanitizedFileName = Config.PoliticianDataPath + args[1] + "/" + args[1] + "_sanitized.txt";
		
		WikiParser wikiParser = new WikiParser();
		JSONObject page = wikiParser.parseAndClean(srcFileName);
		
		WikiWriter wikiWriter = new WikiWriter();
		wikiWriter.writeAsJson(dstJsonFileName, page, args[1]);
		wikiWriter.writeAsCsv(dstCsvFileName, page, args[1]);
		wikiWriter.writeAsSanitizedContent(dstSanitizedFileName, page, args[1]);
		
	}

}
