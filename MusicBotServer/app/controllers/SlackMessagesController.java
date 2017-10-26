package controllers;

import bot.SlackMessageHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SlackMessagesController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result handleSlackMessage() {
        JsonNode body = request().body().asJson();
        if (body.get("challenge") != null) {
            return approveSlack(body);

        }
        String text = "";
        String channel = "";
        String token = "";
        if (body.get("event") != null) {
            //System.out.println("text is: " + body.get("text").asText());
            JsonNode event = Json.toJson(body.get("event"));
            if (event.get("subtype") != null) {
                System.out.println("Got something from the bot");
                if (event.get("subtype").asText().equals("bot_message")) {
                    System.out.println("ignoring this message");
                    return ok();
                }
            }
            text = event.get("text").asText();
            if (event.get("channel") != null) {
                channel = event.get("channel").asText();
            }
        }
        class ReceiveMessage implements Runnable {
            String userInput;
            String channel;
            String token;
            ReceiveMessage(String userInput, String channel, String token) {
                this.userInput = userInput;
                this.channel = channel;
                this.token = token;
            }
            public void run() {
                receiveMessage(userInput, channel, token);
            }
        }
        Thread t = new Thread(new ReceiveMessage(text, channel, token));
        t.start();
        //SlackMessageHandler handler = new SlackMessageHandler();
        //boolean answer = handler.handleSlackMessage(text, channel, body.get("token").asText());
        return (ok().withHeader("X-Slack-No-Retry", "1"));
//        HashMap<String, String> answer = new HashMap<>();
//        answer.put("text", "you said: " + text);
//        return ok(Json.toJson(answer));

    }

    private Result approveSlack(JsonNode body) {
        String challenge = "";
        if (body.get("challenge") != null) {
            System.out.println("challenge is: " + body.get("challenge").asText());
            challenge = body.get("challenge").asText();
        }
        final ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("challenge", challenge);
        return ok(Json.toJson(result));
    }

    private void receiveMessage(String text, String channel, String token) {
        SlackMessageHandler handler = new SlackMessageHandler();
        boolean answer = handler.handleSlackMessage(text, channel, token);
        if (!answer) {
            System.out.println("Failed sending message to the user");
        }
        return;

    }

}
