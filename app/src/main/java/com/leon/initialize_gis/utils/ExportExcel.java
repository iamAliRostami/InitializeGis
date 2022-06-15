package com.leon.initialize_gis.utils;

import static com.leon.initialize_gis.enums.OutputEnum.CSV;
import static com.leon.initialize_gis.enums.OutputEnum.XLS;
import static com.leon.initialize_gis.enums.OutputEnum.XLSX;
import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.leon.initialize_gis.di.view_model.ProgressModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ExportExcel extends AsyncTask<Activity, Integer, String> {
    private final ProgressModel progress;
    private final String startDate, endDate, extension;
    private boolean error = true;
    private final String path, fileName;

    public ExportExcel(Activity activity, String startDate, String endDate, String extension,
                       String path, String fileName) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
        this.extension = extension;
        this.path = path;
        this.fileName = fileName;
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
                    return exportDbToCSV(cursor.getString(0));
                else if (extension.equals(XLS.getValue()))
                    return exportDbToXLS(cursor.getString(0));
                else if (extension.equals(XLSX.getValue()))
                    return exportDbToXLSX(cursor.getString(0));

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progress.getDialog().dismiss();
        if (result != null) {
            if (error)
                new CustomToast().warning(result, Toast.LENGTH_LONG);
            else
                new CustomToast().success("ایجاد فایل با موفقیت انجام شد. محل ذخیره:\n".concat(result),
                        Toast.LENGTH_LONG);
        } else
            new CustomToast().error("ایجاد خروجی با خطا مواجه شد، مجددا تلاش کنید.", Toast.LENGTH_LONG);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    private String exportDbToCSV(final String tableName) {
        final File exportDir = new File(path, "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return null;
        final File file = new File(exportDir, fileName + CSV.getValue());
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
            error = false;
            return extension.equals(CSV.getValue()) ? exportDir.getAbsolutePath() : file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "خطا در ایجاد خروجی.\n".concat("علت خطا: ").concat(e.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String exportDbToXLS(final String tableName) {
        final String csvFileAddress = exportDbToCSV(tableName);
        error = true;
        if (csvFileAddress == null) return null;
        ArrayList arList;
        ArrayList al;
        final File exportDir = new File(path, "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return null;
        final File file = new File(exportDir, fileName + XLS.getValue());
        try {
            if (file.exists()) if (!file.delete()) return null;
            if (!file.createNewFile()) return null;
            String thisLine;
            try {
                final FileInputStream fis = new FileInputStream(csvFileAddress);
                final DataInputStream myInput = new DataInputStream(fis);
                arList = new ArrayList();
                while ((thisLine = myInput.readLine()) != null) {
                    al = new ArrayList();
                    final String[] strar = thisLine.split(",");
                    Collections.addAll(al, strar);
                    arList.add(al);
                }
            } catch (Exception e) {
                return "خطا در ایجاد خروجی.\n".concat("علت خطا: ").concat(e.toString());
            }
            try {
                final HSSFWorkbook hwb = new HSSFWorkbook();
                final HSSFSheet sheet = hwb.createSheet(tableName);
                for (int k = 0; k < arList.size(); k++) {
                    final ArrayList ardata = (ArrayList) arList.get(k);
                    final HSSFRow row = sheet.createRow(k);
                    for (int p = 0; p < ardata.size(); p++) {
                        final HSSFCell cell = row.createCell((short) p);
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
                final FileOutputStream fileOut = new FileOutputStream(file);
                hwb.write(fileOut);
                fileOut.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                return "خطا در ایجاد خروجی.\n".concat("علت خطا: ").concat(ex.toString());
            }
            error = false;
            return exportDir.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.";
        } catch (IOException e) {
            e.printStackTrace();
            return "خطا در ایجاد خروجی.\n".concat("علت خطا: ").concat(e.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String exportDbToXLSX(final String tableName) {
        final String csvFileAddress = exportDbToCSV(tableName);
        error = true;
        if (csvFileAddress == null) return null;
        final File exportDir = new File(path, "/");
        if (!exportDir.exists()) if (!exportDir.mkdirs()) return null;
        final File file = new File(exportDir, fileName + XLSX.getValue());

        try {
            if (file.exists()) if (!file.delete()) return null;
            if (!file.createNewFile()) return null;

            final XSSFWorkbook workBook = new XSSFWorkbook();
            final XSSFSheet sheet = workBook.createSheet(tableName);
            String currentLine;
            int RowNum = 0;
            final BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
            while ((currentLine = br.readLine()) != null) {
                String[] str = currentLine.split(",");
                RowNum++;
                XSSFRow currentRow = sheet.createRow(RowNum);
                for (int i = 0; i < str.length; i++) {
                    currentRow.createCell(i).setCellValue(str[i]);
                }
            }

            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            error = false;
            return exportDir.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "خطا در ایجاد خروجی\n پوشه ی دانلود دستگاه خود را تخلیه کنید.";
        } catch (IOException e) {
            e.printStackTrace();
            return "خطا در ایجاد خروجی.\n".concat("علت خطا: ").concat(e.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
}