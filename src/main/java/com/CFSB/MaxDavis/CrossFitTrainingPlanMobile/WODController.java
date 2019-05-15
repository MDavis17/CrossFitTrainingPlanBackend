package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class WODController {
    GoogleSheetProxy sheetProxy = new GoogleSheetProxy();

    @RequestMapping ("/wod")
    public WOD wod(@RequestParam(value = "date",defaultValue = "") String date) throws IOException, GeneralSecurityException{
        int month,day,year;
        String selectedDate;

        // set date string to pass to sheet proxy
        if(!date.equals("")) { // if a date is passed -> pass it along
            selectedDate = date;
        }
        else { // no date passed -> default to current date
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
            calendar.setTime(new Date());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH)+1; // months start at 0
            day = calendar.get(Calendar.DAY_OF_MONTH);
            selectedDate = String.format("%d/%d/%d",month,day,year);
        }

        String[] wodParts = sheetProxy.getWOD(selectedDate);
        return new WOD(wodParts[0],wodParts[1],wodParts[2],wodParts[3],wodParts[4]);
    }
}
