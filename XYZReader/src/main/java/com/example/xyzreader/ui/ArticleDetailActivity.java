package com.example.xyzreader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleEntity;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import static com.example.xyzreader.data.ArticleLoader.Query;
/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    List<ArticleEntity> mArticleEntities;
    private int mStartId;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private ImageView mCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState == null) {
            if (getIntent() != null ) {
                mStartId  = getIntent().getIntExtra("_ID",0);
                mArticleEntities = getIntent().getParcelableArrayListExtra("DATA");
            }
        }else {
            mArticleEntities= savedInstanceState.getParcelableArrayList("DATA");
            mStartId = savedInstanceState.getInt("_ID");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.article_detail_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCover = (ImageView) findViewById(R.id.photo);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mArticleEntities);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mStartId,true);

        Picasso.with(this).load(mArticleEntities.get(mStartId).getPhoto_url()).into(mCover);

        findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(ArticleDetailActivity.this)
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Picasso.with(ArticleDetailActivity.this).load(mArticleEntities.get(position).getPhoto_url()).into(mCover);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("DATA",(ArrayList)mArticleEntities);
        outState.putInt("_ID",mPager.getCurrentItem());
    }

    private class MyPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        List<ArticleEntity> articleEntities;
        public MyPagerAdapter(android.support.v4.app.FragmentManager fm,List<ArticleEntity> articleEntities) {
            super(fm);
            this.articleEntities = articleEntities;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            ArticleEntity entity = articleEntities.get(position);
            return ArticleDetailFragment.newInstance(entity);
        }

        @Override
        public int getCount() {
            return articleEntities.size();
        }
    }
}
