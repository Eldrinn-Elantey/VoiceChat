package net.gliby.voicechat.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.gliby.voicechat.client.VoiceChatClient;
import net.gliby.voicechat.client.sound.ClientStreamManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiScreenPlayerSettings extends GuiScreen {

    private static final float MAX_VOLUME = 2.0f;

    @SideOnly(Side.CLIENT)
    class PlayerList extends GuiSlot {

        public PlayerList() {
            super(
                GuiScreenPlayerSettings.this.mc,
                GuiScreenPlayerSettings.this.width,
                GuiScreenPlayerSettings.this.height,
                32,
                GuiScreenPlayerSettings.this.height - 40,
                24);
        }

        @Override
        protected void drawBackground() {
            GuiScreenPlayerSettings.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int index, int par1, int slotY, int par3, Tessellator tessellator, int mouseX,
            int mouseY) {

            EntityPlayer player = onlinePlayers.get(index);
            String playerName = player.getCommandSenderName();
            int entityId = player.getEntityId();
            boolean isMuted = VoiceChatClient.getSoundManager().playersMuted.contains(entityId);

            // Player name
            GuiScreenPlayerSettings.this.drawString(
                GuiScreenPlayerSettings.this.fontRendererObj,
                playerName,
                this.width / 2 - 140,
                slotY + 6,
                isMuted ? 0xFF5555 : 0xFFFFFF);

            // Mute/Unmute button with background
            int btnX = this.width / 2 + 115;
            int btnY = slotY + 2;
            int btnWidth = 40;
            int btnHeight = 14;

            boolean hovered = mouseX >= btnX && mouseX <= btnX + btnWidth
                && mouseY >= btnY
                && mouseY <= btnY + btnHeight;

            int btnColor;
            String btnText;
            if (isMuted) {
                btnColor = hovered ? 0xFFCC3333 : 0xFFAA2222;
                btnText = I18n.format("menu.muted");
            } else {
                btnColor = hovered ? 0xFF33AA33 : 0xFF228822;
                btnText = I18n.format("menu.unmuted");
            }

            GuiScreenPlayerSettings.this.drawRect(btnX, btnY, btnX + btnWidth, btnY + btnHeight, btnColor);

            GuiScreenPlayerSettings.this.drawCenteredString(
                GuiScreenPlayerSettings.this.fontRendererObj,
                btnText,
                btnX + btnWidth / 2,
                btnY + 3,
                0xFFFFFF);

            // Volume slider
            float volume = VoiceChatClient.getSoundManager()
                .getVoiceChat()
                .getSettings()
                .getPlayerVolume(playerName);

            int sliderX = this.width / 2 - 50;
            int sliderWidth = 120;
            int sliderY = slotY + 2;
            int sliderHeight = 14;

            float sliderFill = volume / MAX_VOLUME;
            int barColor;
            if (isMuted) {
                barColor = 0xFF882222;
            } else if (volume > 1.0f) {
                barColor = 0xFFAA6600;
            } else {
                barColor = 0xFF00AA00;
            }

            // Slider background
            GuiScreenPlayerSettings.this
                .drawRect(sliderX, sliderY, sliderX + sliderWidth, sliderY + sliderHeight, 0xFF555555);

            // Slider fill
            GuiScreenPlayerSettings.this.drawRect(
                sliderX,
                sliderY,
                sliderX + (int) (sliderWidth * sliderFill),
                sliderY + sliderHeight,
                barColor);

            // 100% mark
            int markX = sliderX + (int) (sliderWidth * (1.0f / MAX_VOLUME));
            GuiScreenPlayerSettings.this.drawRect(markX, sliderY, markX + 1, sliderY + sliderHeight, 0xAAFFFFFF);

            // Volume text
            String volumeText = isMuted ? I18n.format("menu.muted") : (int) (volume * 100) + "%";
            GuiScreenPlayerSettings.this.drawCenteredString(
                GuiScreenPlayerSettings.this.fontRendererObj,
                volumeText,
                sliderX + sliderWidth / 2,
                sliderY + 3,
                0xFFFFFF);
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
            EntityPlayer player = onlinePlayers.get(index);
            String playerName = player.getCommandSenderName();
            int entityId = player.getEntityId();

            int sliderX = this.width / 2 - 50;
            int sliderWidth = 120;
            int sliderEndX = sliderX + sliderWidth;
            int muteX = this.width / 2 + 115;
            int muteWidth = 40;

            // Mute/Unmute toggle
            if (mouseX >= muteX && mouseX <= muteX + muteWidth) {
                if (VoiceChatClient.getSoundManager().playersMuted.contains(entityId)) {
                    VoiceChatClient.getSoundManager().playersMuted.remove(Integer.valueOf(entityId));
                    ClientStreamManager.playerMutedData.remove(entityId);
                } else {
                    VoiceChatClient.getSoundManager().playersMuted.add(entityId);
                    ClientStreamManager.playerMutedData.put(entityId, playerName);
                }
                VoiceChatClient.getSoundManager()
                    .getVoiceChat()
                    .getSettings()
                    .getConfiguration()
                    .save();
                return;
            }

            // Volume slider click
            if (mouseX >= sliderX && mouseX <= sliderEndX) {
                float newVolume = (float) (mouseX - sliderX) / sliderWidth * MAX_VOLUME;
                newVolume = Math.max(0.0f, Math.min(MAX_VOLUME, newVolume));
                VoiceChatClient.getSoundManager()
                    .getVoiceChat()
                    .getSettings()
                    .setPlayerVolume(playerName, newVolume);
                VoiceChatClient.getSoundManager()
                    .getVoiceChat()
                    .getSettings()
                    .getConfiguration()
                    .save();
            }
        }

        @Override
        protected int getContentHeight() {
            return this.getSize() * 24;
        }

        @Override
        protected int getSize() {
            return onlinePlayers.size();
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }
    }

    protected GuiScreen parent;
    private GuiScreenPlayerSettings.PlayerList playerList;
    private List<EntityPlayer> onlinePlayers = new ArrayList<EntityPlayer>();

    public GuiScreenPlayerSettings(GuiScreen par1GuiScreen, VoiceChatClient voiceChat) {
        this.parent = par1GuiScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.playerList.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.playerSettings"), this.width / 2, 16, -1);

        this.drawString(this.fontRendererObj, I18n.format("menu.playerName"), this.width / 2 - 140, 24, 0xAAAAAA);

        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.volume"), this.width / 2 + 10, 24, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 75, this.height - 32, I18n.format("gui.done")));

        refreshPlayerList();
        this.playerList = new GuiScreenPlayerSettings.PlayerList();
        this.playerList.registerScrollButtons(7, 8);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceLastClick) {
        if (button == 0) {
            int sliderX = width / 2 - 50;
            int sliderWidth = 120;
            int sliderEndX = sliderX + sliderWidth;

            if (mouseX >= sliderX && mouseX <= sliderEndX) {
                int listTop = 32;
                int slotHeight = 24;
                int relativeY = mouseY - listTop + (int) playerList.getAmountScrolled();
                int index = relativeY / slotHeight;

                if (index >= 0 && index < onlinePlayers.size()) {
                    String playerName = onlinePlayers.get(index)
                        .getCommandSenderName();
                    float newVolume = (float) (mouseX - sliderX) / sliderWidth * MAX_VOLUME;
                    newVolume = Math.max(0.0f, Math.min(MAX_VOLUME, newVolume));
                    VoiceChatClient.getSoundManager()
                        .getVoiceChat()
                        .getSettings()
                        .setPlayerVolume(playerName, newVolume);
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, button, timeSinceLastClick);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        if (button == 0) {
            VoiceChatClient.getSoundManager()
                .getVoiceChat()
                .getSettings()
                .getConfiguration()
                .save();
        }
        super.mouseMovedOrUp(mouseX, mouseY, button);
    }

    private void refreshPlayerList() {
        onlinePlayers.clear();
        if (mc.theWorld != null) {
            for (Object obj : mc.theWorld.playerEntities) {
                EntityPlayer player = (EntityPlayer) obj;
                if (player != mc.thePlayer) {
                    onlinePlayers.add(player);
                }
            }
        }
    }

    @Override
    public void updateScreen() {
        refreshPlayerList();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
