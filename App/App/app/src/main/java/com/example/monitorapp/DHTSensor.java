package com.example.monitorapp;

public class DHTSensor {
    private double Humidity;
    private double Temperature;
    private long Gas;

    public DHTSensor() {
        this.Gas = 0;
        this.Temperature = 0;
        this.Gas = 0;
    }

    public DHTSensor(double humidity, double temperature, long gas) {
        Humidity = humidity;
        Temperature = temperature;
        Gas = gas;
    }

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public long getGas() {
        return Gas;
    }

    public void setGas(long gas) {
        Gas = gas;
    }
}
