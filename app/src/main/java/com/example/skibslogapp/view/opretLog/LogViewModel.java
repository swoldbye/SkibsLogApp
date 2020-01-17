package com.example.skibslogapp.view.opretLog;

import androidx.lifecycle.ViewModel;

public class LogViewModel extends ViewModel {
    private String noteTxt,
            windDirection, windSpeed,
            waterCurrentDirection;

    public LogViewModel() {
        reset();
    }

    public void reset() {
        noteTxt = "";
        windDirection = "";
        windSpeed = "";
        waterCurrentDirection = "";
    }
    public String getNoteTxt() {
        return noteTxt;
    }

    public void setNoteTxt(String noteTxt) {
        this.noteTxt = noteTxt;
    }
    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWaterCurrentDirection() {
        return this.waterCurrentDirection;
    }
    public void setWaterCurrentDirection(String waterCurrentDirection) {
        this.waterCurrentDirection = waterCurrentDirection;
    }
}
