package androidapp.com.stalwartsecurity.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by mobileapplication on 8/22/17.
 */

public class TicketFragment extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TicketFragment(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LodgeTicketFragment tab1 = new LodgeTicketFragment();
                return tab1;
            case 1:
                TicketListFragment tab2 = new TicketListFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}