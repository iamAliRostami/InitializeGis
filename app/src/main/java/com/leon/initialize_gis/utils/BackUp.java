package com.leon.initialize_gis.utils;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.leon.initialize_gis.BuildConfig;
import com.leon.initialize_gis.R;
import com.leon.initialize_gis.di.view_model.ProgressModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackUp extends AsyncTask<Activity, Integer, Void> {
    private final ProgressModel progress;
    private final String startDate, endDate, extension;

    public BackUp(Activity activity, String startDate, String endDate, String extension) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.extension = extension;
        progress = getApplicationComponent().ProgressModel();
        progress.show(activity, false);
    }

    @SuppressLint("SimpleDateFormat")
    private void exportDatabaseToCSV(String tableName, String child, Activity activity) {
        final File exportDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + child);
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return;
        final File file = new File(exportDir, tableName + "_" + BuildConfig.BUILD_TYPE + "_"
                + BuildConfig.VERSION_CODE + extension);
        try {
            if (file.exists()) if (!file.delete()) return;
            if (!file.createNewFile()) return;
            final CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            final Cursor curCSV = getApplicationComponent().MyDatabase().query("SELECT * FROM "
                    + tableName + " WHERE date BETWEEN " + startDate + " AND " + endDate, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to export
                final String[] arrStr = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount() - 1; i++) {
                    arrStr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            activity.runOnUiThread(() ->
                    new CustomToast().success("پشتیبان گیری با موفقیت انجام شد.\n".concat("محل ذخیره سازی: ")
                            .concat(exportDir.getAbsolutePath()), Toast.LENGTH_SHORT));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.", Toast.LENGTH_SHORT));
        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در پشتیبان گیری.\n".concat("علت خطا: ")
                            .concat(e.toString()), Toast.LENGTH_SHORT));
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        progress.getDialog().dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected Void doInBackground(Activity... activities) {
        final MyDatabase myDatabase = getApplicationComponent().MyDatabase();
        final Cursor cursor = myDatabase.getOpenHelper().getWritableDatabase()
                .query("SELECT name FROM sqlite_master WHERE type='table';");
        final String timeStamp = "/_" + (new SimpleDateFormat(activities[0].getString(R.string.save_format_name_melli)))
                .format(new Date());
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals("UsersPoints"))
                exportDatabaseToCSV(cursor.getString(0), timeStamp, activities[0]);
        }
        return null;
    }
}