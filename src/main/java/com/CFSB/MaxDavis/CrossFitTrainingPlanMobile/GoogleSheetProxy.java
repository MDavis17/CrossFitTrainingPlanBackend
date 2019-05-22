package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetProxy {
    private static final String APPLICATION_NAME = "CrossFit Training Plan API";
    private final String spreadsheetId = "1phA5rOnEct0dZ6Xx6ugX5m6IB_sLMPOgKtblgSsOlpY";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
//    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE,SheetsScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/core-photon-240313-20f8791f9586.json";

    public int getDateRow(String date,List<List<Object>> dateColumn) {
        for(int i = 0; i< dateColumn.size(); i++) {
            String dStr = dateColumn.get(i).toString().replaceAll("[^\\d/]",""); // parse cell date into "m/d/yyyy"
            if(dStr.equals(date)) {
                return i+3;
            }
        }
        return -1;
    }

    public String formatDate(String date) {
        String[] dateParts = date.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        return month+"/"+day+"/"+year;
    }

    public String[] getWOD(String date) throws IOException, GeneralSecurityException {
        // [Metcon, metconStatus, Gymnastics, gymnasticsStatus, Oly, olyStatus, Power, powerStatus, Running, runningStatus]
        String wodParts[] = {"None","FALSE","None","FALSE","None","FALSE","None","FALSE","None","FALSE"};

        //format month and day to remove leading 0s (1 rather than 01)
        date = formatDate(date);

        String range = "A3:A38";
        Sheets service = getSheetService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> dateColumn = response.getValues();
        int rowNum = getDateRow(date,dateColumn);

        // only fill wodParts array if we have a date match
        if(rowNum != -1) {
            // now get the correct row from sheet
            range = String.format("A%d:K%d",rowNum,rowNum);
            response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<Object> wodRow = response.getValues().get(0);

            for(int i = 0; i < wodParts.length; i++) {
                wodParts[i] = wodRow.get(i+1).toString();
            }
        }
        return wodParts;
    }

    public Sheets getSheetService() throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential.fromStream(GoogleSheetProxy.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
                .createScoped(SCOPES);

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

    public UpdateValuesResponse toggleWodPart(String date, String partCol, boolean newState) throws IOException, GeneralSecurityException{
        Sheets service = getSheetService();
        ValueRange requestBody = new ValueRange();
        List<List<Object>> values = new ArrayList<>();
        List<Object> vals = new ArrayList<>();
        vals.add(newState);
        values.add(vals);
        requestBody.setValues(values);

        //get row from date
        String range = "A3:A38";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> dateColumn = response.getValues();
        int dateRow = getDateRow(formatDate(date),dateColumn);

        range = partCol+dateRow;
        System.out.println(requestBody);
        String valueInputOption = "RAW";
        Sheets.Spreadsheets.Values.Update request = service.spreadsheets().values().update(spreadsheetId,range,requestBody);
        request.setValueInputOption(valueInputOption);

        return request.execute();
    }

}
