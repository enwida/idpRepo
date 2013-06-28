package de.enwida.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.enwida.web.dao.implementation.UserDao;
import de.enwida.web.model.User;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class AdminPanel {

    //Dont add test word into class name in order to preventtest run during mvn build 
    //These test case should be run manually when web site is up
    final WebClient webClient = new WebClient();
    
    @Autowired
    private UserDao userDao;

    
    private User user;
    
    private String webSiteLink="http://localhost:8080/enwida/";
	
    @Test
    public void CheckEditGroup() throws Exception {
       HtmlPage page;
       String link=webSiteLink+"user/admin/editGroup";

           page = webClient.getPage(link);
           System.out.println(page.asXml());
    }
    
    @Test
    public void CheckEditRoles() throws Exception {
       HtmlPage page;
       String link=webSiteLink+"user/admin/editRoles";

           page = webClient.getPage(link);
           System.out.println(page.asXml());
    }
    
    @Test
    public void CheckUserList() throws Exception {
       HtmlPage page;
       String link=webSiteLink+"user/admin/userList";

           page = webClient.getPage(link);
           System.out.println(page.asXml());
    }
    
    @Test
    public void CheckUser() throws Exception {
       HtmlPage page;
       String link=webSiteLink+"user/admin/editGroup";

           page = webClient.getPage(link);
           System.out.println(page.asXml());
    }
}
