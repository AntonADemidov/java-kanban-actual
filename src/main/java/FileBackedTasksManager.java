import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File currentStatus = new File("src/main/resources/CurrentStatus.csv");
        TaskManager manager = Managers.getNewFileBackedTasksManager(currentStatus);
        Integer idForAction;

        printInfo();
        int userCommand = Integer.parseInt(scanner.next());

        while (userCommand != 0) {
            switch (userCommand) {
                case 1:
                    manager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                            LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                            Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));

                    manager.createTask(new Task(null, Types.TASK,"Покупки", Status.NEW,
                            LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                            Duration.ofMinutes(45),"молоко; кофе; яйца"));
                    break;

                case 2:
                    if (manager.getTasks().size() != 0) {
                        System.out.print("Введите id задачи для обновления: ");
                        idForAction = Integer.parseInt(scanner.next());

                        if ((manager.getTasks().containsKey(idForAction)) && (manager.getTasks().get(idForAction) != null)) {
                            manager.updateTask(new Task(null, Types.TASK,"Покупки", Status.DONE,
                                    LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                                    Duration.ofMinutes(60),"молоко; кофе; яйца; хлеб"), idForAction);
                        } else {
                            System.out.printf("Задача с id # %d не существует.%n", idForAction);
                        }
                    } else {
                        System.out.println("Список задач пуст => нечего обновлять.");
                    }
                    break;

                case 3:
                    System.out.print("Введите id задачи для отображения: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getTasks().containsKey(idForAction)) {
                        manager.printTask(idForAction);
                    } else {
                        System.out.printf("Задача с id # %d не существует.%n", idForAction);
                    }
                    break;

                case 4:
                    System.out.print("Введите id задачи для удаления: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getTasks().containsKey(idForAction)) {
                        manager.deleteTask(idForAction);
                    } else {
                        System.out.printf("Задача с id # %d не существует.%n", idForAction);
                    }
                    break;

                case 5:
                    manager.printTaskList();
                    break;

                case 6:
                    manager.clearTaskList();
                    break;

                case 7:
                    manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                                    null,null, null,"спринт #1; спринт #2", null),
                            new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                                    LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                                    Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                            new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                                    LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                                    Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

                    manager.createEpic(new Epic(null,Types.EPIC,"Java developer. Модуль #1",null,
                                    null,null,null,"спринт #3", null),
                            new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.IN_PROGRESS,
                                    LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                                    Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"));
                    break;

                case 8:
                    System.out.print("Введите id эпика для обновления: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getEpics().containsKey(idForAction)) {
                        manager.updateEpic(new Epic(null, Types.EPIC, "Java developer. Модуль #1", null,
                                        null,null,null,
                                        "спринт #3; спринт #4", null),
                                new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.DONE,
                                        LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                                        Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"),
                                new Subtask(null, null, Types.SUBTASK,"Спринт #4", Status.NEW,
                                        LocalDateTime.of(2022, Month.DECEMBER, 19, 10, 00),
                                        Duration.ofMinutes(7200),"пройти теорию; доработать трекер задач"),
                                idForAction);
                    } else {
                        System.out.printf("Эпик c id # %d не существует.%n", idForAction);
                    }
                    break;

                case 9:
                    System.out.print("Введите id эпика для отображения: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getEpics().containsKey(idForAction)) {
                        manager.printEpic(idForAction);
                    } else {
                        System.out.printf("Эпик с id # %d не существует.%n", idForAction);
                    }
                    break;

                case 10:
                    System.out.print("Введите id эпика для удаления: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getEpics().containsKey(idForAction)) {
                        manager.deleteEpic(idForAction);
                    } else {
                        System.out.printf("Эпик c id # %d не существует.%n", idForAction);
                    }
                    break;

                case 11:
                    manager.printEpicList();
                    break;

                case 12:
                    manager.clearEpicList();
                    break;

                case 13:
                    System.out.print("Введите id эпика: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if ((manager.getEpics().get(idForAction) != null) &&
                            (manager.getEpics().get(idForAction).getSubtaskIds() != null)) {
                        manager.printEpicSubtaskList(idForAction);
                    } else {
                        System.out.println("Список подзадач пуст.");
                    }
                    break;

                case 14:
                    System.out.print("Введите id эпика для добавления новой подзадачи: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getEpics().get(idForAction) != null) {
                        manager.createSubtask(new Subtask(null, null, Types.SUBTASK,"Спринт #5", Status.NEW,
                                LocalDateTime.of(2022, Month.DECEMBER, 26, 10, 00),
                                Duration.ofMinutes(7200),"пройти теорию; снова доработать трекер задач"), idForAction);
                    } else {
                        System.out.printf("Эпик с id # %d не существует.%n", idForAction);
                    }
                    break;

                case 15:
                    System.out.print("Введите id эпика: ");
                    Integer epicIdForAction = Integer.parseInt(scanner.next());

                    System.out.print("Введите id задачи для обновления: ");
                    Integer subtaskIdForAction = Integer.parseInt(scanner.next());

                    if ((manager.getEpics().get(epicIdForAction) != null) &&
                            (manager.getSubtasks().get(subtaskIdForAction) != null)) {
                        manager.updateSubtask(new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.DONE,
                                LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                                Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"),
                                epicIdForAction, subtaskIdForAction);
                    } else {
                        System.out.println("Указанные эпик или подзадача не существуют.");
                    }
                    break;

                case 16:
                    System.out.print("Введите id позадачи для отображения: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getSubtasks().containsKey(idForAction)) {
                        manager.printSubtask(idForAction);
                    } else {
                        System.out.printf("Задача с id # %d не существует.%n", idForAction);
                    }
                    break;

                case 17:
                    System.out.print("Введите id подзадачи для удаления: ");
                    idForAction = Integer.parseInt(scanner.next());

                    if (manager.getSubtasks().containsKey(idForAction)) {
                        manager.deleteSubtask(idForAction);
                    } else {
                        System.out.printf("Задача с id # %d не существует.%n", idForAction);
                    }
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
                    manager.getPrioritizedTasks();
                    break;

                case 22:
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
        System.out.println(++counter + " - Просмотреть задачу по идентификатору.");
        System.out.println(++counter + " - Удалить задачу по идентификатору.");
        System.out.println(++counter + " - Просмотреть список всех задач.");
        System.out.println(++counter + " - Удалить все задачи.");

        System.out.println(++counter + " - Создать эпики.");
        System.out.println(++counter + " - Обновить эпик.");
        System.out.println(++counter + " - Просмотреть эпик по идентификатору.");
        System.out.println(++counter + " - Удалить эпик по идентификатору.");
        System.out.println(++counter + " - Просмотреть список всех эпиков.");
        System.out.println(++counter + " - Удалить все эпики.");
        System.out.println(++counter + " - Просмотреть список подзадач эпика по идентификатору.");

        System.out.println(++counter + " - Создать новую подзадачу эпика.");
        System.out.println(++counter + " - Обновить подзадачу эпика.");
        System.out.println(++counter + " - Просмотреть подзадачу по идентификатору.");
        System.out.println(++counter + " - Удалить подзадачу по идентификатору.");
        System.out.println(++counter + " - Просмотреть список всех подзадач.");
        System.out.println(++counter + " - Удалить все подзадачи.");

        System.out.println(++counter + " - Просмотреть историю просмотров задач, подзадач и эпиков.");
        System.out.println(++counter + " - Просмотреть список всех задач, подзадач и эпиков в порядке приоритета.");
        System.out.println(++counter + " - Восстановить ТРЕКЕР ЗАДАЧ из файла.");

        System.out.println("0 - Завершить тесты.");
        System.out.print("ВВЕДИТЕ КОМАНДУ: ");
    }

    public static FileBackedTasksManager loadFromFIle(File file) throws IOException, ManagerSaveException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        String csv = Files.readString(Path.of(String.valueOf(file)));
        String[] lines = csv.split("\n");
        List<Integer> history = Collections.emptyList();
        int idCounter = 0;

        for (int i = 1; i < (lines.length); i++) {
            String line = lines[i];

            if (line.isEmpty()) {
                history = TaskManagerCSVFormatter.createHistoryFromString(lines[i + 1]);
                break;
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
                        br.write(TaskManagerCSVFormatter.toString(subtask) +empty);
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
        super.getPrioritizedList().add(task);
        save();
    }

    private void loadEpic(Epic epic) {
        super.getEpics().put(epic.getId(), epic);
        save();
    }

    private void loadSubtask(Subtask subtask) {
        super.getSubtasks().put(subtask.getId(), subtask);
        super.getPrioritizedList().add(subtask);
        save();
    }

    @Override
    public void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int updateTask(Task task, Integer idForAction) {
        super.updateTask(task, idForAction);
        save();
        return task.getId();
    }

    @Override
    public void printTask(Integer idForAction) {
        super.printTask(idForAction);
        save();
    }

    @Override
    public void deleteTask(Integer idForAction) {
        super.deleteTask(idForAction);
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
    public int createEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        super.createEpic(epic, subtask1, subtask2);
        save();
        return epic.getId();
    }

    @Override
    public int createEpic(Epic epic, Subtask subtask) {
        super.createEpic(epic, subtask);
        save();
        return epic.getId();
    }

    @Override
    public int updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction) {
        super.updateEpic(epic, subtask1, subtask2, idForAction);
        save();
        return epic.getId();
    }

    @Override
    public void printEpic(Integer idForAction) {
        super.printEpic(idForAction);
        save();
    }

    @Override
    public void deleteEpic(Integer idForAction) {
        super.deleteEpic(idForAction);
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
    public void printEpicSubtaskList(Integer idForAction) {
        super.printEpicSubtaskList(idForAction);
        save();
    }

    @Override
    public int createSubtask(Subtask subtask, Integer idForAction) {
        super.createSubtask(subtask, idForAction);
        save();
        return subtask.getId();
    }

    @Override
    public int updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction) {
        super.updateSubtask(subtask, epicIdForAction, subtaskIdForAction);
        save();
        return subtask.getId();
    }

    @Override
    public void printSubtask(Integer idForAction) {
        super.printSubtask(idForAction);
        save();
    }

    @Override
    public void deleteSubtask(Integer idForAction) {
        super.deleteSubtask(idForAction);
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