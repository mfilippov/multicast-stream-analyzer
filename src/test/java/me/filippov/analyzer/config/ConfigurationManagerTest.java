package me.filippov.analyzer.config;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfigurationManagerTest {
    private final List<String> list1 = Arrays.asList(
                            "#EXTM3U",
                            "#EXTINF:0, Channel1",
                            "udp://@239.255.1.1:1234",
                            "#EXTINF:0, Channel2",
                            "udp://@239.255.1.2:1234",
                            "#EXTINF:0, Channel3",
                            "udp://@239.255.1.3:1234");

    @Test
    public void shouldCorrectLoadChannelCount() throws Exception {
        List<ChannelInfo> result = ConfigurationManager.getChannelList(list1);
        assertEquals(3, result.size());
    }
    @Test
    public void shouldCorrectLoadChannelName() throws Exception {
        List<ChannelInfo> result = ConfigurationManager.getChannelList(list1);
        assertEquals("Channel1", result.get(0).getName());
        assertEquals("Channel2", result.get(1).getName());
        assertEquals("Channel3", result.get(2).getName());
    }
    @Test
    public void shouldCorrectLoadChannelAddress() throws Exception {
        List<ChannelInfo> result = ConfigurationManager.getChannelList(list1);
        assertEquals("239.255.1.1", result.get(0).getAddress());
        assertEquals("239.255.1.2", result.get(1).getAddress());
        assertEquals("239.255.1.3", result.get(2).getAddress());
    }
    @Test
    public void shouldCorrectLoadChannelPort() throws Exception {
        List<ChannelInfo> result = ConfigurationManager.getChannelList(list1);
        assertEquals(1234, result.get(0).getPort());
        assertEquals(1234, result.get(1).getPort());
        assertEquals(1234, result.get(2).getPort());
    }
}
