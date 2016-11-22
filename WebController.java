package webchase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Takes the user input from the view, processes it, then allows a list of
 *     WebPage objects containing all output results to be retrieved.
 * @author John Filipowicz
 */
public class WebController extends Thread {
    private final List<String> initialURLs;    //List of user inputed URLs
    private final List<String> terms;          //List of user inputed terms
    private final int depth;                   //User inputed depth of search
    private int lastScanned;                   //Last page w/nested urls scanned
    private int waiting;                       //Number of pages scanning
    
    private List<Future<WebPage>> scannedPages;//List of scanned WebPages
    private List<String> urlsScanned;          //List of scanned page URLs
    private List<String> tagsToScan;           //Tags to be scanned for terms
    private final ExecutorService threads;     //Thread pool
    
    //Defines minumum url length: http://ab.cde
    private final int MIN_URL_LENGTH = 13;
    private final int MAX_WAITING = 250;       //Maximum URLs processed at once
    
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
        this.lastScanned = 0;
        
        this.threads = Executors.newCachedThreadPool();
        this.scannedPages = new ArrayList();
        this.urlsScanned = new ArrayList();
        this.initTags();
    }
    
    /**
     * Initializes the TagsToScan field
     */
    private void initTags(){
        //p, li, td, th, a, b, pre, h(1..6)
        tagsToScan = new ArrayList(13);
                
        tagsToScan.add("p");
        tagsToScan.add("li");
        tagsToScan.add("td");
        tagsToScan.add("th");
        tagsToScan.add("h1");
        tagsToScan.add("h2");
        tagsToScan.add("h3");
        tagsToScan.add("h4");
        tagsToScan.add("h5");
        tagsToScan.add("h6");
        tagsToScan.add("a");
        tagsToScan.add("b");
        tagsToScan.add("pre");
    }
    
    @Override
    public void run(){
        try{
            this.scanPages();
            threads.shutdown();
        } catch(IOException | InterruptedException | ExecutionException e){}
    }
    
    /**
     * Scans all initial URLs and their nested URLs to depth, initializing the
     *     list of scanned WebPages. Each WebPage will contain their discovered
     *     output.
     * @throws IOException
     * @throws InterruptedException
     */
    private void scanPages()
            throws IOException, ExecutionException, InterruptedException{
        // Scanning initial URLs
        for(String url: this.emptyIfNull(this.initialURLs)){
            WebPage initWebPage = new WebPage(url, this.terms, this.tagsToScan, this.depth);
            this.scannedPages.add(threads.submit(initWebPage));
            this.urlsScanned.add(url);
        }
        this.scanToDepth();
    }
    
    private void scanToDepth() throws InterruptedException, ExecutionException{
System.out.println("Size: " + this.scannedPages.size());
        boolean validURL = false;
        int scannedSize = this.scannedPages.size();
        
        //submits the valid urls of the previously scanned pages to the thread pool
        for(; this.lastScanned < scannedSize; this.lastScanned++){
            
            if(waiting > this.MAX_WAITING){
                this.clearWaiting();
                waiting = 0;
            }
            
            for(String nestedURL: this.emptyIfNull(this.scannedPages.get(this.lastScanned).get().getPageURLs())){
                int pageDepth = this.scannedPages.get(this.lastScanned).get().getDepth();
                    if((pageDepth - 1 > 0) && this.isValidURL(nestedURL)){
                        WebPage nextPage = new WebPage(nestedURL, this.terms, this.tagsToScan, pageDepth - 1);
                        validURL = true;
                        waiting++;
                        scannedPages.add(threads.submit(nextPage));
                        this.urlsScanned.add(nestedURL);
                    }
            }
        }
        if(validURL){
            this.scanToDepth();
        }
    }
    
    /**
     * Finishes termination of all current threads in the pool
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private void clearWaiting() throws InterruptedException, ExecutionException{
//System.out.println("-------Clear Waiting------------");
        for(Future<WebPage> page: this.scannedPages){
            page.get();
        }
    }
    
    /**
     * Tests if the URL is valid
     * @param url
     * @return 
     */
    private boolean isValidURL(String url)
            throws InterruptedException, ExecutionException{
        boolean result = false;
        
        if(url != null && url.length() >= this.MIN_URL_LENGTH
                && url.toLowerCase().charAt(0) == 'h'
                && !this.urlsScanned.contains(url)){
            result = true;
        }
        return result;
    }
    
    /**
     * Returns whether a WebPage with the URL has been scanned
     * @param url URL to check for
     * @return boolean if it has been scanned
     */
    private boolean haveScanned(String url)
            throws InterruptedException, ExecutionException{
        boolean result = false;
        
        for(Future<WebPage> page: this.scannedPages){
            String urlScanned = page.get().getURL();
                if (urlScanned.equalsIgnoreCase(url)){
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
     * Returns if the thread pool is completed running
     * @return boolean
     */
    public boolean isFinished(){
        return this.threads.isShutdown();
    }
    
    /**
     * Gets the scanned WebPage objects
     * @return WebPage List scanned pages
     */
    public List<Future<WebPage>> getWebPages(){
        return this.scannedPages;
    }
}
