import TestData.Data;
import Utilities.APIBaseLatest;
import Utilities.APIVerification;
import Utilities.TestUtil;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.testng.annotations.Test;
import pojo.LoginBody;
import pojo.Posts;
import utilities.APIConstant;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class TestCases extends APIBaseLatest {

    public static ResponseOptions<Response> response;
    public static APIVerification apiVerification=new APIVerification();
    public static String token;
    public static APIBaseLatest apiBaseLatest;
    public static String uri;

    @Test(dataProvider = "test", dataProviderClass = Data.class)
    public void getOperation(String email, String password) {
        LoginBody loginBody = LoginBody.builder().email(email).password(password).build();

        uri = APIConstant.APIPaths.GET_LISTS_OF_POSTS;
        /*apiBaseLatest = new APIBaseLatest(uri, APIConstant.ApiMethods.POST, null);
        token = apiBaseLatest.Authenticate(loginBody);
        TestUtil.logInReport("Token"+token);
*/
        apiBaseLatest = new APIBaseLatest(uri, "GET");
        response = apiBaseLatest.ExecuteAPI();

        apiVerification.responsecodeValidation(response, 200);

        TestUtil.logInReport("Author is-->" + response.getBody().jsonPath().get("author"));

        //Using JSON Path
        List<String> actualResponse = response.getBody().jsonPath().get("author");

        apiVerification.responseKeyValidationfromArray(response, "author");

        //With Lombok Annotations
        Posts posts1 = response.getBody().as(Posts[].class)[0];
        assertThat(posts1.getAuthor(), notNullValue());

        //JSON Schema Validator
        jsonSchemaValidator(response, "post.json");
    }

    @Test
    public void PostDeleteAndGetOperation() {

        //Using POJO to Set Body
        Posts posts = Posts.builder().id("100").title("Test Automation").author("Neeraj").build();

        uri = APIConstant.APIPaths.GET_LISTS_OF_POSTS;
        apiBaseLatest = new APIBaseLatest(uri, "POST");

        response = apiBaseLatest.ExecuteWithBody(posts);

        apiVerification.responsecodeValidation(response, 201);

        apiVerification.responseKeyValidationfromJsonObject(response, "title");

        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("postId", "100");

        uri = APIConstant.APIPaths.GET_SINGLE_POSTS;
        apiBaseLatest = new APIBaseLatest(uri, "DELETE");
        response = apiBaseLatest.ExecuteWithPathParams(pathParams);
        apiVerification.responsecodeValidation(response, 200);

        HashMap<String, String> pathParams1 = new HashMap<String, String>();
        pathParams1.put("postId", "100");

        uri = APIConstant.APIPaths.GET_SINGLE_POSTS;
        apiBaseLatest = new APIBaseLatest(uri, "GET");
        response = apiBaseLatest.ExecuteWithPathParams(pathParams1);
        apiVerification.responsecodeValidation(response, 404);
    }


    @Test
    public void postPutGetopration() {

        Posts posts = Posts.builder().id("27").title("Test Automation").author("Neeraj").build();
        uri = APIConstant.APIPaths.GET_LISTS_OF_POSTS;

        APIBaseLatest apiBaseLatest = new APIBaseLatest(uri, "POST");
        response = apiBaseLatest.ExecuteWithBody(posts);
        apiVerification.responsecodeValidation(response, 201);

        posts = Posts.builder().id("27").title("Testing").author("Neeraj").build();

        HashMap<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("postId", "27");

        uri = APIConstant.APIPaths.GET_SINGLE_POSTS;
        apiBaseLatest = new APIBaseLatest(uri, "PUT");
        response = apiBaseLatest.ExecuteWithPathParamsAndBody(pathParams, posts);

        apiVerification.responsecodeValidation(response, 200);

        apiVerification.assertContent(posts, (Response) response);
        apiVerification.jsonSchemaValidator(response,"post.json");
        pathParams.clear();
        pathParams.put("postId", "27");
        uri = APIConstant.APIPaths.GET_SINGLE_POSTS;

        apiBaseLatest = new APIBaseLatest(uri, "GET");
        response = apiBaseLatest.ExecuteWithPathParams(pathParams);
    }
}