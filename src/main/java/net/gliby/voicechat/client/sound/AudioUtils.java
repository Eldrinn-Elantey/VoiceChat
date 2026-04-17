package net.gliby.voicechat.client.sound;

public class AudioUtils {

    public static byte[] amplify(byte[] pcmData, float gain) {
        if (gain <= 1.0f || pcmData == null || pcmData.length < 2) {
            return pcmData;
        }

        byte[] result = new byte[pcmData.length];

        for (int i = 0; i < pcmData.length - 1; i += 2) {
            short sample = (short) (((pcmData[i + 1] & 0xFF) << 8) | (pcmData[i] & 0xFF));

            int amplified = (int) (sample * gain);

            if (amplified > Short.MAX_VALUE) amplified = Short.MAX_VALUE;
            else if (amplified < Short.MIN_VALUE) amplified = Short.MIN_VALUE;

            result[i] = (byte) (amplified & 0xFF);
            result[i + 1] = (byte) ((amplified >> 8) & 0xFF);
        }

        return result;
    }
}
