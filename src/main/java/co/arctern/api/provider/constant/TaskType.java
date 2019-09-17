package co.arctern.api.provider.constant;

/**
 * types of task based on offering.
 */
public enum TaskType {

    DELIVERY("DLR"),
    SAMPLE_PICKUP("SP"),
    SERVICE("SVC");

    String value;

    TaskType(String value) {
        this.value = value;
    }
}
