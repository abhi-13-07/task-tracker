import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TaskList {
    private ArrayList<Task> taskList;
    private int size;
    private File storage;
    private int taskMaxWidth;

    TaskList(String path) {
        this.taskList = new ArrayList<Task>();
        this.size = 0;
        this.storage = new File(path);
    }

    public void load() {
        String fileContent = "";

        try {
            Scanner sc = new Scanner(this.storage);

            while (sc.hasNextLine()) {
                fileContent += sc.nextLine() + "\n";
            }

            if (fileContent.isEmpty()) return;

            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSON.parse(fileContent);

            if (obj == null) return;

            for (Map<String, Object> item : obj) {
                int id = Integer.parseInt(item.get("id").toString());
                String task = (String) item.get("task");
                TaskStatus status = TaskStatus.valueOf(item.get("status").toString());
                LocalDateTime createdAt = LocalDateTime.parse(item.get("createdAt").toString());
                LocalDateTime updatedAt = LocalDateTime.parse(item.get("updatedAt").toString());

                this.taskList.add(new Task(id, task, status, createdAt, updatedAt));
                this.size++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Task addNewTask(String task) {
        int id = this.taskList.get(this.size - 1).id + 1;
        Task newTask = new Task(id, task);
        this.appendTask(newTask);
        this.save();
        return newTask;
    }

    public Task updateTask(int id, String task) {
        int index = 0;

        for (int i = 0; i < this.size; i++) {
            Task item = this.taskList.get(i);

            if (item.id == id) {
                index = i;
                item.task = task;
            }
        }

        this.save();
        return this.taskList.get(index);
    }

    public Task updateTask(int id, TaskStatus status) {
        int index = 0;

        for (int i = 0; i < this.size; i++) {
            Task item = this.taskList.get(i);

            if (item.id == id) {
                index = i;
                item.status = status;
            }
        }

        this.save();
        return this.taskList.get(index);
    }

    public Task deleteTask(int id) {
        int index = 0;

        for (int i = 0; i < this.size; i++) {
            Task item = this.taskList.get(i);

            if (item.id == id) {
                index = i;
            }
        }

        Task taskItem = this.taskList.get(index);
        this.taskList.remove(index);
        this.size--;
        this.save();
        return taskItem;
    }

    private void appendTask(Task task) {
        this.taskList.add(task);
        this.size++;
    }

    private void save() {
        String content = "[";

        for (int i = 0; i < this.size; i++) {
            Task task = this.taskList.get(i);
            content += task.toString() + ",";
        }

        content = content.substring(0, content.length() - 1) +  "]";

        try {
            FileWriter writer = new FileWriter(this.storage.getPath());
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void list() {
        int maxWidth = getMaxPrintingWidth(this.taskList);

        System.out.printf("%-3s\t" + "%-" + maxWidth + "s\t" + "%-11s\t%-8s\t%-8s\n", "ID", "TASK", "STATUS", "CREATED AT", "UPDATED AT");

        for (int i = 0; i < this.size; i++) {
            Task task = taskList.get(i);
            System.out.printf("%-3s\t" + "%-" + maxWidth + "s\t" + "%-11s\t%-8s\t%-8s\n", task.id, task.task, task.status, task.createdAt.toLocalDate(), task.updatedAt.toLocalDate());
        }
    }

    public void list(TaskStatus status) {
        int maxWidth = getMaxPrintingWidth(this.taskList);

        System.out.printf("%-3s\t" + "%-" + maxWidth + "s\t" + "%-11s\t%-8s\t%-8s\n", "ID", "TASK", "STATUS", "CREATED AT", "UPDATED AT");

        for (int i = 0; i < this.size; i++) {
            Task task = taskList.get(i);
            if (task.status != status) {
                continue;
            }
            System.out.printf("%-3s\t" + "%-" + maxWidth + "s\t" + "%-11s\t%-8s\t%-8s\n", task.id, task.task, task.status, task.createdAt.toLocalDate(), task.updatedAt.toLocalDate());
        }
    }

    public static int getMaxPrintingWidth(ArrayList<Task> list) {
        if (list.isEmpty()) return 0;

        int largest = list.getFirst().task.length();
        int len;

        for (int i = 1; i < list.size(); i++) {
            len = list.get(i).task.length();
            largest = Math.max(len, largest);
        }

        return largest;
    }
}
