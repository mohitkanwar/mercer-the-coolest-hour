package com.mohitkanwar.weather.coolesthour.integrations.service.impl;

import com.mohitkanwar.weather.coolesthour.integrations.response.model.ResponseCodeAndBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest({URL.class, HttpURLConnection.class})
@SpringBootTest
public class HTTPClientServiceTest {

    @Autowired
    private HTTPClientService httpClientService;

    @Test
    public void get() throws Exception {
        String url = "http://www.example.com";
        URL u = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
        HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(huc);
        PowerMockito.when(huc.getResponseCode()).thenReturn(200);
        String body = "body";
        PowerMockito.when(huc.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
        final ResponseCodeAndBody responseCodeAndBody = httpClientService.get(url);
        assertEquals(200, responseCodeAndBody.getResponseCode());
        // assertEquals(body,responseCodeAndBody.getBody());

    }

    @Test(expected = UnknownHostException.class)
    public void getException() throws Exception {
        String url = "http://www.exampfffffffffffffffffle.com";
        URL u = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(url).thenReturn(u);
        HttpURLConnection huc = PowerMockito.mock(HttpURLConnection.class);
        PowerMockito.when(u.openConnection()).thenReturn(huc);
        PowerMockito.when(huc.getResponseCode()).thenReturn(200);
        String body = "body";
        PowerMockito.when(huc.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
        final ResponseCodeAndBody responseCodeAndBody = httpClientService.get(url);
        assertEquals(200, responseCodeAndBody.getResponseCode());
        // assertEquals(body,responseCodeAndBody.getBody());

    }
}