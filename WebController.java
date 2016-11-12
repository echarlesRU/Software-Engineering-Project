//package expertWebCrawler;
package webchase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import org.jsoup.helper.*;

/**
 * Takes the user input from the view, processes it, then returns a list of
 *     WebPage objects containing all output results.
 * @author John Filipowicz
 */
public class WebController {
    List<String> initialURLs;     //List of user inputed URLs
    List<String> terms;           //List of user inputed terms
    List<WebPage> scannedPages;   //List of scanned WebPages
    Queue<String> yetToScan;      //Queue of URLs to create as WebPage & scan
    int depth;                    //User inputed depth of search
    final int MIN_URL_LENGTH = 7; //Defines minumum url length as "xyz.abc"
    
    /**
     * Initialize all fields
     * @param _initialURLs User inputed URLs
     * @param _terms User inputed search terms
     * @param _depth User inputed search depth
     */
    WebController(List<String> _initialURLs, List<String> _terms, int _depth){
        this.initialURLs = _initialURLs;
        this.terms = _terms;
        this.depth = _depth;
        
        this.yetToScan = new LinkedBlockingQueue();
        this.scannedPages = new ArrayList();
        try{
            this.scanPages();
        } catch(IOException e){}
    }
    
    /**
     * Scans all initial URLs and their nested URLs to depth, initializing the
     *     list of scanned WebPages. Each WebPage will contain their discovered
     *     output.
     */
    private void scanPages() throws IOException{
        //For to initialize the threads[] and start
        //For to join and return... themselves?
        // Blocking queue take for threads
        
        // Working with single thread first
        for(String url: this.initialURLs){
//System.out.println(url);
            WebPage initWebPage = new WebPage(url, this.terms);
            initWebPage.initURLs();
            initWebPage.scanPage();
                
            // Adds all nested urls into the queue
            this.scannedPages.add(initWebPage);
            for(String nestedURL: initWebPage.getPageURLs()){
                if(this.isValidURL(nestedURL))
                    this.yetToScan.add(nestedURL);
            }
        }
        // Since the first layer has been scanned
        this.depth = this.depth - 1;
        
        //For demo only
        while(this.yetToScan.peek() != null){
//System.out.println(this.yetToScan.peek());
            WebPage nextPage = new WebPage(yetToScan.remove(), this.terms);
            nextPage.scanPage();
            this.scannedPages.add(nextPage);
        }
        //End for demo only
        
        /*
        //In progress, do not use
        while(depth > 0){
            if(yetToScan.peek() != null){
                url = yetToScan.remove();
                
                WebPage nextToScan = new WebPage(url, this.terms);
            }
        }
        */
    }
    
    /**
     * Tests if the URL is valid
     * @param url
     * @return 
     */
    private boolean isValidURL(String url){
        boolean result = false;
        
        if(url != null && url.length() >= this.MIN_URL_LENGTH){
            if(!this.haveScanned(url)){
                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * Returns whether a WebPage with the URL has been scanned
     * @param url URL to check for
     * @return boolean if it has been scanned
     */
    private boolean haveScanned(String url){
        boolean result = false;
        
        for(WebPage scanned: scannedPages){
            if (scanned.getURL().equalsIgnoreCase(url)){
                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * Source reference: 
     *    stackoverflow.com/questions/2250031/null-check-in-an-enhanced-for-loop
     * Returns an empty Iterable object if param is null, or param if it is not.
     * @param <T> Type of Iterable object passed
     * @param iterable Iterable object being passed
     * @return iterable or an empty Iterable object
     */
    private <T>Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.emptyList() : iterable;
    }
    
    
    /**
     * Gets the scanned WebPage objects
     * @return WebPage List scanned pages
     */
    public List<WebPage> getWebPages(){
        return this.scannedPages;
    }
}
