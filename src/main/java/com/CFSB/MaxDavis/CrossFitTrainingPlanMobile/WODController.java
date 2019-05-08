package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@RestController
public class WODController {
    GoogleSheetProxy sheetProxy = new GoogleSheetProxy();

//    @RequestMapping("/wod")
//    public WOD wod(@RequestParam(value = "metcon",defaultValue = "None") String metcon,
//                   @RequestParam(value = "gymnastics",defaultValue = "None") String gymnastics,
//                   @RequestParam(value = "oly",defaultValue = "None") String oly,
//                   @RequestParam(value = "power",defaultValue = "None") String power,
//                   @RequestParam(value = "running",defaultValue = "None") String running) {
//
//        return new WOD(metcon,gymnastics,oly,power,running);
//    }

    @RequestMapping ("/wod")
    public WOD wod() throws IOException, GeneralSecurityException{
//        sheetProxy.printWODs(new Date());
        return new WOD("","","","","");
    }
}
