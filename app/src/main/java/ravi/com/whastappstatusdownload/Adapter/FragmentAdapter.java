package ravi.com.whastappstatusdownload.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ravi.com.whastappstatusdownload.Fragment.ImageFragment;
import ravi.com.whastappstatusdownload.Fragment.VideoFragment;

/**
 * Created by nikpatel on 08/09/17.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {

    int tabcount;

    public FragmentAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment fragment = new ImageFragment();
                return fragment;
            case 1:
                Fragment fragment1 = new VideoFragment();
                return fragment1;
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return tabcount;
    }
}
