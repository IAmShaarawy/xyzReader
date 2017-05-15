package com.example.xyzreader.ui;

import android.animation.Animator;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.Utils.FontUtil;
import com.example.xyzreader.Utils.PreferenceUtil;
import com.example.xyzreader.data.ArticleEntity;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ArticleListActivity.class.toString();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    private PreferenceUtil mPreferenceUtil;

    private LocalBroadcastManager mLocalBroadcastManager;
    private List<ArticleEntity> mArticleEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mPreferenceUtil = new PreferenceUtil(this,PreferenceUtil.DefaultKeys.DEFAULT_SHARED_PREFERENCE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

        mArticleEntities = new ArrayList<>();

        mAdapter = new Adapter(mArticleEntities);

        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(0, null, this);

        if (!mPreferenceUtil.getBoolean(PreferenceUtil.DefaultKeys.PREF_IS_FIRST_TIME)) {
            refresh();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    private boolean mIsRefreshing = false;
    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocalBroadcastManager.registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocalBroadcastManager.unregisterReceiver(mRefreshingReceiver);
    }



    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                boolean isSuccess = intent.getBooleanExtra(Intent.EXTRA_RESULT_RECEIVER, false);
                if (isSuccess){
                    mIsRefreshing = false;
                    updateRefreshingUI();
                    mPreferenceUtil.editValue(PreferenceUtil.DefaultKeys.PREF_IS_FIRST_TIME,true);
                }

            }
        }
    };

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
        getSupportLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }



    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {


        int cursorRowCount = cursor.getCount();

        ArticleEntity articleEntity;
        mArticleEntities.clear();
        cursor.moveToFirst();
        for (int i = 0; i < cursorRowCount; i++) {
            articleEntity = new ArticleEntity(
                    cursor.getString(ArticleLoader.Query._ID),
                    cursor.getString(ArticleLoader.Query.TITLE),
                    cursor.getString(ArticleLoader.Query.PUBLISHED_DATE),
                    cursor.getString(ArticleLoader.Query.AUTHOR),
                    cursor.getString(ArticleLoader.Query.THUMB_URL),
                    cursor.getString(ArticleLoader.Query.PHOTO_URL),
                    cursor.getString(ArticleLoader.Query.ASPECT_RATIO),
                    cursor.getString(ArticleLoader.Query.BODY));
            mArticleEntities.add(articleEntity);
            cursor.moveToNext();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    public static void sendMeBroadCast(Context context, boolean isSuccess){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(UpdaterService.BROADCAST_ACTION_STATE_CHANGE);
        intent.putExtra(Intent.EXTRA_RESULT_RECEIVER,isSuccess);
        broadcastManager.sendBroadcast(intent);
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<ArticleEntity> mArticleEntities;

        public Adapter(List<ArticleEntity> articleEntities) {
            mArticleEntities = articleEntities;
        }

        @Override
        public long getItemId(int position) {
           return Long.getLong(mArticleEntities.get(position).get_id());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
            final ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticleListActivity.this,ArticleDetailActivity.class);
                    intent.putExtra("_ID",vh.getAdapterPosition());
                    intent.putExtra("DATA",(ArrayList)mArticleEntities);
                    startActivity(intent);
                }
            });
            return vh;
        }

        private Date parsePublishedDate(String date) {
            try {
                return dateFormat.parse(date);
            } catch (ParseException ex) {
                Log.e(TAG, ex.getMessage());
                Log.i(TAG, "passing today's date");
                return new Date();
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ArticleEntity entity = mArticleEntities.get(position);
            holder.titleView.setText(entity.getTitle());
            Date publishedDate = parsePublishedDate(entity.getPublished_date());
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {

                holder.subtitleView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + "<br/>" + " by "
                                + entity.getAuthor()));
            } else {
                holder.subtitleView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate)
                        + "<br/>" + " by "
                        + entity.getAuthor()));
            }
            Picasso.with(ArticleListActivity.this)
                    .load(entity.getPhoto_url())
                    .into(holder.thumbnailView);
        }

        @Override
        public int getItemCount() {
            return mArticleEntities.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            FontUtil.applyFonts(view);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}
