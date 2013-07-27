package de.enwida.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import de.enwida.web.dao.implementation.UserDaoImpl;
import de.enwida.web.model.User;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class AdminPanel {

    //Dont add test word into class name in order to preventtest run during mvn build 
    //These test case should be run manually when web site is up
    final WebClient webClient = new WebClient();
    
    @Autowired
    private UserDaoImpl userDao;

    
    private User user;
    
    private String webSiteLink="http://localhost:8080/enwida/";
    
    @Test
    public void CheckAdminLinks() throws Exception {
       HtmlPage page;
       user=userDao.getAllUsers().get(0);
       String link=webSiteLink+"user/admin/userList";
       page = webClient.getPage(link);
       
       link=webSiteLink+"user/admin/editGroup?userID="+user.getUserID();       
       page = webClient.getPage(link);
       
       link=webSiteLink+"user/admin/editRoles";
       page = webClient.getPage(link);
       
       link=webSiteLink+"user/admin/editGroup";
       page = webClient.getPage(link);
       
       link=webSiteLink+"user/admin/userID="+user.getUserID();
       page = webClient.getPage(link);
       
       link=webSiteLink+"user/admin/userLog="+user.getUserName();
       page = webClient.getPage(link);
    }

    @Test
    public void CheckUserLinks() throws Exception {
       HtmlPage page;
       user=userDao.getAllUsers().get(0);
       
       page = webClient.getPage(webSiteLink+"user/admin/userList");
          
       page = webClient.getPage(webSiteLink+"user/");
          
       page = webClient.getPage(webSiteLink+"user/register");
        
       page = webClient.getPage(webSiteLink+"user/login");
    }
    
    @Test
    public void CheckRegistrationForm() throws Exception {
        // Get the first page
        final HtmlPage page1 = webClient.getPage(webSiteLink+"user/register");

        // Get the form that we are dealing with and within that form, 
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("registrationForm");

        final HtmlSubmitInput button = form.getInputByName("saveChanges");
        final HtmlTextInput textFieldUserName = form.getInputByName("userName");
        final HtmlTextInput textFieldFirstName = form.getInputByName("firstName");
        final HtmlTextInput textFieldLastName = form.getInputByName("lastName");
        final HtmlPasswordInput textFieldPassword = form.getInputByName("password");
        final HtmlPasswordInput textFieldConfirmPassword = form.getInputByName("confirmPassword");

        // Change the value of the text field
        textFieldUserName.setValueAttribute("test@enwida.de");
        textFieldFirstName.setValueAttribute("test");
        textFieldLastName.setValueAttribute("test");
        textFieldPassword.setValueAttribute("123456");
        textFieldConfirmPassword.setValueAttribute("123456");

        // Now submit the form by clicking the button and get back the second page.
        final HtmlPage page2 = button.click();
        System.out.println(page2.asText());
        webClient.closeAllWindows();
    }
    
}
