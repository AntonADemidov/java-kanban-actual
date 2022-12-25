import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerCSVFormatter {

    public static String getHeader() {
        return "ID,TYPE,NAME,STATUS,START TIME,DURATION,END TIME,DESCRIPTION,EPIC";
    }

    public static String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", task.getId(), Types.TASK, task.getName(),
                task.getStatus(), task.getStartTime(), task.getDuration(), task.getEndTime(), task.getDescription());
    }

    public static String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%d", subtask.getId(), Types.SUBTASK, subtask.getName(),
                subtask.getStatus(), subtask.getStartTime(), subtask.getDuration(), subtask.getEndTime(),
                subtask.getDescription(), subtask.getEpicId());
    }

    public static String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s", epic.getId(), Types.EPIC, epic.getName(), epic.getStatus(),
                epic.getStartTime(), epic.getDuration(), epic.getEndTime(), epic.getDescription());
    }

    public static String toString(HistoryManager historyManager) {
        List<String> historyList = new ArrayList<>();

        for (Task element : historyManager.getHistory()) {
            String id = String.format("%s", element.getId());
            historyList.add(id);
        }

        return String.join(",", historyList);
    }

    public static List<Integer> createHistoryFromString (String line) {
        List<Integer> historyList = new ArrayList<>();
        String[] history = line.split(",");

        for (String piece: history) {
            Integer id = Integer.parseInt(piece);
            historyList.add(id);
        }
        return historyList;
    }

    public static Task fromString(String line) {
        String[] record = line.split(",");

        Integer id = Integer.valueOf(record[0]);
        Types type = Types.valueOf(record[1]);
        String name = record[2];
        Status status = Status.valueOf(record[3]);

        LocalDateTime startTime;
        String time = record[4];
        if (time.equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(record[4]);
        }

        Duration duration;
        time = record[5];
        if (time.equals("null")) {
            duration = null;
        } else {
            duration = Duration.parse(record[5]);
        }

        LocalDateTime endTime;
        time = record[6];
        if (time.equals("null")) {
            endTime = null;
        } else {
            endTime = LocalDateTime.parse(record[6]);
        }

        String description = record[7];

        if (type.equals(Types.TASK)) {
            return new Task(id, type, name, status, startTime, duration, description);
        } else if (type.equals(Types.EPIC)) {
            return new Epic(id, type, name, status, startTime, duration, endTime, description, new ArrayList<>());
        } else {
            Integer epicId = Integer.parseInt(record[8]);
            return new Subtask(id, epicId, type, name, status, startTime, duration, description);
        }
    }
}