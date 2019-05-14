package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
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
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/core-photon-240313-20f8791f9586.json";


    public String[] getWOD(Date date) throws IOException, GeneralSecurityException {
        // [Metcon, Gymnastics, Oly, Power, Running]
        String wodParts[] = {"None","None","None","None","None"};

        //getting parts of date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1; // months start at 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String passedDateStr = String.format("%d/%d/%d",month,day,year);

        //OAuth code flow
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String range = "A3:A38";
        GoogleCredential credential = GoogleCredential.fromStream(GoogleSheetProxy.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
                .createScoped(SCOPES);

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> dateColumn = response.getValues();
        int rowNum = 3; // first row with data in sheet

        for(int i = 0; i< dateColumn.size(); i++) {
            String dStr = dateColumn.get(i).toString().replaceAll("[^\\d/]",""); // parse cell date into "m/d/yyyy"
            if(dStr.equals(passedDateStr)) {
                rowNum = i+3;
                break;
            }
        }

        // now get the correct row from sheet
        range = String.format("A%d:K%d",rowNum,rowNum);
        response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<Object> wodRow = response.getValues().get(0);

        // i is wodParts index, j is index for the workout types in the sheet
        for(int i = 0, j = 1; i < wodParts.length; i++,j+=2) {
            if(wodRow.get(j).toString() != "") {
                wodParts[i] = wodRow.get(j).toString();
            }
        }
        return wodParts;
    }

}
