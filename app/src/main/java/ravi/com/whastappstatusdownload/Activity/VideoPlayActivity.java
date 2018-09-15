package ravi.com.whastappstatusdownload.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ravi.com.whastappstatusdownload.Common.Common;
import ravi.com.whastappstatusdownload.Model.VideoModel;
import ravi.com.whastappstatusdownload.R;

public class VideoPlayActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private static final String TAG = "VideoPlayActivity";
    FloatingActionButton download, share;
    VideoView videoViewVideoMedia;
    File yourAppDir = new File(Environment.getExternalStorageDirectory() + File.separator + "WSDownload");
    private AdView mAdView;
    AdRequest adRequest;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);

        download = findViewById(R.id.download);
        share = findViewById(R.id.share);
        videoViewVideoMedia = findViewById(R.id.videoViewVideoMedia);
        Uri video = Uri.parse(Common.selectedVideo.getPath());
        videoViewVideoMedia.setVideoURI(video);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewVideoMedia);
        mediaController.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        videoViewVideoMedia.setMediaController(mediaController);

        videoViewVideoMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoViewVideoMedia.start();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                try {
                    Uri video = Uri.parse((Common.selectedVideo.getPath()));
                    savefile(video, Common.selectedVideo);
                } catch (Exception e){
                    Log.e(TAG, "onCreateView: "+e.getMessage() );
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                try {
                    Uri video = Uri.parse((Common.selectedVideo.getPath()));
                    shareVideo(video, "");
                } catch (Exception e){
                    Log.e(TAG, "onCreateView: "+e.getMessage() );
                }
            }
        });
        addsence();
    }


    public void shareVideo(Uri path, String appname) {
        Uri uri = path;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("Video/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Video File"));
    }


    public void savefile(Uri sourceuri, VideoModel model) {
        String sourceFilename = sourceuri.getPath();
        if (!yourAppDir.exists() && !yourAppDir.isDirectory()) {
            // create empty directory
            if (yourAppDir.mkdirs()) {
                Log.i("CreateDir", "App dir created");
            } else {
                Log.w("CreateDir", "Unable to create app dir!");
            }
        } else {
            Log.i("CreateDir", "App dir already exists");
        }
        String destinationFilename = yourAppDir + model.getName();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
            Toast.makeText(getApplication(), "Successful Download", Toast.LENGTH_LONG).show();
            addNotification();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addNotification() {
        try {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setContentTitle("WSDownloader")
                            .setContentText("Whatsapp video status download complated");
            Intent notificationIntent = new Intent(Intent.ACTION_GET_CONTENT);
            notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
//        Uri uri = FileProvider.getUriForFile(VideoPlayActivity.this, BuildConfig.APPLICATION_ID + ".provider",yourAppDir);
            notificationIntent.setDataAndType(Uri.fromFile(yourAppDir), "resource/folder");
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        } catch (Exception e){

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private InterstitialAd mInterstitialAd;

    private void addsence() {
        MobileAds.initialize(this, getResources().getString(R.string.APP_ID));
        // banner
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "Banner onAdLoaded: ");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "Banner onAdFailedToLoad: ");
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                Log.e(TAG, "Banner onAdOpened: ");
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                Log.e(TAG, "Banner onAdLeftApplication: ");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                Log.e(TAG, "Banner onAdClosed: ");
                // to the app after tapping on an ad.
            }
        });
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.RewardedVideo),
                adRequest);
    }
    @Override
    public void onRewarded(RewardItem reward) {
        Log.e(TAG, "onRewarded: " );
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.e(TAG, "onRewardedVideoAdLeftApplication: " );
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.e(TAG, "onRewardedVideoAdFailedToLoad: " );
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.e(TAG, "onRewardedVideoAdLoaded: " );
//        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.e(TAG, "onRewardedVideoAdOpened: " );
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.e(TAG, "onRewardedVideoStarted: " );
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.e(TAG, "onRewardedVideoCompleted: ");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.RewardedVideo), adRequest);
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
