package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase{

    @DataProvider
    public Iterator<Object[]> validGroupsFromJson() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/contacts.json"))) {
            String json = "";
            String line = reader.readLine();
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            Gson gson = new Gson();
            List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>(){}.getType()); //List<ContactData>.class
            return contacts.stream().map((g) -> new Object[]{g}).toList().iterator();
        }
    }

    @Test (dataProvider = "validGroupsFromJson")
    public void testContactCreation(ContactData contact) {
        app.goTo().HomePage();
        Contacts before = app.getContactHelper().all();
//        File photo = new File("src/test/resources/cat.jpg");
//        ContactData contact = new ContactData().withFirstname("test_firstname").withLastname("test_surname").withPhoto(photo);
        app.getContactHelper().createContact(contact,true);
        assertThat(app.getContactHelper().getContactCount(), equalTo(before.size() + 1));
        Contacts after = app.getContactHelper().all();
        assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt(ContactData::getId).max().getAsInt()))));
    }
}
