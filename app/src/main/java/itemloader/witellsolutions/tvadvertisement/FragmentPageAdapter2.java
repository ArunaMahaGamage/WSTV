package itemloader.witellsolutions.tvadvertisement;

/**
 * Created by Sumudu on 3/31/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Sumudu on 3/31/2016.
 */
import java.util.ArrayList;
public class FragmentPageAdapter2 extends FragmentStatePagerAdapter {
    ViewPager viewPager;
    static String[] contentName = null, contentType = null, contentTime = null;
    Context c;
    Data d = new Data();

    public FragmentPageAdapter2(FragmentManager fm, String[] contentName, String[] contentType, String[] contentTime, ViewPager viewPager, Context c) {
        super(fm);
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentTime = contentTime;
        this.viewPager = viewPager;
        this.c = c;
    }

    @Override
    public Fragment getItem(int pos) {
        for(int i = 0; i<contentName.length; i++){
            if(i == pos){return new ImageFragment(contentName[pos], pos, contentType[pos], c);}
        }
        return null;
    }

    @Override
    public int getCount() {
        return contentName.length;
    }
}
