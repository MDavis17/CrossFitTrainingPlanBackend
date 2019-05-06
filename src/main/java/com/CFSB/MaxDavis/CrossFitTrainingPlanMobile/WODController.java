package com.CFSB.MaxDavis.CrossFitTrainingPlanMobile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WODController {

    @RequestMapping("/wod")
    public WOD wod(@RequestParam(value = "metcon",defaultValue = "None") String metcon) {
        return new WOD(metcon,"None","None","None","None");
    }
}
