package samson.covidtracker.service;

import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import samson.covidtracker.Model.LocationState;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class CoronaTrackerService {
    private static String usDate = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv";
    private static String data = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationState> locationStates = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchDataVirus() throws IOException, InterruptedException {
        List<LocationState> newState = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usDate))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvofDate = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvofDate);
        for (CSVRecord record : records) {
            LocationState locationState = new LocationState();
            locationState.setUID(record.get("UID"));
            locationState.setIso2(record.get("iso2"));
            locationState.setIso3(record.get("iso3"));
            locationState.setCode3(record.get("code3"));
            locationState.setFIps(record.get("FIPS"));
            locationState.setAdmin2(record.get("Admin2"));
            locationState.setState(record.get("Province_State"));
            locationState.setCountry(record.get("Country_Region"));
            int latestCase = Integer.parseInt(record.get(record.size() - 1));
            int prevousDayCase = Integer.parseInt(record.get(record.size() - 2));
            locationState.setLatestCase(latestCase);
            locationState.setDifference(latestCase - prevousDayCase);


            System.out.println(locationState);
            newState.add(locationState);
        }
        this.locationStates = newState;
    }
}
