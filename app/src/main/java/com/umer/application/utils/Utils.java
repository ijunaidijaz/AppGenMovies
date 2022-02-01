package com.umer.application.utils;



import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umer.application.R;
import com.umer.application.app.MainApp;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private static final CharSequence ALLOWED_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm1234567890";

    public static ProgressDialog progressDialog = null;


//    public static File convertToPDF(Bitmap bitmap, File imageFile) {
//        File myPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + R.string.app_name + System.currentTimeMillis() + ".pdf");
//
//        try {
//            Document document = new Document(PageSize.A4, 25, 25, 30, 30);
//            PdfWriter.getInstance(document, new FileOutputStream(myPath));
//            document.open();
//            Image image = Image.getInstance(imageFile.getAbsolutePath());
//            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
//            image.scalePercent(scaler);
//            image.scaleToFit(PageSize.A4);
//            document.setPageSize(PageSize.A4);
//            document.setMargins(0, 0, 1, 1);
//            document.newPage();
//            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
//            document.add(image);
//            document.close();
//            if (imageFile.exists()) imageFile.delete();
//        } catch (Exception e) {
//            Notify.Toast(e.getMessage());
//        }
//        return myPath;
//    }

    public static void openPDFFile(File pdfFile, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);
    }

    public static String addStorageDirectory(String path) {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
        return path;
    }

    public static String removeExtra(String path) {
        path = path.replace(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", "");
        return path;
    }

    public static boolean isCause(
            Class<? extends Throwable> expected,
            Throwable exc) {
        return expected.isInstance(exc) || (
                exc != null && isCause(expected, exc.getCause())
        );
    }

    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static BigDecimal setDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, BigDecimal.ROUND_UP);
        return bd;
    }

    public static Loading getLoading(Activity activity) {
        return Loading.make(activity)
                .setCancelable(false)
                .setMessage(Utils.getString(R.string.pleaseWait))
                .show();
    }

    public static String getString(int id) {
        return MainApp.getAppContext().getResources().getString(id);
    }

    public static Loading getLoading(Activity activity, String mesage) {
        return Loading.make(activity)
                .setCancelable(false)
                .setMessage(mesage)
                .show();
    }

}
