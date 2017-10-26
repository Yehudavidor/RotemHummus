package controllers;

import Utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ConnectionController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result approveSlack() {
        String redirectUrl = "https://56c3ab79.ngrok.io/approve";
        String clientId = "262860178118.262038424676";
        String clientSecret = "59b9fbb55b8580dd8a58385d45b882d9";
        String accessToken = "";
        String url = "https://slack.com/api/oauth.access";
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("code", request().getQueryString("code"));
        queryParams.put("client_id", clientId);
        queryParams.put("client_secret", clientSecret);
        queryParams.put("redirect_uri", redirectUrl);
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("content-type", "application/json");
            HttpUtils.doGet(url, headers, queryParams);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        uri: 'https://slack.com/api/oauth.access?code='
//                +req.query.code+
//                '&client_id='+process.env.CLIENT_ID+
//                '&client_secret='+process.env.CLIENT_SECRET+
//                '&redirect_uri='+process.env.REDIRECT_URI,
//                method: 'GET'
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("access_token", accessToken);
        result.put("client_id", clientId);
        result.put("client_secret", clientSecret);
        return ok(Json.toJson(result));

    }

}
