/*
Copyright 2021 Quantizr(_risk)
This file is used as part of Dungeon Rooms Mod (DRM). (Github: <https://github.com/Quantizr/DungeonRoomsMod>)
DRM is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
DRM is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with DRM.  If not, see <https://www.gnu.org/licenses/>.
*/

package io.github.quantizr.gui;

import io.github.quantizr.core.AutoRoom;
import io.github.quantizr.core.Waypoints;
import io.github.quantizr.handlers.ConfigHandler;
import io.github.quantizr.handlers.TextRenderer;
import io.github.quantizr.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaypointsGUI extends GuiScreen {

    private GuiButton waypointsEnabled;
    private GuiButton showEntrance;
    private GuiButton showSuperboom;
    private GuiButton showSecrets;
    private GuiButton showFairySouls;
    private GuiButton disableWhenAllFound;
    private GuiButton close;

    public static List<GuiButton> secretButtonList = new ArrayList<>(Arrays.asList(new GuiButton[9]));

    private static boolean waypointGuiOpened = false;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        waypointGuiOpened = true;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int height = sr.getScaledHeight();
        int width = sr.getScaledWidth();

        waypointsEnabled = new GuiButton(0, width / 2 - 90, height / 6, 180, 20, waypointBtnText());
        showEntrance = new GuiButton(1, (width / 2 - 90) - 100, height / 6 + 30, 180, 20, "Show Entrance Waypoints: " + getOnOff(Waypoints.showEntrance));
        showSuperboom = new GuiButton(2, (width / 2 - 90) + 100, height / 6 + 30, 180, 20, "Show Superboom Waypoints: " + getOnOff(Waypoints.showSuperboom));
        showSecrets = new GuiButton(3, (width / 2 - 90) - 100, height / 6 + 60, 180, 20, "Show Secret Waypoints: " + getOnOff(Waypoints.showSecrets));
        showFairySouls = new GuiButton(4, (width / 2 - 90) + 100, height / 6 + 60, 180, 20, "Show Fairy Soul Waypoints: " + getOnOff(Waypoints.showFairySouls));
        disableWhenAllFound = new GuiButton(5, width / 2 - 90, height / 6 + 90, 180, 20, "Disable when all secrets found: " + getOnOff(Waypoints.disableWhenAllFound));
        close = new GuiButton(6, width / 2 - 90, (height / 6)*5, 180, 20, "Close");

        this.buttonList.add(waypointsEnabled);
        this.buttonList.add(showEntrance);
        this.buttonList.add(showSuperboom);
        this.buttonList.add(showSecrets);
        this.buttonList.add(showFairySouls);
        this.buttonList.add(disableWhenAllFound);
        this.buttonList.add(close);

        if (Utils.inDungeons) {
            if (Waypoints.secretNum > 0) {
                if (Waypoints.secretNum <= 5) {
                    for (int i = 1; i <= Waypoints.secretNum; i++) {
                        int adjustPos = (-40 * (Waypoints.secretNum)) - 70 + (80 * i);
                        secretButtonList.set(i - 1, new GuiButton(10 + i, (width / 2) + adjustPos, height / 6 + 170, 60, 20, i + ": " + getOnOff(Waypoints.secretsList.get(i - 1))));
                        this.buttonList.add(secretButtonList.get(i - 1));
                    }
                } else {
                    for (int i = 1; i <= (int) Math.ceil((double) Waypoints.secretNum / 2); i++) {
                        int adjustPos = (-40 * ((int) Math.ceil((double) Waypoints.secretNum / 2))) - 70 + (80 * i);
                        secretButtonList.set(i - 1, new GuiButton(10 + i, (width / 2) + adjustPos, height / 6 + 170, 60, 20, i + ": " + getOnOff(Waypoints.secretsList.get(i - 1))));
                        this.buttonList.add(secretButtonList.get(i - 1));
                    }
                    for (int i = (int) Math.ceil((double) Waypoints.secretNum / 2) + 1; i <= Waypoints.secretNum; i++) {
                        int adjustPos = (-40 * (Waypoints.secretNum - (int) Math.ceil((double) Waypoints.secretNum / 2))) - 70 + (80 * (i-(int) Math.ceil((double) Waypoints.secretNum / 2)));
                        secretButtonList.set(i - 1, new GuiButton(10 + i, (width / 2) + adjustPos, height / 6 + 200, 60, 20, i + ": " + getOnOff(Waypoints.secretsList.get(i - 1))));
                        this.buttonList.add(secretButtonList.get(i - 1));
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Minecraft mc = Minecraft.getMinecraft();

        String displayText = "Dungeon Room Waypoints:";
        int displayWidth = mc.fontRendererObj.getStringWidth(displayText);
        TextRenderer.drawText(mc, displayText, width / 2 - displayWidth / 2, height / 6 - 20, 1D, false);

        String subtext1 = "Toggle Room Specific Waypoints:";
        int subtext1Width = mc.fontRendererObj.getStringWidth(subtext1);
        TextRenderer.drawText(mc, subtext1, width / 2 - subtext1Width / 2, height / 6 + 140, 1D, false);

        String subtext2 = "(You can also press the # key matching the secret instead)";
        int subtext2Width = mc.fontRendererObj.getStringWidth(subtext2);
        TextRenderer.drawText(mc, EnumChatFormatting.GRAY + subtext2, width / 2 - subtext2Width / 2, height / 6 + 150, 1D, false);


        if (!Utils.inDungeons) {
            String subtext3 = "Not in dungeons";
            int subtext3Width = mc.fontRendererObj.getStringWidth(subtext3);
            TextRenderer.drawText(mc, EnumChatFormatting.RED + subtext3, width / 2 - subtext3Width / 2, height / 6 + 170, 1D, false);
        } else if (Waypoints.secretNum == 0) {
            String subtext3 = "No secrets in this room";
            int subtext3Width = mc.fontRendererObj.getStringWidth(subtext3);
            TextRenderer.drawText(mc, EnumChatFormatting.RED + subtext3, width / 2 - subtext3Width / 2, height / 6 + 170, 1D, false);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (button == waypointsEnabled) {
            Waypoints.enabled = !Waypoints.enabled;
            ConfigHandler.writeBooleanConfig("toggles", "waypointsToggled", Waypoints.enabled);
            waypointsEnabled.displayString = waypointBtnText();
        } else if (button == showEntrance) {
            Waypoints.showEntrance = !Waypoints.showEntrance;
            ConfigHandler.writeBooleanConfig("waypoint", "showEntrance", Waypoints.showEntrance);
            showEntrance.displayString = "Show Entrance Waypoints: " + getOnOff(Waypoints.showEntrance);
        } else if (button == showSuperboom) {
            Waypoints.showSuperboom = !Waypoints.showSuperboom;
            ConfigHandler.writeBooleanConfig("waypoint", "showSuperboom", Waypoints.showSuperboom);
            showSuperboom.displayString = "Show Superboom Waypoints: " + getOnOff(Waypoints.showSuperboom);
        } else if (button == showSecrets) {
            Waypoints.showSecrets = !Waypoints.showSecrets;
            ConfigHandler.writeBooleanConfig("waypoint", "showSecrets", Waypoints.showSecrets);
            showSecrets.displayString = "Show Secret Waypoints: " + getOnOff(Waypoints.showSecrets);
        } else if (button == showFairySouls) {
            Waypoints.showFairySouls = !Waypoints.showFairySouls;
            ConfigHandler.writeBooleanConfig("waypoint", "showFairySouls", Waypoints.showFairySouls);
            showFairySouls.displayString = "Show Fairy Soul Waypoints: " + getOnOff(Waypoints.showFairySouls);
        } else if (button == disableWhenAllFound) {
            Waypoints.disableWhenAllFound = !Waypoints.disableWhenAllFound;
            ConfigHandler.writeBooleanConfig("waypoint", "disableWhenAllFound", Waypoints.disableWhenAllFound);
            disableWhenAllFound.displayString = "Disable when all secrets found: " + getOnOff(Waypoints.disableWhenAllFound);
        }
        else if (button == close) {
            player.closeScreen();
        }

        if (Utils.inDungeons) {
            if (Waypoints.secretNum > 0) {
                for (int i = 1; i <= Waypoints.secretNum; i++) {
                    if (button == secretButtonList.get(i-1)) {
                        Waypoints.secretsList.set(i-1, !Waypoints.secretsList.get(i-1));
                        if (AutoRoom.lastRoomName != null) Waypoints.allSecretsMap.replace(AutoRoom.lastRoomName, Waypoints.secretsList);
                        secretButtonList.get(i-1).displayString = i + ": " + getOnOff(Waypoints.secretsList.get(i - 1));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        waypointGuiOpened = false;
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException
    {
        super.keyTyped(c, keyCode);

        if (waypointGuiOpened && Utils.inDungeons) {
            if (Waypoints.secretNum > 0) {
                for (int i = 1; i <= Waypoints.secretNum; i++) {
                    if ((char) keyCode == i+1) {
                        Waypoints.secretsList.set(i - 1, !Waypoints.secretsList.get(i - 1));
                        if (AutoRoom.lastRoomName != null) Waypoints.allSecretsMap.replace(AutoRoom.lastRoomName, Waypoints.secretsList);
                        secretButtonList.get(i - 1).displayString = i + ": " + getOnOff(Waypoints.secretsList.get(i - 1));
                        break;
                    }
                }
            }
        }
    }

    private static String waypointBtnText() {
        if (Waypoints.enabled){
            return EnumChatFormatting.GREEN + "Waypoints Enabled";
        } else {
            return EnumChatFormatting.RED + "Waypoints Disabled";
        }
    }

    private static String getOnOff(boolean bool) {
        if (bool){
            return EnumChatFormatting.GREEN + "On";
        } else {
            return EnumChatFormatting.RED + "Off";
        }
    }
}