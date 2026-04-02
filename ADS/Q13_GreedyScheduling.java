import java.util.*;

public class Q13_GreedyScheduling {
    static class Task {
        int id;
        int time;

        Task(int id, int time) {
            this.id = id;
            this.time = time;
        }
    }

    public static void main(String[] args) {
        int[] t = {3, 1, 2, 5};
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < t.length; i++) tasks.add(new Task(i, t[i]));

        tasks.sort(Comparator.comparingInt(a -> a.time));

        int currentTime = 0;
        int totalTimeSpent = 0;

        System.out.println("Order (taskId, duration):");
        for (Task task : tasks) {
            currentTime += task.time;
            totalTimeSpent += currentTime;
            System.out.println("Task " + task.id + ", duration " + task.time + ", completion time " + currentTime);
        }
        System.out.println("Total time spent by all tasks: " + totalTimeSpent);
    }
}