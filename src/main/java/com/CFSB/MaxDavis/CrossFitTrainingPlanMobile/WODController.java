package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@RestController
public class WODController {
    GoogleSheetProxy sheetProxy = new GoogleSheetProxy();

    @RequestMapping ("/wod")
    public WOD wod() throws IOException, GeneralSecurityException{
        String[] wodParts = sheetProxy.getWOD(new Date());
        return new WOD(wodParts[0],wodParts[1],wodParts[2],wodParts[3],wodParts[4]);
    }
}
