/**
 * 
 */
package parsing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

/**
 * @author Harshit
 * 
 * From HTML file downloaded from Wikipedia.org, create JSON of headings and contents.
 * JSON format is 
 * {
 * 	<topic1>: 
 * 	{
 * 		text: <text>, 
 * 		<subtopic1>: {text: <text>}, 
 * 		...
 * 	}, 
 * 	...
 * }
 */
public class WikiParser {
	
	private enum FSM_state {Start, Construct_Sub_Section, Construct_Section, None};
	private enum FSM_transition {H2, H3, P, None};
	
	private FSM_state mCurrentState;
	private Element mCurrentElement;
	private JSONObject mCurrentSectionJson;
	private String mCurrentSectionTitle;
	private JSONObject mCurrentSubSectionJson;
	private String mCurrentSubSectionTitle;
	
	private JSONObject mPageJson;
	
	private void initParsingVariables() {
		mCurrentState = FSM_state.Start;
		mCurrentElement = null;
		mCurrentSectionJson = null;
		mCurrentSectionTitle = null;
		mCurrentSubSectionJson = null;
		mCurrentSubSectionTitle = null;
		mPageJson = new JSONObject();
	}
	
	private void createNewSection(Element element) {
		mCurrentSectionTitle = element.text();
		mCurrentSectionJson = new JSONObject();
		mCurrentSectionJson.put("text", "");
	}
	
	private void createNewSubSection(Element element) {
		mCurrentSubSectionTitle = element.text();
		mCurrentSubSectionJson = new JSONObject();
		mCurrentSubSectionJson.put("text", "");
	}
	
	private void appendTextInSection(Element element) {
		String currentText = mCurrentSectionJson.getString("text");
		currentText += element.text();
		mCurrentSectionJson.put("text", currentText);
	}
	
	private void appendTextInSubSection(Element element) {
		String currentText = mCurrentSubSectionJson.getString("text");
		currentText += element.text();
		mCurrentSubSectionJson.put("text", currentText);
	}
	
	private void FSM_Transition(FSM_transition transition, Element element) {
		switch(mCurrentState) {
		
		case Start:
			
			switch(transition) {
			
			case H2:
				mCurrentState = FSM_state.Construct_Section;
				createNewSection(element);
				break;
				
			default:
				break;
					
			}
			
			break;
			
		case Construct_Section:
			
			switch(transition) {
			
			case H2:
				mCurrentState = FSM_state.Construct_Section;
				mPageJson.put(mCurrentSectionTitle, mCurrentSectionJson);
				createNewSection(element);
				break;
				
			case H3:
				mCurrentState = FSM_state.Construct_Sub_Section;
				createNewSubSection(element);
				break;
				
			case P:
				mCurrentState = FSM_state.Construct_Section;
				appendTextInSection(element);
				break;
				
			default:
				break;
			
			}
			
			break;
			
		case Construct_Sub_Section:
			
			switch(transition) {
			
			case H2:
				mCurrentState = FSM_state.Construct_Section;
				mCurrentSectionJson.put(mCurrentSubSectionTitle, mCurrentSubSectionJson);
				mPageJson.put(mCurrentSectionTitle, mCurrentSectionJson);
				createNewSection(element);
				break;
				
			case H3:
				mCurrentState = FSM_state.Construct_Sub_Section;
				mCurrentSectionJson.put(mCurrentSubSectionTitle, mCurrentSubSectionJson);
				createNewSubSection(element);
				break;
				
			case P:
				mCurrentState = FSM_state.Construct_Sub_Section;
				appendTextInSubSection(element);
				break;
				
			default:
				break;
			
			}
			
			break;
			
		default:
			break;
			
		}
		
	}
	
	public JSONObject parse(String fileName) throws IOException {
		
		File input = new File(fileName);
		Document doc = Jsoup.parse(input, "UTF-8", "");
		Elements bodyElements = doc.body().select("*");
		
		initParsingVariables();
		
		for(Element element : bodyElements) {
			
			FSM_transition transition = FSM_transition.None;
			
			if(element.nodeName().equalsIgnoreCase("h2"))
				transition = FSM_transition.H2;
			else if(element.nodeName().equalsIgnoreCase("h3"))
				transition = FSM_transition.H3;
			else if(element.nodeName().equalsIgnoreCase("p"))
				transition = FSM_transition.P;
			
			FSM_Transition(transition, element);
			
		}
		
		//Update Sub-Section json and Section json based on the last state
		switch(mCurrentState) {
		
		case Construct_Section:
			mPageJson.put(mCurrentSectionTitle, mCurrentSectionJson);
			break;
			
		case Construct_Sub_Section:
			mCurrentSectionJson.put(mCurrentSubSectionTitle, mCurrentSubSectionJson);
			mPageJson.put(mCurrentSectionTitle, mCurrentSectionJson);
			break;
		
		default:
			break;
			
		}
		
		return mPageJson;
	}
	
	private boolean clean(JSONObject json) {
		
		boolean isSectionEmpty = true;
		
		Iterator<String> keys = json.keys();
		ArrayList<String> keysToRemove = new ArrayList<String>();
		
		while(keys.hasNext()) {
			
			String key = keys.next();
			if(json.get(key) instanceof JSONObject) {
				
				boolean isSubSectionEmpty = clean(json.getJSONObject(key));
				if(isSubSectionEmpty)
					keysToRemove.add(key);
				isSectionEmpty = (isSectionEmpty && isSubSectionEmpty);
				
			} else if (json.get(key) instanceof String) {
				
				boolean isTextEmpty = json.getString(key).isEmpty();
				isSectionEmpty = (isSectionEmpty && isTextEmpty);
				
			}
			
		}
		
		for(String key : keysToRemove)
			json.remove(key);
		
		return isSectionEmpty;
		
	}

	public JSONObject parseAndClean(String fileName) throws IOException {
		
		parse(fileName);
		clean(mPageJson);
		
		return mPageJson;
		
	}
	
}
