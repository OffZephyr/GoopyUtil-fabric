package net.zephyr.goopyutil.init;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.util.GoopyBlacklist;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class BlackWhitelistInit {
    public static void Init() throws IOException {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/OffZephyr/GoopyUtil-Public/main/BlackAndWhiteList.json");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
        JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
        JsonArray blacklist = obj.get("Blacklist").getAsJsonArray();
        JsonArray whitelist = obj.get("Whitelist").getAsJsonArray();
        for(JsonElement user : blacklist){
            String username = user.getAsJsonArray().get(0).getAsString();
            String uuid = user.getAsJsonArray().get(1).getAsString();

            GoopyBlacklist.addToBlacklist(username, uuid);
        }
        for(JsonElement user : whitelist){
            String username = user.getAsJsonArray().get(0).getAsString();
            String uuid = user.getAsJsonArray().get(1).getAsString();

            GoopyBlacklist.addToWhitelist(username, uuid);
        }

        GoopyUtil.LOGGER.info("BLACKLIST initialized.");
    }
}
