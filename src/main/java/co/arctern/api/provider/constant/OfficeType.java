package co.arctern.api.provider.constant;

import lombok.Getter;

/**
 * Office types for area storage (via pinCodes).
 */
public enum OfficeType {

    SO("SUB_POST_OFFICE"),
    HO("HEAD_POST_OFFICE"),
    BO("BRANCH_POST_OFFICE");

    @Getter
    String value;

    OfficeType(String value) {
        this.value = value;
    }
}
