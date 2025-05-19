package edu.ntnu.idi.idatt.boardgame.ui.javafx.audio;

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

  private static final Map<GameSoundEffects, AudioClip> audioClips = new HashMap<>();

  static {
    preloadAudio(GameSoundEffects.VICTORY, "data/audio/victory.mp3");
    preloadAudio(GameSoundEffects.CORRECT_ANSWER, "data/audio/correct-answer.mp3");
    preloadAudio(GameSoundEffects.INCORRECT_ANSWER, "data/audio/wrong-answer.m4a");
    preloadAudio(GameSoundEffects.FREEZE, "data/audio/freeze.m4a");
    preloadAudio(GameSoundEffects.IMMUNITY, "data/audio/immunity.mp3");
    preloadAudio(GameSoundEffects.LADDER_CLIMB, "data/audio/ladder_up.wav");
    preloadAudio(GameSoundEffects.LADDER_FALL, "data/audio/ladder_down.wav");
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
  private static AudioClip preloadAudio(GameSoundEffects name, String audioFilePath) {
    if (audioClips.containsKey(name)) {
      return audioClips.get(name);
    }
    File audioFile = new File(audioFilePath);
    if (!audioFile.exists()) {
      throw new IllegalArgumentException("Audio file not found: " + audioFilePath);
    }
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
  public static void playAudio(GameSoundEffects name) {
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
  public static void stopAudio(GameSoundEffects name) {
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
  public static AudioClip getAudioClip(GameSoundEffects name) {
    return audioClips.get(name);
  }
}
