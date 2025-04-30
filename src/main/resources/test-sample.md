# test용 requestBody 샘플

### 1. 상품 등록 
- 엔드포인트 : POST /pos/product/v1/products 
- request body 샘플
    ```json
        {
            "productNm": "돌체라떼",
            "price": 4000,
            "bgColor": "BLUE",
            "taxYn": "Y",
            "categoryId":"1",
            "storeId": "test-store-id-9c12-8fa2b5ef3a72",
            "optionGroups":[
                {
                    "optionGrpNm":"사이즈"
                    ,"activeYn": "Y"
                    ,"requireYn": "Y"
                    ,"minSelectCnt":1
                    ,"maxSelectCnt":1
                    ,"options":[
                         { "optionNm":"tall", "price":0 }
                        ,{ "optionNm":"grande", "price":1000 }
                        ,{ "optionNm":"venti", "price":1500 }
                    ]
                }
                ,{
                    "optionGrpNm":"샷추가"
                    ,"activeYn": "Y"
                    ,"requireYn": "N"
                    ,"minSelectCnt":1
                    ,"maxSelectCnt":3
                    ,"options":[
                         { "optionNm":"shot", "price":500 }
                    ]
                }
            ]
        }
    ```
- response : 201 Created 
- response body 샘플
    ```json
    {
        "productCd": "P00005",
        "productNm": "돌체라떼",
        "price": 4000,
        "bgColor": "BLUE",
        "taxYn": "Y",
        "storeId": "test-store-id-9c12-8fa2b5ef3a72",
        "categoryId": 1,
        "categoryNm": "음료",
        "createdBy": "TEMP_USER",
        "createdAt": "2025-04-30T19:45:55.651145",
        "modifiedBy": "TEMP_USER",
        "modifiedAt": "2025-04-30T19:45:55.651145",
        "createdAtStr": "2025-04-30 19:45:55",
        "modifiedAtStr": "2025-04-30 19:45:55",
        "optionGroups": [
            {
                "optionGrpId": 1,
                "optionGrpNm": "사이즈",
                "options": [
                    {
                        "optionId": 1,
                        "optionNm": "tall",
                        "extraPrice": 0
                    },
                    {
                        "optionId": 2,
                        "optionNm": "grande",
                        "extraPrice": 0
                    },
                    {
                        "optionId": 3,
                        "optionNm": "venti",
                        "extraPrice": 0
                    }
                ]
            },
            {
                "optionGrpId": 2,
                "optionGrpNm": "샷추가",
                "options": [
                    {
                        "optionId": 4,
                        "optionNm": "shot",
                        "extraPrice": 0
                    }
                ]
            }
        ]
    }
    ```