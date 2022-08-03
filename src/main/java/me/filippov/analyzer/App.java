package me.filippov.analyzer;

import me.filippov.analyzer.config.ChannelInfo;
import me.filippov.analyzer.config.ConfigurationManager;
import me.filippov.analyzer.multicast.MulticastListener;
import me.filippov.analyzer.processing.PacketDecoder;

import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static spark.Spark.get;

public class App {
    private static List<ChannelInfo> channelInfoList;

    public static void main(String[] args) throws Exception {
        channelInfoList = ConfigurationManager.getChannelList(Files.readAllLines(Paths.get(args.length == 2 ? args[0] : "playlist.m3u")));

        get("/list", (request, response) -> {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < channelInfoList.size(); i++) {
                result.append(i);
                result.append(";");
                result.append(channelInfoList.get(i).getName());
                result.append("\n");
            }
            return result.toString();
        });

        get("/:channelId/bytes", (request, response) -> channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getStreamBytes());

        get("/:channelId/scrambled-errors", (request, response) -> channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getScrambledErrorCount());

        get("/:channelId/cc-errors", (request, response) -> channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getCcErrorCount());

        NetworkInterface ni = NetworkInterface.getByName(args.length == 2 ? args[1] : "en1");

        channelInfoList
                .forEach(ci -> new MulticastListener(ni, ci.getAddress(), ci.getPort(), ci.getProcessor(), new PacketDecoder()).start());

    }
}
