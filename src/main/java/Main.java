import managers.HttpTaskManager;
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
        Integer idForAction;

        KVServer kvServer = new KVServer();
        kvServer.start();

        URI uri = URI.create("http://localhost:8078");
        HttpTaskManager manager = Managers.getHttpTaskManager(uri);

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

                    manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1",null,
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



