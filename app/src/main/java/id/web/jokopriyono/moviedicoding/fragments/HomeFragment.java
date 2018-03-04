package id.web.jokopriyono.moviedicoding.fragments;

/*
 * Created by Joko Priyono on 21/02/18.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.activities.SplashActivity;
import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;

public class HomeFragment extends Fragment {

    private CariResponse response;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            response = (CariResponse) bundle.getSerializable(SplashActivity.NOW_PLAYING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());

        UpComingFragment fragUpComing = new UpComingFragment();
        NowPlayingFragment fragNowPlaying = new NowPlayingFragment();

        viewPagerAdapter.addFrag(fragNowPlaying, SplashActivity.NOW_PLAYING);
        viewPagerAdapter.addFrag(fragUpComing, SplashActivity.UP_COMING);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
