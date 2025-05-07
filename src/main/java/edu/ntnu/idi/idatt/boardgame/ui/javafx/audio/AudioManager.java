package edu.ntnu.idi.idatt.boardgame.ui.javafx.audio;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;

/**
 * Manages audio playback for the game.
 * <p>
 * This class is responsible for preloading and playing audio clips used in the game.
 * </p>
 *
 * @version 1.0.0
 * @since v3.0.0
 */
public class AudioManager {

  private final FileProvider fileProvider;
  private final Map<String, AudioClip> audioClips = new HashMap<>();


  public AudioManager(FileProvider fileProvider) {
    this.fileProvider = fileProvider;
    preloadAudio(GameSoundEffects.CLICK.getName(), "data/audio/click.mp3");
    preloadAudio(GameSoundEffects.VICTORY.getName(), "data/audio/victory.mp3");
    preloadAudio(GameSoundEffects.QUESTION.getName(), "data/audio/question.m4a");
    preloadAudio(GameSoundEffects.CORRECT_ANSWER.getName(), "data/audio/answer.m4a");
    preloadAudio(GameSoundEffects.INCORRECT_ANSWER.getName(), "data/audio/wrong-answer.m4a");
    preloadAudio(GameSoundEffects.FREEZE.getName(), "data/audio/freeze.m4a");
    preloadAudio(GameSoundEffects.IMMUNITY.getName(), "data/audio/immunity.mp3");
    preloadAudio(GameSoundEffects.LADDER_CLIMB.getName(), "data/audio/ladder_up.mp3");
    preloadAudio(GameSoundEffects.LADDER_FALL.getName(), "data/audio/ladder_down.mp3");
  }

  /**
   * Preloads an audio clip with the specified name and file path.
   * <p>
   * If the audio clip is already preloaded, it returns the existing instance.
   * </p>
   *
   * @param name          the name of the audio clip
   * @param audioFilePath the file path of the audio clip
   * @return the preloaded audio clip
   */
  public AudioClip preloadAudio(String name, String audioFilePath) {
    if (audioClips.containsKey(name)) {
      return audioClips.get(name);
    }

    if (!fileProvider.exists(audioFilePath)) {
      throw new IllegalArgumentException("Audio file not found: " + audioFilePath);
    }
    File audioFile = new File(audioFilePath);
    AudioClip audioClip = new AudioClip(audioFile.toURI().toString());
    audioClips.put(name, audioClip);
    return audioClip;
  }

  /**
   * Plays the audio clip with the specified name.
   * <p>
   * If the audio clip is not found, an exception is thrown.
   * </p>
   *
   * @param name the name of the audio clip to play
   */
  public void playAudio(String name) {
    AudioClip audioClip = audioClips.get(name);
    if (audioClip != null) {
      audioClip.play();
    } else {
      throw new IllegalArgumentException("Audio clip not found: " + name);
    }
  }

  /**
   * Stops the audio clip with the specified name.
   * <p>
   * If the audio clip is not found, it does nothing.
   * </p>
   *
   * @param name the name of the audio clip to stop
   */
  public void stopAudio(String name) {
    AudioClip audioClip = audioClips.get(name);
    if (audioClip != null) {
      audioClip.stop();
    }
  }

  /**
   * Retrieves the audio clip with the specified name.
   * <p>
   * If the audio clip is not found, it returns null.
   * </p>
   *
   * @param name the name of the audio clip to retrieve
   * @return the audio clip, or null if not found
   */
  public AudioClip getAudioClip(String name) {
    return audioClips.get(name);
  }


}
