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
                String img=domElement.asXml().replace("src=\"", "src=\""+companyLink);
                img=img.replace("src='", "src='"+companyLink);
                images+=img;
         }
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        images.replace("Yahoo", "Enwida");
         return images;
    }

}
