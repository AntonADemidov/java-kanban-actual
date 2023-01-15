import managers.HttpTaskManager;
import managers.TaskManager;
import servers.KVServer;
import tasks.*;
import utilities.Managers;
import utilities.Status;
import utilities.Types;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        KVServer kvServer = new KVServer();
        kvServer.start();

        URI uri = URI.create("http://localhost:8078");
        HttpTaskManager manager = Managers.getHttpTaskManager(uri);

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
                    manager = Managers.getHttpTaskManager(uri);
                    manager.load();
                    break;

                default:
                    System.out.println("Такой команды нет");
                    break;
            }
            printInfo();
            userCommand = Integer.parseInt(scanner.next());
        }
        kvServer.stop();
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

    private static void case13(TaskManager manager, Scanner scanner) throws URISyntaxException, IOException, InterruptedException {
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
        System.out.println(++counter + " - Восстановить ТРЕКЕР ЗАДАЧ c сервера KVServer.");

        System.out.println("0 - Завершить тесты.");
        System.out.print("ВВЕДИТЕ КОМАНДУ: ");
    }
}



