package co.arctern.api.provider.constant;

/**
 * task states for flows for particular tasks.
 */
public enum TaskStateFlowState {

    OPEN,
    ASSIGNED,
    ACCEPTED,
    RESCHEDULED,
    REJECTED,
    TIMED_OUT,
    REASSIGNED,
    STARTED,
    OUT_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    CANCELLED,
    CANCELLATION_REQUESTED,
    COMPLETED
}
