package Utilities;


import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import utilities.PropertyReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;


public class APIBase {

    private static String url = "";
    public String apiUrl = null;
    protected Log logger;
    Database db = new Database();

    public APIBase() {
        if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
            System.setProperty("homePath", "/home/quikr");
        } else {
            System.setProperty("homePath", "/Users/apple");
        }

        logger = LogFactory.getLog(this.getClass().getName());
        PropertyReader propertyReader = new PropertyReader();
        String carApiEnv = System.getProperty("env") == null ? propertyReader.getProperties().get("CarApiEnv") : System.getProperty("env");
        if (carApiEnv.equals("stage")) {
            apiUrl = propertyReader.getProperties().get("carApiFeatureBranch");
        } else if (carApiEnv.equals("prod")) {
            apiUrl = propertyReader.getProperties().get("carApiProduction");
        }

        logger.info("Triggering Cars Api Automation on -->" + carApiEnv + " enviornment and Url-->" + apiUrl);
    }

    public static String getCurrentUrl() {
        return url;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject parseResponse(HttpResponse response) throws Exception {
        int statusCode;

        JSONObject jsonObject = new JSONObject();

        statusCode = response.getStatusLine().getStatusCode();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line = "";

            while ((line = br.readLine()) != null)
                result.append(line);

            jsonObject.put("RESPONSE", result.toString());
            jsonObject.put("STATUS_CODE", String.valueOf(statusCode));
        } catch (Exception e) {
            jsonObject.put("RESPONSE", e.getMessage());
            jsonObject.put("STATUS_CODE", String.valueOf(statusCode));
        }

        return jsonObject;
    }

    public static boolean isValidJson(Object object) throws Exception {
        String jsonData = object.toString();

        if (JSONValue.parse(jsonData) != null)
            return true;

        return false;
    }

    public static JSONObject getJSONBody(String body) throws Exception {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(body);

        return jsonObject;
    }

    public static JSONObject getJsonResponseObject(JSONObject jsonObject) throws Exception {
        Set keys = jsonObject.keySet();
        Iterator a = keys.iterator();
        String value = null;
        while (a.hasNext()) {

            String key = (String) a.next();
            if (key.contains("RESPONSE")) {
                value = (String) jsonObject.get(key);
            }
        }
        System.out.println(value);
        JSONObject jsonObject1 = new JSONObject(value);

        return jsonObject1;
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

    public static String callURL() {
        String myURL = "http://192.168.124.33:9211/new_cars_v3/_search?q=car_make:Honda&_source_include=similarityScore,make_id&from=0&size=2";

        System.out.println("Requeted URL:" + myURL);
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + myURL, e);
        }

        return sb.toString();
    }

    public static Response getByUrl(String url) {
        //  RestUtil.url=url;
        Response response = RestAssured.
                given()
                .log()
                .all()
                .get(url);
        return response;
    }

    public static Response postByUrl(String url) {
        //RestUtil.url=url;
        Response response = RestAssured.
                given()
                .log()
                .all()
                .post(url);
        return response;
    }

    public static Response postByUrl(String url, String body) {
        //   RestUtil.url=url;
        Response response = RestAssured.
                given()
                .log()
                .all()
                .body(body)
                .post(url);
        return response;
    }

    public static Response putByUrl(String url) {
        //  RestUtil.url=url;
        Response response = RestAssured.
                given()
                .log()
                .all()
                .put(url);
        return response;
    }

    public static Response putByUrl(String url, String body) {
        // RestUtil.url=url;
        Response response = RestAssured.
                given()
                .log()
                .all()
                .body(body)
                .put(url);
        return response;
    }

    public static Response postCallJsonMultipartUrl(String url, String file, String mimeType,
                                                    HashMap<String, String> headers, Map<String, String> params) {
        //  RestUtil.url=url;
        Response response = RestAssured
                .given()
                .headers(headers)
                .multiPart("file", new File(file), mimeType)
                .formParams(params)
                .log()
                .all()
                .when()
                .post(url);
        return response;
    }

    public static Response getCallFormUrl(String url, HashMap<String, String> headers, Map<String, String> params) {
        // RestUtil.url=url;
        Response response = RestAssured
                .given()
                .headers(headers)
                .log()
                .all()
                .formParams(params)
                .get(url);
        return response;
    }

    public static Response putCallJsonUrl(String url, HashMap<String, String> headers, String body) {
        //  RestUtil.url=url;
        Response response = RestAssured.
                given()
                .headers(headers)
                .log()
                .all()
                .body(body)
                .put(url);
        return response;
    }

    public static Response postCallJsonUrl(String url, HashMap<String, String> headers, String body) {
        //  RestUtil.url=url;
        Response response = RestAssured.given()
                .headers(headers)
                .body(body)
                .log()
                .all()
                .when()
                .post(url);
        return response;
    }

    public static Response getCallUrl(String url, HashMap<String, String> headers) {
        //   RestUtil.url=url;
        Response response = RestAssured.
                given()
                .headers(headers)
                .log()
                .all()
                .get(url);
        return response;
    }

    public static HashMap<String, Object> getDataString(Response response, String[] param) throws JsonException {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        String responseReceived = response.body().asString();
        System.out.println("responseReceived: " + responseReceived);
        JSONObject jsonObject = new JSONObject(responseReceived);
        for (int i = 0; i < param.length; i++) {
            hm.put(param[i], getResponseParam(jsonObject, param[i]));
        }
        return hm;
    }

    public static String getResponseParam(Response response, String param) throws JsonException {
        String responseReceived = response.body().asString();
        System.out.println("responseReceived: " + responseReceived);
        JSONObject jsonObject = new JSONObject(responseReceived);
        JSONObject object = (JSONObject) jsonObject.get("payload");
        String datavalue = object.getString(param);
        System.out.println("datavalue: " + datavalue);
        return datavalue;
    }

    public static String getResponseParam(JSONObject jsonObject, String param) throws JsonException {
        System.out.println("jsonObject: " + jsonObject);
        String resparam = jsonObject.get(param).toString();
        return resparam;
    }

    public JSONObject executeGet(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = null;

        get.addHeader("X-Quikr-Client", "cars");

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            response = client.execute(get);
        } catch (Exception e) {
            logger.info("Request : " + url + " FAILED ");
        }

        return parseResponse(response);
    }

    public JSONObject executePost(String url, String body) throws Exception {
        HttpPost post = new HttpPost(url);
        HttpResponse response = null;

        post.addHeader("X-Quikr-Client", "cars");

        if (isValidJson(body))
            post.setEntity(new StringEntity(body));

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            response = client.execute(post);
        } catch (Exception e) {
            logger.info("Request : " + url + " FAILED ");
        }

        return parseResponse(response);
    }

    public String body(String path) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line = "";

            while ((line = br.readLine()) != null)
                result.append(line);
        } catch (Exception e) {
            result.append("Error : " + e.getMessage());
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (Exception e) {
                logger.info("Error while closing the file : " + e.getMessage());
            }
        }

        return result.toString();
    }

    public String returnDynamicParameter(String response, String dynamicKeyValue) {

        String ok = response;
        int a = ok.indexOf(dynamicKeyValue);
        String hello = ok.substring(a, ok.length()).replaceAll("[}}}:.\"]", "");
        String my = hello.replaceAll(dynamicKeyValue, "");
        int index = my.indexOf(",");
        String valueofDyamicKey = my.substring(0, index);
        System.out.println(valueofDyamicKey);
        return valueofDyamicKey;
    }

    public ArrayList returnListofValues(String response, String key1, String key2, String value) {
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject lineItems = jsonObject1.getJSONObject(key1);
        JSONArray jsonArray = lineItems.getJSONArray(key2);
        ArrayList<String> ls = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject rec = jsonArray.getJSONObject(i);
            ls.add(rec.getString(value));
        }
        return ls;
    }

    public String checkifArray(JSONObject jsonObject, String value) {
        System.out.println("Here is reponse recieved from jsonObject Checker" + jsonObject);
        Set keys = jsonObject.keySet();
        Iterator a = keys.iterator();
        String key2 = null;
        while (a.hasNext()) {

            key2 = (String) a.next();
            break;
        }
        ArrayList<String> ls = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key2);
            if (jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject rec = jsonArray.getJSONObject(i);
                    System.out.println("Here in Json Array");
                    ls.add(rec.getString(value));
                    System.out.println(ls.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Looks like its not Array, Checking for Json Object Now");
            JSONObject jsonObject2 = new JSONObject(ls.toString());
            JSONObject jsonObject1 = checkifJsonObject(jsonObject2, "identifier");
            ls.add(jsonObject1.toString());
        }


        //JSONObject jsonObject2=new JSONObject(ls.toString());
        return ls.toString();

    }

    public JSONObject checkifJsonObject(JSONObject jsonObject, String value) {
        Set keys = jsonObject.keySet();
        Iterator a = keys.iterator();
        String key1 = null;
        while (a.hasNext()) {

            key1 = (String) a.next();
            break;
        }
        System.out.println(value);

        Object lineItems = jsonObject.get(key1);
        ArrayList<String> ls = new ArrayList<>();
        JSONObject urlObject = (JSONObject) lineItems;
        System.out.println("Here is URL Object" + urlObject);
        try {
            String value1 = urlObject.getString(value);
            System.out.println("Here is Json Object");
            ls.add(value1);

        } catch (Exception e) {
            System.out.println("Looks like value is not present in Json Object");
        }
        //String jsonrepsone=ls.toString();
        //JSONObject jsonObject=new JSONObject("{"+jsonrepsone);

        return urlObject;
    }

    public ArrayList returnListofValuesfromArray(String response, String key1, int key2, String value) {
        JSONObject jsonObject1 = new JSONObject(response);
        Object lineItems = jsonObject1.get(key1);
        ArrayList<String> ls = new ArrayList<>();
        if (lineItems instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) lineItems;
            jsonArray = ((JSONArray) lineItems).getJSONArray(key2);

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject rec = jsonArray.getJSONObject(i);
                System.out.println("Here in Json Array");
                ls.add(rec.getString(value));
            }

        } else {
            JSONObject urlObject = (JSONObject) lineItems;
            System.out.println("Here is URL Object" + urlObject);
            String value1 = urlObject.getString(value);
            System.out.println("Here is Json Object");
            ls.add(value1);
        }
        return ls;
    }

    public String testreturnvalue(JSONObject jsonObject, String value) {

        //JSONObject jsonObject=new JSONObject();
        String values = null;
        jsonObject = checkifJsonObject(jsonObject, value);
        if (jsonObject != null) {
            System.out.println("Going in Json Array");
            values = checkifArray(jsonObject, value);
            return values;
        }

        return jsonObject.toString();
    }

}