public class AuditLog {
    private static StringBuilder logs = new StringBuilder();

    public static void addLog(String action, String timestamp) {
        logs.append(timestamp).append(" - ").append(action).append("\n");
    }

    public static String getLogs() {
        return logs.toString();
    }
}

