package rchat.info.containters.weather;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Weather {
    public String cityName;
    public Date updateTime;
    public WeatherPropCurrent current;
    public List<WeatherPropHourly> hourly;
    public List<WeatherPropDaily> daily;

    public Weather(String cityName, Date updateTime) {
        this.cityName = cityName;
        this.updateTime = updateTime;
        hourly = new ArrayList<>();
        daily = new ArrayList<>();
    }

    public void setCurrent(WeatherPropCurrent current){
        this.current = current;
    }

    public void addHourly(WeatherPropHourly hourly){
        this.hourly.add(hourly);
    }

    public void addDaily(WeatherPropDaily daily){
        this.daily.add(daily);
    }
}
