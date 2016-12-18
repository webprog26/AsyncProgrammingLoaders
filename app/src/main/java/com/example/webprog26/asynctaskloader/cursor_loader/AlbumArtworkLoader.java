package com.example.webprog26.asynctaskloader.cursor_loader;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by webprog26 on 18.12.2016.
 */

public class AlbumArtworkLoader extends android.support.v4.content.AsyncTaskLoader<Bitmap> {

    private int mAlbumId = -1;
    Bitmap mData = null;

    public AlbumArtworkLoader(Context context) {
        super(context);
    }

    public AlbumArtworkLoader(Context context, int mAlbumId) {
        super(context);
        this.mAlbumId = mAlbumId;
    }

    private boolean isDifferentMedia(int newMediaId){
        return mAlbumId !=newMediaId;
    }

    public void setAlbumId(int newAlbumId){
        if(isDifferentMedia(newAlbumId) || mData == null){
            this.mAlbumId = newAlbumId;
            onContentChanged();
        } else if(!isDifferentMedia(newAlbumId)){
            deliverResult(mData);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mData != null){
            deliverResult(mData);
        }

        if(takeContentChanged() || mData == null){
            forceLoad();
        }
    }

    @Override
    public Bitmap loadInBackground() {
        Log.i("AlbumListActivity","Loading Album Art for album "+mAlbumId);
        Bitmap bitmap = null;

        ContentResolver res = getContext().getContentResolver();

        if(mAlbumId != -1){
            try {
                Uri sArtWorkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumUri = ContentUris.withAppendedId(sArtWorkUri, mAlbumId);

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

        return bitmap;

    }

    @Override
    protected void onReset() {
        Log.i("AlbumListActivity","Resetting the loader "+getId());
        if (mData != null) {
            mData.recycle();
            mData = null;
        }
    }

    @Override
    public void onCanceled(Bitmap data) {
        Log.i("AlbumListActivity","Canceling the loader "+getId());
        if (data != null) {
            data.recycle();
            mData = null;
        }
    }

    @Override
    protected void onStopLoading() {
        Log.i("AlbumListActivity","Stopping the loader "+getId());
        cancelLoad();
    }
}
