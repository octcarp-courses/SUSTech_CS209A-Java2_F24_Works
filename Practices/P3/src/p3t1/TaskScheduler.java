package p3t1;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

class TaskScheduler {
    private PriorityQueue<Task> taskQueue;

    public TaskScheduler() {
        this.taskQueue = new PriorityQueue<>();
    }

    public void addTask(String description, int priority) {
        taskQueue.add(new Task(description, priority));
    }

    public List<Task> getTopKTasks(int k) {
        List<Task> topKTasks = new LinkedList<>();
        PriorityQueue<Task> tempQueue = new PriorityQueue<>(taskQueue);

        while (k-- > 0 && !tempQueue.isEmpty()) {
            topKTasks.add(tempQueue.poll());
        }

        return topKTasks;
    }

    public void finishNextTask() {
        if (!taskQueue.isEmpty()) {
            taskQueue.poll();
        }
    }
}