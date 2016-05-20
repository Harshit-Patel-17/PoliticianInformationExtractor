/**
 * 
 */
package parsing;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Harshit
 *
 */
public class WikiParser {
	public static void main(String[] argv) throws IOException {
		System.out.println("Hello world !");
		File input = new File("politician_data/hillary_clinton/hillary_clinton.html");
		Document doc = Jsoup.parse(input, "UTF-8", "");
		Element body = doc.body();
		
		Elements headings = body.getElementsByTag("h3");
		for(Element heading : headings) {
			String text = heading.text();
			System.out.println(text);
		}
	}
}
