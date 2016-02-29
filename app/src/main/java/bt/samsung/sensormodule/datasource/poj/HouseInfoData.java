package bt.samsung.sensormodule.datasource.poj;

/**
 * Created by xinyu.he on 2016/2/25.
 */
public class HouseInfoData {
    public String UV;
    public String Humidity;
    public String Temperature;

    public HouseInfoData(String uv, String humidity, String temperature) {
        this.UV = uv;
        this.Humidity = humidity;
        this.Temperature = temperature;
    }
}
