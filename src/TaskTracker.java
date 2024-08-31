import java.util.Arrays;
import java.util.Objects;

public class TaskTracker {
    public static void main(String[] args) {
        // initial task list
        String userProfile = System.getenv("USERPROFILE");
        TaskList list = new TaskList(userProfile + "/todos.json");

        list.load();

        String keyword = args[0].trim();

        int id;
        String text;

        switch (keyword) {
            case "add":
                text = args[1].trim();
                Task newTask = list.addNewTask(text);
                System.out.println("Added new task with ID (" + newTask.id + ")");
                break;
            case "update":
                id = Integer.parseInt(args[1]);
                text = args[2];

                Task updatedTask = list.updateTask(id, text);
                System.out.println("Updated task with ID (" + updatedTask.id + ")");
                break;
            case "list":
                if (args.length <= 1) {
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
                break;
            case "delete":
                id = Integer.parseInt(args[1]);
                Task deletedTask = list.deleteTask(id);
                System.out.println("Deleted task with ID (" + deletedTask.id + ")");
                break;
            case "mark-in_progress":
                id = Integer.parseInt(args[1]);
                list.updateTask(id, TaskStatus.in_progress);
                break;
            case "mark-done":
                id = Integer.parseInt(args[1]);
                list.updateTask(id, TaskStatus.done);
                break;
            default:
                System.out.println("Invalid argument at pos 0");
                System.out.println("Try one of these");
                System.out.println("\tadd\n\tupdate\n\tupdate\n\tdelete\n\tmark-in_progress\n\tmark-done");
                break;
        }
    }
}
