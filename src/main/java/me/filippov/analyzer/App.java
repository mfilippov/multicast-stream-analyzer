package me.filippov.analyzer;

import me.filippov.analyzer.config.ChannelInfo;
import me.filippov.analyzer.config.ConfigurationManager;
import me.filippov.analyzer.multicast.MulticastListener;
import me.filippov.analyzer.processing.PacketDecoder;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static spark.Spark.get;

public class App {
    private static List<ChannelInfo> channelInfoList;

    public static void main(String[] args) throws Exception {
        channelInfoList = ConfigurationManager.getChannelList(Files.readAllLines(Paths.get(args.length == 2?args[1]:"playlist.m3u")));

        get(new Route("/list") {
            @Override
            public Object handle(Request request, Response response) {
                StringBuilder result = new StringBuilder();
                for(int i = 0; i < channelInfoList.size(); i++) {
                    result.append(i);
                    result.append(";");
                    result.append(channelInfoList.get(i).getName());
                    result.append("\n");
                }
                return result.toString();
            }
        });

        get(new Route("/:channelId/bytes") {
            @Override
            public Object handle(Request request, Response response) {
                return channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getStreamBytes();
            }
        });

        get(new Route("/:channelId/scrambled-errors"){
            @Override
            public Object handle(Request request, Response response) {
                return channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getScrambledErrorCount();
            }
        });

        get(new Route("/:channelId/cc-errors"){
            @Override
            public Object handle(Request request, Response response) {
                return channelInfoList.get(Integer.parseInt(request.params(":channelId"))).getProcessor().getCcErrorCount();
            }
        });

        NetworkInterface ni = NetworkInterface.getByName(args.length == 2 ? args[2] : "en1");

        channelInfoList.stream()
                .forEach(ci -> new MulticastListener(ni, ci.getAddress(), ci.getPort(), ci.getProcessor(), new PacketDecoder()).start());

    }
}
