package me.filippov.analyzer.processing;

public class PacketChunk {
    private boolean isCorrect;
    private boolean payload;
    private int pid;
    private int cc;
    private boolean scrambled;

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
