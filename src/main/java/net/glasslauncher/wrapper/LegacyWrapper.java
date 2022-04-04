package net.glasslauncher.wrapper;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.text.StringSubstitutor;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class LegacyWrapper {
    public static String UUID;
    public static String USERNAME;
    public static String SESSION;
    public static String SERVER;
    public static String MOD_ARG;
    public static String OVERRIDE_PATH;
    public static String MAIN_CLASS;

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        Option usernameOption = Option.builder("u").hasArg().desc("Username").longOpt("username").build();
        Option sessionOption = Option.builder("i").hasArg().desc("UUID").longOpt("uuid").build();
        Option uuidOption = Option.builder("s").hasArg().desc("Session").longOpt("session").build();
        Option serverOption = Option.builder("r").hasArg().desc("Server").longOpt("server").build();
        Option modArgOption = Option.builder("a").hasArg().desc("Modified Args").longOpt("modArg").build();
        Option overridePathOption = Option.builder("p").hasArg().desc("Override Path").longOpt("path").build();
        Option mainClassOption = Option.builder("m").hasArg().desc("Override Path").longOpt("mainClass").build();
        options.addOption(usernameOption);
        options.addOption(sessionOption);
        options.addOption(uuidOption);
        options.addOption(serverOption);
        options.addOption(modArgOption);
        options.addOption(overridePathOption);
        options.addOption(mainClassOption);
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
        UUID = commandLine.getOptionValue(uuidOption);
        USERNAME = commandLine.getOptionValue(usernameOption);
        SESSION = commandLine.getOptionValue(sessionOption);
        SERVER = commandLine.getOptionValue(serverOption);
        MOD_ARG = commandLine.getOptionValue(modArgOption);
        OVERRIDE_PATH = commandLine.getOptionValue(overridePathOption);
        MAIN_CLASS = commandLine.getOptionValue(mainClassOption);

        if (USERNAME == null || USERNAME.isEmpty()) {
            System.out.println("[FATAL] [GLW] Can't find username to launch with! Aborting!");
            System.exit(1);
            return; // just in case
        }
        if (SESSION == null || SESSION.isEmpty()) {
            SESSION = "";
            System.out.println("[WARN] [GLW] Can't find user session token, online mode will not work!");
        }
        if (UUID == null || UUID.isEmpty()) {
            UUID = "";
            System.out.println("[INFO] [GLW] Can't find user UUID, mods which require this will not work!");
        }
        if (SERVER == null || SERVER.isEmpty()) {
            SERVER = null;
        }
        else {
            System.out.println("[INFO] [GLW] Server argument found, launching minecraft with it!");
        }

        URL.setURLStreamHandlerFactory(new WrapperProtocolFactory());

        if (OVERRIDE_PATH != null) {
            // On windows and linux, this works just fine, and you'll get a .minecraft folder inside the target folder.
            // On macos, you will get a "Library/Application Support" folder inside the targer folder containing .minecraft instead.
            // There is no custom directory argument for any vanilla pre-release versions. Stop trying to pass them.
            try {
                LegacyWrapperUtils.setEnv(new HashMap<String, String>() {{
                    put("APPDATA", OVERRIDE_PATH);
                    put("user.home", OVERRIDE_PATH);
                }});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (MAIN_CLASS == null) {
            MAIN_CLASS = "net.minecraft.client.Minecraft";
        }
        if (MOD_ARG == null) {
            MOD_ARG = "${u}^^${s}";
        }

        HashMap<String, String> replacer = new HashMap<>();
        options.getOptions().forEach((option -> {
            String value = commandLine.getOptionValue(option);
            replacer.put(option.getOpt(), value == null? "" : value);
            replacer.put(option.getLongOpt(), value == null? "" : value);
        }));

        MOD_ARG = new StringSubstitutor(replacer).replace(MOD_ARG);

        try {
            Class.forName(MAIN_CLASS).getDeclaredMethod("main", String[].class).invoke(null, (Object) MOD_ARG.split("\\^\\^"));
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
