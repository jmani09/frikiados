package com.appyuken.frikiadospremium.gameModes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.appyuken.frikiadospremium.R;

public class BossAnimationActivity extends Activity {
	
	private Animation animPlayer;
	private Animation animBoss;
	private Animation animVs;
	private ImageView imagePlayer;
	private ImageView imageBoss;
	private ImageView imageVs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_animation);
		
		imagePlayer = (ImageView) findViewById(R.id.imagePlayer);
		imageBoss = (ImageView) findViewById(R.id.imageBoss);
		imageVs = (ImageView) findViewById(R.id.imageVs);
		
		animPlayer = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move_right_to_left);
		animBoss = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move_left_to_right);
		animVs = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.blink);
		
		startAnimations();
		
	}

	private void startAnimations() {
		// TODO Auto-generated method stub
		imageVs.startAnimation(animVs);
		imageVs.setVisibility(View.VISIBLE);
		imagePlayer.startAnimation(animPlayer);
		imagePlayer.setVisibility(View.VISIBLE);	
		imageBoss.startAnimation(animBoss);
		imageBoss.setVisibility(View.VISIBLE);
		
		
	
		
		
	}
	
	
}
