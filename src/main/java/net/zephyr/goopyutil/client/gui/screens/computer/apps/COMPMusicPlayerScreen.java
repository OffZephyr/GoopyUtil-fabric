package net.zephyr.goopyutil.client.gui.screens.computer.apps;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.util.Computer.ComputerSong;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class COMPMusicPlayerScreen extends COMPBaseAppScreen {
    public Identifier SMALL_BAR = new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/window_smallbar_20.png");

    boolean playlistsScreen = false;
    float playlistOffset = 0;
    float playlistOffsetDiff = 0;
    float playlistOffsetOld = 0;
    Map<ComputerData.ComputerPlaylist, Boolean> playlists = new LinkedHashMap<>();
    public COMPMusicPlayerScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        playlists = new HashMap<>();

        ComputerData.ComputerPlaylist allTracks = new ComputerData.ComputerPlaylist("All Tracks");
        for(ComputerSong song : ComputerData.getSongs()){
            allTracks.addSong(song);
        }

        playlists.put(allTracks, false);

        for(ComputerData.ComputerPlaylist list : ComputerData.getPlaylists()){
            playlists.put(list, false);
        }

        playlistsScreen = false;
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        float buttonOffset = (2/256f)*screenSize;
        float buttonSize = (16/256f)*screenSize;

        if(mouseX > (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset && mouseX < (int)topCornerX + (int)appAvailableSizeX - (int)buttonOffset && mouseY >(int)topCornerY + (int)buttonOffset && mouseY < (int)topCornerY + (int)buttonOffset + (int) buttonSize) {
            playlistsScreen = !playlistsScreen;
        }

        if (playlistsScreen && mouseX > topCornerX && mouseY > topCornerY && mouseX < topCornerX + appAvailableSizeX && mouseY < topCornerY + appAvailableSizeY && !dragging) {
            float topBarPos = (20 / 256f) * screenSize;
            float textOffset = (8 / 256f) * screenSize;
            int spacingOffset = 12;
            int spacing = 0;

            for (ComputerData.ComputerPlaylist play : playlists.keySet()) {
                float iconSize = 10;
                int yPos = (int) topCornerY + (int) topBarPos + (int) textOffset + spacing + (int) playlistOffset;

                if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > yPos - 2 && mouseY < yPos - 2 + spacingOffset && !dragging) {
                    openClosePlaylist(play);
                }

                if (playlists.get(play)) {
                    for (int j = 0; j < play.getList().size(); j++) {
                        int songYPos = yPos - 2 + spacingOffset + spacingOffset * j;
                        if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > songYPos && mouseY < songYPos  + (int) iconSize + 2 && !dragging) {
                            System.out.println(play.getList().get(j).getName());
                        }
                    }
                    spacing += spacingOffset * (play.getList().size() + 1);
                } else {
                    spacing += spacingOffset;
                }
            }
        }
            return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    void openClosePlaylist(ComputerData.ComputerPlaylist setList){
        for(ComputerData.ComputerPlaylist list : playlists.keySet()){
            if(list != setList)
                playlists.replace(list, false);
        }
        playlists.replace(setList, !playlists.get(setList));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(this.width/2 - this.screenSize/2, this.height/2 - this.screenSize/2, this.width/2 + this.screenSize/2, this.height/2 + this.screenSize/2, 0xFF182562);

        float topBarPos = (20/256f) * screenSize;
        float textOffset = (8/256f) * screenSize;
        float buttonOffset = (2/256f) * screenSize;
        float buttonSize = (16/256f) * screenSize;
        float buttonTextureSize = (128/256f)*screenSize;

        if(playlistsScreen) {

            context.fill(this.width / 2 - this.screenSize / 2, this.height / 2 - this.screenSize / 2, this.width / 2 + this.screenSize / 2, this.height / 2 + this.screenSize / 2, 0x99000000);

            int spacingOffset = 12;
            int spacing = 0;
            for (ComputerData.ComputerPlaylist play : playlists.keySet()) {
                float iconSize = 10;
                int yPos = (int) topCornerY + (int) topBarPos + (int) textOffset + spacing + (int)playlistOffset;

                String playlistName = play.getName();
                if (!playlists.get(play)) playlistName = playlistName + "...";
                context.drawText(this.textRenderer, playlistName, this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize, yPos, 0xFFFFFFFF, false);

                if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > yPos - 2 && mouseY < yPos - 2 + spacingOffset && !dragging) {
                    if (holding) {
                        context.fill((int) topCornerX, yPos - 2, (int) topCornerX + (int) appAvailableSizeX, yPos - 2 + spacingOffset, 0x99FFFFFF);
                    } else {
                        context.fill((int) topCornerX, yPos - 2, (int) topCornerX + (int) appAvailableSizeX, yPos - 2 + spacingOffset, 0x66FFFFFF);
                    }
                }

                if (playlists.get(play)) {
                    for (int j = 0; j < play.getList().size(); j++) {

                        int songYPos = yPos - 2 + spacingOffset + spacingOffset * j;
                        ComputerSong song = play.getList().get(j);

                        String text = song.getName() + " | " + song.getAuthor();

                        if(j % 2 == 0)
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x44000011);

                        context.drawTexture(song.getIconTexture(), this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize, songYPos + 1, 0, 0, (int) iconSize, (int) iconSize, (int) iconSize, (int) iconSize);
                        context.drawText(this.textRenderer, text, this.width / 2 - (int) appAvailableSizeX / 2 + (int) iconSize * 3, songYPos + (int) buttonOffset, 0xFFFFFFFF, false);

                        if (mouseX > topCornerX && mouseX < topCornerX + appAvailableSizeX && mouseY > songYPos && mouseY < songYPos  + (int) iconSize + 2 && !dragging) {
                            if (holding) {
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x66FFFFFF);
                            } else {
                            context.fill((int) topCornerX, songYPos, (int) topCornerX + (int) appAvailableSizeX, songYPos + (int) iconSize + 2, 0x33FFFFFF);
                            }
                        }

                    }
                    spacing += spacingOffset * (play.getList().size() + 1);
                } else {
                    spacing += spacingOffset;
                }
            }

            if (dragging) {
                playlistOffset = playlistOffsetOld + playlistOffsetDiff;
                playlistOffsetDiff = mouseY - gotMouseY;
            } else {
                playlistOffsetOld = playlistOffset;
                playlistOffsetDiff = 0;

                if (playlistOffset > 0) {
                    playlistOffset += (0 - playlistOffset) / 15;
                }
                float bottomHeight = -(spacing - (appAvailableSizeY - (4 * textOffset)));
                if(bottomHeight > 0) bottomHeight = 0;
                if (playlistOffset < bottomHeight) {
                    playlistOffset += (bottomHeight - playlistOffset) / 15;
                }
            }
        }

        context.drawTexture(SMALL_BAR, (int)topCornerX, (int)topCornerY, 0, 0, (int)appAvailableSizeX, (int)topBarPos, (int)appAvailableSizeX, (int)topBarPos);

        if(mouseX > (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset && mouseX < (int)topCornerX + (int)appAvailableSizeX - (int)buttonOffset && mouseY >(int)topCornerY + (int)buttonOffset && mouseY < (int)topCornerY + (int)buttonOffset + (int) buttonSize){
            if(holding){
                context.drawTexture(BUTTONS, (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset, (int)topCornerY + (int)buttonOffset, buttonSize*5, buttonSize, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
            else {
                context.drawTexture(BUTTONS, (int)topCornerX + (int)appAvailableSizeX - (int)buttonSize - (int)buttonOffset, (int)topCornerY + (int)buttonOffset, buttonSize*4, buttonSize, (int)buttonSize, (int)buttonSize, (int)buttonTextureSize, (int)buttonTextureSize);
            }
        }
        else {
            if (playlistsScreen) {
                context.drawTexture(BUTTONS, (int) topCornerX + (int) appAvailableSizeX - (int) buttonSize - (int) buttonOffset, (int) topCornerY + (int) buttonOffset, buttonSize * 5, buttonSize, (int) buttonSize, (int) buttonSize, (int) buttonTextureSize, (int) buttonTextureSize);
            } else {
                context.drawTexture(BUTTONS, (int) topCornerX + (int) appAvailableSizeX - (int) buttonSize - (int) buttonOffset, (int) topCornerY + (int) buttonOffset, buttonSize * 3, buttonSize, (int) buttonSize, (int) buttonSize, (int) buttonTextureSize, (int) buttonTextureSize);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }
}
