package lifeline.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lifeline.exception.LifelineException;
import lifeline.task.Deadline;
import lifeline.task.Event;
import lifeline.task.Task;
import lifeline.task.TaskList;
import lifeline.task.ToDo;


public class Storage {

    private String filepath;
    private Gson gson;

    public Storage(String filepath) {
        this.filepath = filepath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public void save(TaskList tasks) throws LifelineException {
        try {
            createDirectoryIfMissing();
            FileWriter fileWriter = new FileWriter(filepath);
            fileWriter.write(gson.toJson(tasks.getTaskList(), ArrayList.class));
            fileWriter.close();
        } catch (IOException e) {
            throw new LifelineException("Unable to save tasks at the moment");
        }
    }

    public TaskList load() throws LifelineException {
        try {
            FileReader fileReader = new FileReader(filepath);
            JsonArray arr = JsonParser.parseReader(fileReader).getAsJsonArray();
            TaskList savedTasks = new TaskList(new ArrayList<Task>());
            for (int i = 0; i < arr.size(); i++) {
                JsonObject currTask = arr.get(i).getAsJsonObject();
                if (currTask.has("by")) {
                    savedTasks.add(new Deadline(currTask.get("name").getAsString(),
                            gson.fromJson(currTask.get("by"), LocalDateTime.class),
                            currTask.get("isDone").getAsBoolean()));

                } else if (currTask.has("startTime")) {
                    savedTasks.add(new Event(currTask.get("name").getAsString(),
                            gson.fromJson(currTask.get("date"), LocalDate.class),
                            gson.fromJson(currTask.get("startTime"), LocalTime.class),
                            gson.fromJson(currTask.get("endTime"), LocalTime.class),
                            currTask.get("isDone").getAsBoolean()));
                } else {
                    savedTasks.add(new ToDo(currTask.get("name").getAsString(),
                            currTask.get("isDone").getAsBoolean()));
                }
            }
            fileReader.close();
            return savedTasks;
        } catch (IOException e) {
            throw new LifelineException("Unable to find your saved tasks!\n");
        }
    }

    private void createDirectoryIfMissing() {
        int indexOfLastFileSeparator = filepath.indexOf(File.separator);
        String directoryToSaveTo = filepath.substring(0, indexOfLastFileSeparator == -1 ? 0 : indexOfLastFileSeparator);
        File directory = new File(directoryToSaveTo);
        directory.mkdirs();
    }
}
