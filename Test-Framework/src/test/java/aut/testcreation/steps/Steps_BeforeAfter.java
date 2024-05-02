package aut.testcreation.steps;

import framework.engine.bdd.CucumberBaseTestRunner;
import io.cucumber.java8.En;

public class Steps_BeforeAfter extends CucumberBaseTestRunner implements En {
    public Steps_BeforeAfter(){
        Before(1,CucumberBaseTestRunner::setUp);
        After(CucumberBaseTestRunner::tearDown);
    }
}
