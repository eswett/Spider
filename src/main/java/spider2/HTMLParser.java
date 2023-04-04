package spider2;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.Set;
import java.util.HashSet;

public class HTMLParser {
	
	private String baseURL;
	//I could instantiate the file writer here
//	private 
//	private Set<String> urls = null;
	Connection connection = null;
	
	/**
	 * Parameterized construc
	 * @param connection
	 */
	HTMLParser(Connection connection){
		baseURL = "smt-stage.qa.siliconmtn.com";
		this.connection = connection;
	}
	
    public String makeGETRequest(String host, String path) {
    	StringBuilder html = new StringBuilder();
    	
    	DataOutputStream out = connection.out;
    	BufferedReader in = connection.in;
    	
    	try {
			out.writeBytes("GET " + path + " HTTP/1.1\r\n"); //adding these carriage returns made the call work
	        out.writeBytes("Host: " + host + "\r\n\r\n");
//	        out.writeBytes("Connection: keep-alive \r\n");
	        
	        String inData = null;
	    	
	        while((inData = in.readLine()) != null) {
	           html.append(inData).append("\n");
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return html.toString();
    }
    
	public Set<String> collectRelativeURLs(String host) {
		
		Set<String> urls = new HashSet<>();

		String baseHTML = makeGETRequest(host, "/");
		Document doc = Jsoup.parse(baseHTML);
		
		//This select method grabs all anchor tags that have an href value starting with '/'
		Elements links = doc.select("a[href^=/]");
		for(Element e: links) {
			if(urls.contains(e.attr("href"))) {
				continue;
			} else {
				urls.add(e.attr("href"));
			}
		}
		
		for(String s: urls) {
			System.out.println(s);
		}
		return urls;
	}
	
	public List<Document> buildDocs(Set<String> urlStrings) {
		
		List<Document> docs = new ArrayList<>();
		
		for(String absolutePath: urlStrings) {
			String fullHTML = makeGETRequest(baseURL, absolutePath);
			System.out.print(fullHTML);
			Document d = Jsoup.parse(fullHTML);
			docs.add(d);
		}
		return docs;
	}
	
	
}
