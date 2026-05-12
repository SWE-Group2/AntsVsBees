package core;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private MediaPlayer mediaPlayer;
    private String currentFilePath;
    private double volume = 0.5;

    public void playBackgroundMusic(String filePath) {
        if (mediaPlayer != null && filePath.equals(currentFilePath)) {
            mediaPlayer.play();
            return;
        }

        URL resource = MusicManager.class.getClassLoader().getResource(filePath);

        if (resource == null) {
            System.out.println("Music file not found: " + filePath);
            return;
        }

        Media media = new Media(resource.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        currentFilePath = filePath;

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever
        mediaPlayer.setVolume(volume); // 0.0 to 1.0

        mediaPlayer.play();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
