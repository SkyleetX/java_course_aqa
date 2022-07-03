package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.openqa.selenium.json.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase{

    @DataProvider
    public Iterator<Object[]> validGroupsFromXml() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/groups.xml"))) {
            String xml = "";
            String line = reader.readLine();
            while (line != null) {
                xml += line;
                line = reader.readLine();
            }
            XStream xstream = new XStream();
            xstream.processAnnotations(GroupData.class);
            List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml);
            return groups.stream().map((g) -> new Object[]{g}).toList().iterator();
        }
    }

    @DataProvider
    public Iterator<Object[]> validGroupsFromJson() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/groups.json"))) {
            String json = "";
            String line = reader.readLine();
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            Gson gson = new Gson();
            List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>(){}.getType()); //List<GroupData>.class
            return groups.stream().map((g) -> new Object[]{g}).toList().iterator();
        }
    }

    @Test (dataProvider = "validGroupsFromJson")
    public void testGroupCreation(GroupData group) {
        app.goTo().GroupPage();
        Groups before = app.db().groups();
        app.group().create(group);
        assertThat(app.group().getGroupCount(), equalTo(before.size() + 1));
        Groups after = app.db().groups();
        assertThat(after, equalTo(
                before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    }

    @Test(enabled = true)
    public void testBadGroupCreation() {
        app.goTo().GroupPage();
        Groups before = app.db().groups();
        GroupData group = new GroupData().withName("test1'");
        app.group().create(group);
        assertThat(app.group().getGroupCount(), equalTo(before.size()));
        Groups after = app.db().groups();
        assertThat(after, equalTo(before));
    }
}



//поиск максимального значения (new)
//int max = after.stream().max(Comparator.comparingInt(GroupData::id)).get().id();

//поиск максимального значения (лямбда вариант)
//int max = after.stream().max((o1, o2) -> Integer.compare(o1.id(),o2.id())).get().id();

//поиск максимального значения (цикл вариант)
//        int max = 0;
//        for (GroupData g : after) {
//            if (g.id() > 0) {
//                max = g.id();
//            }
//        }
//
//Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after)); проверка неупорядоченных множеств (если важен контроль id)