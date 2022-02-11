package net.glasslauncher.wrapper;

import net.fabricmc.loader.impl.launch.knot.KnotClient;

import java.net.*;

public class LegacyWrapper {
    public static String UUID;
    public static String USERNAME;
    public static String TOKEN;

    public static void main(String[] args) {
        UUID = args[0];
        USERNAME = args[1];
        TOKEN = args[3];
        URL.setURLStreamHandlerFactory(new WrapperProtocolFactory());
        KnotClient.main(args);
    }
}
