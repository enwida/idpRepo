package de.enwida.web.controller;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LogoFinder {

    public String getImages(String companyName) {
        final WebClient webClient = new WebClient();
        HtmlPage page;
        String companyLink="http://"+companyName+"/";
        String images="";
        try {
            page = webClient.getPage(companyLink);
            //if page is not found, we dont want to search
            if(page.toString().contains("search") || (page.toString().contains("Search")))
                    return null;
            final DomNodeList<DomElement> div = page.getElementsByTagName("img");
            for (DomElement domElement : div) {
                String imgSrc=domElement.asXml();
                //if this is not an absolute path update it
                if (!imgSrc.contains("http")){
                    imgSrc=imgSrc.replace("src=\"", "src=\""+companyLink);
                    imgSrc=imgSrc.replace("src='", "src='"+companyLink);
                    
                }
                images+=imgSrc;
         }
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        images.replace("Yahoo", "Enwida");
         return images;
    }

}
