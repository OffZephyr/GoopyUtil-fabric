package net.zephyr.goopyutil.init;

import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.Apps.BrowserApp;
import net.zephyr.goopyutil.blocks.computer.Apps.CodeApp;
import net.zephyr.goopyutil.blocks.computer.Apps.MusicApp;
import net.zephyr.goopyutil.blocks.computer.Apps.RemoteApp;
import net.zephyr.goopyutil.blocks.computer.ComputerData;
import net.zephyr.goopyutil.util.Computer.ComputerAI;
import net.zephyr.goopyutil.util.Computer.ComputerApp;
import net.zephyr.goopyutil.util.Computer.ComputerPlaylist;
import net.zephyr.goopyutil.util.Computer.ComputerSong;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.ArrayList;
import java.util.List;

public class DefaultComputerInit implements ComputerData.Initializer {
    @Override
    public List<Wallpaper> getWallpapers() {
        List<Wallpaper> wallpapers = new ArrayList<>();
        wallpapers.add(new Wallpaper("blue_checker", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/blue_checker.png")));
        wallpapers.add(new Wallpaper("red_checker", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/red_checker.png")));
        wallpapers.add(new Wallpaper("windows_xp", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/windows_xp.png")));
        wallpapers.add(new Wallpaper("markiplier", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/markiplier.png")));
        wallpapers.add(new Wallpaper("whistle", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/wallpapers/whistle.png")));
        return wallpapers;
    }

    @Override
    public List<ComputerApp> getApps() {
        List<ComputerApp> apps = new ArrayList<>();
        apps.add(new MusicApp("music_player", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/music_icon.png")));
        apps.add(new BrowserApp("browser", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/browser_icon.png")));
        apps.add(new RemoteApp("remote", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/remote_icon.png")));
        apps.add(new CodeApp("code", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/icons/code_icon.png")));
        return apps;
    }

    @Override
    public List<ComputerSong> getSongs() {
        List<ComputerSong> songs = new ArrayList<>();
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_13.value(), "13", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/alpha.png"), 178));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CAT.value(), "Cat", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/alpha.png"), 185));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_BLOCKS.value(), "Blocks", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 345));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CHIRP.value(), "Chirp", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 185));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_FAR.value(), "Far", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 174));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_MALL.value(), "Mall", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 197));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_MELLOHI.value(), "Mellohi", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 96));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_STAL.value(), "Stal", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 150));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_STRAD.value(), "Strad", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 188));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_WARD.value(), "Ward", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 251));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_11.value(), "11", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 71));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_WAIT.value(), "Wait", "C418", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/beta.png"), 238));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_PIGSTEP.value(), "Pigstep", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/nether_update_ost.png"), 149));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_OTHERSIDE.value(), "Otherside", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/caves_cliffs_ost.png"), 195));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_5.value(), "5", "Samuel Åberg", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/wild_update_ost.png"), 178));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_RELIC.value(), "Relic", "Aaron Cherof", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/trails_tales_ost.png"), 218));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_PRECIPICE.value(), "Precipice", "Aaron Cherof", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 299));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR.value(), "Creator", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 176));
        songs.add(new ComputerSong(SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX.value(), "Creator (Music Box)", "Lena Raine", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/tricky_trials_ost.png"), 73));

        songs.add(new ComputerSong(SoundsInit.CASUAL_BONGOS, "Casual Bongos", "Some stock Library", Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/music_icons/casual_bongos.png"), 8));
        return songs;
    }

    @Override
    public List<ComputerPlaylist> getPlaylists() {

        List<ComputerPlaylist> playlists = new ArrayList<>();
        playlists.add(new ComputerPlaylist("Minecraft",
                "13",
                "Cat",
                "Blocks",
                "Chirp",
                "Far",
                "Mall",
                "Mellohi",
                "Stal",
                "Strad",
                "Ward",
                "11",
                "Wait",
                "Pigstep",
                "Otherside",
                "5",
                "Relic",
                "Precipice",
                "Creator",
                "Creator (Music Box)"
        ));

        playlists.add(new ComputerPlaylist("GoopyUtil",
                "Casual Bongos"
        ));
        return playlists;
    }

    @Override
    public List<AnimatronicAI> getAnimatronics() {
        List<AnimatronicAI> AIs = new ArrayList<>();
        AIs.add(new AnimatronicAI(Text.translatable("goopyutil.mod_name"), EntityInit.ZEPHYR, "goopyutil.zephyr"));
        return AIs;
    }

    @Override
    public List<AnimatronicCategory<?>> getAICategories() {
        AnimatronicCategory<?> subCategory = new AnimatronicCategory<>(getAnimatronics(), Text.translatable("goopyutil.computer.demo_subcategory"), "goopyutil.entities");
        List<AnimatronicCategory<?>> subcategories = new ArrayList<>();
        subcategories.add(subCategory);

        AnimatronicCategory<?> Category = new AnimatronicCategory<>(subcategories, Identifier.of(GoopyUtil.MOD_ID, "textures/gui/computer/gu_category_button.png"), "goopyutil.main");
        List<AnimatronicCategory<?>> categories = new ArrayList<>();
        categories.add(Category);

        return categories;
    }
    @Override
    public List<ComputerAI> getAIBehaviors() {
        List<ComputerAI> AIs = new ArrayList<>();

        AIs.add(new ComputerAI("stage",
                new ComputerAI.Option<>("deactivated", false),
                new ComputerAI.Option<>("performing", false, "deactivated", true),
                new ComputerAI.Option<>("spawn_pos", true),
                new ComputerAI.Option<>("position", new BlockPos(0, 0, 0), "spawn_pos", true),
                new ComputerAI.Option<>("teleport", false),
                new ComputerAI.Option<>("aggressive", false)
        ));

        AIs.add(new ComputerAI("moving",
                new ComputerAI.Option<>("weeping_angel", false),
                new ComputerAI.Option<>("aggressive", false),
                new ComputerAI.Option<>("performing_idle", false, "custom_animation", true),
                new ComputerAI.Option<>("deactivated_idle", false, "custom_animation", true),
                new ComputerAI.Option<>("custom_idle_animation", false),
                new ComputerAI.Option<>("idle_animation", new ArrayList<RawAnimation>(), "custom_idle_animation")
        ));

        AIs.add(new ComputerAI("statue",
                new ComputerAI.Option<>("animation", new ArrayList<RawAnimation>()),
                new ComputerAI.Option<>("spawn_pos", true),
                new ComputerAI.Option<>("position", new BlockPos(0, 0, 0), "spawn_pos", true),
                new ComputerAI.Option<>("teleport", false, "spawn_pos", true),
                new ComputerAI.Option<>("spawn_rot", true),
                new ComputerAI.Option<>("rotation", 0, "spawn_rot", true),
                new ComputerAI.Option<>("rotate_head", false),
                new ComputerAI.Option<>("head_yaw", 0, "rotate_head"),
                new ComputerAI.Option<>("head_pitch", 0, "rotate_head")
        ));

        return AIs;
    }
}
