import java.util.Arrays;
import java.util.Objects;

public class TaskTracker {
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
        System.out.println("COMMANDS\tDESCRIPTION");
        System.out.println("add <Todo> - \tadds new item to the list");
        System.out.println("update <ID> <Todo Text> -\tupdates item with specified ID");
        System.out.println("delete <ID> - \tdeletes item with specified ID");
        System.out.println("list <todo | in_progress | done>? - \tlists todo based on specified filter.");
        System.out.println("mark-in_progress <ID> - \tmarks item with specified ID as in_progress");
        System.out.println("mark-done <ID> - \tmarks item with specified ID as done");
        System.out.println("help - \tdisplays list of available commands");
    }
}
