package me.filippov.analyzer.processing;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PacketDecoder implements IPacketDecoder {
    @Override
    public List<PacketChunk> decode(ByteBuffer byteBuffer) {
        List<PacketChunk> result = new ArrayList<>();
        byte[] buffer = new byte[1316];
        byteBuffer.get(buffer);
        for (int i = 0; i < 1316; i+=188) {
            boolean isSync = buffer[i] == 71;
            int isPayload = buffer[i + 1] & 64;
            int pid = 256 * (buffer[i + 1] & 31) + buffer[i + 2];
            int cc = buffer[i + 3] & 15;
            int scrambled = buffer[i + 3] & 192;
            PacketChunk chunk = new PacketChunk(isSync, isPayload == 0, pid, cc, scrambled != 0);
            result.add(chunk);
        }
        return result;
    }
}
