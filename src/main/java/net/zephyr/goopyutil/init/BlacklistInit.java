package net.zephyr.goopyutil.init;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.util.GoopyBlacklist;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class BlacklistInit {
    public static void Init() throws IOException {
        //GoopyBlacklist.addToBlacklist(MinecraftClient.getInstance().getSession().getUsername(), MinecraftClient.getInstance().getSession().getUuid());
        //GoopyBlacklist.addToWhiteList(MinecraftClient.getInstance().getSession().getUsername(), MinecraftClient.getInstance().getSession().getUuid());

        /*GoopyBlacklist.addToBlacklist("OVDR", "fec3c5e6-3543-4eae-b0bc-cef14f4852a6");
        GoopyBlacklist.addToBlacklist("ChrisArrived", "4c96a213-9f20-461c-9764-2f1dd169a12a");
        GoopyBlacklist.addToBlacklist("ChrisHasLeft", "8e05329b-10f2-4b16-9234-d4251b8c9ae8");
        GoopyBlacklist.addToBlacklist("skybattlelover", "fb4cebb2-d5bc-475a-acd4-ac7d04e2ed65");
        GoopyBlacklist.addToBlacklist("Shallomilk", "1a7346ba-b2be-45be-89e9-d7af898967de");
        GoopyBlacklist.addToBlacklist("SlurpNR", "f99dd0f3-355f-49a4-a36b-24107d3f717a");
        GoopyBlacklist.addToBlacklist("daaalt", "8efd2c72-9929-47a3-bdc1-7fd243346a7b");
        GoopyBlacklist.addToBlacklist("EdmunnTB", "3b3f9323-ebd3-40a7-8fea-9866d72eac87");
        GoopyBlacklist.addToBlacklist("DEGincarnate", "ab65cf26-efdb-4a26-83e3-f2573f49962c");
        GoopyBlacklist.addToBlacklist("EpicFt", "30454c40-df74-4746-92f1-4e70dfd0c19a");
        GoopyBlacklist.addToBlacklist("ANYA_KUN", "2c77e398-352d-4870-91dd-c03fb6ce8b1f");*/

        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
            System.out.println(result);
        } catch(Exception ignore){

        }
        GoopyUtil.LOGGER.info("BLACKLIST initialized.");
    }
}
