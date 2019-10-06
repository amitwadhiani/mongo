package co.arctern.api.provider.util;

import co.arctern.api.provider.constant.TaskType;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * code generator util for task.
 */
public class CodeGeneratorUtil {

    private static final String STATIC_TASK_PREFIX = "TA";

    /**
     * generate task code.
     *
     * @param id
     * @param orderId
     * @param type
     * @return
     */
    public String generateTaskCode(Long id, Long orderId, TaskType type) {
        return STATIC_TASK_PREFIX
                + "-"
                + type.getValue()
                + orderId.toString()
                + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
    }

}
