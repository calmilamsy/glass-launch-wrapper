package net.glasslauncher.wrapper;

import java.net.*;

public class WrapperProtocolFactory implements URLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("http".equals(protocol)) {
            return new WrapperProtocolHandler();
        }
        return null;
    }
}
