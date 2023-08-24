package net.glasslauncher.wrapper;

import java.net.*;

public class WrapperProtocolFactory implements URLStreamHandlerFactory {
    public static void setup() {
        URL.setURLStreamHandlerFactory(new WrapperProtocolFactory());
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("http".equals(protocol)) {
            return new WrapperProtocolHandler();
        }
        return null;
    }
}
