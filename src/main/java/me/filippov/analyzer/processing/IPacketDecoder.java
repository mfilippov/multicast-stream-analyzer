package me.filippov.analyzer.processing;

import java.nio.ByteBuffer;
import java.util.List;

public interface IPacketDecoder {
    List<PacketChunk> decode(ByteBuffer byteBuffer);
}
