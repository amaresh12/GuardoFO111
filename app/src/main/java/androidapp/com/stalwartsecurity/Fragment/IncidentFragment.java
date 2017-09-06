package androidapp.com.stalwartsecurity.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class IncidentFragment extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public IncidentFragment(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LodgeIncidentFragment tab1 = new LodgeIncidentFragment();
                return tab1;
            case 1:
                IncidentReportFragment tab2 = new IncidentReportFragment();
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