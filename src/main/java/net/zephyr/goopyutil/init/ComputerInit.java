package net.zephyr.goopyutil.init;

import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.Apps.BrowserApp;
import net.zephyr.goopyutil.blocks.computer.Apps.MusicApp;
import net.zephyr.goopyutil.blocks.computer.Apps.RemoteApp;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.client.gui.screens.computer.COMPDesktopScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPBrowserScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPMusicPlayerScreen;
import net.zephyr.goopyutil.client.gui.screens.computer.apps.COMPRemoteScreen;
import net.zephyr.goopyutil.util.Computer.ComputerSong;

public class ComputerInit {
    public static void init(){
        ComputerData.addWallpaper("blue_checker", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/blue_checker.png"));
        ComputerData.addWallpaper("red_checker", Identifier.of(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/red_checker.png"));
        ComputerData.addWallpaper("windows_xp", Identifier.of(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/windows_xp.png"));
        ComputerData.addWallpaper("markiplier", Identifier.of(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/markiplier.png"));
        ComputerData.addWallpaper("whistle", Identifier.of(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/whistle.png"));

        ComputerData.registerComputerApp(new MusicApp(new COMPMusicPlayerScreen(Text.empty()), "music_player", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/music_icon.png")));
        ComputerData.registerComputerApp(new BrowserApp(new COMPBrowserScreen(Text.empty()), "browser",Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/browser_icon.png")));
        ComputerData.registerComputerApp(new RemoteApp(new COMPRemoteScreen(Text.empty()), "remote",Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/remote_icon.png")));

        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_13.value(), "13", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_13.png"), 178));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CAT.value(), "Cat", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_cat.png"), 185));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_BLOCKS.value(), "Blocks", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_blocks.png"), 345));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CHIRP.value(), "Chirp", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_chirp.png"), 185));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_FAR.value(), "Far", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_far.png"), 174));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_MALL.value(), "Mall", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_mall.png"), 197));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_MELLOHI.value(), "Mellohi", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_mellohi.png"), 96));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_STAL.value(), "Stal", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_stal.png"), 150));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_STRAD.value(), "Strad", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_strad.png"), 188));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_WARD.value(), "Ward", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_ward.png"), 251));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_11.value(), "11", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_11.png"), 71));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_WAIT.value(), "Wait", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_wait.png"), 238));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_PIGSTEP.value(), "Pigstep", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_pigstep.png"), 149));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_OTHERSIDE.value(), "Otherside", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_otherside.png"), 195));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_5.value(), "5", "Samuel Åberg", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_5.png"), 178));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_RELIC.value(), "Relic", "Aaron Cherof", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_relic.png"), 218));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_PRECIPICE.value(), "Precipice", "Aaron Cherof", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_precipice.png"), 299));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR.value(), "Creator", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_creator.png"), 176));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX.value(), "Creator (Music Box)", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_creator_music_box.png"), 73));

        ComputerData.registerPlaylist("Minecraft");

        ComputerData.addSongToPlaylist("Minecraft", "13");
        ComputerData.addSongToPlaylist("Minecraft", "Cat");
        ComputerData.addSongToPlaylist("Minecraft", "Blocks");
        ComputerData.addSongToPlaylist("Minecraft", "Chirp");
        ComputerData.addSongToPlaylist("Minecraft", "Far");
        ComputerData.addSongToPlaylist("Minecraft", "Mall");
        ComputerData.addSongToPlaylist("Minecraft", "Mellohi");
        ComputerData.addSongToPlaylist("Minecraft", "Stal");
        ComputerData.addSongToPlaylist("Minecraft", "Strad");
        ComputerData.addSongToPlaylist("Minecraft", "Ward");
        ComputerData.addSongToPlaylist("Minecraft", "11");
        ComputerData.addSongToPlaylist("Minecraft", "Wait");
        ComputerData.addSongToPlaylist("Minecraft", "Pigstep");
        ComputerData.addSongToPlaylist("Minecraft", "Otherside");
        ComputerData.addSongToPlaylist("Minecraft", "5");
        ComputerData.addSongToPlaylist("Minecraft", "Relic");
        ComputerData.addSongToPlaylist("Minecraft", "Precipice");
        ComputerData.addSongToPlaylist("Minecraft", "Creator");
        ComputerData.addSongToPlaylist("Minecraft", "Creator (Music Box)");

    }
}
