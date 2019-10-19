package co.arctern.api.provider.constant;

import lombok.Getter;

public enum OfficeType {

    SUB_POST_OFFICE("S.O"),
    HEAD_POST_OFFICE("H.O"),
    BRANCH_POST_OFFICE("B.O");

    @Getter
    String value;

    OfficeType(String value) {
        this.value = value;
    }
}
