package com.leon.initialize_gis.utils;

import static com.leon.initialize_gis.enums.OutputEnum.CSV;
import static com.leon.initialize_gis.enums.OutputEnum.XLS;
import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.di.view_model.ProgressModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ExportExcel extends AsyncTask<Activity, Integer, String> {
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
    protected String doInBackground(Activity... activities) {
        final Cursor cursor = getApplicationComponent().MyDatabase().getOpenHelper().getWritableDatabase()
                .query("SELECT name FROM sqlite_master WHERE type='table';");
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals("UsersPoints")) {
                if (extension.equals(CSV.getValue()))
                    return exportDbToCSV(cursor.getString(0), activities[0]);
                else if (extension.equals(XLS.getValue()))
                    return exportDbToXLS(cursor.getString(0), activities[0]);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progress.getDialog().dismiss();
        if (result != null)
            new CustomToast().success("پشتیبان گیری با موفقیت انجام شد.\n".concat("محل ذخیره سازی: ")
                    .concat(result), Toast.LENGTH_SHORT);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @SuppressLint("SimpleDateFormat")
    private String exportDbToCSV(final String tableName, final Activity activity) {
        final String child = (new SimpleDateFormat(activity.getString(R.string.save_format_name_melli)))
                .format(new Date());
        final File exportDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return null;
        final File file = new File(exportDir, tableName + "_" + child + "." + CSV.getValue());
        try {
            if (file.exists()) if (!file.delete()) return null;
            if (!file.createNewFile()) return null;
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
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.", Toast.LENGTH_SHORT));
        } catch (IOException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی.\n".concat("علت خطا: ")
                            .concat(e.toString()), Toast.LENGTH_SHORT));
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    private String exportDbToXLS(final String tableName, final Activity activity) {
        final String inFilePath = exportDbToCSV(tableName, activity);
        if (inFilePath == null) return null;

        ArrayList arList;
        ArrayList al;


        final String child = (new SimpleDateFormat(activity.getString(R.string.save_format_name_melli)))
                .format(new Date());
        final File exportDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return null;
        final File file = new File(exportDir, tableName + "_" + child + "." + XLS.getValue());
        try {
            if (file.exists()) if (!file.delete()) return null;
            if (!file.createNewFile()) return null;


            String thisLine;
            try {
                FileInputStream fis = new FileInputStream(inFilePath);
                DataInputStream myInput = new DataInputStream(fis);
                arList = new ArrayList();
                while ((thisLine = myInput.readLine()) != null) {
                    al = new ArrayList();
                    String strar[] = thisLine.split(",");
                    Collections.addAll(al, strar);
                    arList.add(al);
                    System.out.println();
                }
            } catch (Exception e) {
                activity.runOnUiThread(() ->
                        new CustomToast().error("خطا در ایجاد خروجی.\n".concat("علت خطا: ")
                                .concat(e.toString()), Toast.LENGTH_SHORT));
                return null;
            }
            try {
                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet(tableName + "_" + child);
                for (int k = 0; k < arList.size(); k++) {
                    ArrayList ardata = (ArrayList) arList.get(k);
                    HSSFRow row = sheet.createRow(k);
                    for (int p = 0; p < ardata.size(); p++) {
                        HSSFCell cell = row.createCell((short) p);
                        String data = ardata.get(p).toString();
                        if (data.startsWith("=")) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            data = data.replaceAll("\"", "");
                            data = data.replaceAll("=", "");
                            cell.setCellValue(data);
                        } else if (data.startsWith("\"")) {
                            data = data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(data);
                        } else {
                            data = data.replaceAll("\"", "");
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(data);
                        }
                    }
                }
                FileOutputStream fileOut = new FileOutputStream(file);
                hwb.write(fileOut);
                fileOut.close();
            } catch (Exception ex) {
                activity.runOnUiThread(() ->
                        new CustomToast().error("خطا در ایجاد خروجی.\n".concat("علت خطا: ")
                                .concat(ex.toString()), Toast.LENGTH_SHORT));
                return null;
            }
            return exportDir.getAbsolutePath();

        } catch (FileNotFoundException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.", Toast.LENGTH_SHORT));
        } catch (IOException e) {
            activity.runOnUiThread(() ->
                    new CustomToast().error("خطا در ایجاد خروجی.\n".concat("علت خطا: ")
                            .concat(e.toString()), Toast.LENGTH_SHORT));
        }
        return null;
    }
}