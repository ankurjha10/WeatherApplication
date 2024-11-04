package com.weatherApp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.Scanner;

public class Main {
    static void DisplayInterface(){
        System.out.println("***************************************");
        System.out.println("*          WEATHER APPLICATION        *");
        System.out.println("***************************************");
        System.out.println("*                                     *");
        System.out.println("*       Welcome to Weather App!      *");
        System.out.println("*                                     *");
        System.out.println("***************************************");
        System.out.println("*                                     *");
        System.out.println("*   Please select an option:         *");
        System.out.println("*                                     *");
        System.out.println("*   1. Check Current Weather          *");
        System.out.println("*   2. Exit                           *");
        System.out.println("*                                     *");
        System.out.println("***************************************");
        System.out.println("*   Enter your choice:                *");
        System.out.print("*   > ");
    }
    static void CheckWeather(String city) throws IOException, InterruptedException {

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=98d37aae248170f379dc22a9478950a9";
        var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        var client = HttpClient.newBuilder().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        //Parse JSON response
        JSONObject json = new JSONObject(responseBody);

        if (response.statusCode() == 200){

            //Extracting Information from the response body which is in JSON Format
            String cityName = json.getString("name");
            JSONObject main = json.getJSONObject("main");
            float temperature = (float) (main.getFloat("temp") - 273.15);
            float feelsLike = (float) (main.getFloat("feels_like") - 273.15);
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            String weatherInfo = weather.getString("main");
            String weatherDescription = weather.getString("description");

            //Printing the Information Extracted:
            System.out.println("Fetching Current weather for " + cityName + "...");
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Feels Like: " + feelsLike + "°C");
            System.out.println("Weather: " + weatherInfo);
            System.out.println("Weather Description: " + weatherDescription);
        }
        else if (response.statusCode() == 404) {
            String notFound = json.getString("message");
            System.out.println(notFound);
            System.out.println("Enter a valid City Name");
        }
        else {
            System.out.println("Failed to fetch Information from the Server...");
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException{

        Scanner sc = new Scanner(System.in);
        boolean continueApp = true;

        DisplayInterface();
        menu: while (true) {
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    System.out.print("Enter the Name of the City: ");
                    String city = sc.nextLine();
                    CheckWeather(city);
                    break menu;

                case 2:
                    continueApp = false;
                    System.out.println("Thank you for using the Weather Application!");
                    break menu;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}