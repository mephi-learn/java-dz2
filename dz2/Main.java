package dz2;

// Параметры командной строки:
// Первым параметром ВСЕГДА идёт API ключ. Можно указать его сразу, а можно передать путь к файлу, его содержащему (если не нужно светить его в истории команд)
// Третьим и четвёртым параметром идут широта и долгота. Необязательные параметры, если не указать, широта и долгота будет определены автоматически
// Третьим (или пятым, в зависимости от наличия широты и долготы) параметром идёт лимит дней. Необязательный параметр, по умолчанию считает все дни (0)

public class Main {
    public static void main(String[] args) {
        try {
            String apiKey = Arguments.GetKey(args);
            int limit = Arguments.GetLimit(args);
            double latitude = Arguments.GetLatitude(args);
            double longitude = Arguments.GetLongitude(args);
            Weather weather = API.Get(apiKey, limit, latitude, longitude);
            System.out.printf("Raw JSON:\n%s\n\n", weather.getPrettyJson());
            System.out.printf("Latitude: %.4f\n", weather.getLatitude());
            System.out.printf("Longitude: %.4f\n", weather.getLongitude());
            System.out.printf("Current temperature: %d\n", weather.getCurrentTemp());
            System.out.printf("Average temperature for %d day(s): %.2f\n", weather.getDays(), weather.getAverageTemp());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
