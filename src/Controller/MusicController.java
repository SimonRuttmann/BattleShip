package Controller;

import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Simple MediaPlayer to play music
 * The music can be turn louder and quieter at the GameOptions scene
 */
public class MusicController {


    //Workaround to trick out the garbage collector
    private static MediaPlayer mediaPlayer;     //Controls the media
    private static Media music;                 //describes the media itself

    public void playMusic() {
        music = new Media(getClass().getResource("/Gui_View/Music/Soundtrack.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Platform.runLater( ()-> {
            // mediaPlayer.play();
                }
        );
        mediaPlayer.setVolume((double)ActiveGameState.getMusicVolume()/100);

        MediaView mediaView = new MediaView();
        mediaView.setMediaPlayer(mediaPlayer);

    }

    /**
     * Called by GameOptionsController
     * @param volume The volume of the music
     */
    public void setVolume(int volume){
        mediaPlayer.setVolume((double)volume/100);
    }

}
