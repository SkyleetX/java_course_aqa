package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.List;

public class ContactHelper extends HelperBase{

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void initContactCreation() {
        click(By.linkText("add new"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstname());
        type(By.name("lastname"), contactData.getLastname());
        attach(By.name("photo"), contactData.getPhoto());

        if (creation) {
            if (contactData.getGroup() != null) {
                new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
            }
        } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void submitContactCreation() {
        click(By.name("submit"));
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    public void selectContactForDeletion(int index) {
        wd.findElements(By.name("selected[]")).get(index).click();
    }

    public void selectContactForModification() {
        click(By.xpath("//img[@src='icons/pencil.png']"));
    }

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    public void updateContactCreation() {
        click(By.name("update"));
    }

    public void createContact(ContactData contactData, boolean creation) {
        initContactCreation();
        skipInputAddress();
        fillContactForm(contactData, creation);
        submitContactCreation();
        contactCache = null;
        returnToHomePage();
    }

    public void modify(ContactData contact) {
        selectContactForModification();
        fillContactForm(contact, false);
        updateContactCreation();
        returnToHomePage();
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteContact();
        clickOkAlert();
        contactCache = null;
        returnToHomePageForDeletion();
    }

    public boolean ifThereAContact() {
        return isElementPresent(By.xpath("//*[@id=\"maintable\"]/tbody/tr[2]/td[8]/a"));
    }

    public void deleteContact() {
        click(By.xpath("//*[@id=\"content\"]/form[2]/div[2]/input"));
    }

    public int getContactCount() {
        return wd.findElements(By.name("selected[]")).size(); //количество групп
    }

    public void clickOkAlert() {
        isAlertPresent();
    }

    public void returnToHomePageForDeletion() {
        click(By.linkText("home"));
    }

    public Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.name("entry"));
        for (WebElement element : elements) {
            int id = Integer.parseInt(element.findElement(By.className("center")).findElement(By.tagName("input")).getAttribute("id"));
            ContactData contact = new ContactData().withId(id);
            contactCache.add(contact);
        }
        return new Contacts(contactCache);
    }

    public void skipInputAddress() {
        click(By.cssSelector("input[value='Next']"));
    }
}
