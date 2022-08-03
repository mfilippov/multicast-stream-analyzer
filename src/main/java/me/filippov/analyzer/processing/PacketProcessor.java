package me.filippov.analyzer.processing;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PacketProcessor implements IPacketProcessor {

    private final AtomicLong streamBytes;
    private final AtomicLong scrambledErrorCount;
    private final AtomicLong continuousCounterErrorCount;
    private final ConcurrentHashMap<Integer, Integer> channelState;

    public PacketProcessor() {
        streamBytes = new AtomicLong(0);
        scrambledErrorCount = new AtomicLong(0);
        continuousCounterErrorCount = new AtomicLong(0);
        channelState = new ConcurrentHashMap<>();
    }

    public PacketProcessor(long streamBytes,
                           long scrambledErrorCount,
                           long continuousCounterErrorCount,
                           ConcurrentHashMap<Integer, Integer> channelState) {
        this.streamBytes = new AtomicLong(streamBytes);
        this.scrambledErrorCount = new AtomicLong(scrambledErrorCount);
        this.continuousCounterErrorCount = new AtomicLong(continuousCounterErrorCount);
        this.channelState = channelState;
    }

    @Override
    public long getStreamBytes() {
        return streamBytes.get();
    }

    @Override
    public long getScrambledErrorCount() {
        return scrambledErrorCount.get();
    }

    @Override
    public long getCcErrorCount() {
        return continuousCounterErrorCount.get();
    }

    @Override
    public void process(List<PacketChunk> chunks) {
        for (PacketChunk chunk: chunks) {
            int pid = chunk.getPid();
            int cc = chunk.getCc();
            if (16 <= pid && pid <= 8190 && chunk.isPayload() && chunk.isSync()) {
                if (channelState.containsKey(pid) && channelState.get(pid) + 1 != cc
                        && channelState.get(pid) != 15 && cc != 0) {
                    if (continuousCounterErrorCount.get() == Long.MAX_VALUE) {
                        continuousCounterErrorCount.set(1);
                    } else {
                        continuousCounterErrorCount.incrementAndGet();
                    }
                }
                if (chunk.isScrambled()) {
                    if (scrambledErrorCount.get() == Long.MAX_VALUE) {
                        scrambledErrorCount.set(1);
                    } else {
                        scrambledErrorCount.incrementAndGet();
                    }
                }
                channelState.put(pid, cc);
            }
        }
        long delta = Long.MAX_VALUE - streamBytes.get();
        if (delta > 1316) {
            streamBytes.addAndGet(1316);
        } else {
            streamBytes.set(1316 - delta);
        }
    }

}
