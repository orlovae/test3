package ru.aleksandrorlov.test3.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import ru.aleksandrorlov.test3.data.Contract;

/**
 * Created by alex on 05.09.17.
 */

public class DownloadAvatar extends AsyncTask<Void, Void, Void> {
    private final String NAME_DIR = "users";
    private final String SCHEME = "file://";
    private final String ERROR_DOWNLOAD = "error_download_image.png";
    private final String FILE_EXTENSION_JPG = "image/jpeg";

    private Context context;
    private TreeMap<Integer, String> mapForDownloadAvatar;



    public DownloadAvatar(Context context, TreeMap<Integer, String> mapForDownloadAvatar){
        this.context = context;
        this.mapForDownloadAvatar = mapForDownloadAvatar;
    }

    @Override
    protected Void doInBackground(Void... params) {
        File directory;
        ContextWrapper cw = new ContextWrapper(context);
        boolean SDWrite = isExternalStorageWritable();


        if (SDWrite) {
            directory = getAlbumStorageDir(context);
        } else {
            directory = cw.getDir(NAME_DIR, Context.MODE_PRIVATE);
        }

        String pathError = getFileErrorDownloadAvatar(directory).getAbsolutePath();

        for (Map.Entry<Integer, String> entry : mapForDownloadAvatar.entrySet()
                ) {
            String url = entry.getValue();
            Bitmap bitmap = null;
            String fileName = "";
            String typeImage = "";
            File path = null;

            if (url != null && !url.equals("")) {
                try {
                    InputStream inputStream = new URL(url).openStream();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();

                    typeImage = options.outMimeType;

                    options.inSampleSize = calculateInSampleSize(options, 400, 800);
                    options.inJustDecodeBounds = false;
//                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    InputStream iS = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(iS, null, options);
                    iS.close();

                    fileName = createNameFile(url);
                } catch (Exception e) {
                    entry.setValue(pathError);
                    e.printStackTrace();
                }
            } else {
                bitmap = getBitmapErrorDownloadAvatar();
                fileName = ERROR_DOWNLOAD;
            }

            try {
                if (SDWrite) {
                    path = new File(directory, fileName);
                    entry.setValue(path.getAbsolutePath());
                } else {
                    path = new File(directory, fileName);
                    entry.setValue(SCHEME + path.getAbsolutePath());
                }

                FileOutputStream out = new FileOutputStream(path);


                if (typeImage.equals(FILE_EXTENSION_JPG)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                }

                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                entry.setValue(pathError);
            }
        }
        return null;
    }

    private String createNameFile(String imageURL){
        Uri uri = Uri.parse(imageURL);
        return uri.getLastPathSegment();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ContentValues cv = new ContentValues();
        for (Map.Entry<Integer, String> entry : mapForDownloadAvatar.entrySet()
                ) {
            int id = entry.getKey();
            cv.put(Contract.User.COLUMN_AVATAR_PATH, entry.getValue());
            Uri uri = ContentUris.withAppendedId(Contract.User.CONTENT_URI, id);

            context.getContentResolver().update(uri, cv, null, null);
            cv.clear();
        }
        Log.d("DownloadAvatar", "stop onPostExecute");
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File getAlbumStorageDir(Context context) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), NAME_DIR);
        if (!file.mkdirs()) {
            Exception e = new Exception();
            e.printStackTrace();
        }
        return file;
    }

    private Bitmap getBitmapErrorDownloadAvatar() {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getAssets().open(ERROR_DOWNLOAD);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private File getFileErrorDownloadAvatar(File directory) {
        Bitmap bitmap = getBitmapErrorDownloadAvatar();
        File file = new File(directory, ERROR_DOWNLOAD);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

//    private String getTypeImage(File path) {
//        try {
//            /**3 - количество символов в расширении файла**/
//            int startSubstring = path.getAbsolutePath().toString().length() - 3;
//            int endSubstring = path.getAbsolutePath().toString().length();
//            return path.getAbsolutePath().toString().substring(startSubstring, endSubstring);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
