package e.mirzashafique.lib;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import e.mirzashafique.lib.fragments.CameraFragment;
import e.mirzashafique.lib.fragments.FilesFragment;
import e.mirzashafique.lib.fragments.ImagesFragment;
import e.mirzashafique.lib.fragments.MusicFragment;
import e.mirzashafique.lib.fragments.VideosFragment;
import e.mirzashafique.lib.listener.SubmitAction;
import e.mirzashafique.lib.model.Config;
import e.mirzashafique.lib.model.SingletonList;

public class AllStorageActivity extends AppCompatActivity implements SubmitAction {

    //Variables Initialization
    private SectionsPagerAdapter2 mSectionsPagerAdapter;
    private Config config;

    //Views Initialization
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_storage);

        Intent intent = getIntent();
        config = intent.getParcelableExtra("all-storage-actvity");
//
//        button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result", config.getMaxFile()+"");
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SingletonList.getmInstence().getSelectedFiles().clear();
        mSectionsPagerAdapter = new SectionsPagerAdapter2(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        submitButton = findViewById(R.id.submit_button);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(mViewPager);

        //click listners
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void actionMethod() {
        int size = SingletonList.getmInstence().getSelectedFiles().size();
        if (size <= 0) {
            submitButton.setVisibility(View.GONE);
        } else {
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setText("Submit " + size);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter2 adapter = new SectionsPagerAdapter2(getSupportFragmentManager());

        if (config.isCamera()) {
            CameraFragment cameraFragment = new CameraFragment();
            cameraFragment.setSubmitAction(this);
            Bundle bundle = new Bundle();
            bundle.putInt("selection-size", config.getMaxCamera());
            cameraFragment.setArguments(bundle);
            adapter.addFragment(cameraFragment, "Camera");
        }
        if (config.isFiles()) {
            FilesFragment filesFragment = new FilesFragment();
            filesFragment.setSubmitAction(this);
            Bundle bundle = new Bundle();
            bundle.putInt("selection-size", config.getMaxFile());
            filesFragment.setArguments(bundle);
            adapter.addFragment(filesFragment, "Files");
        }
        if (config.isImages()) {
            ImagesFragment imagesFragment = new ImagesFragment();
            imagesFragment.setSubmitAction(this);
            Bundle bundle = new Bundle();
            bundle.putInt("selection-size", config.getMaxImages());
            imagesFragment.setArguments(bundle);
            adapter.addFragment(imagesFragment, "Images");

        }
        if (config.isAudios()) {
            MusicFragment musicFragment = new MusicFragment();
            musicFragment.setSubmitAction(this);
            Bundle bundle = new Bundle();
            bundle.putInt("selection-size", config.getMaxAudios());
            musicFragment.setArguments(bundle);
            adapter.addFragment(musicFragment, "Music");
        }
        if (config.isVideos()) {
            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setSubmitAction(this);
            Bundle bundle = new Bundle();
            bundle.putInt("selection-size", config.getMaxVideos());
            videosFragment.setArguments(bundle);
            adapter.addFragment(videosFragment, "Videos");
        }

        viewPager.setAdapter(adapter);
    }

    private class SectionsPagerAdapter2 extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter2(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Fragment fragment = null;
//            if (position == 0) {
//                fragment = new FilesFragment();
//                ((FilesFragment) fragment).setSubmitAction(AllStorageActivity.this);
//            } else if (position == 1) {
//                fragment = new ImagesFragment();
//                ((ImagesFragment) fragment).setSubmitAction(AllStorageActivity.this);
//            } else if (position == 2) {
//                fragment = new MusicFragment();
//                ((MusicFragment) fragment).setSubmitAction(AllStorageActivity.this);
//            } else if (position == 3) {
//                fragment = new VideosFragment();
//                ((VideosFragment) fragment).setSubmitAction(AllStorageActivity.this);
//            }
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (position == 0) {
//                return "Files";
//            } else if (position == 1) {
//                return "Images";
//            } else if (position == 2) {
//                return "Music";
//            } else if (position == 3) {
//                return "Videos";
//            }
//            return null;
//        }
//    }
}
