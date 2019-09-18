package co.arctern.api.provider.util;

import co.arctern.api.provider.constant.TaskType;
import org.apache.commons.lang3.RandomStringUtils;

public class CodeGeneratorUtil {

    private static final String STATIC_TASK_PREFIX = "TA";

    public String generateTaskCode(Long id, String patientName, TaskType type) {
        return STATIC_TASK_PREFIX
                + type.getValue()
                + patientName.substring(0, 3)
                + ":"
                + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
    }

}
