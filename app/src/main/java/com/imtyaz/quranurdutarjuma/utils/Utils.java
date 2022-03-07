package com.imtyaz.quranurdutarjuma.utils;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.app.NotificationCompat;

import com.imtyaz.quranurdutarjuma.R;
import com.imtyaz.quranurdutarjuma.activities.MainActivity;
import com.imtyaz.quranurdutarjuma.app.MainApp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


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

    public static void sendNotification(Context context, String messageBody, String title) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 12, intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setContentTitle(title);
//        int color = ContextCompat.getColor(this, R.color.colorPrimary);
//        builder.setContentTitle(HtmlCompat.fromHtml("<font color=\"" + color + "\">" + title + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        builder.setContentText(messageBody);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setVibrate(new long[0]);
        builder.setSound(defaultSoundUri);
        builder.setAutoCancel(true);
//        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL("https://picsum.photos/200/300"));
//        s.setSummaryText("Image");
//        builder.setStyle(s);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel1";
//            if (type != null) {
//                channelId = "channel2";
//            }
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "New Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(0, builder.build());

    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
