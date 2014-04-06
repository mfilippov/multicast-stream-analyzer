package me.filippov.analyzer.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    public static List<ChannelInfo> getChannelList(List<String> lines) throws IOException {
        List<ChannelInfo> result = new ArrayList<>();
        String address = null;
        String name = null;
        int port = 0;
        for (String line: lines) {
            if (name != null) {
                String[] parts = line.split("@");
                address = parts[1].split(":")[0];
                port = Integer.parseInt(parts[1].split(":")[1]);
            }
            if (line.startsWith("#EXTINF")) {
                name =  line.split(",", 2)[1].trim();
            }
            if (address != null) {
                result.add(new ChannelInfo(address, port, name));
                address = null;
                name = null;
            }
        }
        return result;
    }
}
