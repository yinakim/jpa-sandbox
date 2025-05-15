package com.pos.common.constants;

import lombok.Getter;

@Getter
public enum DiscountType {
    PERCENT("비율할인")
    ,AMOUNT("금액할인")
    ,EMPTY("할인없음")
    ;

    private final String typeName;

    DiscountType(String typeName) {
        this.typeName = typeName;
    }

}
