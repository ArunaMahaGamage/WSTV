package itemloader.witellsolutions.tvadvertisement;

/**
 * Created by Sumudu on 3/30/2016.
 */
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;

/**
 * Created by Sumudu on 3/10/2016.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {
    ViewPager viewPager;
    static String [] contentName = null, contentType = null;

    public FragmentPageAdapter(FragmentManager fm, String [] contentName, String[] contentType, ViewPager viewPager){
        super(fm);
        this.contentName = contentName;
        this.contentType = contentType;
        this.viewPager = viewPager;
    }
    @Override
    public Fragment getItem(int pos) {
        for(int i =0; i<contentName.length; i++){
            if(i == pos){
               // return new ImageFragment(contentName[i]);
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return contentName.length;
    }
}
