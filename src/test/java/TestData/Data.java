package TestData;

import org.testng.annotations.DataProvider;

public class Data {


    @DataProvider(name = "test")
    public Object[][] testingData() {
        return new Object[][]{{"bruno@email.com", "bruno"}};

    }
}
