package co.arctern.api.provider.constant;

import lombok.Getter;

/**
 * types of task based on offering.
 */
public enum TaskType {

    DELIVERY("DLR"),
    SAMPLE_PICKUP("SP"),
    SERVICE("SVC");

    @Getter
    String value;

    TaskType(String value) {
        this.value = value;
    }


}
