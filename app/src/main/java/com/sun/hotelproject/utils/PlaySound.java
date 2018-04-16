package com.sun.hotelproject.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by win7 on 2018/1/30.
 */

public class PlaySound {
    public static void play(int Id,Context c){
        MediaPlayer mediaPlayer = null;
        AudioManager audioManager = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0) {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(c,
                        Id);
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }
}
