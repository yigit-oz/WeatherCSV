import java.io.File;
import org.apache.commons.csv.*;
import edu.duke.*;

public class WeatherCSV {
    
    public CSVRecord hottestHourInFile(CSVParser parser) {
        CSVRecord max = null;
        for(CSVRecord record: parser) {
            max = getLargestOfTwo(record, max);
        }
        return max;
    }

    public void testhottestHourInFile(CSVParser parser) {
        FileResource fr = new FileResource();
        CSVParser cv = fr.getCSVParser();

        CSVRecord maxTemp = hottestHourInFile(cv);
        System.out.println("Hottest temperature was " + maxTemp.get("TemperatureF") + " at " + maxTemp.get("TimeEST"));

    }

    public CSVRecord fileWithHottestTemperature() {
        CSVRecord max = null;
        DirectoryResource dr = new DirectoryResource();
        for(File f: dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser cv = fr.getCSVParser();
            CSVRecord current = hottestHourInFile(cv);
            max = getLargestOfTwo(current, max);

        }
        return max;
    }

    public void testfileWithHottestTemperature() {
        CSVRecord hottestInManyDays = fileWithHottestTemperature();
        System.out.println("Hottest temperature was " + hottestInManyDays.get("TemperatureF") + " at " +
                hottestInManyDays.get("DateUTC"));
    }

    public CSVRecord getLargestOfTwo(CSVRecord current, CSVRecord max) {
        if (max == null) {
            max = current;
        } else {
            double currentTemp = Double.parseDouble(current.get("TemperatureF")),
                    maxTemp = Double.parseDouble(max.get("TemperatureF"));
            if (currentTemp > maxTemp) {
                max = current;
            }
        }
        return max;
    }

    public double averageTemperatureInFile(CSVParser parser) {
        double total = 0;
        int count = 0;
        for(CSVRecord record: parser) {
            String temperatureString = record.get("TemperatureF");
            double temperature = Double.parseDouble(temperatureString);
            total += temperature;
            count++;
        }
        return total/count;
    }

    public void testAverageTemperatureInFile() {
        FileResource fr = new FileResource();
        CSVParser cv = fr.getCSVParser();
        double answer = averageTemperatureInFile(cv);
        System.out.println("Average temperature in file is " + answer);
    }

    public CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord min = null;
        for(CSVRecord current: parser) {
            if (min == null) {
                min = current;
            } 
            else {
                double currentTemp = Double.parseDouble(current.get("TemperatureF")),
                        minTemp = Double.parseDouble(min.get("TemperatureF"));
                if (currentTemp < minTemp) {
                    min = current;
                }
            }
        }
        return min;
    }

    public void testcoldestHourInFile() {
        FileResource fr = new FileResource();
        CSVParser cv = fr.getCSVParser();

        CSVRecord minTemp = coldestHourInFile(cv);
        System.out.println("Coldest temperature was " + minTemp.get("TemperatureF") + " at " + minTemp.get("DateUTC"));
    }

    public String fileWithColdestTemperature() {
        CSVRecord min = null;
        String WantedFile = null;
        DirectoryResource dir = new DirectoryResource();
        for(File f: dir.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser cv = fr.getCSVParser();
            CSVRecord current = coldestHourInFile(cv);
            if (min == null) {
                min = current;
                WantedFile = f.getName();
            } 
            else {
                double currentTemp = Double.parseDouble(current.get("TemperatureF")),
                        minTemp = Double.parseDouble(min.get("TemperatureF"));
                if (currentTemp < minTemp && currentTemp != -9999) {
                    min = current;
                    WantedFile = f.getName();
                }
            }
        }
        return WantedFile; 
    }

    public void testfileWithColdestTemperature() {
        String coldestTemperatureDay = fileWithColdestTemperature();
        File fr = new File(coldestTemperatureDay);
        String year = coldestTemperatureDay.substring(8, 12);
        FileResource res = new FileResource("nc_weather/" + year + "/" + fr);
        CSVParser cv = res.getCSVParser();

        System.out.println("Coldest day was in file " + coldestTemperatureDay);
        System.out.println("Coldest temperature on that day was " + coldestHourInFile(cv).get("TemperatureF"));
        System.out.println("All the Temperatures on the coldest day were:");

        cv = res.getCSVParser();
        for(CSVRecord record: cv) {
            System.out.println(record.get("DateUTC") + ": " + record.get("TemperatureF"));
        }
    }

    public CSVRecord lowestHumidityInFile(CSVParser parser) {
        CSVRecord min = null;
        for(CSVRecord current: parser) {
            min = getMinimumOfTwo(current, min);
        }
        return min;
    }

    public void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was " + csv.get("Humidity") + " at " + csv.get("DateUTC"));
    }

    public CSVRecord getMinimumOfTwo(CSVRecord current, CSVRecord min) {
        if (min == null) {
            min = current;
        } 
        else {
            if (!current.get("Humidity").equals("N/A")) {
                double currentH = Double.parseDouble(current.get("Humidity")),
                        minH = Double.parseDouble(min.get("Humidity"));
                if (currentH < minH) {
                    min = current;
                }
            }
        }
        return min;
    }

    public CSVRecord lowestHumidityInManyFiles() {
        CSVRecord min = null;
        DirectoryResource dir = new DirectoryResource();
        for(File f: dir.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser cv = fr.getCSVParser();
            CSVRecord current = lowestHumidityInFile(cv);
            min = getMinimumOfTwo(current, min);
        }
        return min;
    }

    public void testLowestHumidityInManyFiles() {
        CSVRecord record = lowestHumidityInManyFiles();
        System.out.println("Lowest Humidity was " + record.get("Humidity") + " at " + record.get("DateUTC"));
    }

    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        int count = 0;
        double total = 0;
        for(CSVRecord record: parser) {
            if(!record.get("Humidity").equals("N/A")) {
                double humidity = Double.parseDouble(record.get("Humidity"));
                if(humidity >= value) {
                    double temperature = Double.parseDouble(record.get("TemperatureF"));
                    total += temperature;
                    count++;
                }
            }
        }
        if(count == 0) {
            return 0;
        }
        return total/count;
    }

    public void testAverageTemperatureWithHighHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser cv = fr.getCSVParser();
        double result = averageTemperatureWithHighHumidityInFile(cv, 80);
        if(result == 0) {
            System.out.println("No temperatures with that humidity");
        }
        else {
            System.out.println("Average Temp when high Humidity is " + result);
        }
    }

    public static void main(String[] args) {
        WeatherCSV test = new WeatherCSV();
        test.testLowestHumidityInManyFiles();
    }    
}
