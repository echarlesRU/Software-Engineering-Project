package webchase;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * WebPage containing its occurrences of certain terms
 * @author John Filipowicz
 */
public class WebPage {
    List<URL> pageURLs;          //All URLs found on this page
    List<String> thisOutput;     //All found terms on this page
    List<String> terms;          //Terms to search for
    URL thisURL;                 //URL of this WebPage
    String[] tagsToScan;         //Tags to scan in the html of the page 
    String content;              //Content of this webpage
    
    /**
     * Initialize all fields of the WebPage
     * @param _thisURL the url of this WebPage
     * @param _terms The terms to be searched for
     * @throws IOException will be caught, not thrown after implementation 
     */
    public WebPage(URL _thisURL, List<String> _terms) throws IOException{
        this.thisURL = _thisURL;
        this.terms = _terms;
        this.content = thisURL.getContent().toString();
        //initialize tagsToSearch
        //initialize pageURLs
        //initialize thisOutput
    }
    
    /**
     * Scan this WebPage's content for the search terms
     */
    public void scanPage(){
        
    }
    
    /**
     * Gets the WebPage's URL
     * @return URL pageURL
     */
    public URL getURL(){
        return null;
    }
    
    /**
     * Gets the URL list
     * @return List#URL pageURLs
     */
    public List<URL> getPageURLs(){
        return null;
    }
    
    /**
     * Gets the output list
     * @return List#String output
     */
    public List<String> getOutput(){
        return null;
    }
}