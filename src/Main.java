import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ManagerSaveException {
        Scanner scanner = new Scanner(System.in);
        File currentStatus = new File("src/CurrentStatus.csv");
        TaskManager manager = Managers.getNewManager(currentStatus);

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
                            manager = Managers.getSavedManager(currentStatus);
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
}