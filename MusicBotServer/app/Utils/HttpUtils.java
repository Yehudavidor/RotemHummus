package Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by orr on 04/06/2017.
 */



public class HttpUtils {

    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    public static HttpResult doPost(String url, String data) throws IOException {
        return doPost(url,data,null);
    }

    public static HttpResult doPost(String url, String data, Map<String, String> headers) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }

        //httpPost.setHeader("server-auth-token", "JImdYBh6hTha94m2mz2r");
        if (data!=null) {
            httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        }
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != SC_OK) {
            //return null;
            return makeResponse(response.getStatusLine().getStatusCode(),null);
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return makeResponse(HttpResult.Status.OK,result.toString());

    }


    public static HttpResult doGet(String url, Map<String, String> headers) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }

//        httpGet.setHeader("server-auth-token", "JImdYBh6hTha94m2mz2r");

        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != SC_OK) {
            //return null;
            return makeResponse(response.getStatusLine().getStatusCode(),null);
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return makeResponse(response.getStatusLine().getStatusCode(),result.toString());
    }

    public static HttpResult doGet(String url, Map<String, String> headers, Map<String, String> queryParams) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (queryParams == null || queryParams.isEmpty()) {
            return doGet(url, headers);
        }
        url += "?";
        for (String paramName : queryParams.keySet()) {
            url += paramName + "=" + queryParams.get(paramName) + "&";
        }
        url = url.substring(0, url.length() - 1);
        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }

//        httpGet.setHeader("server-auth-token", "JImdYBh6hTha94m2mz2r");

        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != SC_OK) {
            //return null;
            return makeResponse(response.getStatusLine().getStatusCode(),null);
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return makeResponse(response.getStatusLine().getStatusCode(),result.toString());
    }

    static HttpResult makeResponse(int status, String data) {
        HttpResult res = new HttpResult();
        res.status = status;
        res.data = data;
        return res;
    }
}
