package dk.via.sep4.cloud.Persistance;

import java.io.File;

public class SensorReading {
    private double temperature;
    private File file;
    private FileLog logger;

    public SensorReading(Double temperature)
    {
        this.temperature=temperature;
        this.file=new File("src/main/java/dk/via/sep4/cloud/Persistance/Logs.txt");
        this.logger=FileLog.getInstance(file);
        logReadings();
    }
    private void logReadings()
    {
        try
        {
            logger.log("Measured "+temperature+"°C");
        }
        catch(Exception E)
        {
            System.out.println(E.getMessage());
        }
    }

    public double getTemperature()
    {
        return temperature;
    }
    public void setTemperature(double temperature)
    {
        this.temperature=temperature;
    }
}
