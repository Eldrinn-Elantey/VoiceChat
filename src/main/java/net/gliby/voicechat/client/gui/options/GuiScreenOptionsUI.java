package net.gliby.voicechat.client.gui.options;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import net.gliby.voicechat.client.VoiceChatClient;
import net.gliby.voicechat.client.gui.GuiBoostSlider;
import net.gliby.voicechat.client.gui.GuiUIPlacement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiScreenOptionsUI extends GuiScreen {

    private final VoiceChatClient voiceChat;
    private final GuiScreen parent;

    private GuiBoostSlider opacity;
    private GuiButton btnVoiceIcons;
    private GuiButton btnVoicePlates;
    private GuiButton btnSpeakingIndicator;

    public GuiScreenOptionsUI(VoiceChatClient voiceChat, GuiScreen parent) {
        this.voiceChat = voiceChat;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(parent);
                break;
            case 1:
                voiceChat.getSettings()
                    .resetUI(width, height);
                voiceChat.getSettings()
                    .setVoiceIconsAllowed(true);
                voiceChat.getSettings()
                    .setVoicePlatesAllowed(true);
                voiceChat.getSettings()
                    .setSpeakingIndicatorAllowed(true);
                opacity.sliderValue = 1.0f;
                updateButtonLabels();
                break;
            case 2:
                mc.displayGuiScreen(new GuiUIPlacement(this));
                break;
            case 3:
                voiceChat.getSettings()
                    .setVoiceIconsAllowed(
                        !voiceChat.getSettings()
                            .isVoiceIconAllowed());
                updateButtonLabels();
                break;
            case 4:
                voiceChat.getSettings()
                    .setVoicePlatesAllowed(
                        !voiceChat.getSettings()
                            .isVoicePlateAllowed());
                updateButtonLabels();
                break;
            case 5:
                voiceChat.getSettings()
                    .setSpeakingIndicatorAllowed(
                        !voiceChat.getSettings()
                            .isSpeakingIndicatorAllowed());
                updateButtonLabels();
                break;
        }
    }

    @Override
    public void drawScreen(int x, int y, float time) {
        drawDefaultBackground();
        glPushMatrix();
        glTranslatef(width / 2 - (fontRendererObj.getStringWidth("Gliby's Voice Chat Options") / 2) * 1.5f, 0, 0);
        glScalef(1.5f, 1.5f, 0);
        drawString(mc.fontRenderer, "Gliby's Voice Chat Options", 0, 6, -1);
        glPopMatrix();
        glPushMatrix();
        glTranslatef(width / 2 - (fontRendererObj.getStringWidth(I18n.format("menu.uiOptions")) / 2), 12, 0);
        drawString(mc.fontRenderer, I18n.format("menu.uiOptions"), 0, 12, -1);
        glPopMatrix();
        super.drawScreen(x, y, time);
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, this.width / 2 - 75, height - 34, 150, 20, I18n.format("gui.back")));
        buttonList.add(new GuiButton(1, this.width / 2 - 75, 73, 150, 20, I18n.format("menu.resetAll")));
        buttonList.add(new GuiButton(2, this.width / 2 - 150, 50, 150, 20, I18n.format("menu.uiPlacement")));
        buttonList.add(
            opacity = new GuiBoostSlider(
                -1,
                this.width / 2 + 2,
                50,
                "",
                I18n.format("menu.uiOpacity") + ": "
                    + (voiceChat.getSettings()
                        .getUIOpacity() == 0 ? I18n.format("options.off")
                            : (int) (voiceChat.getSettings()
                                .getUIOpacity() * 100) + "%"),
                0));
        opacity.sliderValue = voiceChat.getSettings()
            .getUIOpacity();

        buttonList.add(btnVoiceIcons = new GuiButton(3, this.width / 2 - 150, 96, 150, 20, ""));
        buttonList.add(btnVoicePlates = new GuiButton(4, this.width / 2 + 2, 96, 150, 20, ""));
        buttonList.add(btnSpeakingIndicator = new GuiButton(5, this.width / 2 - 150, 120, 150, 20, ""));

        updateButtonLabels();
    }

    private void updateButtonLabels() {
        String on = I18n.format("options.on");
        String off = I18n.format("options.off");

        btnVoiceIcons.displayString = I18n.format(
            "menu.showVoiceIcons",
            voiceChat.getSettings()
                .isVoiceIconAllowed() ? on : off);
        btnVoicePlates.displayString = I18n.format(
            "menu.showVoicePlates",
            voiceChat.getSettings()
                .isVoicePlateAllowed() ? on : off);
        btnSpeakingIndicator.displayString = I18n.format(
            "menu.showSpeakingIndicator",
            voiceChat.getSettings()
                .isSpeakingIndicatorAllowed() ? on : off);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        voiceChat.getSettings()
            .getConfiguration()
            .save();
    }

    @Override
    public void updateScreen() {
        voiceChat.getSettings()
            .setUIOpacity(opacity.sliderValue);
        opacity.setDisplayString(
            I18n.format("menu.uiOpacity") + ": "
                + (voiceChat.getSettings()
                    .getUIOpacity() == 0 ? I18n.format("options.off")
                        : (int) (voiceChat.getSettings()
                            .getUIOpacity() * 100) + "%"));
    }
}
