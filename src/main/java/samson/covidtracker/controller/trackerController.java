package samson.covidtracker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import samson.covidtracker.Model.LocationState;
import samson.covidtracker.service.CoronaTrackerService;

import java.util.List;

@Controller
public class trackerController {

    @Autowired
    CoronaTrackerService coronaTrackerService;
    @GetMapping("/")
    public String home(Model model){
        List<LocationState> locationStates = coronaTrackerService.getLocationStates();
        int total=locationStates.stream().mapToInt(a->a.getLatestCase()).sum();
        int newCases=locationStates.stream().mapToInt(a->a.getDifference()).sum();
        model.addAttribute("LocationState",locationStates);
        model.addAttribute("totalCases",total);
        model.addAttribute("newCases",newCases);

        return "home";
    }
}
