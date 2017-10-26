package bot;

import Utils.HttpUtils;
import Utils.HttpResult;
import ai.api.*;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SlackMessageHandler {
    private String text;
    // hard coded path to publish to the bot
    private final String path = "https://hooks.slack.com/services/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";
    private final String path2 = "https://slack.com/api/chat.postMessage";
    private final String userAuthToken = "xoxb-263081292471-dUKeF3xuh0MZvTZI6HarOLdR";
    private HashMap<String, String> userToEndpoint;
    private ArrayList<String> tmpList;
    private final String apiKey = "b08beebca6ab4cb2ba0ddb7a2c1ab52c";
    private AIDataService aiDataService;
    private String lastMessageTS;
    public SlackMessageHandler() {
        userToEndpoint = new HashMap<>();
        userToEndpoint.put("D7PV9BRB4", "https://hooks.slack.com/services/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"); // my bot
        userToEndpoint.put("D7PV9BT6W", "https://hooks.slack.com/services/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"); //hadar
        userToEndpoint.put("muli", "https://hooks.slack.com/services/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");
        userToEndpoint.put("D7QRVUA2J", "https://hooks.slack.com/services/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");
        AIConfiguration config = new AIConfiguration(apiKey);
        this.aiDataService = new AIDataService(config);
        tmpList = new ArrayList<>();

    }

    public boolean handleSlackMessage(String msg, String channel, String token, String timestamp) {
        System.out.println("Handling message from user");
        String message = msg;
        // create parameters
        HashMap<String, String> parameters = new HashMap<>();
        if (!channel.equals("")) {
            parameters.put("channel", channel);
        }
        parameters.put("text", getAiResponse(msg));
        parameters.put("token", userAuthToken);
        String url = userToEndpoint.get(channel);
        if (url == null) {
            url = path;
        }
        tmpList.add(msg);
        if (msg.equals("exit")) {
            tmpList = new ArrayList<>();
        }
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("content-type", "application/json");
            if (lastMessageTS != null && lastMessageTS.equals(timestamp)) {
                System.out.println("Got same message from user, ignoring");
                return true;
            }
            setLastMessageTS(timestamp);
            HttpResult result = HttpUtils.doPost(url, new Gson().toJson(parameters), headers);
            return result.status == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
//
    private String getAiResponse(String userInput) {
        AIRequest request = new AIRequest(userInput);
        AIServiceContext serviceContext = AIServiceContextBuilder.buildFromSessionId(UUID.randomUUID().toString());
        try {
            AIResponse response = aiDataService.request(request, serviceContext);
            if (response != null) {
                if (response.getResult() != null && response.getResult().getFulfillment() != null) {
                    return response.getResult().getFulfillment().getSpeech();
                }
            }
        }
        catch(AIServiceException e){
                return "I can't connect to API.AI at the moment, please try again later :(";
            }
            return "";
    }

    public String getLastMessageTS() { return this.lastMessageTS; }

    public void setLastMessageTS(String ts) {
        this.lastMessageTS = ts;
    }



}
