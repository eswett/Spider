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
 * <b>Description:</b> This class is a controller for my program. It defines two methods,
 * one for each of the stages of the assignment.
 * 
 * <b>Copyright:</b> Copyright (c) 2023
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Evan Swett
 * @version 3.0
 * @since April 13, 2023
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class App {
    
	//Constants 
    public static final String HOST = "smt-stage.qa.siliconmtn.com";
    public static final int PORT_NUMBER = 443;
    
    //credentials for logging into the admin site
	public static final String FORM_DATA = "requestType=reqBuild&pmid=ADMIN_LOGIN&emailAddress=evan.swett%40siliconmtn.com&password=1Lovecash%21&l=";

	Parser parser = new Parser();

	/** Main makes an instance of my application and calls both
	 * methods
	 * @param args
	 */
    public static void main(String args[]){
    	App app = new App();
    	app.crawl();
    	app.adminLogin();
    	System.out.println("Reached end of program");
    }
    
    /** First half of the assignment is to download all of the pages that
     * 	can be accessed via the base host. This 
     */
    public void crawl() {
    	Connector connection = new Connector(HOST, PORT_NUMBER);
    	
    	//collect html from home page
    	String baseHTML = connection.getHTML(HOST, PORT_NUMBER, "/");
    	
    	//parse out the relative urls from the page
    	Set<String> urls = parser.collectRelativeURLs(baseHTML);
    	
    	//build Jsoup Document objects from relative urls
    	List<Document> docs = connection.buildDocs(urls);
    	
    	//write each document to a file
    	for(Document doc: docs) {
        	parser.documentToFile(doc); 
    	}
    }
    
    /** This method connects to the admintool by passing in my admin credentials
     * 	then redirects to the page specified in the instructions and downloads it.
     */
    public void adminLogin() {
    	Connector connection = new Connector(HOST, PORT_NUMBER);
    	
    	//sending post req to form action location
    	connection.postRequest(HOST, PORT_NUMBER, "/sb/admintool", FORM_DATA); 
    	connection.getHTML(HOST, PORT_NUMBER, "/admintool"); //I get redirected to "/admintool"
    	
    	//read in the pages html via a socket
    	String cacheHTML = connection.getHTML(HOST, PORT_NUMBER, "/sb/admintool?cPage=stats&actionId=FLUSH_CACHE");
    	
    	//make it into a JSoup document
    	Document adminDoc = Jsoup.parse(cacheHTML);
    	
    	//write to file
    	parser.documentToFile(adminDoc);
    }

}
