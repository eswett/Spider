/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package spider2;

import java.util.Set;
import java.util.List;
import org.jsoup.nodes.*;
import org.jsoup.*;


/****************************************************************************
 * <b>Title:</b> App.java
 * <b>Description:</b> Class that is used to create the client/server connection
 * by utilizing SSL Sockets. This class has methods to do GET and POST requests
 * while preserving the session via cookies.
 * 
 * 
 * <b>Copyright:</b> Copyright (c) 2023
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Evan Swett
 * @version 3.0
 * @since April 12, 2023
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class App {
    

    public static final String HOST = "smt-stage.qa.siliconmtn.com";
    public static final int PORT_NUMBER = 443;
	public static final String FORM_DATA = "requestType=reqBuild&pmid=ADMIN_LOGIN&emailAddress=evan.swett%40siliconmtn.com&password=1Lovecash%21&l=";

    private List<Document> docs = null;
	private Set<String> urls = null;
	
	Parser parser;


    public static void main(String args[]){
    	App app = new App();
    	app.crawl();
    	app.enterAdmin();
    }
    
    /** First half of the assignment connects 
     * 
     */
    public void crawl() {
    	Connector connection = new Connector(HOST, PORT_NUMBER);
    	parser = new Parser();
    	
    	String baseHTML = connection.getHTML(HOST, PORT_NUMBER, "/");
    	
    	urls = parser.collectRelativeURLs(baseHTML);
    	docs = connection.buildDocs(urls);
    	
    	for(Document doc: docs) {
        	parser.documentToFile(doc); 
    	}
    }
    
    public void enterAdmin() {
    	Connector connection = new Connector(HOST, PORT_NUMBER);
    	parser = new Parser();
    	
    	connection.postRequest(HOST, PORT_NUMBER, "/sb/admintool", FORM_DATA); //sending to form action location
    	connection.getHTML(HOST, PORT_NUMBER, "/admintool"); //I get redirected to "/admintool"
    	
    	String cacheHTML = connection.getHTML(HOST, PORT_NUMBER, "/sb/admintool?cPage=stats&actionId=FLUSH_CACHE");
    	Document adminDoc = Jsoup.parse(cacheHTML);
    	parser.documentToFile(adminDoc);
    }

}
