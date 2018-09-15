package ravi.com.whastappstatusdownload.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ravi.com.whastappstatusdownload.Activity.HomeActivity;
import ravi.com.whastappstatusdownload.Common.BitmapHelper;
import ravi.com.whastappstatusdownload.Common.Common;
import ravi.com.whastappstatusdownload.ImageViewActivity;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.R;
import ravi.com.whastappstatusdownload.UnifiedNativeAdViewHolder;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by nikpatel on 24/04/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Adapter";
    public Context mContext;
    private Activity activity;

    private List<Object> list = new ArrayList<>();

    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    public ImageAdapter(Context mContext, List<Object> list, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.ad_unified,
                        viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                View menuItemLayoutView1 = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.recyclerview_media_row_item, viewGroup, false);
                return new FileHolder(menuItemLayoutView1);
                // Fall through.
            default:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.recyclerview_media_row_item, viewGroup, false);
                return new FileHolder(menuItemLayoutView);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = list.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) list.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holders).getAdView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                FileHolder holder = (FileHolder) holders;
                final ImgModel currentFile = (ImgModel) list.get(position);
                Log.e(TAG, "onBindViewHolder: " + currentFile.getName());

                holder.cardViewImageMedia.setVisibility(View.VISIBLE);
                holder.cardViewVideoMedia.setVisibility(View.GONE);
                Log.e(TAG, "onBindViewHolder: jpg ");
                holder.imageViewImageMedia.setImageBitmap(BitmapHelper.decodeFile(currentFile.getPath(), 200, 200, true));


                holder.cardViewImageMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ImageViewActivity.class);
                        Common.SELECT_POSTION = position;
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
                holder.imgsize.setText(getFileSize(currentFile.getSize()));
                // fall through
            default:

                FileHolder holder1 = (FileHolder) holders;
                final ImgModel currentFile1 = (ImgModel) list.get(position);
                Log.e(TAG, "onBindViewHolder: " + currentFile1.getName());

                holder1.cardViewImageMedia.setVisibility(View.VISIBLE);
                holder1.cardViewVideoMedia.setVisibility(View.GONE);
                Log.e(TAG, "onBindViewHolder: jpg ");
                holder1.imageViewImageMedia.setImageBitmap(BitmapHelper.decodeFile(currentFile1.getPath(), 200, 200, true));


                holder1.cardViewImageMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ImageViewActivity.class);
                        Common.SELECT_POSTION = position;
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
                holder1.imgsize.setText(getFileSize(currentFile1.getSize()));
        }
    }



    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

//        if (icon == null) {
//            adView.getIconView().setVisibility(View.INVISIBLE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }

//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void upDatedata(ArrayList<Object> mRecyclerViewItems) {
        list = mRecyclerViewItems;
        notifyDataSetChanged();
    }


    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewImageMedia, play, pause;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        Button buttonImageDownload;
        TextView imgsize, videosize;

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
