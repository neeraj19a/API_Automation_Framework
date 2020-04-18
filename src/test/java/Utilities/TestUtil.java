package Utilities;

import io.restassured.http.Headers;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;

public class TestUtil extends APIBaseLatest {

    public TestUtil(String uri, String method) {
        super(uri, method);
    }

    public static void logInReport(Object object) {
        if (object instanceof RestAssuredResponseImpl) {
            Response obj = (Response) object;

            log.info("API Response" + " \n" + obj.getBody().asString());

            testLevelLog.get().info("<details>" + "<summary>" + "<b>" + "<font color=" + "green>" + "API Response"
                    + "</font>" + "</b >" + "</summary>" + obj.getBody().asString() + "</details>"
                    + " \n");
        } else if (object instanceof Headers) {
            log.info("Headers are here" + " \n" + Headers.class.cast(object) + "\n" + "\n");

            testLevelLog.get().info("<details>" + "<summary>" + "<b>" + "<font color=" + "green>" + "Headers are here"
                    + "</font>" + "</b >" + "</summary>" + Headers.class.cast(object) + "</details>"
                    + " \n");
        }
        /*else {
            testLevelLog.get().info("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Don't Know Which class Pls Check"
                    + "</font>" + "</b >" + "</summary>" + object + "</details>"
                    + " \n");
        }*/
    }

    public static void logInReport(String string) {
        if (string.contains("Error")) {

            log.error("Errors are here" + " \n" + string);
            testLevelLog.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Error Occurred "
                    + "</font>" + "</b >" + "</summary>" + string + "</details>"
                    + " \n");
        } else {
            log.info("Response is Here" + " \n" + string);
            testLevelLog.get().info("<details>" + "<summary>" + "<b>" + "<font color=" + "green>" + " Response is Here "
                    + "</font>" + "</b >" + "</summary>" + string + "</details>"
                    + " \n");
        }
    }

    public static void logInReport(Object object, String method) {
        if (object.toString().contains(method)) {

            log.info("Request is Here" + " \n" + String.class.cast(object));
            testLevelLog.get().info("<details>" + "<summary>" + "<b>" + "<font color=" + "green>" + method + " Request is Here "
                    + "</font>" + "</b >" + "</summary>" + String.class.cast(object) + "</details>"
                    + " \n");

        }
    }

    public static void logTimeInReport(Long time) {

        log.info("API Response Time is " + time);
        testLevelLog.get().info("<summary>" + "<b>" + "<font color=" + "green>" + " API Response Time is  " + time +
                "</font>" + "</b >" + "</summary>"
                + " \n");


    }

    //To archieve report
   /* public static void archiveTestReport() {

        String reportName = "TestReport.html";

        //Existing test report to be archeved
        String lastTestReportFilePath = "/src/test/TestReport/TestReport.html";

        //Location where report is archieved
        String archiveReportPath = System.getProperty("user.dir") + "/src/test/archievedReport/";

        Date date = new Date();
        SimpleDateFormat dateFormate = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String formatedDate = dateFormate.format(date);
        String archiveTestReportName = formatedDate + "_" + reportName;

        File oldReport = new File(lastTestReportFilePath);

        File newFile = new File(archiveReportPath + archiveTestReportName);

        System.out.println(oldReport.exists());

        if (oldReport.exists()) {
            System.out.println("inside if");
            oldReport.renameTo(newFile);
            oldReport.delete();
        }

    }*/


    //To check ID is present method
  /*  public static boolean checkJsonHasKey(String key, Response response) {
        JSONObject json = new JSONObject(response.asString());     //store response in json
        return json.has(key); //check this key is present in response
    }
*/
}