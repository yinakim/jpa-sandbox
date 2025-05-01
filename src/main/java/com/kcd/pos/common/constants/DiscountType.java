package com.kcd.pos.common.constants;

import lombok.Getter;

@Getter
public enum DiscountType {
     PERCENT("비율할인")
    ,AMOUNT("금액할인")
    ;

    private final String typeName;

    DiscountType(String typeName) {
        this.typeName = typeName;
    }

}
