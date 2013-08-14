package de.enwida.web.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LogoFinder {

    public String getImages(String companyName) {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage page;
        URL companyLink = null;
        try {
            companyLink = new URL("http://www."+companyName+"/");
        } catch (MalformedURLException e1) {
            //page not valid
           return null;
        }
        String images="";
        try {
            page = webClient.getPage(companyLink);
            //if page is not found, we dont want to search
            if(page.toString().contains("search") || (page.toString().contains("Search")))
                    return null;
            final DomNodeList<DomElement> div = page.getElementsByTagName("img");
            for (DomElement domElement : div) {
                String absolutePath=page.getUrl().toString();
                String imgSrc=domElement.getAttribute("src");
                while(imgSrc.contains("../")){
                    absolutePath=goOneLevelUpFromEnd(absolutePath);
                    imgSrc=goOneLevelUpChar(imgSrc);
                }
                
                String url=null;
                //if this is not an absolute path update it
                if (!imgSrc.contains("http")){
                    url = absolutePath+imgSrc; 
                }else{
                    url = domElement.getAttribute("src"); 
                }
                //url = url.replace("http:", "https:");
                final BufferedImage bi = ImageIO.read(new URL(url));
                if (bi != null && bi.getHeight() < 200 && bi.getWidth() < 200) {
                    images += "<img src='"+url+"'/>";
                }
//                try {
//                    List<String> urls = downloadCssAndImages(companyLink);
//                    for (String imgUrl : urls) {
//                        images +="<img src='"+imgUrl+"'>";                        
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
         }
        } catch ( IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        images = images.replace("Yahoo", "Enwida");
        return images;
    }

	@SuppressWarnings("serial")
	public static final HashMap<String, String> acceptTypes = new HashMap<String, String>() {
		{
        put("html", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        put("img", "image/png,image/*;q=0.8,*/*;q=0.5");
        put("script", "*/*");
        put("style", "text/css,*/*;q=0.1");
    }};
    
	@SuppressWarnings("unchecked")
	public List<String> downloadCssAndImages(URL companyLink) {
        List<String> urls = new ArrayList<String>();
        final WebClient client = new WebClient();
        HtmlPage page;
        try {
            page = client.getPage(companyLink);
        } catch (Exception e) {
            return null;
        }
        String xPathExpression = "//*[name() = 'img' or name() = 'link' and @type = 'text/css']";
        List<HtmlElement> resultList = (List<HtmlElement>) page.getByXPath(xPathExpression);
        for (HtmlElement htmlElement : resultList) {
            String path = htmlElement.getAttribute("src").equals("") ? htmlElement
                    .getAttribute("href") : htmlElement.getAttribute("src");
            if (path == null || path.equals(""))
                continue;

            URL url;
            try {
                url = page.getFullyQualifiedUrl(path);
                client.addRequestHeader("Accept",
                        acceptTypes.get(htmlElement.getTagName().toLowerCase()));
                TextPage page2 = client.getPage(url);

                Pattern p = Pattern.compile("url\\(\\s*(['\"]?+)(.*?)\\1\\s*\\)");
                Matcher m = p.matcher(page2.getContent());
                while (m.find()) {
                    String imgSrc=m.group().replace("url(", "");
                    imgSrc=imgSrc.replace(")", "");
                    if(m.group().toString().contains("img")){
                        String absolutePath=url.toString();
                        while(imgSrc.contains("../")){
                            absolutePath=goOneLevelUpFromEnd(absolutePath);
                            imgSrc=goOneLevelUpChar(imgSrc);
                        }
                        urls.add(absolutePath +imgSrc);
                    }
                }
            } catch (Exception e) {
                break;
            }

        }

        return urls;
    }
    
    private String goOneLevelUpChar(String str) {
        String strProcesses=str.substring(1,str.length());
        while(!strProcesses.substring(0,1).equals("/")){
            strProcesses= strProcesses.substring(1,strProcesses.length());
        }
        return strProcesses;
    }
    
    
    private String goOneLevelUpFromEnd(String str) {
        str=str.substring(0,str.length()-1);
        int lastSlashPos = str.lastIndexOf('/');

        if (lastSlashPos >= 0)
        {
            return str.substring(0, lastSlashPos); //strip off the slash
        }
        return str;
    }
}
