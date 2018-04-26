package ravi.com.whastappstatusdownload.Fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRecyclerViewMediaList = (RecyclerView) view.findViewById(R.id.recyclerViewMedia);
        mRecyclerViewMediaList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        loadData();
    }
    private void loadData() {
        File f = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION);
        VideoAdapter adapter;
        if (f.isDirectory()){
            adapter = new VideoAdapter(getContext(),getItem(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION)),getActivity());
        } else {
            adapter = new VideoAdapter(getContext(),getItem(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_BUSINESS_STATUSES_LOCATION)),getActivity());
        }
        if (arrayList.size() > 0){
            mRecyclerViewMediaList.setAdapter(adapter);
        } else {

        }
    }


    private ArrayList getItem(File parentDir){
        VideoModel addarray = new VideoModel();
        arrayList.clear();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
//                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif") || file.getName().endsWith(".mp4")) {
//                    addarray = new model();
//                    addarray.setName(file.getName());
//                    addarray.setPath(file.getAbsolutePath());
//                    arrayList.add(addarray);
//                }
                if (file.getName().endsWith(".mp4")) {
                    addarray = new VideoModel();
                    addarray.setName(file.getName());
                    Log.e(TAG, "getItem: "+file.length());
                    addarray.setSize(file.length());
                    addarray.setPath(file.getAbsolutePath());
                    arrayList.add(addarray);
                }
            }
        }
        Collections.reverse(arrayList);
        Log.e(TAG, "## ## getItem: " +arrayList.size() );
        return arrayList;
    }

}
