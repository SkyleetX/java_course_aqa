package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Browser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private final Properties properties;
    WebDriver wd;

    private SessionHelper sessionHelper;
    private NavigationHelper navigationHelper;
    private GroupHelper groupHelper;
    private String browser;
    private ContactHelper contactHelper;
    private DbHelper dbHelper;

    public ApplicationManager(String browser) throws IOException {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        //System.setProperty("webdriver.chrome.driver", "/Git_repo/java_pft/addressbook-web-tests/src/test/resources/chromedriver.exe");
        //String browser = String.valueOf(Browser.CHROME);
        //Browser browser = Browser.CHROME;
        String target = System.getProperty("target", "local");
        properties.load(new FileReader(String.format("src/test/resources/%s.properties", target)));

        dbHelper = new DbHelper();

        if (browser.equals(String.valueOf(Browser.CHROME))) {
            wd = new ChromeDriver();
        } else if (browser.equals(String.valueOf(Browser.FIREFOX))) {
            wd = new FirefoxDriver();
        } else if (browser.equals(String.valueOf(Browser.IE))) {
            wd = new InternetExplorerDriver();
        } else if (browser.equals(String.valueOf(Browser.EDGE))) {
            wd = new EdgeDriver();
        }

        wd.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        wd.get(properties.getProperty("web.baseUrl"));
        groupHelper = new GroupHelper(wd);
        contactHelper = new ContactHelper(wd);
        navigationHelper = new NavigationHelper(wd);
        sessionHelper = new SessionHelper(wd);
        sessionHelper.login(properties.getProperty("web.adminLogin"),properties.getProperty("web.adminPassword"));
    }



    public void stop() {
        wd.quit();
    }

    public GroupHelper group() {
        return groupHelper;
    }

    public NavigationHelper goTo() {
        return navigationHelper;
    }

    public ContactHelper getContactHelper() {
        return contactHelper;
    }

    public DbHelper db() {
        return dbHelper;
    }
}
