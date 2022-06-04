package com.leon.initialize_gis.utils;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.di.view_model.ProgressModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportExcel extends AsyncTask<Activity, Integer, Void> {
    private final ProgressModel progress;
    private final String startDate, endDate, extension;

    public ExportExcel(Activity activity, String startDate, String endDate, String extension) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.extension = extension;
        progress = getApplicationComponent().ProgressModel();
        progress.show(activity, false);
    }

    @Override
    protected Void doInBackground(Activity... activities) {
        final Cursor cursor = getApplicationComponent().MyDatabase().getOpenHelper().getWritableDatabase()
                .query("SELECT name FROM sqlite_master WHERE type='table';");
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals("UsersPoints"))
                exportDbToCSV(cursor.getString(0), activities[0]);
        }
        return null;
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

    @SuppressLint("SimpleDateFormat")
    private void exportDbToCSV(final String tableName, final Activity activity) {
        final String child = (new SimpleDateFormat(activity.getString(R.string.save_format_name_melli)))
                .format(new Date());
        final File exportDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return;
        final File file = new File(exportDir, tableName + "_" + child + "." + extension);
        try {
            if (file.exists()) if (!file.delete()) return;
            if (!file.createNewFile()) return;
            final String query = String.format("SELECT * FROM %s WHERE date BETWEEN '%s' AND '%s'",
                    tableName, startDate, endDate);
            final CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            final Cursor cursor = getApplicationComponent().MyDatabase().query(query, null);

            csvWrite.writeNext(cursor.getColumnNames());
            while (cursor.moveToNext()) {
                final String[] arrStr = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount() - 1; i++) {
                    arrStr[i] = cursor.getString(i);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            cursor.close();
            activity.runOnUiThread(() ->
                    new CustomToast().success("پشتیبان گیری با موفقیت انجام شد.\n".concat("محل ذخیره سازی: ")
                            .concat(exportDir.getAbsolutePath()), Toast.LENGTH_SHORT));
        } catch (FileNotFoundException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.", Toast.LENGTH_SHORT));
        } catch (IOException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در پشتیبان گیری.\n".concat("علت خطا: ")
                            .concat(e.toString()), Toast.LENGTH_SHORT));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void exportDbToXLS(final String tableName, final Activity activity) {
        final String child = (new SimpleDateFormat(activity.getString(R.string.save_format_name_melli)))
                .format(new Date());
        final File exportDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return;
        final File file = new File(exportDir, tableName + "_" + child + "." + extension);
        try {
            if (file.exists()) if (!file.delete()) return;
            if (!file.createNewFile()) return;
        } catch (FileNotFoundException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.", Toast.LENGTH_SHORT));
        } catch (IOException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در پشتیبان گیری.\n".concat("علت خطا: ")
                            .concat(e.toString()), Toast.LENGTH_SHORT));
        }


    }

}