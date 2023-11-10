import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class TaskManagerWithGUI {

    static class Task {
        String title;
        String description;
        String deadline;
        int estimatedTime;

        public Task(String title, String description, String deadline, int estimatedTime) {
            this.title = title;
            this.description = description;
            this.deadline = deadline;
            this.estimatedTime = estimatedTime;
        }

        @Override
        public String toString() {
            return "Title: " + title + "\nDescription: " + description + "\nDeadline: " + deadline + "\nEstimated Time: " + estimatedTime + " hours\n";
        }
    }

    static Queue<Task> taskQueue = new PriorityQueue<>((a, b) -> a.deadline.compareTo(b.deadline));

    public static void addTask() {
        JFrame addFrame = new JFrame("Add Task");
        addFrame.setSize(300, 200);
        addFrame.setLayout(new GridLayout(5, 2));

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField deadlineField = new JTextField();
        JTextField timeField = new JTextField();

        addFrame.add(new JLabel("Title:"));
        addFrame.add(titleField);
        addFrame.add(new JLabel("Description:"));
        addFrame.add(descField);
        addFrame.add(new JLabel("Deadline:"));
        addFrame.add(deadlineField);
        addFrame.add(new JLabel("Estimated Time (hours):"));
        addFrame.add(timeField);

        JButton addButton = new JButton("Add");
        addFrame.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descField.getText();
                String deadline = deadlineField.getText();
                int estimatedTime = Integer.parseInt(timeField.getText());

                Task newTask = new Task(title, description, deadline, estimatedTime);
                taskQueue.add(newTask);
                addFrame.setVisible(false);
            }
        });

        addFrame.setVisible(true);
    }

    public static void updateTask() {
        JFrame updateFrame = new JFrame("Update Task");
        updateFrame.setSize(300, 200);
        updateFrame.setLayout(new GridLayout(6, 2));

        JComboBox<String> tasksComboBox = new JComboBox<>();
        for (Task task : taskQueue) {
            tasksComboBox.addItem(task.title);
        }

        JTextField newTitleField = new JTextField();
        JTextField newDescField = new JTextField();
        JTextField newDeadlineField = new JTextField();
        JTextField newTimeField = new JTextField();

        updateFrame.add(new JLabel("Select Task:"));
        updateFrame.add(tasksComboBox);
        updateFrame.add(new JLabel("New Title:"));
        updateFrame.add(newTitleField);
        updateFrame.add(new JLabel("New Description:"));
        updateFrame.add(newDescField);
        updateFrame.add(new JLabel("New Deadline:"));
        updateFrame.add(newDeadlineField);
        updateFrame.add(new JLabel("New Estimated Time (hours):"));
        updateFrame.add(newTimeField);

        JButton updateButton = new JButton("Update");
        updateFrame.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTaskTitle = tasksComboBox.getItemAt(tasksComboBox.getSelectedIndex());
                for (Task task : taskQueue) {
                    if (task.title.equals(selectedTaskTitle)) {
                        task.title = newTitleField.getText();
                        task.description = newDescField.getText();
                        task.deadline = newDeadlineField.getText();
                        task.estimatedTime = Integer.parseInt(newTimeField.getText());
                        break;
                    }
                }
                updateFrame.setVisible(false);
            }
        });

        updateFrame.setVisible(true);
    }

    public static void showAllTasks() {
        JFrame showFrame = new JFrame("All Tasks");
        showFrame.setSize(400, 300);
        showFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();

        for (Task task : taskQueue) {
            textArea.append(task.toString() + "\n");
        }

        showFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        showFrame.setVisible(true);
    }

    public static void prioritizeTasks() {
        JFrame prioritizeFrame = new JFrame("Prioritize Tasks");
        prioritizeFrame.setSize(300, 200);
        prioritizeFrame.setLayout(new GridLayout(taskQueue.size() + 1, 1));

        JComboBox<String> tasksComboBox = new JComboBox<>();
        for (Task task : taskQueue) {
            tasksComboBox.addItem(task.title);
        }

        JButton prioritizeButton = new JButton("Move Up");
        prioritizeFrame.add(tasksComboBox);
        prioritizeFrame.add(prioritizeButton);

        prioritizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tasksComboBox.getSelectedIndex();
                if (selectedIndex > 0) {
                    Task[] taskArray = taskQueue.toArray(new Task[0]);
                    Task selectedTask = Arrays.stream(taskArray).filter(task -> task.title.equals(tasksComboBox.getItemAt(selectedIndex))).findFirst().orElse(null);
                    if (selectedTask != null) {
                        taskQueue.remove(selectedTask);
                        taskQueue.removeIf(task -> task.title.equals(selectedTask.title));
                        taskQueue.add(selectedTask);
                        for (Task task : taskArray) {
                            if (!task.title.equals(selectedTask.title)) {
                                taskQueue.add(task);
                            }
                        }
                    }
                }
            }
        });

        prioritizeFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        JButton addTaskButton = new JButton("Add Task");
        JButton updateTaskButton = new JButton("Update Task");
        JButton showTasksButton = new JButton("Show All Tasks");
        JButton prioritizeButton = new JButton("Prioritize Tasks");

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        updateTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });

        showTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTasks();
            }
        });

        prioritizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prioritizeTasks();
            }
        });

        frame.add(addTaskButton);
        frame.add(updateTaskButton);
        frame.add(showTasksButton);
        frame.add(prioritizeButton);

        frame.setVisible(true);
    }
}
