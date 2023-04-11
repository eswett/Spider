package spider2;

import java.util.List;
import java.util.Set;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.select.Evaluator.ContainsData;

import java.io.*;
import java.util.HashSet;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import java.util.Map;

public class Parser {
	
	private String baseURL;
	private int port;
    
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
		
//		
//		try {
//			Connection.Response res = doc.connection().execute();
//			Map<String, String> cookies = res.cookies();
//			for(Map.Entry<String, String> entry : cookies.entrySet()) {
//				System.out.print(entry);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//This select method grabs all anchor tags that have an href value starting with '/'
		Elements links = doc.select("a[href^=/]");
		for(Element e: links) {
			urls.add(e.attr("href"));
		}

		return urls;
	}
	
	
	public void documentToFile(List<Document> docs) {
		for(Document doc: docs) {
			String title = doc.select("title").text().replace(" ", ""); //text()

			
			String outerHTML = doc.outerHtml();

			try (FileWriter out = new FileWriter("src/main/resources/" + title + ".html")) {
				out.write(outerHTML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void grabCookies(String rawHTML) {
		Set<String> myCookies = new HashSet<>();
		Document doc = Jsoup.parse(rawHTML);
//		Elements cookies = doc.select("*:containsOwn('Set-Cookie')");
//		for(Element e  : cookies) {
//			System.out.println(e);
//		}
//		System.out.print("cookie test");
	}
	
}
