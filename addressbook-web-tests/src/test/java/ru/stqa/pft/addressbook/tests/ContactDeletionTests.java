package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();
        if (! app.getContactHelper().ifThereAContact()) {
            app.getContactHelper().createContact(new ContactData().withFirstname("first_username").withLastname("first_surname"), false);
        }
    }

    @Test(enabled = true)
    public void testContactDeletion() throws InterruptedException {
        Contacts before = app.getContactHelper().all();
        ContactData deletedContact = before.iterator().next();
        app.getContactHelper().delete(deletedContact);
        //Thread.sleep(4000);
        assertThat(app.getContactHelper().getContactCount(), equalTo(before.size() - 1));
        Contacts after = app.getContactHelper().all();
        assertThat(after, equalTo(before.without(deletedContact)));
    }
}
