package com.evanv.debateduel.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Context context;
    public DownloadImageFromInternet(ImageView imageView, Context context) {
        this.imageView=imageView;
        this.context=context;
    }
    protected Bitmap doInBackground(String... urls) {
        String imageURL=urls[0];
        Bitmap bimage=null;
        File cachedFile = new File(context.getCacheDir(), imageURL.hashCode() + ".png");
        if (cachedFile.exists()) {
            try {
                bimage = BitmapFactory.decodeStream(new FileInputStream(cachedFile));
                return bimage;
            } catch (FileNotFoundException e) {
                Log.e("DebateDuel", e.getMessage());
            }
        }
        try {
            InputStream in=new java.net.URL(imageURL).openStream();
            bimage= BitmapFactory.decodeStream(in);

            if (bimage.getWidth() > bimage.getHeight()) {
                int cropAmount = bimage.getWidth() - bimage.getHeight();
                bimage = Bitmap.createBitmap(bimage, cropAmount/2, 0, bimage.getHeight(), bimage.getHeight());
            }
            else if (bimage.getHeight() > bimage.getWidth()) {
                int cropAmount = bimage.getHeight() - bimage.getWidth();
                bimage = Bitmap.createBitmap(bimage, 0, cropAmount/2, bimage.getWidth(), bimage.getWidth());
            }
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(cachedFile);
            bimage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bimage;
    }
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
