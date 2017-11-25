package com.appyuken.frikiadospremium;

import com.appyuken.frikiadospremium.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppRater {
    private final static String APP_TITLE = "FrikiadosPremium";// App Name
    private final static String APP_PNAME = "com.appyuken.frikiadospremium";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }   

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
    	Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/MYRIADPROBOLD.ttf");
        final Dialog dialog = new Dialog(mContext,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_rate_frikiados);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(R.color.opacity));
		final LinearLayout dialog2 = (LinearLayout) dialog
				.findViewById(R.id.linearOpacity);
		dialog2.setVisibility(LinearLayout.VISIBLE);
		Animation animation = AnimationUtils
				.loadAnimation(mContext, R.anim.fade_in);
		animation.setDuration(500);
		dialog2.setAnimation(animation);
		animation.start();
		dialog.setCancelable(false);

		final Button btnDontLike = (Button) dialog.findViewById(R.id.btnDontLike);
		final Button btnRate = (Button) dialog.findViewById(R.id.btnRate);
		final Button btnLater = (Button) dialog
				.findViewById(R.id.btnLater);
		btnRate.setTypeface(font);
		btnLater.setTypeface(font);
		btnLater.setTypeface(font);
		btnRate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
		});  
		btnLater.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
		
		btnDontLike.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                enviarEmail(mContext);
                dialog.dismiss();
            }
        });
		
    /*    LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("Si te divierte " + APP_TITLE + ", por favor valorala. Gracias por su ayuda!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);*/

       /* Button b1 = new Button(mContext);
        b1.setText("Valora " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Recordar más tarde");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No me gusta");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                enviarEmail(mContext);
                dialog.dismiss();
            }
        });
        ll.addView(b3);*/

       // dialog.setContentView(ll);        
        dialog.show();        
    }

	protected static void enviarEmail(Context mContext) {
		// TODO Auto-generated method stub
		String[] to = { "frikiadosgame@gmail.com"};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		// String[] to = direccionesEmail;
		// String[] cc = copias;
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mejoras Frikiados");
		emailIntent.putExtra(Intent.EXTRA_TEXT,
				"Escribe aquí tu sugerencia para pdoer mejorar Frikiados");
		emailIntent.setType("message/rfc822");
		mContext.startActivity(Intent.createChooser(emailIntent, "Email "));
	}
}