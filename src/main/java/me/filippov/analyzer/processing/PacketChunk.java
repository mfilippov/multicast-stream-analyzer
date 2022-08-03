package me.filippov.analyzer.processing;

public class PacketChunk {
    private final boolean isCorrect;
    private final boolean payload;
    private final int pid;
    private final int cc;
    private final boolean scrambled;

    public PacketChunk(boolean isSync, boolean payload, int pid, int cc, boolean scrambled) {
        this.isCorrect = isSync;
        this.payload = payload;
        this.pid = pid;
        this.cc = cc;
        this.scrambled = scrambled;
    }

    public boolean isSync() {
        return isCorrect;
    }

    public boolean isPayload() {
        return payload;
    }

    public int getPid() {
        return pid;
    }

    public int getCc() {
        return cc;
    }

    public boolean isScrambled() {
        return scrambled;
    }
}
