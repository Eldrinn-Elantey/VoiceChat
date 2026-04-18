package net.gliby.voicechat.client.keybindings;

import net.gliby.voicechat.client.VoiceChatClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

class KeySpeakEvent extends KeyEvent {

    private final VoiceChatClient voiceChat;

    KeySpeakEvent(VoiceChatClient voiceChat, EnumBinding keyBind, int keyID, boolean repeating) {
        super(keyBind, keyID, repeating);
        this.voiceChat = voiceChat;
    }

    @Override
    public void keyDown(KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        if (tickEnd && voiceChat.getSettings()
            .getInputDevice() != null && Minecraft.getMinecraft().currentScreen == null) {
            voiceChat.recorder.set(
                voiceChat.getSettings()
                    .getSpeakMode() == 1 ? !voiceChat.isRecorderActive() : true);
            voiceChat.setRecorderActive(
                voiceChat.getSettings()
                    .getSpeakMode() == 1 ? !voiceChat.isRecorderActive() : true);
        }
    }

    @Override
    public void keyUp(KeyBinding kb, boolean tickEnd) {
        if (tickEnd && voiceChat.getSettings()
            .getSpeakMode() == 0) {
            voiceChat.setRecorderActive(false);
            voiceChat.recorder.stop();
        }
    }
}
