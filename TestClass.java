/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webchase;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.*;

/**
 *
 * @author John Filipowicz
 */
public class TestClass {

//public class URLReader {
    //public static void main(String[] args) throws Exception {
    //public TestClass(String url, List<String> terms){
    public TestClass(String url, String terms){
        
        
        try {
            
            Cleaner clean = new Cleaner(Whitelist.relaxed());
            Document doc = Jsoup.connect(url).get();
            doc = clean.clean(doc);
            Elements anchorTags = doc.getElementsByTag("a");
            
            for (Element link : anchorTags) {
                String linkHref = link.attr("abs:href");
                System.out.println(linkHref);
            }
            //System.out.println(doc);
            //System.out.println("\n" + doc.title());
            
        } catch (IOException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

