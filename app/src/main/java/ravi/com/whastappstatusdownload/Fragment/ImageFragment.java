package ravi.com.whastappstatusdownload.Fragment;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ravi.com.whastappstatusdownload.Adapter.ImageAdapter;
import ravi.com.whastappstatusdownload.Common.Common;
import ravi.com.whastappstatusdownload.Model.ImgModel;
import ravi.com.whastappstatusdownload.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static final String TAG = "ImageFragment";
    private static final String WHATSAPP_BUSINESS_STATUSES_LOCATION = "/WhatsApp Business/Media/.Statuses";
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    RecyclerView mRecyclerViewMediaList;
    LinearLayout no_text;
    ImageAdapter adapter;


    // The number of native ads to load.
    public static final int NUMBER_OF_ADS = 5;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of MenuItems and native ads that populate the RecyclerView.
    private ArrayList<Object> mRecyclerViewItems = new ArrayList<>();

    // List of native ads that have been successfully loaded.
    private ArrayList<UnifiedNativeAd> mNativeAds = new ArrayList<>();



    public ImageFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        try {
            init(view);
        } catch (Exception e){
            Log.e(TAG, "onCreateView: "+e.getMessage() );
        }
        return view;
    }
    private void init(View view) {
        no_text = view.findViewById(R.id.no_text);
        MobileAds.initialize(getContext(), getResources().getString(R.string.APP_ID));
        mRecyclerViewMediaList = (RecyclerView) view.findViewById(R.id.recyclerViewMedia);
        mRecyclerViewMediaList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        adapter = new ImageAdapter(getContext(),mRecyclerViewItems,getActivity());
        mRecyclerViewMediaList.setAdapter(adapter);
        loadData();
        loadNativeAds();
    }

    private void insertAdsInMenuItems() {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (mRecyclerViewItems.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index = index + offset;
        }
        adapter.upDatedata(mRecyclerViewItems);
    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(Objects.requireNonNull(getContext()), getResources().getString(R.string.NativeAds));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems();
                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }


    private void loadData() {
        mRecyclerViewItems.clear();
        File f = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION);
        if (f.isDirectory()){
            mRecyclerViewItems.addAll(getItem(f));
        }
        File f1 = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_BUSINESS_STATUSES_LOCATION);

        if (f1.isDirectory()){
            mRecyclerViewItems.addAll(getItem(f1));
        }

        if (mRecyclerViewItems.size() > 0){
            no_text.setVisibility(View.GONE);
            mRecyclerViewMediaList.setVisibility(View.VISIBLE);
            mRecyclerViewMediaList.setAdapter(adapter);
        } else {
            no_text.setVisibility(View.VISIBLE);
            mRecyclerViewMediaList.setVisibility(View.GONE);
        }
    }

    private ArrayList<ImgModel> getItem(File parentDir){
        ImgModel addarray = new ImgModel();
//        arrayList.clear();
        ArrayList<ImgModel> arrayList1 = new ArrayList<ImgModel>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg")) {
                    addarray = new ImgModel();
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
