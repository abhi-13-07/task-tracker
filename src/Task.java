import java.time.LocalDateTime;

public class Task {
    public final int id;
    public String task;
    public TaskStatus status;
    public final LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Task(int id, String task) {
        this.id = id;
        this.task = task;
        this.status = TaskStatus.todo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(int id, String task, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.task = task;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String setTask(String task) {
        this.task = task;
        return this.task;
    }

    public TaskStatus setStatus(TaskStatus status) {
        this.status = status;
        return this.status;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        String jsonNotation;
        jsonNotation = String.format("{\"id\": %s,\"task\": \"%s\",\"status\": \"%s\",\"createdAt\": \"%s\",\"updatedAt\": \"%s\"}", id, task, status, createdAt, updatedAt);
        return jsonNotation;
    }
}
