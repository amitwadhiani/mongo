package co.arctern.api.provider.util;

public interface MessageUtil {

    /**
     * success message.
     */
    StringBuilder SUCCESS_MESSAGE = new StringBuilder("Success");

    /**
     * task accept message.
     */
    StringBuilder TASK_ACCEPT_MESSAGE = new StringBuilder("Task accepted.");

    /**
     * task assigned message.
     */
    StringBuilder TASK_ASSIGNED_MESSAGE = new StringBuilder("Task assigned.");

    /**
     * retry request message.
     */
    StringBuilder TRY_AGAIN_MESSAGE = new StringBuilder("Please try again.");

    /**
     * task reject message.
     */
    StringBuilder TASK_REJECT_MESSAGE = new StringBuilder("Task Rejected.");

    /**
     * task reassign message.
     */
    StringBuilder TASK_REASSIGN_MESSAGE = new StringBuilder("Task reassigned.");

    /**
     * task cancel message.
     */
    StringBuilder TASK_CANCEL_MESSAGE = new StringBuilder("Task cancelled.");

    /**
     * wrong otp message.
     */
    StringBuilder WRONG_OTP_MESSAGE = new StringBuilder("Wrong OTP entered. Please Try again.");

    /**
     * otp expired message.
     */
    StringBuilder EXPIRED_OTP_MESSAGE = new StringBuilder("OTP expired. Please request an OTP again.");

    /**
     * user not logged in message.
     */
    StringBuilder USER_NOT_LOGGED_IN_MESSAGE = new StringBuilder("User not logged in.");

    /**
     * invalid role id message.
     */
    StringBuilder INVALID_ROLE_ID_MESSAGE = new StringBuilder("Invalid role id.");

    /**
     * user not found message.
     */
    StringBuilder USER_NOT_FOUND_MESSAGE = new StringBuilder("User not found .");

    /**
     * invalid task id message.
     */
    StringBuilder INVALID_TASK_ID_MESSAGE = new StringBuilder("User not found .");

    /**
     * request user creation message.
     */
    StringBuilder REGISTER_USER_MESSAGE = new StringBuilder("No user found. Ask admin for new sign up. ");

    /**
     * invalid offering id message.
     */
    StringBuilder INVALID_OFFERING_ID_MESSAGE = new StringBuilder("Invalid id");

    /**
     * task not assigned or inactive message.
     */
    StringBuilder TASK_NOT_ASSIGNED_OR_INACTIVE_MESSAGE = new StringBuilder("Task not assigned/active for this user.");

    /**
     * only admin login allowed message.
     */
    StringBuilder ONLY_ADMIN_LOGIN_ALLOWED_MESSAGE = new StringBuilder("Only Admin login allowed.");

    /**
     * invalid area id message.
     */
    StringBuilder INVALID_AREA_ID_MESSAGE = new StringBuilder("Invalid area Id.");

    /**
     * rating entity already generated for task id message.
     */
    StringBuilder RATING_ALREADY_GENERATED_MESSAGE = new StringBuilder("Rating already generated for this task.");

    /**
     * Task already started message.
     */
    StringBuilder TASK_ALREADY_STARTED_MESSAGE = new StringBuilder("Task has already been started.");

    /**
     * User already exists message by username.
     */
    StringBuilder USERNAME_ALREADY_EXISTS_MESSAGE = new StringBuilder("User with this username already exists. ");

    /**
     * User already exists message by phone.
     */
    StringBuilder PHONE_ALREADY_EXISTS_MESSAGE = new StringBuilder("User with this phone already exists. ");

    /**
     * User already exists message by email.
     */
    StringBuilder EMAIL_ALREADY_EXISTS_MESSAGE = new StringBuilder("User with this email already exists. ");

}
