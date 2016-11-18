package webchase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Takes the user input from the view, processes it, then allows a list of
 *     WebPage objects containing all output results to be retrieved.
 * @author John Filipowicz
 */
public class WebController extends Thread {
    private List<String> initialURLs;     //List of user inputed URLs
    private List<String> terms;           //List of user inputed terms
    private int depth;                    //User inputed depth of search
    
    private List<WebPage> scannedPages;   //List of scanned WebPages
    private Queue<String> yetToScan;      //Queue of URLs to be scanned
    private List<String> beenQueued;      //List of URLs that have been queued
    
    //Defines minumum url length: http://ab.cde
    private final int MIN_URL_LENGTH = 13;
    //Defines max wait time for a thread to 1 minute
    private final int MAX_WAIT = 60000;
    
    /**
     * Initialize all fields
     * @param _initialURLs User inputed URLs
     * @param _terms User inputed search terms
     * @param _depth User inputed search depth
     */
    public WebController(List<String> _initialURLs, List<String> _terms, int _depth){
        this.initialURLs = _initialURLs;
        this.terms = _terms;
        this.depth = _depth;
        
        this.yetToScan = new LinkedBlockingQueue();
        this.scannedPages = new ArrayList();
        this.beenQueued = new ArrayList();
    }
    
    @Override
    public void run(){
        try{
            this.scanPages();
        } catch(IOException | InterruptedException e){}
    }
    
    /**
     * Scans all initial URLs and their nested URLs to depth, initializing the
     *     list of scanned WebPages. Each WebPage will contain their discovered
     *     output.
     * @throws IOException
     * @throws InterruptedException
     */
    private void scanPages() throws IOException, InterruptedException{
        List<WebPage> scanning = new ArrayList();
        
        // Scanning initial URLs
        for(String url: this.emptyIfNull(this.initialURLs)){
            WebPage initWebPage = new WebPage(url, this.terms);
            initWebPage.start();
            scanning.add(initWebPage);
        }
        
        for(WebPage page: this.emptyIfNull(scanning)){
            page.join(this.MAX_WAIT);
            this.scannedPages.add(page);
            
            for(String nestedURL: this.emptyIfNull(page.getPageURLs())){
                if(this.isValidURL(nestedURL)){
                    this.yetToScan.add(nestedURL);
                    this.beenQueued.add(nestedURL);
                }
            }
        }
        
        // Since the first layer has been scanned
        this.depth = this.depth - 1;
        
        if(depth > 0)
            this.scanQueue();
    }
    
    /**
     * Scan the URLs on the queue and add their nested URLs 
     * @throws InterruptedException 
     */
    private void scanQueue() throws InterruptedException{
        List<WebPage> scanning = new ArrayList();
        
        while(this.yetToScan.peek() != null){
            WebPage nextPage = new WebPage(this.yetToScan.remove(), this.terms);
            nextPage.start();
            scanning.add(nextPage);
        }
        
        for(WebPage page: this.emptyIfNull(scanning)){
            page.join(this.MAX_WAIT);
            this.scannedPages.add(page);
            
            for(String nestedURL: this.emptyIfNull(page.getPageURLs())){
                if(this.isValidURL(nestedURL)){
                    this.yetToScan.add(nestedURL);
                    this.beenQueued.add(nestedURL);
                }
            }
        }
        this.depth = this.depth - 1;
        
        if(this.depth > 0)
            this.scanQueue();
    }
    
    /**
     * Tests if the URL is valid
     * @param url
     * @return 
     */
    private boolean isValidURL(String url){
        boolean result = false;
        
        if(url != null && url.length() >= this.MIN_URL_LENGTH){
            if(!this.haveQueued(url)){
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
    private boolean haveQueued(String url){
        boolean result = false;
        
        for(String queued: this.emptyIfNull(this.beenQueued)){
            if (queued.equalsIgnoreCase(url)){
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
