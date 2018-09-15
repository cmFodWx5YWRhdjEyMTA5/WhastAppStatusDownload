package ravi.com.whastappstatusdownload.Fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import ravi.com.whastappstatusdownload.Adapter.ImageAdapter;
import ravi.com.whastappstatusdownload.Adapter.VideoAdapter;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.Model.VideoModel;
import ravi.com.whastappstatusdownload.Model.model;
import ravi.com.whastappstatusdownload.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";
    private static final String WHATSAPP_BUSINESS_STATUSES_LOCATION = "/WhatsApp Business/Media/.Statuses";
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    RecyclerView mRecyclerViewMediaList;

    ArrayList<VideoModel> arrayList = new ArrayList<>();
    TextView no_text;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        try {
            init(view);
        } catch (Exception e){
            Log.e(TAG, "onCreateView: "+e.getMessage() );
        }
        return view;
    }

    private void init(View view) {
        no_text = view.findViewById(R.id.no_text);
        mRecyclerViewMediaList = (RecyclerView) view.findViewById(R.id.recyclerViewMedia);
        mRecyclerViewMediaList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        loadData();
    }
    private void loadData() {
        File f = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION);
        VideoAdapter adapter;
        if (f.isDirectory()){
            arrayList.addAll(getItem(f));
        }
        File f1 = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_BUSINESS_STATUSES_LOCATION);
        if (f1.isDirectory()){
            arrayList.addAll(getItem(f1));
        }
        adapter = new VideoAdapter(getContext(),arrayList,getActivity());

        if (arrayList.size() > 0){
            no_text.setVisibility(View.GONE);
            mRecyclerViewMediaList.setVisibility(View.VISIBLE);
            mRecyclerViewMediaList.setAdapter(adapter);
        } else {
            no_text.setVisibility(View.VISIBLE);
            mRecyclerViewMediaList.setVisibility(View.GONE);
        }
    }
    private ArrayList<VideoModel> getItem(File parentDir){
        VideoModel addarray = new VideoModel();
        ArrayList<VideoModel> arrayList1 = new ArrayList<>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    addarray = new VideoModel();
                    addarray.setName(file.getName());
                    Log.e(TAG, "getItem: "+file.length());
                    addarray.setSize(file.length());
                    addarray.setPath(file.getAbsolutePath());
                    arrayList1.add(addarray);
                }
            }
        }
        Collections.reverse(arrayList1);
        Log.e(TAG, "## ## getItem: " +arrayList1.size() );
        return arrayList1;
    }
}
