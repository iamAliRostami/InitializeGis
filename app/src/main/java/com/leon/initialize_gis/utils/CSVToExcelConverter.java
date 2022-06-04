package com.leon.initialize_gis.utils;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.leon.initialize_gis.di.view_model.ProgressModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CSVToExcelConverter extends AsyncTask<String, Void, Boolean> {
    private final ProgressModel dialog;

    public CSVToExcelConverter(Activity activity) {
        super();
        this.dialog = getApplicationComponent().ProgressModel();
        this.dialog.show(activity, false);
    }


    @Override
    protected Boolean doInBackground(String... params) {
        ArrayList arList = null;
        ArrayList al;

        String inFilePath = "/storage/emulated/0/Download/UsersPoints_20220602_232150_890.CSV";
        String outFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test.xls";
        String thisLine;

        try {

            FileInputStream fis = new FileInputStream(inFilePath);
            DataInputStream myInput = new DataInputStream(fis);
            int i = 0;
            arList = new ArrayList();
            while ((thisLine = myInput.readLine()) != null) {
                al = new ArrayList();
                String strar[] = thisLine.split(",");
                for (int j = 0; j < strar.length; j++) {
                    al.add(strar[j]);
                }
                arList.add(al);
                System.out.println();
                i++;
            }
        } catch (Exception e) {
            System.out.println("shit");
        }

        try {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for (int k = 0; k < arList.size(); k++) {
                ArrayList ardata = (ArrayList) arList.get(k);
                HSSFRow row = sheet.createRow((short) 0 + k);
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
                    //*/
                    // cell.setCellValue(ardata.get(p).toString());
                }
                System.out.println();
            }
            FileOutputStream fileOut = new FileOutputStream(outFilePath);
            hwb.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } //main method ends
        return true;
    }

    protected void onPostExecute(final Boolean success) {

        dialog.getDialog().dismiss();

        if (success)
            new CustomToast().success("file is built!", Toast.LENGTH_LONG);
        else
            new CustomToast().error("file fail to build", Toast.LENGTH_SHORT);
    }
}