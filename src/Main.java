import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import parsing.WikiParser;;

public class Main {

	public static void main(String[] args) throws IOException {
		
		WikiParser wikiParser = new WikiParser();
		JSONObject page = wikiParser.parse("politician_data/hillary_clinton/hillary_clinton.html");
		
		File file = new File("politician_data/hillary_clinton/hillary_clinton.json");
		if(!file.exists())
			file.createNewFile();
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(page.toString());
		bufferedWriter.close();
	}

}
