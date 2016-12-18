package com.example.webprog26.asynctaskloader.cursor_loader;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webprog26.asynctaskloader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webprog26 on 18.12.2016.
 */

public class FullAlbumCursorAdapter extends CursorAdapter {

    Context context;
    private LayoutInflater layoutInflater;
    private LoaderManager loaderManager;
    private List<Integer> ids;
    private int count;

    public FullAlbumCursorAdapter(Context context, LoaderManager loaderManager) {
        super(context.getApplicationContext(), null, true);
        this.context = context;
        this.loaderManager = loaderManager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ids = new ArrayList<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = (View) layoutInflater.inflate(R.layout.album_item, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.album_art);
        TextView artistView = (TextView) view.findViewById(R.id.album_artist);
        TextView albumView = (TextView) view.findViewById(R.id.album_name);

        int viewId = FullAlbumCursorAdapter.class.hashCode() + count++;
        Log.i("AlbumListActivity", "NewView["+viewId+"] with loader "+count);
        view.setId(viewId);
        loaderManager.initLoader(viewId, null, new ArtworkLoaderCallbacks(context, imageView));
        ids.add(viewId);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.album_art);
        TextView artistView = (TextView) view.findViewById(R.id.album_artist);
        TextView albumView = (TextView) view.findViewById(R.id.album_name);
        imageView.setImageBitmap(null);
        Loader<?> loader = loaderManager.getLoader(view.getId());
        AlbumArtworkLoader artworkLoader = (AlbumArtworkLoader) loader;

        int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
        Log.i("AlbumListActivity", "bindView [" + view.getId() + "]  to album [" + albumId+"]");
        artworkLoader.setAlbumId(albumId);
        artistView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
        albumView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
    }

    public void destroyLoaders(){
        for(Integer id: ids){
            loaderManager.destroyLoader(id);
        }
    }

    public static class ArtworkLoaderCallbacks implements LoaderManager.LoaderCallbacks<Bitmap>{

        private Context context;
        private ImageView imageView;

        public ArtworkLoaderCallbacks(Context context, ImageView imageView) {
            this.context = context.getApplicationContext();
            this.imageView = imageView;
        }

        @Override
        public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
            return new AlbumArtworkLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
            imageView.setImageBitmap(data);
        }

        @Override
        public void onLoaderReset(Loader<Bitmap> loader) {

        }
    }
}
