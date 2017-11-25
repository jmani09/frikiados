package com.appyuken.frikiadospremium.service;

import com.appyuken.frikiadospremium.R;
import com.appyuken.frikiadospremium.classes.SessionManager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundAudiioService extends Service {
	MediaPlayer mediaPlayer;
	int currentPos = 0;
	private SessionManager sessionManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("Entra en audio");
		sessionManager = new SessionManager(getApplicationContext());
		mediaPlayer = MediaPlayer.create(this, R.raw.music_frikiados);// raw/s.mp3
		mediaPlayer.setLooping(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (sessionManager.isMusicEnabled()) {
		if (!mediaPlayer.isPlaying()) {
			if (sessionManager.getMusicPosition() == 0) {
				System.out.println("Igual a cero "
						+ sessionManager.getMusicPosition());
				mediaPlayer.start();
			} else {
				System.out.println("No cero: "
						+ sessionManager.getMusicPosition());
				mediaPlayer.seekTo((int) sessionManager.getMusicPosition());
				mediaPlayer.start();
			}
		}
		}
		return START_STICKY;
	}

	public void onDestroy() {
		System.out.println("Distroy");
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				currentPos = mediaPlayer.getCurrentPosition();
				sessionManager.setMusicPosition(currentPos);
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
	}

	/*
	 * public void onPause() { if (mediaPlayer.isPlaying()) {
	 * mediaPlayer.pause(); currentPos = mediaPlayer.getCurrentPosition();
	 * sessionManager.setMusicPosition(currentPos); mediaPlayer.stop(); }
	 * mediaPlayer.release(); }
	 */
	public void onLowMemory() {
		mediaPlayer.release();
	}

}
