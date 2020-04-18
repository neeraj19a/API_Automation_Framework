package Utilities;

import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class APIVerification extends APIBaseLatest {

    JsonProcessing data = new JsonProcessing();

    public static void responsecodeValidation(ResponseOptions<Response> response, int statusCode) {
        try {
            Assert.assertEquals(statusCode, response.getStatusCode());
            testLevelLog.get().pass("PASS Successfully Validated the Status Code  " + response.getStatusCode());
        } catch (AssertionError error) {
            testLevelLog.get().fail("<summary>" + "<b>" + "<font color=" + "red>" + "FAIL Expected Status Code is :: " + statusCode + " instead of getting :: " + response.getStatusCode() + "</font>" + "</b >" + "</summary>");
        } catch (Exception e) {
            testLevelLog.get().fail("FAIL" + e.fillInStackTrace());
        }
    }


    public static void responseKeyValidationfromArray(ResponseOptions<Response> response, String key) {
        try {
            JSONArray jsonArray = new JSONArray(response.getBody().asString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                testLevelLog.get().pass("PASS ,VALIDATED values are " + jsonObject.get(key));
            }
        } catch (Exception e) {
            testLevelLog.get().fail("FAIL " + e.fillInStackTrace());
        }
    }


    public static void responseKeyValidationfromJsonObject(ResponseOptions<Response> response, String key) {
        try {
            JSONObject jsonObject = new JSONObject(response.getBody().asString());
            if (jsonObject.has(key) && jsonObject.get(key) != null) {
                testLevelLog.get().pass("PASS ,Successfully Validated Value of " + key + " and the value is " + jsonObject.get(key));
            } else {
                testLevelLog.get().fail("Key is not Available");
            }
        } catch (Exception e) {
            testLevelLog.get().fail(e.fillInStackTrace());
        }

    }

    public static Long getResponseTime(ResponseOptions<Response> response) {
        long time = response.timeIn(TimeUnit.MILLISECONDS);
        return time;
    }

    public void assertContent(Object postModel, Response response) {
        Map<?, ?> actualResponseBody = response.jsonPath().get();
        log.info("Actual Response Content:" + actualResponseBody);

        Map<?, ?> expectedResponseBody = data.ConvertModelToMap(postModel);
        log.info("Expected Response Content:" + expectedResponseBody);

        try {
            Assert.assertEquals(expectedResponseBody, actualResponseBody);
            log.info("PASS Validated Expected and Actual Response");
            testLevelLog.get().pass("PASS Validated Expected and Actual Response");
        } catch (AssertionError assertionError) {
            testLevelLog.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Mismatch In Expected and Actual Response </font>" + "</b >" + "</summary>" + "Expected is \n " + expectedResponseBody + " \n and Actual Body is \n " + actualResponseBody + "</details>");
        } catch (Exception e) {
            testLevelLog.get().fail("FAIL" + e.fillInStackTrace());
        }
    }

}
