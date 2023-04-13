package spider2;

import java.util.Set;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.util.HashSet;



/****************************************************************************
 * <b>Title:</b> Parser.java
 * <b>Project:</b> Spider
 * <b>Description:</b> Class that contains methods for creating Jsoup documents
 * and Files
 * 
 * 
 * <b>Copyright:</b> Copyright (c) 2023
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Evan Swett
 * @version 3.0
 * @since April 4, 2023
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class Parser {    
    /**Function to parse out the relative urls embedded in the homepage
     * Takes in a string of raw HTML in string format, parses into Jsoup
     * document, then traverses the document object's elements until it
     * finds tags that start with "<a href=/" and adds them to a set to
     * remove duplicates
     * @param host
     * @return
     */
	public Set<String> collectRelativeURLs(String rawHTML) {
		
		Set<String> urls = new HashSet<>();
		
		Document doc = Jsoup.parse(rawHTML);
		
		//This select method grabs all anchor tags that have an href value starting with '/'
		Elements links = doc.select("a[href^=/]");
		for(Element e: links) {
			urls.add(e.attr("href"));
		}

		return urls;
	}
	
	/** This method takes a Jsoup document, grabs title information and HTML,
	 * then uses a FileWriter to write webpages to my resources folder
	 * 
	 * @param doc
	 */
	public void documentToFile(Document doc) {
		
		String title = doc.select("title").text().replace(" ", ""); //text()

		String outerHTML = doc.outerHtml();

		try (FileWriter out = new FileWriter("src/main/resources/" + title + ".html")) {
			out.write(outerHTML);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
