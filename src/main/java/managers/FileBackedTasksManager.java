package managers;

import tasks.*;
import utilities.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager(){
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        File currentStatus = new File("src/main/resources/CurrentStatus.csv");
        TaskManager manager = Managers.getNewFileBackedTasksManager(currentStatus);

        printInfo();
        int userCommand = Integer.parseInt(scanner.next());

        while (userCommand != 0) {
            switch (userCommand) {
                case 1:
                    case1(manager);
                    break;

                case 2:
                    case2(manager, scanner);
                    break;

                case 3:
                    case3(manager, scanner);
                    break;

                case 4:
                    case4(manager, scanner);
                    break;

                case 5:
                    manager.printTaskList();
                    break;

                case 6:
                    manager.clearTaskList();
                    break;

                case 7:
                    case7(manager);
                    break;

                case 8:
                    case8(manager, scanner);
                    break;

                case 9:
                    case9(manager, scanner);
                    break;

                case 10:
                    case10(manager, scanner);
                    break;

                case 11:
                    manager.printEpicList();
                    break;

                case 12:
                    manager.clearEpicList();
                    break;

                case 13:
                    case13(manager, scanner);
                    break;

                case 14:
                    case14(manager, scanner);
                    break;

                case 15:
                    case15(manager, scanner);
                    break;

                case 16:
                    case16(manager, scanner);
                    break;

                case 17:
                    case17(manager, scanner);
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
                    manager = case22(currentStatus);
                    break;

                default:
                    System.out.println("Такой команды нет");
                    break;
            }
            printInfo();
            userCommand = Integer.parseInt(scanner.next());
        }
    }

    private static void case1(TaskManager manager) throws URISyntaxException, IOException, InterruptedException {
        manager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));

        manager.createTask(new Task(null, Types.TASK,"Покупки", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                Duration.ofMinutes(45),"молоко; кофе; яйца"));
    }

    private static void case2(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        if (manager.getTasks().size() != 0) {
            System.out.print("Введите id задачи для обновления: ");
            Integer idForAction = Integer.parseInt(scanner.next());

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
    }

    private static void case3(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id задачи для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getTasks().containsKey(idForAction)) {
            manager.printTask(idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    private static void case4(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id задачи для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getTasks().containsKey(idForAction)) {
            manager.deleteTask(idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    private static void case7(TaskManager manager) throws URISyntaxException, IOException, InterruptedException {
        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"спринт #1; спринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1",null,
                        null,null,null,"спринт #3", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.IN_PROGRESS,
                        LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"));
    }

    private static void case8(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id эпика для обновления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getEpics().containsKey(idForAction)) {
            manager.updateEpic(new Epic(null, Types.EPIC, "Java developer. Модуль #1", null,
                            null,null,null,
                            "спринт #3; спринт #4", null),
                    new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.DONE,
                            LocalDateTime.of(2022, Month.DECEMBER, 19, 10, 00),
                            Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"),
                    new Subtask(null, null, Types.SUBTASK,"Спринт #4", Status.NEW,
                            LocalDateTime.of(2022, Month.DECEMBER, 26, 10, 00),
                            Duration.ofMinutes(7200),"пройти теорию; доработать трекер задач"),
                    idForAction);
        } else {
            System.out.printf("Эпик c id # %d не существует.%n", idForAction);
        }
    }

    private static void case9(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id эпика для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getEpics().containsKey(idForAction)) {
            manager.printEpic(idForAction);
        } else {
            System.out.printf("Эпик с id # %d не существует.%n", idForAction);
        }
    }

    private static void case10(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id эпика для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getEpics().containsKey(idForAction)) {
            manager.deleteEpic(idForAction);
        } else {
            System.out.printf("Эпик c id # %d не существует.%n", idForAction);
        }
    }

    private static void case13(TaskManager manager, Scanner scanner) {
        System.out.print("Введите id эпика: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if ((manager.getEpics().get(idForAction) != null) &&
                (manager.getEpics().get(idForAction).getSubtaskIds() != null)) {
            manager.printEpicSubtaskList(idForAction);
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    private static void case14(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id эпика для добавления новой подзадачи: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getEpics().get(idForAction) != null) {
            manager.createSubtask(new Subtask(null, null, Types.SUBTASK,"Спринт #5", Status.NEW,
                    LocalDateTime.of(2022, Month.DECEMBER, 26, 10, 00),
                    Duration.ofMinutes(7200),"пройти теорию; снова доработать трекер задач"), idForAction);
        } else {
            System.out.printf("Эпик с id # %d не существует.%n", idForAction);
        }
    }

    private static void case15(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
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
    }

    private static void case16(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id позадачи для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getSubtasks().containsKey(idForAction)) {
            manager.printSubtask(idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    private static void case17(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Введите id подзадачи для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (manager.getSubtasks().containsKey(idForAction)) {
            manager.deleteSubtask(idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    private static TaskManager case22(File currentStatus) throws ManagerSaveException, IOException {
        try {
            if (currentStatus == null) {
                throw new ManagerSaveException("Объект класса File для сохранения информации не инициализирован.");
            } else {
                TaskManager manager = Managers.getSavedFileBackedTasksManager(currentStatus);
                return manager;
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Произошла неизвестная проверяемая ошибка ввода-вывода класса IOException.");
            e.printStackTrace();
            return null;
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
    public boolean createTask(Task task) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createTask(task);
        if (successCreation){
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateTask(Task task, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateTask(task, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printTask(idForAction);
        save();
    }

    @Override
    public void deleteTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteTask(idForAction);
        save();
    }

    @Override
    public void printTaskList() {
        super.printTaskList();
    }

    @Override
    public void clearTaskList() throws URISyntaxException, IOException, InterruptedException {
        super.clearTaskList();
        save();
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask1, Subtask subtask2) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createEpic(epic, subtask1, subtask2);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createEpic(epic, subtask);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateEpic(epic, subtask1, subtask2, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printEpic(idForAction);
        save();
    }

    @Override
    public void deleteEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteEpic(idForAction);
        save();
    }

    @Override
    public void printEpicList() {
        super.printEpicList();
    }

    @Override
    public void clearEpicList() throws URISyntaxException, IOException, InterruptedException {
        super.clearEpicList();
        save();
    }

    @Override
    public void printEpicSubtaskList(Integer idForAction) {
        super.printEpicSubtaskList(idForAction);
    }

    @Override
    public boolean createSubtask(Subtask subtask, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createSubtask(subtask, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateSubtask(subtask, epicIdForAction, subtaskIdForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printSubtask(idForAction);
        save();
    }

    @Override
    public void deleteSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteSubtask(idForAction);
        save();
    }

    @Override
    public void printSubtaskList() {
        super.printSubtaskList();
    }

    @Override
    public void clearSubtaskList() throws URISyntaxException, IOException, InterruptedException {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void getHistory() {
        super.getHistory();
    }

    @Override
    public void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }
}