/* 카테고리 초기 데이터 생성 */
INSERT INTO CATEGORY (CATEGORY_NM, STORE_ID, DELETE_YN,
                      CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
('음료', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
('디저트', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

/* 상품 초기데이터 생성 + 상품고유ID 시퀀스 세팅*/
INSERT INTO PRODUCT (PRODUCT_CD, PRODUCT_NM,
                     PRICE, STORE_ID, CATEGORY_ID, DELETE_YN, BG_COLOR, TAX_YN,
                     CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('P00001', '아메리카노',
     2000, 'sample-store-id-9c12-8fa2b5ef3a72', 1, 'N', 'BLUE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),

    ('P00002', '라떼',
     3000, 'sample-store-id-9c12-8fa2b5ef3a72', 1, 'N', 'BLUE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),

    ('P00003', '에그타르트',
     3000, 'sample-store-id-9c12-8fa2b5ef3a72', 2, 'N', 'YELLOW', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),

    ('P00004', '초코타르트',
     4000, 'sample-store-id-9c12-8fa2b5ef3a72', 2, 'N', 'YELLOW', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);
INSERT INTO PRODUCT_CD_SEQ (PRODUCT_SEQ)
VALUES (1),(2),(3),(4);