package com.techzhiqi.quiz.yizhandaodi;



import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.techzhiqi.quiz.yizhandaodi.quiz.Constants;
import com.techzhiqi.quiz.yizhandaodi.quiz.GamePlay;
import com.techzhiqi.quiz.yizhandaodi.util.DbDownloader;
import com.techzhiqi.quiz.yizhandaodi.util.Flags;

public class SplashActivity extends Activity implements OnClickListener {

	private ProgressDialog progressdialog;

	Flags flags = new Flags();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		// ////////////////////////////////////////////////////////////////////
		// ////// GAME MENU /////////////////////////////////////////////////
		Button playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);
		Button diffSettingsBtn = (Button) findViewById(R.id.diffsettingBtn);
		diffSettingsBtn.setOnClickListener(this);
		Button catSettingBtn = (Button) findViewById(R.id.catsettingBtn);
		catSettingBtn.setOnClickListener(this);
		Button rulesBtn = (Button) findViewById(R.id.rulesBtn);
		rulesBtn.setOnClickListener(this);
		Button updateBtn = (Button) findViewById(R.id.updatelib);
		updateBtn.setOnClickListener(this);
		Button exitBtn = (Button) findViewById(R.id.exitBtn);
		exitBtn.setOnClickListener(this);
	}

	/**
	 * Listener for game menu
	 */
	public void onClick(View v) {
		Intent i;

		switch (v.getId()) {
		case R.id.playBtn:
			// once logged in, load the main page
			// Log.d("LOGIN", "User has started the game");

			/*
			 * show progressing dialog
			 */
			progressdialog = ProgressDialog.show(this, "", "载入题库...", true);
			Thread workt = new Thread(new Runnable() {
				public void run() {
					// Initialise Game with retrieved question set ///
					GamePlay c = new GamePlay();
					c.initGame(getApplicationContext());
					// TODO: get user options: diff, cat, tpc, isRandom,
					// startQID

					((QuizApplication) getApplication()).setCurrentGame(c);
					progressdialog.dismiss();
				}
			});
			workt.start();
			try {
				workt.join();
			} catch (InterruptedException e) {
				Log.e("yizhandaodi", "splash playbtn", e);
			}

			// Start Game Now.. //
			i = new Intent(this, QuestionActivity.class);
			startActivityForResult(i, Constants.PLAYBUTTON);
			break;

		case R.id.rulesBtn:
			i = new Intent(this, RulesActivity.class);
			startActivityForResult(i, Constants.RULESBUTTON);
			break;

		case R.id.diffsettingBtn:
			i = new Intent(this, DiffSetting.class);
			startActivityForResult(i, Constants.DIFFSETTINGBUTTON);
			break;

		case R.id.catsettingBtn:
			i = new Intent(this, CatSetting.class);
			startActivityForResult(i, Constants.CATSETTINGBUTTON);
			break;

		case R.id.updatelib:

			updateLib();
			if (!flags.network) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"请确认数据网络可用", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				if (flags.getnewdb) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"同步成功,您已更新至最新版题库", Toast.LENGTH_SHORT);
					toast.show();
					
				} else {
					if(!flags.download){
					Toast toast = Toast.makeText(getApplicationContext(),
							"您使用的已经是最新版题库", Toast.LENGTH_SHORT);
					toast.show();
					}else{
						Toast toast = Toast.makeText(getApplicationContext(),
								"您使用的已经是最新题库", Toast.LENGTH_SHORT);
						toast.show();
					}
					
				}
			}

			break;

		case R.id.exitBtn:
			finish();
			break;
		}

	}

	private void updateLib() {

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			flags.network=true;
			progressdialog = ProgressDialog.show(this, "", "同步题库...", true);
			Thread work = new Thread(new Runnable() {

				public void run() {

					try {
						DbDownloader downloader = new DbDownloader(SplashActivity.this,flags);
						if (downloader.download("http://server:port/servletpattern")) {
							Log.i("yizhandaodi", "download succeed!");
						} else {
							Log.i("yizhandaodi", "download failed!");
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						flags.download = false;
					}
					progressdialog.dismiss();
				}
			});
			work.start();
			
			try {
				work.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			flags.network = false;

		}
	}

	

}