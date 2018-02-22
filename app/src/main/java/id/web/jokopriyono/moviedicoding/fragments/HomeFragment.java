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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.web.jokopriyono.moviedicoding.R;
import id.web.jokopriyono.moviedicoding.activities.SplashActivity;
import id.web.jokopriyono.moviedicoding.response.nowplaying.NowPlayingResponse;
import id.web.jokopriyono.moviedicoding.response.upcoming.UpComingResponse;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        Bundle bundle = this.getArguments();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        NowPlayingFragment fragNowPlaying = new NowPlayingFragment();
        UpComingFragment fragUpComing = new UpComingFragment();

        if (bundle!=null) {
            NowPlayingResponse nowPlayingResponse = (NowPlayingResponse) bundle.getSerializable(SplashActivity.NOW_PLAYING);
            UpComingResponse upComingResponse = (UpComingResponse) bundle.getSerializable(SplashActivity.UP_COMING);
            Bundle bundle2;
            if (nowPlayingResponse!=null) {
                bundle2 = new Bundle();
                bundle2.putSerializable(SplashActivity.NOW_PLAYING, nowPlayingResponse);
                fragNowPlaying.setArguments(bundle2);
            }
            if (upComingResponse!=null) {
                bundle2 = new Bundle();
                bundle2.putSerializable(SplashActivity.UP_COMING, upComingResponse);
                fragUpComing.setArguments(bundle2);
            }

        }
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
