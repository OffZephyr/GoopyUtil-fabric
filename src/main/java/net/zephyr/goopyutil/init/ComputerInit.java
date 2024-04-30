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
        ComputerData.addWallpaper("blue_checker", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/blue_checker.png"));
        ComputerData.addWallpaper("red_checker", new Identifier(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/red_checker.png"));
        ComputerData.addWallpaper("windows_xp", new Identifier(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/windows_xp.png"));
        ComputerData.addWallpaper("markiplier", new Identifier(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/markiplier.png"));
        ComputerData.addWallpaper("whistle", new Identifier(GoopyUtil.MOD_ID,"textures/gui/computer/wallpapers/whistle.png"));

        ComputerData.registerComputerApp(new MusicApp(new COMPMusicPlayerScreen(Text.empty()), "music_player", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/icons/music_icon.png")));
        ComputerData.registerComputerApp(new BrowserApp(new COMPBrowserScreen(Text.empty()), "browser",new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/icons/browser_icon.png")));
        ComputerData.registerComputerApp(new RemoteApp(new COMPRemoteScreen(Text.empty()), "remote",new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/icons/remote_icon.png")));

        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_13, "13", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_13.png"), 178));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CAT, "Cat", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_cat.png"), 185));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_BLOCKS, "Blocks", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_blocks.png"), 345));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_CHIRP, "Chirp", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_chirp.png"), 185));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_FAR, "Far", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_far.png"), 174));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_MALL, "Mall", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_mall.png"), 197));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_MELLOHI, "Mellohi", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_mellohi.png"), 96));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_STAL, "Stal", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_stal.png"), 150));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_STRAD, "Strad", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_strad.png"), 188));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_WARD, "Ward", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_ward.png"), 251));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_11, "11", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_11.png"), 71));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_WAIT, "Wait", "C418", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_wait.png"), 238));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_OTHERSIDE, "Otherside", "Lena Raine", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_otherside.png"), 195));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_RELIC, "Relic", "Aaron Cherof", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_relic.png"), 218));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_5, "5", "Samuel Åberg", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_5.png"), 178));
        ComputerData.registerComputerSong(new ComputerSong(SoundEvents.MUSIC_DISC_PIGSTEP, "Pigstep", "Lena Raine", new Identifier(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/music_disc_pigstep.png"), 149));

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
        ComputerData.addSongToPlaylist("Minecraft", "Otherside");
        ComputerData.addSongToPlaylist("Minecraft", "Relic");
        ComputerData.addSongToPlaylist("Minecraft", "5");
        ComputerData.addSongToPlaylist("Minecraft", "Pigstep");
    }
}
