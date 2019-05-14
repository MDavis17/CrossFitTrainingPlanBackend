package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sqladmin.SQLAdminScopes;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
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
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String CREDENTIALS_FILE_PATH = "/core-photon-240313-20f8791f9586.json";
//    private static final String CREDENTIALS_FILE_PATH = "/client_secret_644210756067-k4mkpkmth15tltm8go1j4bb7q2uu2ejm.apps.googleusercontent.com.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetProxy.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        LocalServerReceiver receiver = new LocalServerReceiver();
        VerificationCodeReceiver receiver = new GooglePromptReceiver();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        // may want to maunally set the redirect uri to my api endpoint
    }


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
//        final String range = "Class Data!A3:K";
        String range = "A3:A38";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> dateColumn = response.getValues();
//        List<Object> dateColumn = response.setMajorDimension("COLUMNS").getValues().get(0);
        int rowNum = 3; // first row with data in sheet
//        for(Object d : dateColumn) {

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

//        String metcon = row.get(1).toString();
//        String gymnastics = row.get(3).toString();
//        String oly = row.get(5).toString();
//        String power = row.get(7).toString();
//        String running = row.get(9).toString();
        return wodParts;
    }

    public String[] getWOD_2LO(Date date) throws IOException, GeneralSecurityException {
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
//        final String range = "Class Data!A3:K";
        String range = "A3:A38";

//        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("../../resources/core-photon-240313-20f8791f9586.json"))
//                .createScoped(Collections.singleton(SQLAdminScopes.SQLSERVICE_ADMIN));
        GoogleCredential credential = GoogleCredential.fromStream(GoogleSheetProxy.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
                .createScoped(SCOPES);

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> dateColumn = response.getValues();
//        List<Object> dateColumn = response.setMajorDimension("COLUMNS").getValues().get(0);
        int rowNum = 3; // first row with data in sheet
//        for(Object d : dateColumn) {

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

//        String metcon = row.get(1).toString();
//        String gymnastics = row.get(3).toString();
//        String oly = row.get(5).toString();
//        String power = row.get(7).toString();
//        String running = row.get(9).toString();
        return wodParts;
    }

    public void printWODs(Date date) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A3:K";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("printing all olympic workouts:");
            for (List row : values) {
                System.out.printf("date: %s, workout: %s\n",row.get(0),row.get(5));
            }
        }
    }

}
