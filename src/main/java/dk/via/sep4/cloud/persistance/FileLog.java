package dk.via.sep4.cloud.persistance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FileLog
{
  private static final Map<File, FileLog> instances=new HashMap<File, FileLog>();
  private final CurrentTime currentTime;
  private final File logDirectory;
  private File logFile;

  private FileLog(File logFile) {
    String homePath = System.getProperty("user.home");
    // Default download dir on Windows. (~/Downloads on other systems).
    logDirectory = new File(homePath, "Downloads");
    currentTime=CurrentTime.getInstance();
    this.logFile=logFile;
  }
  public static synchronized FileLog getInstance(File logFile)
  {
    if(!instances.containsKey(logFile))
    {
      FileLog instance=new FileLog(logFile);
      instances.put(logFile, instance);
    }
    return instances.get(logFile);
  }
  private File getLogFile() {
    return logFile;
  }
  public void log(String message) throws IOException {
    try (FileWriter fileWriter = new FileWriter(getLogFile(), true);
        PrintWriter writer = new PrintWriter(fileWriter)) {
      String logLine = currentTime.getFormattedTime() + " - " + message;
      writer.println(logLine);
    }
  }
}