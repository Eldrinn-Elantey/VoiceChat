package net.gliby.voicechat.client;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.gliby.gman.JINIFile;
import net.gliby.voicechat.VoiceChat;
import net.gliby.voicechat.client.device.Device;
import net.gliby.voicechat.client.device.DeviceHandler;
import net.gliby.voicechat.client.gui.EnumUIPlacement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// TODO NEXT-UPDATE Implement dynamic configuration stuffs, get rid of hard-coded settings.
@SideOnly(Side.CLIENT)
public class Configuration {

    private static final String VOLUME_CONTROL = "VolumeControl", INPUT_DEVICE = "InputDevice",
        WORLD_VOLUME = "WorldVolume", INPUT_BOOST = "InputBoost", SPEAK_MODE = "SpeakMode",
        ENCODING_QUALITY = "EncodingQuality", ENCODING_MODE = "EncodingMode", DECODING_ENCHANTMENT = "EnhancedDecoding",
        UI_OPACITY = "UIOpacity", UI_POSITION_PLATE = "UIPositionPlate", UI_POSITION_SPEAK = "UIPositionSpeak",
        VERSION = "LastVersion", DEBUG = "Debug", SNOOPER = "GlibysSnooper", MODPACK_ID = "ModPackID";

    private final File location;
    private JINIFile init;
    private final Settings settings;
    private static final String PLAYER_VOLUMES = "PlayerVolumes";
    private static final String SHOW_VOICE_ICONS = "ShowVoiceIcons", SHOW_VOICE_PLATES = "ShowVoicePlates",
        SHOW_SPEAKING_INDICATOR = "ShowSpeakingIndicator";

    Configuration(Settings settings, File file) {
        this.settings = settings;
        this.location = file;
    }

    void init(DeviceHandler deviceHandler) {
        if (!load(deviceHandler)) {
            VoiceChat.getLogger()
                .info("No Configuration file found, will create one with default settings.");
            settings.setSetupNeeded(true);
            if (save()) VoiceChat.getLogger()
                .info("Created Configuration file with default settings.");
        }
    }

    private boolean load(DeviceHandler handler) {
        try {
            if (location.exists()) {
                this.init = new JINIFile(location);
                settings.setVolumeControl(init.ReadBool("Game", VOLUME_CONTROL, true));
                final Device defaultDevice = handler.getDefaultDevice();
                if (defaultDevice != null) settings.setInputDevice(
                    handler.getDeviceByName(init.ReadString("Audio", INPUT_DEVICE, defaultDevice.getName())));
                settings.setWorldVolume(init.ReadFloat("Audio", WORLD_VOLUME, 1.0f));
                settings.setInputBoost(init.ReadFloat("Audio", INPUT_BOOST, 1.0f));
                settings.setSpeakMode(
                    init.ReadFloat("Audio", SPEAK_MODE, Float.valueOf(0))
                        .intValue());

                settings.setEncodingQuality(init.ReadFloat("AdvancedAudio", ENCODING_QUALITY, Float.valueOf(1)));
                settings.setEncodingMode(
                    init.ReadFloat("AdvancedAudio", ENCODING_MODE, Float.valueOf(1))
                        .intValue());
                settings.setPerceptualEnchantment(init.ReadBool("AdvancedAudio", DECODING_ENCHANTMENT, true));

                settings.setUIOpacity(init.ReadFloat("Interface", UI_OPACITY, Float.valueOf(1)));
                String[] positionArray = init
                    .ReadString(
                        "Interface",
                        UI_POSITION_SPEAK,
                        settings.getUIPositionSpeak().x + ":"
                            + settings.getUIPositionSpeak().y
                            + ":"
                            + settings.getUIPositionSpeak().type
                            + ":"
                            + settings.getUIPositionSpeak().scale)
                    .split(":");
                settings.setUIPosition(
                    EnumUIPlacement.SPEAK,
                    Float.parseFloat(positionArray[0]),
                    Float.parseFloat(positionArray[1]),
                    Float.parseFloat(positionArray[3]),
                    Integer.parseInt(positionArray[2]));
                positionArray = init
                    .ReadString(
                        "Interface",
                        UI_POSITION_PLATE,
                        settings.getUIPositionPlate().x + ":"
                            + settings.getUIPositionPlate().y
                            + ":"
                            + settings.getUIPositionPlate().type
                            + ":"
                            + settings.getUIPositionPlate().scale)
                    .split(":");
                settings.setUIPosition(
                    EnumUIPlacement.VOICE_PLATES,
                    Float.parseFloat(positionArray[0]),
                    Float.parseFloat(positionArray[1]),
                    Float.parseFloat(positionArray[3]),
                    Integer.parseInt(positionArray[2]));

                settings.setVoiceIconsAllowed(init.ReadBool("Interface", SHOW_VOICE_ICONS, true));
                settings.setVoicePlatesAllowed(init.ReadBool("Interface", SHOW_VOICE_PLATES, true));
                settings.setSpeakingIndicatorAllowed(init.ReadBool("Interface", SHOW_SPEAKING_INDICATOR, true));

                settings.setSnooperAllowed(init.ReadBool("Miscellaneous", SNOOPER, false));
                settings.setModPackID(init.ReadInteger("Miscellaneous", MODPACK_ID, 1));
                settings.setDebug(init.ReadBool("Miscellaneous", DEBUG, false));
                String volumesRaw = init.ReadString("Players", PLAYER_VOLUMES, "");
                Map<String, Float> volumes = new HashMap<String, Float>();
                if (!volumesRaw.isEmpty()) {
                    String[] entries = volumesRaw.split(";");
                    for (String entry : entries) {
                        String[] parts = entry.split("=");
                        if (parts.length == 2) {
                            try {
                                volumes.put(parts[0], Float.parseFloat(parts[1]));
                            } catch (NumberFormatException e) {}
                        }
                    }
                }
                settings.setPlayerVolumes(volumes);
                return true;
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean save() {
        if (init == null || !location.exists()) try {
            this.init = new JINIFile(location);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        init.WriteBool("Game", VOLUME_CONTROL, settings.isVolumeControlled());
        init.WriteString(
            "Audio",
            INPUT_DEVICE,
            settings.getInputDevice() != null ? settings.getInputDevice()
                .getName() : "none");
        init.WriteFloat("Audio", WORLD_VOLUME, settings.getWorldVolume());
        init.WriteFloat("Audio", INPUT_BOOST, settings.getInputBoost());
        init.WriteFloat("Audio", SPEAK_MODE, settings.getSpeakMode());

        init.WriteFloat("AdvancedAudio", ENCODING_QUALITY, settings.getEncodingQuality());
        init.WriteFloat("AdvancedAudio", ENCODING_MODE, settings.getEncodingMode());
        init.WriteBool("AdvancedAudio", DECODING_ENCHANTMENT, settings.isPerceptualEnchantmentAllowed());

        init.WriteFloat("Interface", UI_OPACITY, settings.getUIOpacity());
        init.WriteString(
            "Interface",
            UI_POSITION_SPEAK,
            settings.getUIPositionSpeak().x + ":"
                + settings.getUIPositionSpeak().y
                + ":"
                + settings.getUIPositionSpeak().type
                + ":"
                + settings.getUIPositionSpeak().scale);
        init.WriteString(
            "Interface",
            UI_POSITION_PLATE,
            settings.getUIPositionPlate().x + ":"
                + settings.getUIPositionPlate().y
                + ":"
                + settings.getUIPositionPlate().type
                + ":"
                + settings.getUIPositionPlate().scale);

        init.WriteBool("Interface", SHOW_VOICE_ICONS, settings.isVoiceIconAllowed());
        init.WriteBool("Interface", SHOW_VOICE_PLATES, settings.isVoicePlateAllowed());
        init.WriteBool("Interface", SHOW_SPEAKING_INDICATOR, settings.isSpeakingIndicatorAllowed());

        init.WriteString(
            "Miscellaneous",
            VERSION,
            VoiceChat.getProxyInstance()
                .getVersion());
        init.WriteBool("Miscellaneous", SNOOPER, settings.isSnooperAllowed());
        init.WriteBool("Miscellaneous", DEBUG, settings.isDebug());
        init.WriteInteger("Miscellaneous", MODPACK_ID, settings.getModPackID());
        StringBuilder volumesBuilder = new StringBuilder();
        for (Map.Entry<String, Float> entry : settings.getPlayerVolumes()
            .entrySet()) {
            if (volumesBuilder.length() > 0) volumesBuilder.append(";");
            volumesBuilder.append(entry.getKey())
                .append("=")
                .append(entry.getValue());
        }
        init.WriteString("Players", PLAYER_VOLUMES, volumesBuilder.toString());
        return init.UpdateFile();
    }
}
