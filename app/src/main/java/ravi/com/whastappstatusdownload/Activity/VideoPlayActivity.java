package ravi.com.whastappstatusdownload.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ravi.com.whastappstatusdownload.Common;
import ravi.com.whastappstatusdownload.HomeActivity;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.Model.VideoModel;
import ravi.com.whastappstatusdownload.R;

public class VideoPlayActivity extends AppCompatActivity {

    FloatingActionButton download,share;
    VideoView videoViewVideoMedia;
    File yourAppDir = new File(Environment.getExternalStorageDirectory()+File.separator+"WSDownload");

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
                Uri video = Uri.parse((Common.selectedVideo.getPath()));
                savefile(video,Common.selectedVideo);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri video = Uri.parse((Common.selectedVideo.getPath()));
                shareVideo(video,"");
            }
        });
    }


    public void shareVideo(Uri path, String appname) {
        Uri uri = path;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("Video/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Video File"));
    }


    public void savefile(Uri sourceuri,VideoModel model) {
        String sourceFilename= sourceuri.getPath();
        if(!yourAppDir.exists() && !yourAppDir.isDirectory())
        {
            // create empty directory
            if (yourAppDir.mkdirs())
            {
                Log.i("CreateDir","App dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
        }
        String destinationFilename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WSDownload/"+model.getName();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
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
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("WSDownloader")
                        .setContentText("Whatsapp video status download complated");
        Intent notificationIntent = new Intent(Intent.ACTION_GET_CONTENT);
        notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
        notificationIntent.setDataAndType(Uri.fromFile(yourAppDir), "resource/folder");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
}
