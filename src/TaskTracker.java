import java.util.Arrays;
import java.util.Objects;

public class TaskTracker {
    static String[] commands = {
            "add <Todo>",
            "update <ID> <Todo Text>",
            "delete <ID>",
            "list <todo | in_progress | done>?",
            "mark-in_progress <ID>",
            "mark-done <ID>",
            "help"
    };
    static String[] descriptions = {
            "adds new item to the list",
            "updates item with specified ID",
            "deletes item with specified ID",
            "lists todo based on specified filter.",
            "marks item with specified ID as in_progress",
            "marks item with specified ID as done",
            "displays list of available commands",
    };

    public static void main(String[] args) {
        if (args.length == 0) {
            showAllCommands();
        } else {
            String userProfile = System.getenv("USERPROFILE");
            TaskList list = new TaskList(userProfile + "/todos.json");

            // load the list
            list.load();

            String keyword = args[0].trim();

            int id;
            String text;

            switch (keyword) {
                case "add":
                    addNewItem(args, list);
                    break;
                case "update":
                    updateItem(args, list);
                    break;
                case "list":
                    displayList(args, list);
                    break;
                case "delete":
                    deleteItem(args, list);
                    break;
                case "mark-in_progress":
                    updateItemStatus(args, list, TaskStatus.in_progress);
                    break;
                case "mark-done":
                    updateItemStatus(args, list, TaskStatus.done);
                    break;
                case "help":
                    showAllCommands();
                    break;
                default:
                    System.out.println("Invalid argument at pos 0");
                    System.out.println("Try using help");
                    break;
            }
        }
    }

    private static void addNewItem(String[] args, TaskList list) {
        String text = args[1].trim();
        Task newTask = list.addNewTask(text);
        System.out.println("Added new task with ID (" + newTask.id + ")");
    }

    private static void updateItem(String[] args, TaskList list) {
        int id = Integer.parseInt(args[1]);
        String text = args[2];

        Task updatedTask = list.updateTask(id, text);
        System.out.println("Updated task with ID (" + updatedTask.id + ")");
    }

    private static void deleteItem(String[] args, TaskList list) {
        int id = Integer.parseInt(args[1]);
        Task deletedTask = list.deleteTask(id);
        System.out.println("Deleted task with ID (" + deletedTask.id + ")");
    }

    private static void displayList(String[] args, TaskList list) {
        if (args.length == 1) {
            list.list();
        } else {
            String filter = args[1];

            try {
                TaskStatus validFilter = TaskStatus.valueOf(filter);
                list.list(validFilter);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid argument at pos 1");
                System.out.println("Should be one of \n\ttodo\n\tin_progress\n\tdone");
            }
        }
    }

    private static void updateItemStatus(String[] args, TaskList list, TaskStatus status) {
        int id = Integer.parseInt(args[1]);
        list.updateTask(id, status);
    }

    private static void showAllCommands() {
        int commandMaxWidth = lenOfLargestString(commands);
        int descriptionMaxWidth = lenOfLargestString(descriptions);

        System.out.printf("%-" + commandMaxWidth + "s \t" + "%-" + descriptionMaxWidth + "s\n", "COMMANDS", "DESCRIPTION");

        for (int i = 0; i < commands.length; i++) {
            System.out.printf("%-" + commandMaxWidth + "s \t" + "%-" + descriptionMaxWidth + "s\n", commands[i], descriptions[i]);
        }
    }

    public static int lenOfLargestString(String[] arr) {
        int largest = arr[0].length();
        int len;

        for (int i = 0; i < arr.length; i++) {
            len = arr[i].length();

            if (len > largest) {
                largest = len;
            }
        }

        return largest;
    }
}
