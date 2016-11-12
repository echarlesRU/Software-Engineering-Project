package webchase;

import java.io.IOException;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.safety.*;
import org.jsoup.select.*;

/**
 * WebPage containing its occurrences of certain terms
 * @author John Filipowicz
 */
public class WebPage {
    List<String> pageURLs;       //All URLs found on this page
    List<String> thisOutput;     //All found terms on this page
    List<String> terms;          //Terms to search for
    List<String> tagsToScan;     //Tags to scan in the html of this page 
    String thisURL;              //URL of this WebPage
    Document pageHTML;           //Raw HTML of this page
    
    
    /**
     * Initialize all fields of the WebPage
     * @param _thisURL the url of this WebPage
     * @param _terms The terms to be searched for
     */
    public WebPage(String _thisURL, List<String> _terms) {
        this.thisURL = _thisURL;
        this.terms = _terms;
        
        try{
            Cleaner sanitize = new Cleaner(Whitelist.relaxed());
//System.out.println(thisURL);
            pageHTML = Jsoup.connect(this.thisURL).get();
            pageHTML = sanitize.clean(pageHTML);
            
            this.thisOutput = new ArrayList();
            this.initTags();
            //this.initURLs();
            //this.scanPage();
        } catch (IOException e){}
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
    
    public void initURLs() throws IOException{
        this.pageURLs = new ArrayList();
        Elements anchorTags = pageHTML.getElementsByTag("a");
        
        for (Element link : anchorTags) {
            String href = link.attr("abs:href");
            this.pageURLs.add(href);
        }
            
            //System.out.println(doc);
        System.out.println("\n" + pageHTML.title());
        
    }
    
    /**
     * Scan this WebPage's content for the search terms
     * @throws IOException
     */
    public void scanPage() throws IOException{
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
    
    /**
     * Returns if the url of this WebPage is equal to o's
     * @param o Object to compare to
     * @return boolean if the object is equal to this
     */
    @Override
    public boolean equals(Object o){
        return this.thisURL.equals(((WebPage)o).getURL());
    }
}