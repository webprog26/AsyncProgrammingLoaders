package com.example.webprog26.asynctaskloader.cursor_loader;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.webprog26.asynctaskloader.R;

/**
 * Created by webprog26 on 18.12.2016.
 */

public class AlbumListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int ALBUM_LIST_LOADER = "phone_list".hashCode();
    private FullAlbumCursorAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_layout);

        GridView gridView = (GridView) findViewById(R.id.album_grid);
        mAdapter = new FullAlbumCursorAdapter(getApplicationContext(), getSupportLoaderManager());
        gridView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(ALBUM_LIST_LOADER, null, AlbumListActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART
        };
        return new CursorLoader(this, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                columns, // projection
                null, // selection
                null, // selectionArgs
                null // sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isFinishing()){
            getSupportLoaderManager().destroyLoader(ALBUM_LIST_LOADER);
            mAdapter.destroyLoaders();
        }
    }
}
