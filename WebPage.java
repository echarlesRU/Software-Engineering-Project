/**
* License: CC Attribution Non-Commercial 4.0 International
*    See short hand summary here:
*        http://creativecommons.org/licenses/by-nc/4.0/
*    See legal specifications here:
*        http://creativecommons.org/licenses/by-nc/4.0/legalcode
*
*    Reuse of code allowed under the conditions in the link above.
*/
package webchase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.safety.*;
import org.jsoup.select.*;

/**
 * WebPage containing its occurrences of certain terms
 * @author John Filipowicz
 */
public class WebPage implements Callable{
    private List<String> pageURLs;        //All URLs found on this page
    private List<String> thisOutput;      //All found terms on this page
    private final List<String> terms;     //Terms to search for
    private final List<String> tagsToScan;//HTML tags to scan for terms 
    private final String thisURL;         //URL of this WebPage
    private final int depth;              //Depth of WebPage
    
    /**
     * Initialize all fields of the WebPage
     * @param _thisURL the url of this WebPage
     * @param _terms The terms to be searched for
     * @param _tags The tags to scan for terms
     * @param _depth The depth level of this WebPage
     */
    public WebPage(String _thisURL, List<String> _terms, List<String> _tags, int _depth) {
        this.thisURL = _thisURL;
        this.terms = _terms;
        this.tagsToScan = _tags;
        this.depth = _depth;
        
        this.thisOutput = new ArrayList();
    }
    
    @Override
    public WebPage call(){
        Document pageHTML;
        try {
            Cleaner sanitize = new Cleaner(Whitelist.relaxed());
            pageHTML = Jsoup.connect(this.thisURL).timeout(8000).get();
            pageHTML = sanitize.clean(pageHTML);
            
            if(depth > 1)
                this.initURLs(pageHTML);
            this.scanPage(pageHTML);
        } catch (IOException e){}
     
        return this;
    }
    
    /**
     * Initialize the pageURLs field with the nested URLs of the web page
     * @param pageHTML
     * @throws IOException 
     */
    private void initURLs(Document pageHTML) throws IOException{
        this.pageURLs = new ArrayList();
        Elements anchorTags = null;
        
        if(pageHTML != null)
            anchorTags = pageHTML.getElementsByTag("a");
        
        for (Element link : this.emptyIfNull(anchorTags)){
            String href = link.attr("abs:href");
            this.pageURLs.add(href);
        }
    }
    
    /**
     * Scan this WebPage's content for the search terms
     * @throws IOException
     */
    private void scanPage(Document pageHTML) throws IOException{
        //For each search term
        for(String term: this.emptyIfNull(this.terms)){
            //For each searchable tag
            for(String tag: this.emptyIfNull(this.tagsToScan)){
                Elements tags = pageHTML.getElementsByTag(tag);
                //For each instance of the searchable tag
                for(Element individualTag: this.emptyIfNull(tags)){
                    String tagText = individualTag.text();
                    if(tagText.toLowerCase().contains(term.toLowerCase())
                            && !this.thisOutput.contains(tagText)){
                        this.thisOutput.add(tagText);
                    }
                }
            }
        }
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
     * Gets the WebPage's depth
     * @return int depth
     */
    public int getDepth(){
        return this.depth;
    }
    
    /**
     * Gets the WebPage's URL
     * @return String pageURL
     */
    public String getURL(){
        return thisURL;
    }
    
    /**
     * Gets the URL list
     * @return String List pageURLs
     */
    public List<String> getPageURLs(){
        return pageURLs;
    }
    
    /**
     * Gets the output list
     * @return String List output
     */
    public List<String> getOutput(){
        return thisOutput;
    }
}