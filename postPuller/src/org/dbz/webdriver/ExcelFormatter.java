package org.dbz.webdriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelFormatter {
    private static final String stringToIgnore = "Due to non-posting";

    private static final String whiteSpaceRegEx = "\\r?\\n";

    public ExcelFormatter() {

    }


    public void writeZCastReport(Map<String, List<String>> builds, String filePath, String outputFileName) throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("builds");
        System.out.println("Determining number of rows to create. . .");
        Set<Map.Entry<String, List<String>>> weeklyBuilds = builds.entrySet();
        List<String> thisWeekBuilds = new ArrayList<>();

        for (Map.Entry<String, List<String>> teamsBuilds : weeklyBuilds) {
            HSSFSheet teamSheet = wb.createSheet(teamsBuilds.getKey());
            writeBuildsToSheet(teamSheet, teamsBuilds.getValue());
            List<String> teamBuilds = teamsBuilds.getValue();
            thisWeekBuilds.add(teamBuilds.get(teamBuilds.size()-1));
        }
        writeBuildsToSheet(sheet1, thisWeekBuilds);
        wb.setSheetOrder("builds",  0);
       
        writeWorkbookToFile(wb, filePath, outputFileName);

        System.out.println("done");
    }

    public boolean writeSingleTeamBuilds(List<String> builds, String teamName, String filepath, String outputFileName){
        boolean success = true;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet(teamName);
        
        writeBuildsToSheet(sheet1, builds);
        success = writeWorkbookToFile(wb, filepath, outputFileName);
        
        return success;
    }
    
    public boolean writeWorkbookToFile(HSSFWorkbook spreadsheet, String filePath, String outputFileName){
        boolean success = true;
        
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath + outputFileName);

            spreadsheet.write(fileOut);
            spreadsheet.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
    
    
    public void writeBuildsToSheet(HSSFSheet sheet, List<String> builds) {
        //since POI works in a row based way, let's figure out how many rows we need (column #s = how many weeks are in the season + pre-season)
        int[] weeks = new int[builds.size() +1];
        int weeksIndex = 0;

        for (String build : builds) {
            String[] postSize = build.split(whiteSpaceRegEx); //using both since I don't know how webdriver handles the carriage returns
            weeks[weeksIndex] = postSize.length;
            weeksIndex++;
        }

        IntSummaryStatistics stats = Arrays.stream(weeks).summaryStatistics();
        int maxLines = stats.getMax(); //the max here should be the max # of rows needed in the spreadsheet

        //now create the number of rows we need, plus a little extra space
        HSSFRow[] rows = new HSSFRow[maxLines + 3];

        for (int i = 0; i < maxLines; i++) {
            rows[i] = sheet.createRow(i);
        }

        try {

            int cellIndex = 0;
            //idea should be that each *column* contains a week's build
            for (int columnIndex = 0; columnIndex < builds.size(); columnIndex++) {

                String buildlines[] = builds.get(columnIndex).split(whiteSpaceRegEx);
                for (int buildLine = 0; buildLine < buildlines.length; buildLine++) {
                    //System.out.println("adding: " + buildlines[buildLine] );
                    if (StringUtils.containsIgnoreCase(buildlines[buildLine], stringToIgnore)) {
                        continue;
                    }

                    rows[buildLine].createCell(cellIndex).setCellValue(buildlines[buildLine]);
                    sheet.autoSizeColumn(cellIndex);
                }

                //skip a column to help readability
                cellIndex += 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
