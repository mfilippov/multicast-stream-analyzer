package me.filippov.analyzer.processing;

import java.util.List;

public interface IPacketProcessor {
    void process(List<PacketChunk> chunks);
    long getStreamBytes();
    long getScrambledErrorCount();
    long getCcErrorCount();
}
