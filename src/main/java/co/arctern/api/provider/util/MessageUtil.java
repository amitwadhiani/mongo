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
     * task assigned message.
     */
    public static final StringBuilder TASK_ASSIGNED_MESSAGE = new StringBuilder("Task assigned.");

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

    /**
     * wrong otp message.
     */
    public static final String WRONG_OTP_MESSAGE = "Wrong OTP entered. Please Try again.";

    /**
     * otp expired message.
     */
    public static final String EXPIRED_OTP_MESSAGE = "OTP expired. Please request an OTP again.";

    /**
     * user not logged in message.
     */
    public static final String USER_NOT_LOGGED_IN_MESSAGE = "User not logged in.";

    /**
     * invalid role id message.
     */
    public static final String INVALID_ROLE_ID_MESSAGE = "Invalid role id.";

    /**
     * user not found message.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User not found .";

    /**
     * invalid task id message.
     */
    public static final String INVALID_TASK_ID_MESSAGE = "User not found .";

    /**
     * request user creation message.
     */
    public static final String REGISTER_USER_MESSAGE = "No user found. Ask admin for new sign up. ";

}
