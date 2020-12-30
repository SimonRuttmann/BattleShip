package Controller;

import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;

public class MusicController {

    //Muss static sein und eine view haben -> Garbage collector austricksen durch (dauerhafte referenz auf objekt in mainmenu)
    private static MediaPlayer mediaPlayer;    //Medium kontrollieren -> Laut leise stop start
    private static Media music;            //Beschreibung des mediums an sich

    public void playMusic() {
        music = new Media(getClass().getResource("/Gui_View/Music/Soundtrack.mp3").toExternalForm());
        //Media music2 = new Media(new File("/Gui_View/Music/Soundtrack.mp3").toURI().toURL().toExternalForm());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Platform.runLater( ()-> {
            // mediaPlayer.play();
                }
        );
        mediaPlayer.setVolume((double)ActiveGameState.getMusicVolume()/100);

      /*  Thread musicThread = new Thread(new Runnable() {
            @Override
            public void run() {

                mediaPlayer.play();
            }
        });
        musicThread.start();
*/

        //Media player -> Medium abspielen
        //Media view -> Medium anzeigen
        MediaView mediaView = new MediaView();
        mediaView.setMediaPlayer(mediaPlayer);

    }

    public void setVolume(int volume){
        mediaPlayer.setVolume((double)volume/100);
    }

}
