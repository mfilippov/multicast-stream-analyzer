package me.filippov.analyzer;

import me.filippov.analyzer.processing.IPacketProcessor;
import me.filippov.analyzer.processing.PacketChunk;
import me.filippov.analyzer.processing.PacketProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class PacketProcessorTest {

    @Test
    public void shouldCorrectIncrementStreamBytes() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, false));
        chunkList.add(new PacketChunk(true, true, 100, 3, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(1316, packetProcessor.getStreamBytes());
    }

    @Test
    public void shouldCorrectOverflowStreamBytes() {
        IPacketProcessor packetProcessor = new PacketProcessor(Long.MAX_VALUE - 10, 0, 0, new ConcurrentHashMap<>());
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, false));
        chunkList.add(new PacketChunk(true, true, 100, 3, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(1306, packetProcessor.getStreamBytes());
    }

    @Test
    public void shouldCorrectDetectScrambledErrorCount() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, true));
        chunkList.add(new PacketChunk(true, true, 100, 3, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(1, packetProcessor.getScrambledErrorCount());
    }

    @Test
    public void shouldCorrectOverflowScrambledErrorCount() {
        IPacketProcessor packetProcessor = new PacketProcessor(0, Long.MAX_VALUE, 0, new ConcurrentHashMap<>());
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, true));
        chunkList.add(new PacketChunk(true, true, 100, 3, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(1, packetProcessor.getScrambledErrorCount());
    }

    @Test
    public void shouldCorrectDetectCcErrorCountWhenLostOneChunk() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        chunkList.add(new PacketChunk(true, true, 100, 8, false));
        packetProcessor.process(chunkList);
        assertEquals(1, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldCorrectDetectCcErrorCountWhenCcOverflow() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 14, false));
        chunkList.add(new PacketChunk(true, true, 100, 15, false));
        chunkList.add(new PacketChunk(true, true, 100, 0, false));
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, false));
        chunkList.add(new PacketChunk(true, true, 100, 3, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        packetProcessor.process(chunkList);
        assertEquals(0, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldCorrectOverflowCcErrorCount() {
        IPacketProcessor packetProcessor = new PacketProcessor(0, 0, Long.MAX_VALUE, new ConcurrentHashMap<>());
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 100, 1, false));
        chunkList.add(new PacketChunk(true, true, 100, 2, false));
        chunkList.add(new PacketChunk(true, true, 100, 4, false));
        chunkList.add(new PacketChunk(true, true, 100, 5, false));
        chunkList.add(new PacketChunk(true, true, 100, 6, false));
        chunkList.add(new PacketChunk(true, true, 100, 7, false));
        chunkList.add(new PacketChunk(true, true, 100, 8, false));
        packetProcessor.process(chunkList);
        assertEquals(1, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldBeIgnoreChunkWithoutSyncByte() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(false, true, 100, 5, false));
        chunkList.add(new PacketChunk(false, true, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(0, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldBeIgnoreChunkWithoutPayload() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, false, 100, 5, false));
        chunkList.add(new PacketChunk(true, false, 100, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(0, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldBeIgnoreChunkWithPid16OrLess() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 15, 5, false));
        chunkList.add(new PacketChunk(true, true, 15, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(0, packetProcessor.getCcErrorCount());
    }

    @Test
    public void shouldBeIgnoreChunkWithPid8190OrMore() {
        IPacketProcessor packetProcessor = new PacketProcessor();
        ArrayList<PacketChunk> chunkList = new ArrayList<>();
        chunkList.add(new PacketChunk(true, true, 8191, 5, false));
        chunkList.add(new PacketChunk(true, true, 8191, 7, false));
        packetProcessor.process(chunkList);
        assertEquals(0, packetProcessor.getCcErrorCount());
    }

}
