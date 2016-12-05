package webchase;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import org.jsoup.Jsoup;

/**
 * Takes the user input from the view, processes it and periodically writes the
 *     results to disc.
 * @author John Filipowicz
 */
public class WebController extends Observable implements Runnable{
    private final List<String> initialURLs;    //List of user inputed URLs
    private final List<String> terms;          //List of user inputed terms
    private final int depth;                   //User inputed depth of search
    private int addedPages;                    //Number of pages scanning
    private List<String> invalids;            //Invalid initial URLs
    
    private ArrayList<Future<WebPage>> scannedPages;//List of scanned WebPages
    private ArrayList<String> urlsScanned;          //List of scanned page URLs
    private List<String> tagsToScan;           //Tags to be scanned for terms
    private final ExecutorService threads;     //Thread pool
    
    private String fileName;                   //Name of file writen to
    private final String urlKey;               //Identifier for a url line
    private final String outputKey;            //Identifier for an output line
    
    //Defines minumum url length: http://ab.cde
    private final int MIN_URL_LENGTH = 13;
    private final int MAX_BEFORE_WRITE = 75;  //Maximum URLs processed at once
    //Maximum URLS held for duplicate comparisions
    private final int MAX_URL_STORED = 750;
    
    /**
     * Initialize all fields
     * @param _initialURLs User inputed URLs
     * @param _terms User inputed search terms
     * @param _depth User inputed search depth
     */
    public WebController(List<String> _initialURLs, List<String> _terms, int _depth){
        this.fileName = "writeTest.txt";
        try {new PrintWriter(this.fileName).close();} catch (IOException e) {}

        this.initialURLs = _initialURLs;
        this.checkInitURLs();
        
        this.terms = _terms;
        this.depth = _depth;
        
        this.threads = Executors.newCachedThreadPool();
        this.scannedPages = new ArrayList();
        this.urlsScanned = new ArrayList();
        this.initTags();
        
        this.urlKey = "-~-";
        this.outputKey = "\t-@-";
    }
    
    /**
     * Checks if the initial URLs produce a valid response code and removes them
     *     if they do not, adding them to an error list.
     */
    private void checkInitURLs(){
        this.invalids = new ArrayList();
        
        for(String url: this.emptyIfNull(this.initialURLs)){
            try{
                int responseCode = Jsoup.connect(url).response().statusCode();

                if(responseCode != 0 && (responseCode < 200 || responseCode >= 400)){
                        this.invalids.add(url + " (Code: " + responseCode + ") ");
                }
            }catch(Exception e){
                this.invalids.add(url);
            }
        }
        for(String url: this.emptyIfNull(this.invalids)){
            this.initialURLs.remove(url);
        }
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
        } catch(IOException | InterruptedException | ExecutionException
                | RejectedExecutionException e){}
        Platform.runLater(() -> {
            super.setChanged();
            super.notifyObservers(this.invalids);
        });
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
            this.scannedPages.add(this.threads.submit(initWebPage));
            this.urlsScanned.add(url);
        }
        this.scanToDepth();
    }
    
    /**
     * Scans all pages while depth is > 0. Writes to disk periodically.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException 
     */
    private void scanToDepth()
            throws InterruptedException, ExecutionException, IOException{
        //submits valid urls of the previously scanned pages to thread pool
        for(int i = 0; i < this.scannedPages.size(); i++){
            //processes valid nested urls
            for(String nestedURL: this.emptyIfNull(this.scannedPages.get(i).get().getPageURLs())){
                int pageDepth = this.scannedPages.get(i).get().getDepth();
                    if((pageDepth - 1 > 0) && this.isValidURL(nestedURL)){
                        WebPage nextPage = new WebPage(nestedURL, this.terms, this.tagsToScan, pageDepth - 1);
                        this.addedPages++;
                        this.scannedPages.add(this.threads.submit(nextPage));
                        this.urlsScanned.add(nestedURL);
                    }
                    if(this.addedPages > this.MAX_BEFORE_WRITE){
                        this.write(i);
                        i = 0;
                        this.addedPages = this.scannedPages.size();
                    }
                    if(this.urlsScanned.size() > this.MAX_URL_STORED){
                        int size = this.urlsScanned.size();
                        this.urlsScanned = new ArrayList<>(this.urlsScanned.subList(size - size / 10, size));
                        this.urlsScanned.trimToSize();
                    }
            }
        }
            if(this.scannedPages != null && this.scannedPages.size() > 0)
                this.write(this.scannedPages.size());
    }
    
    /**
     * Write or append to provided file name field all scanned page URLs/output
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException 
     */
    private void write(int stopWriteIndex)
            throws IOException, InterruptedException, ExecutionException{
        //Writer Initialization
        FileWriter fWriter;
        if(this.fileName == null){
            this.fileName = "writeTest.txt";
            fWriter = new FileWriter(this.fileName);
        }
        else{
            //bool append param: True -> end of file , False -> beginning
            fWriter = new FileWriter(this.fileName, true);
        }
        try (   BufferedWriter bWriter = new BufferedWriter(fWriter);
                PrintWriter pWriter = new PrintWriter(bWriter)) {
            if(stopWriteIndex > this.scannedPages.size())
                stopWriteIndex = this.scannedPages.size();
            //Writing scanned page url and output
            for(int i = 0; i < stopWriteIndex; i++){
                WebPage page = this.scannedPages.get(i).get();
                if(!(page.getOutput() == null || page.getOutput().isEmpty())){
                    pWriter.println(this.urlKey + page.getURL());
                    for(String output: page.getOutput()){
                        pWriter.println(this.outputKey + output);
                    }
                }
            }
        } catch(IOException | InterruptedException | ExecutionException e){
            // add sexy list management code to remove erroneous future
        }
        //Reduce list and close write streams
        this.scannedPages.subList(0, stopWriteIndex).clear();
        this.scannedPages.trimToSize();
        fWriter.close();
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
     * Shuts down the threads and writes any results left to disk
     */
    public void halt(){
        try {
            this.threads.shutdown();
            this.write(this.scannedPages.size());
        } catch (InterruptedException | ExecutionException | IOException e) {}
    }
}
