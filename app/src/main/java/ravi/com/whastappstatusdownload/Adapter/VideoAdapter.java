package ravi.com.whastappstatusdownload.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ravi.com.whastappstatusdownload.Common;
import ravi.com.whastappstatusdownload.Model.VideoModel;
import ravi.com.whastappstatusdownload.R;
import ravi.com.whastappstatusdownload.Activity.VideoPlayActivity;

/**
 * Created by nikpatel on 24/04/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.FileHolder> {
    private static final String TAG = "Adapter";
    public Context mContext;
    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/WSDownloader/";
    private Activity activity;

    private List<VideoModel> list = new ArrayList<>();

    public VideoAdapter(Context mContext, List<VideoModel> list, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
        this.list = list;
    }

    @Override
    public VideoAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_media_row_item, parent, false);
        return new VideoAdapter.FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final FileHolder holder, int position) {
        final VideoModel currentFile =(VideoModel) list.get(position);
//        holder.buttonVideoDownload.setOnClickListener(this.downloadMediaItem(currentFile));
        Log.e(TAG, "onBindViewHolder: " +currentFile.getName());

        Log.e(TAG, "onBindViewHolder: mp4 ");
        holder.cardViewImageMedia.setVisibility(View.GONE);
        holder.cardViewVideoMedia.setVisibility(View.VISIBLE);

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(currentFile.getPath(),MediaStore.Images.Thumbnails.MINI_KIND);

        holder.videoViewVideoMedia.setImageBitmap(thumb);
//        holder.play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri video = Uri.parse(currentFile.getPath());
//                holder.videoViewVideoMedia.setVideoURI(video);
//                holder.videoViewVideoMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        mp.setLooping(false);
//                        holder.play.setVisibility(View.GONE);
//                        holder.pause.setVisibility(View.VISIBLE);
//                        holder.videoViewVideoMedia.start();
//                    }
//                });
//            }
//        });
//        holder.pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.play.setVisibility(View.VISIBLE);
//                holder.pause.setVisibility(View.GONE);
//                holder.videoViewVideoMedia.stopPlayback();
//            }
//        });


        holder.videosize.setText(getFileSize(currentFile.getSize()));

        holder.cardViewVideoMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, VideoPlayActivity.class);
                Common.selectedVideo = currentFile;
                mContext.startActivity(intent);

            }
        });

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

    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView play,pause;
        ImageView videoViewVideoMedia;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        Button buttonVideoDownload;
        TextView imgsize,videosize;

        public FileHolder(View itemView) {
            super(itemView);
            play = (ImageView) itemView.findViewById(R.id.play);
            pause = (ImageView) itemView.findViewById(R.id.pause);
            imgsize = (TextView) itemView.findViewById(R.id.imgsize);
            videosize = (TextView) itemView.findViewById(R.id.videosize);
            videoViewVideoMedia = (ImageView) itemView.findViewById(R.id.videoViewVideoMedia);
            cardViewVideoMedia = (CardView) itemView.findViewById(R.id.cardViewVideoMedia);
            cardViewImageMedia = (CardView) itemView.findViewById(R.id.cardViewImageMedia);
            buttonVideoDownload = (Button) itemView.findViewById(R.id.buttonVideoDownload);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
