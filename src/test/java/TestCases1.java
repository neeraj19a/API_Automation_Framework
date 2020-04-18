import Utilities.APIBaseLatest;
import Utilities.APIVerification;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.testng.annotations.Test;
import pojo.Users;
import utilities.APIConstant;

import static Utilities.APIVerification.responsecodeValidation;

public class TestCases1 extends APIBaseLatest {
    public static ResponseOptions<Response> response;
    public static APIVerification apiVerification;
    public static String token;
    public static APIBaseLatest apiBaseLatest;
    public static String uri;

    @Test
    public void getListOfUsers(){
        uri = APIConstant.APIPaths.GET_LIST_OF_USERS;

        apiBaseLatest = new APIBaseLatest(uri, "GET");
        response = apiBaseLatest.ExecuteAPI();

        responsecodeValidation(response, 200);

       jsonSchemaValidator(response,"users.json");
    }

    @Test
    public void postUser(){
        uri=APIConstant.APIPaths.POST_USER;

        apiBaseLatest=new APIBaseLatest(uri,"POST");

        Users users=Users.builder().name("Neeraj").job("QA Lead").build();
        response=apiBaseLatest.ExecuteWithBody(users);

        responsecodeValidation(response,201);

        jsonSchemaValidator(response,"user.json");
    }
}
