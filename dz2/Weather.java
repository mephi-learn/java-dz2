package dz2;

class Weather {
    int days;
    double latitude;
    double longitude;
    int currentTemp;
    double averageTemp;
    String prettyJson;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getDays() {
        return days;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public String getPrettyJson() {
        return prettyJson;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public void setPrettyJson(String prettyJson) {
        this.prettyJson = prettyJson;
    }

    public String toString() {
        return getClass().getName() + " @ Days: " + this.getDays() + ", Current temperature: " + this.getCurrentTemp() + ", Average temperature: " + this.getAverageTemp();
    }
}
