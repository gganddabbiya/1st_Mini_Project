package session;

public class Session {
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        Session.userId = userId;
    }
}