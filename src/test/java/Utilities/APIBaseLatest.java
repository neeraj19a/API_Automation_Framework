package Utilities;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utilities.APIConstant;
import utilities.PropertyReader;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class APIBaseLatest {
    //FOR LOGS
    public static Logger log = Logger.getLogger(APIBaseLatest.class);

    //For Report generation
    public static ExtentReports extentReport;
    public static ThreadLocal<ExtentTest> classLevelLog = new ThreadLocal<ExtentTest>(); //LHS Node create
    public static ThreadLocal<ExtentTest> testLevelLog = new ThreadLocal<ExtentTest>();   //Test case Log
    static StringWriter requestWriter;
    static PrintStream requestCapture;
    private RequestSpecBuilder builder = new RequestSpecBuilder();
    private String method;
    private String url;

    public APIBaseLatest() {

    }

    /**
     * APIBaseLatest constructor With Token to pass the initial settings for the the following method
     *
     * @param uri
     * @param method
     * @param token
     */
    public APIBaseLatest(String uri, String method, String token) {

        //Formulate the API url
        this.url = PropertyReader.getProperties().get("url") + uri;
        System.out.println("url-->" + this.url);
        log.info("URL-->" + this.url);

        this.method = method;

        if (token != null)
            builder.addHeader("Authorization", "Bearer " + token);
    }

    /**
     * APIBaseLatest constructor Without Token to pass the initial settings for the the following method
     *
     * @param uri
     * @param method
     */
    public APIBaseLatest(String uri, String method) {
        //Formulate the API url
        this.url = PropertyReader.getProperties().get("url") + uri;
        System.out.println("url-->" + this.url);
        log.info("URL-->" + this.url);

        this.method = method;

    }

    public static void testLogging(ResponseOptions<Response> responseResponseOptions, String method) {
        TestUtil.logInReport(responseResponseOptions.getHeaders());
        TestUtil.logInReport(requestWriter.toString(), method);
        TestUtil.logInReport(responseResponseOptions.getBody().asString());
        TestUtil.logTimeInReport(APIVerification.getResponseTime(responseResponseOptions));
    }

    /**
     * This function is used to generate the String of random length
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        final String charset = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    /**
     * This function is used to generate Integer of random length
     *
     * @param length
     * @return
     */
    public static String getRandomInteger(int length) {
        final String charset = "12345678910111213";
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    @BeforeSuite
    public void setUp() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        //call getExtent method present in Extentmanager class and send path of report to be generated
        // Extent Report: Create object for Extent Manager class and in initialize
        // report -Name...Location..
        extentReport = ExtentManager
                .GetExtent("src/main/testReport/TestReport.html");
        extentReport.setSystemInfo("OS", "Windows 10");
        extentReport.setSystemInfo("Host Name", "T460");
        extentReport.setSystemInfo("Environment", "Neeraj Bakhtani");
        extentReport.setSystemInfo("Report Name", "REST API Report");
    }

    //Create node in LHS class level in extent report
    @BeforeClass
    public synchronized void beforeClass() {
        //create class at lhs of the report for each class
        ExtentTest test = extentReport.createTest(getClass().getSimpleName());
        classLevelLog.set(test);
    }

    /**
     * ExecuteAPI to execute the API for GET/POST/DELETE
     *
     * @return ResponseOptions<Response>
     */
    public ResponseOptions<Response> ExecuteAPI() {
        RequestSpecification requestSpecification = builder.build();
        RequestSpecification request = RestAssured.given().log().all();
        request.contentType(ContentType.JSON);
        request.spec(requestSpecification);

        ResponseOptions<Response> responseResponseOptions;

        requestWriter = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(requestWriter));

        if (this.method.equalsIgnoreCase(APIConstant.ApiMethods.POST)) {
            responseResponseOptions = request.filter(new RequestLoggingFilter(requestCapture)).post(this.url);
            requestCapture.flush();

            testLogging(responseResponseOptions, APIConstant.ApiMethods.POST);

            return responseResponseOptions;
        } else if (this.method.equalsIgnoreCase(APIConstant.ApiMethods.DELETE)) {
            responseResponseOptions = request.filter(new RequestLoggingFilter(requestCapture)).delete(this.url);
            requestCapture.flush();

            testLogging(responseResponseOptions, APIConstant.ApiMethods.DELETE);
            return responseResponseOptions;

        } else if (this.method.equalsIgnoreCase(APIConstant.ApiMethods.GET)) {
            responseResponseOptions = request.filter(new RequestLoggingFilter(requestCapture)).get(this.url);
            requestCapture.flush();

            testLogging(responseResponseOptions, APIConstant.ApiMethods.GET);
            return responseResponseOptions;

        } else if (this.method.equalsIgnoreCase(APIConstant.ApiMethods.PUT)) {
            responseResponseOptions = request.filter(new RequestLoggingFilter(requestCapture)).put(this.url);
            requestCapture.flush();

            testLogging(responseResponseOptions, APIConstant.ApiMethods.PUT);
            return responseResponseOptions;
        }

        return null;
    }

    /**
     * Authenticate to get the token variable
     *
     * @param body
     * @return string token
     */
    //Object can be anything a HashMap or POJO
    public String Authenticate(Object body) {
        builder.setBody(body);
        return ExecuteAPI().getBody().jsonPath().get("access_token");
    }

    /**
     * Executing API with query params being passed as the input of it
     *
     * @param queryPath
     * @return Reponse
     */
    public ResponseOptions<Response> ExecuteWithQueryParams(Map<String, String> queryPath) {
        builder.addQueryParams(queryPath);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithPathParams
     *
     * @param pathParams
     * @return
     */
    public ResponseOptions<Response> ExecuteWithPathParams(Map<String, String> pathParams) {
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    /**
     * ExecuteWithPathParamsAndBody
     *
     * @param pathParams
     * @param body
     * @return
     */
    public ResponseOptions<Response> ExecuteWithPathParamsAndBody(Map<String, String> pathParams, Object body) {
        builder.setBody(body);
        builder.addPathParams(pathParams);
        return ExecuteAPI();
    }

    public ResponseOptions<Response> ExecuteWithBody(Object body) {

        builder.setBody(body);
        return ExecuteAPI();
    }

    @BeforeMethod
    public synchronized void beforeMethod(Method method) {
        //log info in report
        ExtentTest child = classLevelLog.get().createNode(method.getName());
        testLevelLog.set(child);
        log.info("==================================================================================" + " \n");
        log.info("Execution of Test Case:- " + method.getName() + " started");
        testLevelLog.get().log(Status.INFO, " Execution of Test Case:- " + method.getName() + " started");
    }

    @AfterMethod
    protected void afterMethod(ITestResult result) {

      /*  System.out.println("Execution of method:- " + result.getName() + " FINISHED");

        if (result.getStatus() == ITestResult.FAILURE) {
            //ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            //ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
        } else {
            //ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
        }
*/
        //Log details in Extent report

        log.info("Execution of Test Case:- " + result.getName() + " finished" + "\n");
        log.info("==================================================================================" + " \n");
        testLevelLog.get().log(Status.INFO, "<b>" + "Execution of Test Case:- " + result.getName() + " finished" + "<b>");
    }

    @AfterSuite
    public void tearDownFramework() {
        extentReport.flush();
    }

    /**
     * @param t
     * @return
     */
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public void jsonSchemaValidator(ResponseOptions<Response> response, String json) {
        //Json Schema Validator
        try {
            assertThat(response.getBody().asString(), matchesJsonSchemaInClasspath(json));
            testLevelLog.get().pass("JSON Schema of " + json + " is valid ");
        } catch (AssertionError e) {
            testLevelLog.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" +"JSON Schema of " + json + " is not valid ,Pls Check "+"</font>" + "</b >" + "</summary>" + e.fillInStackTrace()+ "</details>");
        }
        catch (Exception e){
            testLevelLog.get().fail("Exception occurred "+e.fillInStackTrace());
        }
    }
}
