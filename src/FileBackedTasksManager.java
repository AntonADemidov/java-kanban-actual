import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File currentStatus = new File("src/CurrentStatus.csv");
        TaskManager manager = Managers.getNewFileBackedTasksManager(currentStatus);

        printInfo();
        int userCommand = Integer.parseInt(scanner.next());

        while (userCommand != 0) {
            switch (userCommand) {
                case 1:
                    manager.createTask(new Task(Types.TASK, null,"Переезд", Status.NEW,
                            "cобрать коробки; упаковать кошку; сказать слова прощания"));

                    manager.createTask(new Task(Types.TASK, null, "Покупки", Status.NEW,
                            "молоко; кофе; яйца"));
                    break;

                case 2:
                    manager.updateTask(new Task(Types.TASK,null, "Покупки", Status.DONE,
                            "молоко; кофе; яйца; хлеб"));
                    break;

                case 3:
                    manager.printTask();
                    break;

                case 4:
                    manager.deleteTask();
                    break;

                case 5:
                    manager.printTaskList();
                    break;

                case 6:
                    manager.clearTaskList();
                    break;

                case 7:
                    manager.createEpic(new Epic(Types.EPIC,null, "Java developer. Модуль #1", null,
                                    "cпринт #2; cпринт #3", new ArrayList<>()),
                            new Subtask(Types.SUBTASK,null, "Спринт #1", Status.DONE,
                                    "пройти теорию; создать счетчик калорий", null),
                            new Subtask(Types.SUBTASK,null, "Спринт #2", Status.DONE,
                                    "пройти теорию; автоматизировать бухгалтерию", null));

                    manager.createEpic(new Epic(Types.EPIC,null, "Java developer. Модуль #1", null,
                                    "cпринт #3", new ArrayList<>()),
                            new Subtask(Types.SUBTASK,null, "Спринт #3", Status.IN_PROGRESS,
                                    "пройти теорию; создать трекер задач", null));
                    break;

                case 8:
                    manager.updateEpic(new Epic(Types.EPIC,null, "Java developer. Модуль #1", null,
                                    "cпринт #1; cпринт #2; cпринт #3; cпринт #4", new ArrayList<>()),
                            new Subtask(Types.SUBTASK,null, "Спринт #3", Status.IN_PROGRESS,
                                    "пройти теорию; создать трекер задач", null),
                            new Subtask(Types.SUBTASK,null, "Спринт #4", Status.NEW,
                                    "пройти теорию; доработать трекер задач", null));
                    break;

                case 9:
                    manager.printEpic();
                    break;

                case 10:
                    manager.deleteEpic();
                    break;

                case 11:
                    manager.printEpicList();
                    break;

                case 12:
                    manager.clearEpicList();
                    break;

                case 13:
                    manager.printEpicSubtaskList();
                    break;

                case 14:
                    manager.createSubtask(new Subtask(Types.SUBTASK,null, "Спринт #5", Status.NEW,
                            "пройти теорию; снова доработать трекер задач", null));
                    break;

                case 15:
                    manager.updateSubtask(new Subtask(Types.SUBTASK,null, "Спринт #3", Status.DONE,
                            "пройти теорию; создать трекер задач", null));
                    break;

                case 16:
                    manager.printSubtask();
                    break;

                case 17:
                    manager.deleteSubtask();
                    break;

                case 18:
                    manager.printSubtaskList();
                    break;

                case 19:
                    manager.clearSubtaskList();
                    break;

                case 20:
                    manager.getHistory();
                    break;

                case 21:
                    try {
                        if (currentStatus == null) {
                            throw new ManagerSaveException("Объект класса File для сохранения информации не инициализирован.");
                        } else {
                            manager = Managers.getSavedFileBackedTasksManager(currentStatus);
                        }
                    } catch (ManagerSaveException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Произошла неизвестная проверяемая ошибка ввода-вывода класса IOException.");
                        e.printStackTrace();
                    }
                    break;

                default:
                    System.out.println("Такой команды нет");
                    break;
            }
            printInfo();
            userCommand = Integer.parseInt(scanner.next());
        }
    }
    public static void printInfo() {
        int counter = 0;

        System.out.println("\n");
        System.out.println("ЗАПУСК ТЕСТОВОГО БЛОКА - ДОСТУПНЫЕ ДЕЙСТВИЯ:");
        System.out.println(++counter + " - Создать задачи.");
        System.out.println(++counter + " - Обновить задачу.");
        System.out.println(++counter + " - Посмотреть задачу по идентификатору.");
        System.out.println(++counter + " - Удалить задачу по идентификатору.");
        System.out.println(++counter + " - Посмотреть список всех задач.");
        System.out.println(++counter + " - Удалить все задачи.");

        System.out.println(++counter + " - Создать эпики.");
        System.out.println(++counter + " - Обновить эпик.");
        System.out.println(++counter + " - Посмотреть эпик по идентификатору.");
        System.out.println(++counter + " - Удалить эпик по идентификатору.");
        System.out.println(++counter + " - Посмотреть список всех эпиков.");
        System.out.println(++counter + " - Удалить все эпики.");
        System.out.println(++counter + " - Посмотреть список подзадач эпика по идентификатору.");

        System.out.println(++counter + " - Создать новую подзадачу эпика.");
        System.out.println(++counter + " - Обновить подзадачу эпика.");
        System.out.println(++counter + " - Посмотреть подзадачу по идентификатору.");
        System.out.println(++counter + " - Удалить подзадачу по идентификатору.");
        System.out.println(++counter + " - Посмотреть список всех подзадач.");
        System.out.println(++counter + " - Удалить все подзадачи.");
        System.out.println(++counter + " - Посмотреть историю просмотров задач, подзадач и эпиков");
        System.out.println(++counter + " - Восстановить ТРЕКЕР ЗАДАЧ из файла");

        System.out.println("0 - Завершить тесты.");
        System.out.print("ВВЕДИТЕ КОМАНДУ: ");
    }

    public static FileBackedTasksManager loadFromFIle(File file) throws IOException, ManagerSaveException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        String csv = Files.readString(Path.of(String.valueOf(file)));
        String[] lines = csv.split("\n");
        List<Integer> history = Collections.emptyList();
        int idCounter = 0;

        for (int i = 1; i < (lines.length - 1); i++) {
            String line = lines[i];

            if (line.isEmpty()) {
                history = TaskManagerCSVFormatter.createHistoryFromString(lines[i + 1]);
            } else {
                Task task = TaskManagerCSVFormatter.fromString(line);
                int id = task.getId();

                if (id > idCounter) {
                    idCounter = id;
                }

                if (task.getType().equals(Types.TASK)) {
                    tasksManager.loadTask(task);
                } else if (task.getType().equals(Types.EPIC)) {
                    tasksManager.loadEpic((Epic) task);
                } else {
                    tasksManager.loadSubtask((Subtask) task);
                }
            }
        }

        for (Map.Entry<Integer, Subtask> entry : tasksManager.getSubtasks().entrySet()) {
            Subtask subtask = entry.getValue();
            Epic epic = tasksManager.getEpics().get(subtask.getEpicId());
            epic.getSubtaskIds().add(subtask.getId());
        }

        for (Integer taskId : history) {
            tasksManager.getHistoryManager().add(tasksManager.findTask(taskId, tasksManager));
            tasksManager.save();
        }

        tasksManager.setIdCounter(idCounter);
        System.out.println("СТАТУС ТРЕКЕРА ВОССТАНОВЛЕН ИЗ ФАЙЛА.");
        return tasksManager;
    }

    private Task findTask (Integer id, FileBackedTasksManager tasksManager) {
        if (tasksManager.getTasks().get(id) != null) {
            return tasksManager.getTasks().get(id);
        } else if (tasksManager.getSubtasks().get(id) != null) {
            return tasksManager.getSubtasks().get(id);
        } else {
            return tasksManager.getEpics().get(id);
        }
    }

    private void save() {
        try {
            if (file == null) {
                throw new ManagerSaveException("Объект класса File для сохранения информации не инициализирован");
            } else {
                try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                    br.write(TaskManagerCSVFormatter.getHeader() + empty);

                    for (Task task : super.getTasks().values()) {
                        br.write(TaskManagerCSVFormatter.toString(task) + empty);
                    }

                    for (Epic epic : super.getEpics().values()) {
                        br.write(TaskManagerCSVFormatter.toString(epic) + empty);
                    }

                    for (Subtask subtask : super.getSubtasks().values()) {
                        br.write(TaskManagerCSVFormatter.toString(subtask) + empty);
                    }
                    br.write("" + empty);
                    br.write(TaskManagerCSVFormatter.toString(super.getHistoryManager()));
                } catch (IOException e) {
                    System.out.println("Произошла неизвестная проверяемая ошибка ввода-вывода класса IOException");
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    System.out.println("Произошла неизвестная непроверяемая ошибка класса RuntimeException");
                    e.printStackTrace();
                }
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadTask(Task task) {
        super.getTasks().put(task.getId(), task);
        save();
    }

    private void loadEpic(Epic epic) {
        super.getEpics().put(epic.getId(), epic);
        save();
    }

    private void loadSubtask(Subtask subtask) {
        super.getSubtasks().put(subtask.getId(), subtask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void printTask() {
        super.printTask();
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void printTaskList() {
        super.printTaskList();
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        super.createEpic(epic, subtask1, subtask2);
        save();
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask) {
        super.createEpic(epic, subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        super.updateEpic(epic, subtask1, subtask2);
        save();
    }

    @Override
    public void printEpic() {
        super.printEpic();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void printEpicList() {
        super.printEpicList();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void printEpicSubtaskList() {
        super.printEpicSubtaskList();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void printSubtask() {
        super.printSubtask();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public void printSubtaskList() {
        super.printSubtaskList();
    }

    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void getHistory() {
        super.getHistory();
    }
}