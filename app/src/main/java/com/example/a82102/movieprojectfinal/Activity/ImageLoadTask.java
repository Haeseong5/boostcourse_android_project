package com.example.a82102.movieprojectfinal.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

//url과 이미지 뷰를 전달하면 이미지를 가져와서 비트맵으로 만들고
//화면에 표지해줄 수 있는역할을 하는 클래스
public class ImageLoadTask extends AsyncTask<Void,Void,Bitmap> {
    private String urlStr;
    private ImageView imageView;

    private static HashMap<String, Bitmap> bitmapHashMap = new HashMap<String, Bitmap>();

    public ImageLoadTask(String url, ImageView imageView)
    {
        this.urlStr = url;
        this.imageView = imageView;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Bitmap doInBackground(Void... voids) {


        Bitmap bitmap=null;
        URL url = null;
        try {
//            if(bitmapHashMap.containsKey(urlStr))
//            {
//                Bitmap oldBitmap = bitmapHashMap.remove(urlStr);
//                if (oldBitmap!=null)
//                {
//                    oldBitmap.recycle();
//                    oldBitmap = null;
//                }
//            }
            url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            bitmapHashMap.put(urlStr,bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // doInBackground 에서 받아온 total 값 사용 장소
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.invalidate();
    }

}
