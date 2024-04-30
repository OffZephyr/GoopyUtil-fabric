package net.zephyr.goopyutil.util;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class GoopyBlacklist {
    public static Map<String, String> Blacklist = new HashMap<>();
    public static Map<String, String> Whitelist = new HashMap<String,String>() {{
        put("ZephyrOff", "968e10ad-8ff8-40e0-9db5-f6ee94ee3a04");
        put("_ShadowLink_", "820a388b-24a8-44a5-b5de-256b09471eea");
        put("04beph", "aa4b117e-3f46-4a5a-aaf7-c75ab7b42a79");
        put("mephilis12", "f76d371b-97e7-4891-b0ca-799d844215f4");
        put("TheMintyTea", "cf9a00e6-2ec2-4233-9f6f-7b36aaf744ff");
        put("thunderingstatic", "65b7d891-9ab4-4e20-a005-af3077fbb3d1");
        put("MrFazbear000", "b4e5541f-1355-4ebc-8a87-90325ec77a7e");
        put("Drloganblox07", "c5e4869c-c0d9-4730-a400-359bd6482d19e");
        put("DasenSenju", "ca4e92b4-700d-49fc-976e-9d460291f5c2");
        put("S0YSAUCE_", "7510b77e-d085-4d73-8f9e-db37c5342bba");
        put("NazNodji", "1b9334ae-cac6-481b-827e-39368680f30d");
        put("KaiTheRaccoon", "6934b72c-22f3-4896-a55c-4ad47dbf1f46");

    }};
    public static void addToBlacklist(String username, String UUID) {
        Blacklist.put(username, UUID);
    }

    public static Map<String, String> getBlacklist() {
        return Blacklist;
    }
    public static Map<String, String> getWhitelist() {
        return Whitelist;
    }
}

