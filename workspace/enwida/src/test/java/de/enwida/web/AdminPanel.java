package de.enwida.web;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.LogoFinder;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class AdminPanel {

    //Dont add test word into class name in order to preventtest run during mvn build 
    //These test case should be run manually when web site is up
    final WebClient webClient = new WebClient();
    
    @Autowired
    private IUserService userService;
    
    private String webSiteLink="http://localhost:8080/enwida/";
    
    private HtmlPage page; 

    private String link=webSiteLink+"user/admin/";
    
    private HtmlAnchor anchor;
    
    @Test
    public void LogoFinder() throws Exception{
       //check if logo finder works
        LogoFinder lf=new LogoFinder();
        lf.getImages("siemens.de");
    }
    
    @Test
    public void TestRegisterPage() throws Exception{
        registerUser("olcay@siemens.de", "secret");
        registerUser("olcay@tum.de", "secret");
        registerUser("olcay@in.tum.de", "secret");
        registerUser("olcay@gmail.de", "secret");
        registerUser("olcay@a.de", "secret");
        registerUser("olcay@olcay.de", "secret");
    }

    @Test
    public void loginWithInvalidMailAddress() throws Exception{
      //Login with Invalid mail address
        Assert.assertEquals(false, Login("test","secret2"));
    }
    
    @Test
    public void loginWithValidMailAddress() throws Exception{
        //Login with valid mail address
        Assert.assertEquals(true, Login("test","secret"));
    }
    
    @Test
    public void LoginWithFirstAndLastName() throws Exception{
        
        //login with first and last name
        Assert.assertEquals(true, Login("test test","secret"));
    }
    
    @Test   
    public void LoginWithNotEnabledUser() throws Exception{
        //register new user
        registerUser("test@enwida.de","secret");
        Assert.assertEquals(false, Login("test@enwida.de","secret"));
    }
    
    @Test   
    public void LoginEnabledUser() throws Exception{
        Assert.assertEquals(true, Login("test","secret"));
    }
    
    private boolean link(String gotoLink,String pageTitle) throws Exception {
        link=webSiteLink+gotoLink;
        page = webClient.getPage(link);
        return page.getTitleText().equalsIgnoreCase(pageTitle);
    }
    
    //Select user from list and check its details
    @Test
    public void CheckUserDetails() throws Exception{
        
        Login("test", "secret");
        //Go to UserList page
        String link=webSiteLink+"user/admin/";
        //Get user lists
        page = webClient.getPage(link);
        List<HtmlAnchor> list = page.getAnchors();
        for (HtmlAnchor htmlAnchor : list) {
            if (htmlAnchor.toString().contains("userID=")){
                anchor=htmlAnchor;
                break;
            }
        }
        //we click on the user test@enwida.de
        page = anchor.click();

        Assert.assertEquals(false, page.getTitleText().equalsIgnoreCase("Enwida Admin Page"));
    }

    //Try to login to the page
    public boolean Login(String userName,String password) throws Exception{
        // Get the first page
        final HtmlPage page1 = webClient.getPage(webSiteLink+"user/login.html");

        // Get the form that we are dealing with and within that form, 
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("loginForm");

        final HtmlSubmitInput button = form.getInputByName("submit");
        final HtmlTextInput textFieldUserName = form.getInputByName("j_username");
        final HtmlPasswordInput textFieldPassword = form.getInputByName("j_password");
        // Change the value of the text field
        textFieldUserName.setValueAttribute(userName);
        textFieldPassword.setValueAttribute(password);
        
        // Now submit the form by clicking the button and get back the second page.
        page = button.click();
        //Make sure we are in user list page
        link=webSiteLink+"user/admin/admin_userlist";
        page = webClient.getPage(link);
        return page.getTitleText().equalsIgnoreCase("Enwida Admin Page");
    }

    public void registerUser(String userName,String password) throws Exception {
        // Get the first page
        final HtmlPage page1 = webClient.getPage(webSiteLink+"user/register");

        // Get the form that we are dealing with and within that form, 
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("registrationForm");

        final HtmlSubmitInput button = form.getInputByName("submit");
        final HtmlTextInput textFieldUserName = form.getInputByName("userName");
        final HtmlTextInput textFieldFirstName = form.getInputByName("firstName");
        final HtmlTextInput textFieldLastName = form.getInputByName("lastName");
        final HtmlPasswordInput textFieldPassword = form.getInputByName("password");
        final HtmlPasswordInput textFieldConfirmPassword = form.getInputByName("confirmPassword");

        // Change the value of the text field
        textFieldUserName.setValueAttribute(userName);
        textFieldFirstName.setValueAttribute("test");
        textFieldLastName.setValueAttribute("test");
        textFieldPassword.setValueAttribute(password);
        textFieldConfirmPassword.setValueAttribute(password);

        // Now submit the form by clicking the button and get back the second page.
        final HtmlPage page2 = button.click();
        System.out.println(page2.asText());
        Assert.assertTrue(page2.getTitleText().equalsIgnoreCase("Enwida Registration"));
        webClient.closeAllWindows();
    }
    
}
