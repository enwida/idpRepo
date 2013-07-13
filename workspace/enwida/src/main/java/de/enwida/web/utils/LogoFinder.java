package de.enwida.web.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LogoFinder {

    public String getImages(String companyName) {
        final WebClient webClient = new WebClient();
        HtmlPage page;
        String companyLink="https://"+companyName+"/";
        String images="";
        try {
            page = webClient.getPage(companyLink);
            //if page is not found, we dont want to search
            if(page.toString().contains("search") || (page.toString().contains("Search")))
                    return null;
            final DomNodeList<DomElement> div = page.getElementsByTagName("img");
            for (DomElement domElement : div) {
                String imgSrc=domElement.asXml();
                String url=null;
                //if this is not an absolute path update it
                if (!imgSrc.contains("http")){
                    imgSrc=imgSrc.replace("src=\"", "src=\""+companyLink);
                    imgSrc=imgSrc.replace("src='", "src='"+companyLink);
                    url = companyLink+domElement.getAttribute("src"); 
                }else{
                    url = domElement.getAttribute("src"); 
                }
                url = url.replace("http", "https");
                final BufferedImage bi = ImageIO.read(new URL(url));
                if (bi != null && bi.getHeight() < 200 && bi.getWidth() < 200) {
                    images += imgSrc;
                }
         }
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        images = images.replace("Yahoo", "Enwida");
        return images;
    }

}
