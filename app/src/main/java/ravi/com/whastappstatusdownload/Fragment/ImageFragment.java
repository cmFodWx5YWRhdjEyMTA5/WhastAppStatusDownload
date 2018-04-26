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
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String TAG = "ImageFragment";
    ArrayList<ImgModel> arrayList = new ArrayList<>();
    private static final String WHATSAPP_BUSINESS_STATUSES_LOCATION = "/WhatsApp Business/Media/.Statuses";
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    RecyclerView mRecyclerViewMediaList;
    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRecyclerViewMediaList = (RecyclerView) view.findViewById(R.id.recyclerViewMedia);
        mRecyclerViewMediaList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        loadData();
    }

    private void loadData() {
        File f = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION);
        ImageAdapter adapter;
        if (f.isDirectory()){
            adapter = new ImageAdapter(getContext(),getItem(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION)),getActivity());
        } else {
            adapter = new ImageAdapter(getContext(),getItem(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_BUSINESS_STATUSES_LOCATION)),getActivity());
        }
        if (arrayList.size() > 0){
            mRecyclerViewMediaList.setAdapter(adapter);
        } else {

        }
    }
    private ArrayList getItem(File parentDir){
        ImgModel addarray = new ImgModel();
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
                if (file.getName().endsWith(".jpg")) {
                    addarray = new ImgModel();
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
