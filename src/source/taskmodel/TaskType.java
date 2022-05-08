public enum TaskType {
    TASK,
    EPICTASK,
    SUBTASK,
    UNIDENTIFIED_TASK;

    public static TaskType getTaskType(String type) {
        switch(type) {
            case "class Task":
                return TASK;
            case "class EpicTask":
                return EPICTASK;
            case "class SubTask":
                return SUBTASK;
            default:
                return UNIDENTIFIED_TASK;
        }
    }
}
