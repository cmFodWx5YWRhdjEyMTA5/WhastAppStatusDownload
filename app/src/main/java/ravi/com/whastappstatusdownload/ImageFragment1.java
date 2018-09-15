package ravi.com.whastappstatusdownload;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import ravi.com.whastappstatusdownload.Common.BitmapHelper;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.Model.model;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment1 extends Fragment {
    private static final String TAG = "ImageFragment1";
    File yourAppDir = new File(Environment.getExternalStorageDirectory()+File.separator+"WSDownload");
    FloatingActionButton btnShare,btnDownload;

    ImageView image_view;
    String img_name,img_url;

    public ImageFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image2, container, false);
        Bundle bundle = getArguments();
//        img_name = bundle.getString("img_url");
        img_url = bundle.getString("img_url");
        Log.e(TAG, "onCreateView: " +img_url );try {
            init(view);
        } catch (Exception e){
            Log.e(TAG, "onCreateView: "+e.getMessage() );
        }

        return view;
    }

    public void download(){
        Log.e(TAG, "download: " );
        BitmapDrawable drawable = (BitmapDrawable) image_view.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        downloadwallpaper(bitmap,getActivity());
    }
    private void downloadwallpaper(Bitmap finalBitmap , Activity activity) {

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
        Calendar c = Calendar.getInstance();
        String mseconds = String.valueOf(c.get(Calendar.MILLISECOND));
        File file = new File(yourAppDir, mseconds+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

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
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle("WSDownloader")
                        .setContentText("Whatsapp Image status download complated");
        Intent notificationIntent = new Intent(Intent.ACTION_GET_CONTENT);
        notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
        notificationIntent.setDataAndType(Uri.fromFile(yourAppDir), "resource/folder");
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }



    private void init(View view) {
        image_view = view.findViewById(R.id.image_view);
        btnDownload = view.findViewById(R.id.buttonImageDownload);
        btnShare = view.findViewById(R.id.share);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
//        options.inJustDecodeBounds = false;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        options.inDither = true;

        Bitmap bm = BitmapFactory.decodeFile(img_url,options);
        image_view.setImageBitmap(bm);
//        if(bm!=null && !bm.isRecycled())
//        {
//            bm.recycle();
//            bm=null;
//        }


//        Bitmap myBitmap = BitmapFactory.decodeFile(img_url);
////        image_view.setImageBitmap(myBitmap);
//
//        image_view.setImageBitmap(BitmapHelper.decodeFile(img_url, myBitmap.getWidth(), myBitmap.getHeight(), true));

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void share() {
        Uri uri = Uri.parse(img_url);
        shareImage(uri);
    }

    public void shareImage(Uri path) {
        Uri uri = path;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("image/jpeg");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Image File"));
    }
}
