package co.arctern.api.provider.util;

public interface MessageUtil {

    /**
     * success message.
     */
    public static final StringBuilder SUCCESS_MESSAGE = new StringBuilder("Success");

    /**
     * task accept message.
     */
    public static final StringBuilder TASK_ACCEPT_MESSAGE = new StringBuilder("Task accepted.");

    /**
     * retry request message.
     */
    public static final StringBuilder TRY_AGAIN_MESSAGE = new StringBuilder("Please try again.");

    /**
     * task reject message.
     */
    public static final StringBuilder TASK_REJECT_MESSAGE = new StringBuilder("Task Rejected.");

    /**
     * task reassign message.
     */
    public static final StringBuilder TASK_REASSIGN_MESSAGE = new StringBuilder("Task reassigned.");

    /**
     * task cancel message.
     */
    public static final StringBuilder TASK_CANCEL_MESSAGE = new StringBuilder("Task cancelled.");

    public static final String WRONG_OTP_MESSAGE = "Wrong OTP entered. Please Try again.";

    public static final String EXPIRED_OTP_MESSAGE = "OTP expired. Please request an OTP again.";

    public static final String USER_NOT_LOGGED_IN_MESSAGE = "User not logged in.";

    public static final String INVALID_ROLE_ID_MESSAGE = "Invalid role id.";

    public static final String USER_NOT_FOUND_MESSAGE = "User not found .";

    public static final String INVALID_TASK_ID_MESSAGE = "User not found .";

    public static final String REGISTER_USER_MESSAGE = "No user found. Ask admin for new sign up. ";

}
