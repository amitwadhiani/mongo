package co.arctern.api.provider.constant;

/**
 * Rider states for flows for particular tasks.
 */
public enum TaskEventState {
    OPEN,
    ASSIGNED,
    ACCEPTED,
    REJECTED,
    REASSIGNED,
    OUT_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    CANCELLED,
    COMPLETED,

}
