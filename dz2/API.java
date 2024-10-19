package dz2;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Может использоваться как статически, так и объект. При инициализации объекта необходимо передать API ключ, но зато потом все методы можно вызывать без него

class API {
    String apiKey;

    public API(String apiKey) {
        this.apiKey = apiKey;
    }

    // Get получает погоду из объекта. Всё на автомате
    public Weather Get() throws InvalidQueryException, InvalidArgumentException {
        if (this.apiKey == null) {
            throw new InvalidArgumentException("no stored api key");
        }
        return Get(this.apiKey, 0, -1, (double) -1);
    }

    // Get получает погоду из объекта с передачей лимита дней
    public Weather Get(int limit) throws InvalidQueryException, InvalidArgumentException {
        if (this.apiKey == null) {
            throw new InvalidArgumentException("no stored api key");
        }
        return Get(this.apiKey, limit, -1, (double) -1);
    }

    // Get получает погоду из объекта с передачей широты и долготы
    public Weather Get(double latitude, double longitude) throws InvalidQueryException, InvalidArgumentException {
        if (this.apiKey == null) {
            throw new InvalidArgumentException("no stored api key");
        }
        return Get(this.apiKey, 0, latitude, longitude);
    }

    // Get получает погоду из объекта с передачей широты, долготы и лимита
    public Weather Get(double latitude, double longitude, int limit) throws InvalidQueryException, InvalidArgumentException {
        if (this.apiKey == null) {
            throw new InvalidArgumentException("no stored api key");
        }
        return Get(this.apiKey, limit, latitude, longitude);
    }

    // Get получает погоду статически
    public static @NotNull Weather Get(String apiKey) throws InvalidQueryException {
        return Get(apiKey, 0, -1, (double) -1);
    }

    // Get получает погоду статически с передачей лимита дней
    public static Weather Get(String apiKey, int limit) throws InvalidQueryException, InvalidArgumentException {
        return Get(apiKey, limit, -1, (double) -1);
    }

    // Get получает погоду статически с передачей широты и долготы
    public static Weather Get(String apiKey, double latitude, double longitude) throws InvalidQueryException, InvalidArgumentException {
        return Get(apiKey, 0, latitude, longitude);
    }

    // Get получает погоду статически с передачей широты, долготы и лимита
    public static Weather Get(String apiKey, double latitude, double longitude, int limit) throws InvalidQueryException, InvalidArgumentException {
        return Get(apiKey, limit, latitude, longitude);
    }

    // Get получает погоду статически с передачей лимита дней
    public static @NotNull Weather Get(String apiKey, int limit, double latitude, double longitude) throws InvalidQueryException {
        String body = query(apiKey, latitude, longitude);
        JsonElement json = JsonParser.parseString(body);

        Weather weather = new Weather();
        weather.setLatitude(getLatitude(json));
        weather.setLongitude(getLongitude(json));
        weather.setDays(limit == 0 ? 7 : limit);
        weather.setCurrentTemp(getCurrentTemp(json));
        weather.setAverageTemp(getAverageTemp(json, limit));
        weather.setPrettyJson(gePrettyJson(json));
        return weather;
    }

    // getCurrentTemp получает из JSON объекта текущую широту
    private static double getLatitude(@NotNull JsonElement json) {
        return json.getAsJsonObject().getAsJsonObject("info").get("lat").getAsDouble();
    }

    // getCurrentTemp получает из JSON объекта текущую долготу
    private static double getLongitude(@NotNull JsonElement json) {
        return json.getAsJsonObject().getAsJsonObject("info").get("lon").getAsDouble();
    }

    // getCurrentTemp получает из JSON объекта текущую температуру
    private static int getCurrentTemp(@NotNull JsonElement json) {
        return json.getAsJsonObject().getAsJsonObject("fact").get("temp").getAsInt();
    }

    // getAverageTemp получает из JSON объекта среднюю температуру за указанное количество дней (от 1 до 7, где 0 - это все доступные дни)
    private static double getAverageTemp(@NotNull JsonElement json, int limit) {
        JsonArray forecasts = json.getAsJsonObject().getAsJsonArray("forecasts").getAsJsonArray();
        int day = 0, evening = 0, morning = 0, night = 0, dayCount = forecasts.size();
        if (limit > 0) {
            dayCount = limit;
        }
        for (int i = 0; i < dayCount; i++) {
            JsonObject forecast = forecasts.get(i).getAsJsonObject();
            JsonObject parts = forecast.getAsJsonObject("parts");
            day += parts.getAsJsonObject("day").get("temp_avg").getAsInt();
            evening += parts.getAsJsonObject("evening").get("temp_avg").getAsInt();
            morning += parts.getAsJsonObject("morning").get("temp_avg").getAsInt();
            night += parts.getAsJsonObject("night").get("temp_avg").getAsInt();
        }
        return (double) (day + evening + morning + night) / 4 / dayCount;
    }

    // gePrettyJson преобразует JSON в человеко-читаемый формат
    private static String gePrettyJson(JsonElement json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    // query делает HTTPS запрос к API службы погоды
    private static String query(String apiKey, double latitude, double longitude) throws InvalidQueryException {
        String url = "https://api.weather.yandex.ru/v2/forecast";
        if (latitude != -1 && longitude != -1) {
            url += "?lat=" + latitude + "&lon=" + longitude;
        }
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("X-Yandex-Weather-Key", apiKey)
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new InvalidQueryException(e.getMessage());
        }
        return switch (response.statusCode()) {
            case 200 -> response.body();
            case 403 -> throw new InvalidQueryException("invalid API key");
            default -> throw new InvalidQueryException(response.body());
        };
    }
}
