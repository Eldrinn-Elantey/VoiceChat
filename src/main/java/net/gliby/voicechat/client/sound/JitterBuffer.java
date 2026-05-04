package net.gliby.voicechat.client.sound;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;

class JitterBuffer {

    private static final int INITIAL_CAPACITY = 4096;

    private byte[] buffer;
    private int writePos;
    private final AudioFormat format;
    private int threshold;

    JitterBuffer(AudioFormat format, int jitter) {
        this.format = format;
        this.buffer = new byte[INITIAL_CAPACITY];
        this.writePos = 0;
        updateJitter(jitter);
    }

    void clearBuffer(int jitterSize) {
        writePos = 0;
        updateJitter(jitterSize);
    }

    byte[] get() {
        return Arrays.copyOf(buffer, writePos);
    }

    private int getSizeInBytes(AudioFormat fmt, int size) {
        final int s = (int) (fmt.getSampleRate() / 1000);
        final int sampleSize = (int) ((fmt.getSampleSizeInBits() / 8) * 0.49f);
        return sampleSize != 0 ? s * size / sampleSize : 0;
    }

    public boolean isReady() {
        return writePos > threshold;
    }

    void push(byte[] data) {
        ensureCapacity(writePos + data.length);
        System.arraycopy(data, 0, buffer, writePos, data.length);
        writePos += data.length;
    }

    void updateJitter(int size) {
        this.threshold = getSizeInBytes(format, size);
    }

    private void ensureCapacity(int needed) {
        if (needed > buffer.length) {
            buffer = Arrays.copyOf(buffer, Math.max(needed, buffer.length * 2));
        }
    }
}
