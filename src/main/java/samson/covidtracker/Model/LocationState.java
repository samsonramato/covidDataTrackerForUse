package samson.covidtracker.Model;

import lombok.Data;

@Data
public class LocationState {;
    private String admin2;
    private String UID;
    private String iso2;
    private String iso3;
    private String code3;
    private String fIps;
    private String state;
    private String country;
    private int latestCase;
    private int difference;

}
