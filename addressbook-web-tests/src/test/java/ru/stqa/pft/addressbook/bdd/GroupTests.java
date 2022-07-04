package ru.stqa.pft.addressbook.bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/bdd", plugin = {"pretty", "html:build/cucumber-report.html"})
public class GroupTests extends AbstractTestNGCucumberTests {

}
