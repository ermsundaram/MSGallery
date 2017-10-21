package com.androidprofessional.gallery.msgallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by androidprofessional on 21/10/17.
 */

public class GalleryUtils {
    static ArrayList<HashMap<String, Object>> allImagesList= new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String,Object>> loadImages(Activity context){
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        final Cursor imagecursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String picturePath=imagecursor.getString(dataColumnIndex);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("type", "path");
            map.put("path", picturePath);
            allImagesList.add(map);
        }

        return allImagesList;
    }
}
