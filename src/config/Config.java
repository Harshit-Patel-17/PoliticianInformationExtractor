/**
 * 
 */
package config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Harshit
 *
 * Read config.xml file and initialize static parameters
 */
public class Config {

	public static String PoliticianDataPath;
	
	public static void initConfig(String configFilePath) throws ParserConfigurationException, SAXException, IOException {
		
		File configFile = new File(configFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(configFile);
		
		PoliticianDataPath = doc.getElementsByTagName("politician_data_path").item(0).getTextContent();
	}
	
}
