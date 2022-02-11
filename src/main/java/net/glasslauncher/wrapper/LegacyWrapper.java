package net.glasslauncher.wrapper;

import net.fabricmc.loader.launch.knot.KnotClient;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.net.*;
import java.util.*;

public class LegacyWrapper {
    public static String UUID;
    public static String USERNAME;
    public static String TOKEN;

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        Option usernameOption = Option.builder("u").hasArg().desc("Username").longOpt("username").build();
        Option sessionOption = Option.builder("i").hasArg().desc("UUID").longOpt("uuid").build();
        Option uuidOption = Option.builder("s").hasArg().desc("Session").longOpt("session").build();
        options.addOption(usernameOption);
        options.addOption(sessionOption);
        options.addOption(uuidOption);
        DefaultParser parser = new DefaultParser() {
            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException {
                final List<String> knownArgs = new ArrayList<>();
                for (int i = 0; i < arguments.length; i++) {
                    if (options.hasOption(arguments[i])) {
                        knownArgs.add(arguments[i]);
                        if (options.getOption(arguments[i]).hasArg() && i + 1 < arguments.length) {
                            i++;
                            knownArgs.add(arguments[i]);
                        }
                    }
                }
                return super.parse(options, knownArgs.toArray(new String[0]));
            }
        };
        CommandLine commandLine = parser.parse(options, args);
        UUID = (commandLine.getOptionValue(uuidOption));
        USERNAME = (commandLine.getOptionValue(usernameOption));
        TOKEN = (commandLine.getOptionValue(sessionOption));

        URL.setURLStreamHandlerFactory(new WrapperProtocolFactory());
        KnotClient.main(args);
    }
}
