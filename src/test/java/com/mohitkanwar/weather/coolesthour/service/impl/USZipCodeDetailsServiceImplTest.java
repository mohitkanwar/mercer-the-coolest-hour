package com.mohitkanwar.weather.coolesthour.service.impl;

import com.mohitkanwar.weather.coolesthour.exceptions.SetupException;
import com.mohitkanwar.weather.coolesthour.exceptions.USZipCodeNotFound;
import com.mohitkanwar.weather.coolesthour.integrations.response.model.ResponseCodeAndBody;
import com.mohitkanwar.weather.coolesthour.integrations.service.impl.HTTPClientService;
import com.mohitkanwar.weather.coolesthour.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class USZipCodeDetailsServiceImplTest {

    private final String SUCCESS_RES_BODY = "[{\"Version\":1,\"Key\":\"39354_PC\",\"Type\":\"PostalCode\",\"Rank\":55,\"LocalizedName\":\"Mountain View\",\"EnglishName\":\"Mountain View\",\"PrimaryPostalCode\":\"94043\",\"Region\":{\"ID\":\"NAM\",\"LocalizedName\":\"North America\",\"EnglishName\":\"North America\"},\"Country\":{\"ID\":\"US\",\"LocalizedName\":\"United States\",\"EnglishName\":\"United States\"},\"AdministrativeArea\":{\"ID\":\"CA\",\"LocalizedName\":\"California\",\"EnglishName\":\"California\",\"Level\":1,\"LocalizedType\":\"State\",\"EnglishType\":\"State\",\"CountryID\":\"US\"},\"TimeZone\":{\"Code\":\"PST\",\"Name\":\"America/Los_Angeles\",\"GmtOffset\":-8.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-10T10:00:00Z\"},\"GeoPosition\":{\"Latitude\":100.001,\"Longitude\":-200.202,\"Elevation\":{\"Metric\":{\"Value\":7.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":24.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"ParentCity\":{\"Key\":\"337169\",\"LocalizedName\":\"Mountain View\",\"EnglishName\":\"Mountain View\"},\"SupplementalAdminAreas\":[{\"Level\":2,\"LocalizedName\":\"Santa Clara\",\"EnglishName\":\"Santa Clara\"}],\"DataSets\":[\"Alerts\",\"DailyAirQualityForecast\",\"DailyPollenForecast\",\"ForecastConfidence\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"189270_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Creteil\",\"EnglishName\":\"Creteil\",\"PrimaryPostalCode\":\"94043\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"FR\",\"LocalizedName\":\"France\",\"EnglishName\":\"France\"},\"AdministrativeArea\":{\"ID\":\"94\",\"LocalizedName\":\"Val-de-Marne\",\"EnglishName\":\"Val-de-Marne\",\"Level\":1,\"LocalizedType\":\"Department\",\"EnglishType\":\"Department\",\"CountryID\":\"FR\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Paris\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":48.783,\"Longitude\":2.467,\"Elevation\":{\"Metric\":{\"Value\":41.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":133.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]}]";
    private final String SUCCESS_RES_BODY_WITHOUT_US_ZIPCODE = "[{\"Version\":1,\"Key\":\"412879_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Vlaamse Raad - Vlaams Parlement\",\"EnglishName\":\"Vlaamse Raad - Vlaams Parlement\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"BE\",\"LocalizedName\":\"Belgium\",\"EnglishName\":\"Belgium\"},\"AdministrativeArea\":{\"ID\":\"BRU\",\"LocalizedName\":\"Brussels\",\"EnglishName\":\"Brussels\",\"Level\":1,\"LocalizedType\":\"Province\",\"EnglishType\":\"Province\",\"CountryID\":\"BE\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Brussels\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":50.848,\"Longitude\":4.343,\"Elevation\":{\"Metric\":{\"Value\":33.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":109.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"372874_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Lausanne\",\"EnglishName\":\"Lausanne\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"CH\",\"LocalizedName\":\"Switzerland\",\"EnglishName\":\"Switzerland\"},\"AdministrativeArea\":{\"ID\":\"VD\",\"LocalizedName\":\"Vaud\",\"EnglishName\":\"Vaud\",\"Level\":1,\"LocalizedType\":\"Canton\",\"EnglishType\":\"Canton\",\"CountryID\":\"CH\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Zurich\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":46.525,\"Longitude\":6.643,\"Elevation\":{\"Metric\":{\"Value\":584.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":1916.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"411465_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Kobenhavn K\",\"EnglishName\":\"Kobenhavn K\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"DK\",\"LocalizedName\":\"Denmark\",\"EnglishName\":\"Denmark\"},\"AdministrativeArea\":{\"ID\":\"84\",\"LocalizedName\":\"Capital\",\"EnglishName\":\"Capital\",\"Level\":1,\"LocalizedType\":\"Region\",\"EnglishType\":\"Region\",\"CountryID\":\"DK\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Copenhagen\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":55.678,\"Longitude\":12.571,\"Elevation\":{\"Metric\":{\"Value\":3.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":11.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"493719_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Budapest\",\"EnglishName\":\"Budapest\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"HU\",\"LocalizedName\":\"Hungary\",\"EnglishName\":\"Hungary\"},\"AdministrativeArea\":{\"ID\":\"BU\",\"LocalizedName\":\"Budapest\",\"EnglishName\":\"Budapest\",\"Level\":1,\"LocalizedType\":\"Capital City\",\"EnglishType\":\"Capital City\",\"CountryID\":\"HU\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Budapest\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":47.501,\"Longitude\":19.047,\"Elevation\":{\"Metric\":{\"Value\":88.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":289.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"420552_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Luxembourg\",\"EnglishName\":\"Luxembourg\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"LU\",\"LocalizedName\":\"Luxembourg\",\"EnglishName\":\"Luxembourg\"},\"AdministrativeArea\":{\"ID\":\"LU\",\"LocalizedName\":\"Luxembourg\",\"EnglishName\":\"Luxembourg\",\"Level\":1,\"LocalizedType\":\"Canton\",\"EnglishType\":\"Canton\",\"CountryID\":\"LU\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Luxembourg\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":49.612,\"Longitude\":6.13,\"Elevation\":{\"Metric\":{\"Value\":303.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":993.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"372873_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Amsterdam Binnenstad en Oostelijk Havengebied\",\"EnglishName\":\"Amsterdam Binnenstad en Oostelijk Havengebied\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"NL\",\"LocalizedName\":\"Netherlands\",\"EnglishName\":\"Netherlands\"},\"AdministrativeArea\":{\"ID\":\"NH\",\"LocalizedName\":\"North Holland\",\"EnglishName\":\"North Holland\",\"Level\":1,\"LocalizedType\":\"Province\",\"EnglishType\":\"Province\",\"CountryID\":\"NL\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Amsterdam\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":52.367,\"Longitude\":4.903,\"Elevation\":{\"Metric\":{\"Value\":-2.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":-5.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"416506_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Oslo\",\"EnglishName\":\"Oslo\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"EUR\",\"LocalizedName\":\"Europe\",\"EnglishName\":\"Europe\"},\"Country\":{\"ID\":\"NO\",\"LocalizedName\":\"Norway\",\"EnglishName\":\"Norway\"},\"AdministrativeArea\":{\"ID\":\"03\",\"LocalizedName\":\"Oslo\",\"EnglishName\":\"Oslo\",\"Level\":1,\"LocalizedType\":\"County\",\"EnglishType\":\"County\",\"CountryID\":\"NO\"},\"TimeZone\":{\"Code\":\"CET\",\"Name\":\"Europe/Oslo\",\"GmtOffset\":1.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":\"2019-03-31T01:00:00Z\"},\"GeoPosition\":{\"Latitude\":59.913,\"Longitude\":10.741,\"Elevation\":{\"Metric\":{\"Value\":11.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":35.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"Alerts\",\"MinuteCast\"]},{\"Version\":1,\"Key\":\"425677_PC\",\"Type\":\"PostalCode\",\"Rank\":500,\"LocalizedName\":\"Herne Bay\",\"EnglishName\":\"Herne Bay\",\"PrimaryPostalCode\":\"1011\",\"Region\":{\"ID\":\"OCN\",\"LocalizedName\":\"Oceania\",\"EnglishName\":\"Oceania\"},\"Country\":{\"ID\":\"NZ\",\"LocalizedName\":\"New Zealand\",\"EnglishName\":\"New Zealand\"},\"AdministrativeArea\":{\"ID\":\"AUK\",\"LocalizedName\":\"Auckland\",\"EnglishName\":\"Auckland\",\"Level\":1,\"LocalizedType\":\"Region\",\"EnglishType\":\"Region\",\"CountryID\":\"NZ\"},\"TimeZone\":{\"Code\":\"NZDT\",\"Name\":\"Pacific/Auckland\",\"GmtOffset\":13.0,\"IsDaylightSaving\":true,\"NextOffsetChange\":\"2019-04-06T14:00:00Z\"},\"GeoPosition\":{\"Latitude\":-36.845,\"Longitude\":174.734,\"Elevation\":{\"Metric\":{\"Value\":3.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":10.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[]}]";
    @Autowired
    private USZipCodeDetailsServiceImpl usZipCodeDetailsService;
    @MockBean
    private HTTPClientService httpClientService;

    @Test
    public void getLocationFromZipCode() throws IOException {
        String zipcode = "Valid US Zipcode";
        ResponseCodeAndBody responseCodeAndBody = new ResponseCodeAndBody();
        responseCodeAndBody.setResponseCode(200);
        responseCodeAndBody.setBody(SUCCESS_RES_BODY);
        when(httpClientService.get(anyString())).thenReturn(responseCodeAndBody);
        Location location = usZipCodeDetailsService.getLocationFromZipCode(zipcode);
        assertEquals(100.001, location.getLatitude(), 0);
        assertEquals(-200.202, location.getLongitude(), 0);
    }

    @Test(expected = USZipCodeNotFound.class)
    public void getLocationInvalidZipCode() throws IOException {
        String zipcode = "Valid US Zipcode";
        ResponseCodeAndBody responseCodeAndBody = new ResponseCodeAndBody();
        responseCodeAndBody.setResponseCode(200);
        responseCodeAndBody.setBody(SUCCESS_RES_BODY_WITHOUT_US_ZIPCODE);
        when(httpClientService.get(anyString())).thenReturn(responseCodeAndBody);
        Location location = usZipCodeDetailsService.getLocationFromZipCode(zipcode);
    }

    @Test(expected = SetupException.class)
    public void getLocationIOException() throws IOException {
        when(httpClientService.get(anyString())).thenThrow(new IOException());
        Location location = usZipCodeDetailsService.getLocationFromZipCode("doesn't matter");
    }
}