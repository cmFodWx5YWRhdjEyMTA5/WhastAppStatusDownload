package ravi.com.whastappstatusdownload;

import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import ravi.com.whastappstatusdownload.Adapter.ImageAdapter;
import ravi.com.whastappstatusdownload.Common.Common;
import ravi.com.whastappstatusdownload.Model.ImgModel;

public class ImageViewActivity extends AppCompatActivity  {
    private static final String TAG = "ImageViewActivity";
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    ArrayList<ImgModel> arrayList = new ArrayList<>();
    private static final String WHATSAPP_BUSINESS_STATUSES_LOCATION = "/WhatsApp Business/Media/.Statuses";
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";

    private AdView mAdView;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Log.e(TAG, "onCreate: " );

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext(), viewPager);
        viewPager.setAdapter(adapter);
        try {
            loadData();

            addsence();
        } catch (Exception e){
            Log.e(TAG, "onCreateView: "+e.getMessage() );
        }

    }

    private void loadData() {
        File f = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION);
        ImageAdapter adapter;
        if (f.isDirectory()){
            arrayList.addAll(getItem(f));
        }
        File f1 = new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_BUSINESS_STATUSES_LOCATION);
        if(f1.isDirectory()) {
            arrayList.addAll(getItem(f1));
        }
        loadImages();
    }
    private ArrayList<ImgModel> getItem(File parentDir){
        ImgModel addarray = new ImgModel();
        ArrayList<ImgModel> arrayList1 = new ArrayList<>();
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

    private void loadImages() {
        if (arrayList.size() != 0){
            for (int i = 0; i < arrayList.size() ; i++){
                Log.e(TAG, "loadImages: " +i );
                Log.e(TAG, "loadImages: "+arrayList.get(i).getPath()+"\n"+arrayList.get(i).getName() );
                addPage(arrayList.get(i).getPath(),arrayList.get(i).getName());
            }
        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPage(String img_url,String img_name) {
        Bundle bundle = new Bundle();
        bundle.putString("img_url", img_url);
        ImageFragment1 fragmentChild = new ImageFragment1();
        fragmentChild.setArguments(bundle);
        adapter.addFrag(fragmentChild, img_name);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(Common.SELECT_POSTION);
    }

    private void addsence() {
        MobileAds.initialize(this, getResources().getString(R.string.APP_ID));

        // banner
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "Banner onAdLoaded: " );
                // Code to be executed when an ad finishes loading.
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "Banner onAdFailedToLoad: " );
                // Code to be executed when an ad request fails.
            }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                Log.e(TAG, "Banner onAdOpened: " );
                // covers the screen.
            }
            @Override
            public void onAdLeftApplication() {
                Log.e(TAG, "Banner onAdLeftApplication: " );
                // Code to be executed when the user has left the app.
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                Log.e(TAG, "Banner onAdClosed: " );
                // to the app after tapping on an ad.
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(adRequest);
            }

        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "onAdLoaded: " );
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e(TAG, "onAdFailedToLoad: " );
            }

            @Override
            public void onAdOpened() {
                Log.e(TAG, "onAdOpened: " );
            }

            @Override
            public void onAdLeftApplication() {
                Log.e(TAG, "onAdLeftApplication: " );
            }

            @Override
            public void onAdClosed() {
                Log.e(TAG, "onAdClosed: " );
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
