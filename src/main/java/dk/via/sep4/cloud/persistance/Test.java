package dk.via.sep4.cloud.persistance;

public class Test {
    public static void main(String[] args) {
        SensorReading reading1=new SensorReading(25.0);
        System.out.println(reading1.getTemperature());
    }
}
