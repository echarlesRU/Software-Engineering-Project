package webchase;

import java.net.URL;
import java.util.List;
import java.util.Queue;

/**
 * Takes the user input from the view, processes it, then returns a list of
 *     WebPage objects containing all output results.
 * @author John Filipowicz
 */
public class WebController {
    List<URL> initialURLs;        //List of user inputed URLs
    List<String> terms;           //List of user inputed terms
    List<WebPage> scannedPages;   //List of scanned WebPages
    Queue<URL> yetToScan;         //Queue of URLs to create as WebPage & scan
    int depth;                    //User inputed depth of search

    /**
     * Initialize all fields
     * @param _initialURLs User inputed URLs
     * @param _terms User inputed search terms
     * @param _depth User inputed search depth
     */
    WebController(List<URL> _initialURLs, List<String> _terms, int _depth){
        this.initialURLs = _initialURLs;
        this.terms = _terms;
        this.depth = _depth;
        
        this.scanPages();
    }
    
    /**
     * Scans all initial URLs and their nested URLs to depth, initializing the
     *     list of scanned WebPages. Each WebPage will contain their discovered
     *     output.
     */
    private void scanPages(){
        // To be written
    }
}
