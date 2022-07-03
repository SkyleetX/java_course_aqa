package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {
    //There is a bug on site (can't modify contact) UPD: fixed, but not working :D
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();
        if (app.getContactHelper().all().size() == 0) {
            app.getContactHelper().createContact(new ContactData().withFirstname("test1").withLastname("test2"), false);
        }
    }
    @Test(enabled = true)
    public void testContactModification() {
        Contacts before = app.getContactHelper().all();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData().withId(modifiedContact.getId()).withFirstname("test1").withLastname("test2").withPhoto(new File("src/test/resources/cat.jpg"));
        app.getContactHelper().modify(contact);
        assertThat(app.getContactHelper().getContactCount(), equalTo(before.size()));
        Contacts after = app.getContactHelper().all();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    }
}
