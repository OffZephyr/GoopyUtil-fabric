package net.zephyr.goopyutil.blocks.computer;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.client.gui.screens.BlockEntityScreen;
import net.zephyr.goopyutil.util.Computer.ComputerApp;
import net.zephyr.goopyutil.util.Computer.ComputerSong;

import java.util.*;

public class ComputerData {
    private static List<Wallpaper> Wallpapers = new ArrayList<>();
    private static List<ComputerApp> Apps = new ArrayList<>();
    private static List<ComputerSong> Songs = new ArrayList<>();
    private static List<ComputerPlaylist> Playlists = new ArrayList<>();

    public static void addWallpaper(String id, Identifier texture){
        Wallpapers.add(new Wallpaper(id, texture));
    }
    public static void registerComputerApp(ComputerApp app){
        Apps.add(app);
    }
    public static void registerComputerSong(ComputerSong song){
        Songs.add(song);
    }
    public static void registerPlaylist(String name) {
        Playlists.add(new ComputerPlaylist(name));
    }
    public static void addSongToPlaylist(String name, String songName){

        ComputerSong music = null;
        for(ComputerSong song : ComputerData.getSongs()){
            if(Objects.equals(song.getName(), songName)) music = song;
        }

        if(music == null) return;

        for(ComputerPlaylist list : Playlists){
            if(Objects.equals(list.getName(), name)) list.addSong(music);
        }
    }

    public static List<Wallpaper> getWallpapers(){
        return Wallpapers;
    }
    public static List<ComputerApp> getApps(){
        return Apps;
    }
    public static List<ComputerSong> getSongs(){
        return Songs;
    }
    public static List<ComputerPlaylist> getPlaylists(){
        return Playlists;
    }
    public static ComputerPlaylist getPlaylists(String name){
        for(ComputerPlaylist list : Playlists){
            if(Objects.equals(list.getName(), name)) return list;
        }
        return null;
    }

    public static class Wallpaper {
        String id;
        Identifier texture;
        public Wallpaper(String id, Identifier texture){
            this.id = id;
            this.texture = texture;
        }
        public String getId(){
            return this.id;
        }
        public Identifier getTexture(){
            return this.texture;
        }
    }

    public static class ComputerPlaylist {
        String name;
        List<ComputerSong> playlist = new ArrayList<>();
        public ComputerPlaylist(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void addSong(ComputerSong song){
            playlist.add(song);
        }
        public List<ComputerSong> getList() {
            return playlist;
        }
    }
}
