import java.util.ArrayList;
import java.util.List;

public class TaskManagerCSVFormatter {

    public static String getHeader() {
        return "ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC";
    }

    public static String toString(Task task) {
        return String.format("%s,%d,%s,%s,%s", Types.TASK, task.getId(), task.getName(),
                task.getStatus(), task.getDescription());
    }

    public static String toString(Subtask subtask) {
        return String.format("%s,%d,%s,%s,%s,%d", Types.SUBTASK, subtask.getId(),
                subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
    }

    public static String toString(Epic epic) {
        return String.format("%s,%d,%s,%s,%s", Types.EPIC, epic.getId(), epic.getName(),
                epic.getStatus(), epic.getDescription());
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

        Types type = Types.valueOf(record[0]);
        Integer id = Integer.valueOf(record[1]);
        String name = record[2];
        Status status = Status.valueOf(record[3]);
        String description = record[4];

        if (type.equals(Types.TASK)) {
            return new Task(type, id, name, status, description);
        } else if (type.equals(Types.EPIC)) {
            return new Epic(type, id, name, status, description, new ArrayList<>());
        } else {
            Integer epicId = Integer.parseInt(record[5]);
            return new Subtask(type, id, name, status, description, epicId);
        }
    }
}