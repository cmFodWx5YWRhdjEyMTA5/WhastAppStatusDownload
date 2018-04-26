package ravi.com.whastappstatusdownload.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ravi.com.whastappstatusdownload.BitmapHelper;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.Model.model;
import ravi.com.whastappstatusdownload.R;

/**
 * Created by nikpatel on 24/04/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FileHolder> {
    private static final String TAG = "Adapter";
    public Context mContext;
//    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/WSDownloader/";
    private Activity activity;
    File yourAppDir = new File(Environment.getExternalStorageDirectory()+File.separator+"WSDownload");

    private List<ImgModel> list = new ArrayList<>();

    public ImageAdapter(Context mContext, List<ImgModel> list, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ImageAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_media_row_item, parent, false);
        return new ImageAdapter.FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final FileHolder holder, int position) {
        final ImgModel currentFile =(ImgModel) list.get(position);
//        holder.buttonVideoDownload.setOnClickListener(this.downloadMediaItem(currentFile));
        Log.e(TAG, "onBindViewHolder: " +currentFile.getName());


            holder.cardViewImageMedia.setVisibility(View.VISIBLE);
            holder.cardViewVideoMedia.setVisibility(View.GONE);
//
            Log.e(TAG, "onBindViewHolder: jpg " );
//            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getPath(),200,200,true);
//            holder.imageViewImageMedia.setImageBitmap(myBitmap);
        holder.imageViewImageMedia.setImageBitmap(BitmapHelper.decodeFile(currentFile.getPath(), 200, 200, true));


        holder.cardViewImageMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image_row_item);
                dialog.setCancelable(true);
                final ImageView imageViewImageMedia = (ImageView) dialog.findViewById(R.id.imageViewImageMedia);
                FloatingActionButton buttonImageDownload = (FloatingActionButton) dialog.findViewById(R.id.buttonImageDownload);
                FloatingActionButton buttonImageShare = (FloatingActionButton) dialog.findViewById(R.id.share);
                Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getPath());
                imageViewImageMedia.setImageBitmap(myBitmap);
                buttonImageDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BitmapDrawable drawable = (BitmapDrawable) imageViewImageMedia.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        downloadwallpaper(bitmap,currentFile,activity);
                    }
                });
                buttonImageShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(currentFile.getPath());
                        shareVideo(uri);
                    }
                });
                dialog.show();
            }
        });

        holder.imgsize.setText(getFileSize(currentFile.getSize()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private static void SaveImage(Bitmap finalBitmap,ImgModel model,Activity activity) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/WS");
        myDir.mkdirs();
        String fname = "WS-"+ model.getName();
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
//            MediaStore.Images.Media.insertImage(getContentResolver(),model.getPath(),model.getName(),model.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

    }
    private static void DOWNLOAD(Bitmap finalBitmap,ImgModel model,Activity activity) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/WSDownload");
        myDir.mkdirs();
        String fname = "WS-"+ model.getName();
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
//            MediaStore.Images.Media.insertImage(getContentResolver(),model.getPath(),model.getName(),model.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void downloadwallpaper(Bitmap finalBitmap,ImgModel model,Activity activity) {

        OutputStream fOut = null;
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

        File file = new File(yourAppDir, model.getName()); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

        Log.e(TAG, "downloadwallpaper: " +file.toString() );

        try {
            fOut = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(activity.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
            addNotification();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void shareVideo(Uri path) {
        Uri uri = path;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/jpeg");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(share, "Share Image File"));
    }
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("WSDownloader")
                        .setContentText("Whatsapp Image status download complated");
        Intent notificationIntent = new Intent(Intent.ACTION_GET_CONTENT);
        notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
        notificationIntent.setDataAndType(Uri.fromFile(yourAppDir), "resource/folder");
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewImageMedia,play,pause;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        Button buttonImageDownload;
        TextView imgsize,videosize;

        public FileHolder(View itemView) {
            super(itemView);
            play = (ImageView) itemView.findViewById(R.id.play);
            pause = (ImageView) itemView.findViewById(R.id.pause);
            imgsize = (TextView) itemView.findViewById(R.id.imgsize);
            videosize = (TextView) itemView.findViewById(R.id.videosize);
            imageViewImageMedia = (ImageView) itemView.findViewById(R.id.imageViewImageMedia);
            cardViewImageMedia = (CardView) itemView.findViewById(R.id.cardViewImageMedia);
            cardViewVideoMedia = (CardView) itemView.findViewById(R.id.cardViewVideoMedia);
            buttonImageDownload = (Button) itemView.findViewById(R.id.buttonImageDownload);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
