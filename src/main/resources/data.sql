
-- Table: CATEGORY(CATEGORY_ID PK), PRODUCT(PRODUCT_ID PK), OPTION_GROUP(OPTION_GRP_ID PK)
-- Assume schema.sql has already created all required tables.

/* 카테고리 초기 데이터 생성 */
INSERT INTO CATEGORY (CATEGORY_NM, STORE_ID, DELETE_YN,
                      CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('커피', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('차(Tea)', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('타르트', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('케이크', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('쿠키', 'sample-store-id-9c12-8fa2b5ef3a72', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

/* 상품 초기데이터 생성 + 상품고유ID 시퀀스 세팅*/
INSERT INTO PRODUCT (PRODUCT_CD, PRODUCT_NM,
                     PRICE, STORE_ID, CATEGORY_ID, DELETE_YN, BG_COLOR, TAX_YN,
                     CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('P00001', '아메리카노', 2000, 'sample-store-id-9c12-8fa2b5ef3a72', 1, 'N', 'BLUE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00002', '카페라떼', 2500, 'sample-store-id-9c12-8fa2b5ef3a72', 1, 'N', 'BLUE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00003', '유자차', 2800, 'sample-store-id-9c12-8fa2b5ef3a72', 2, 'N', 'GREEN', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00004', '페퍼민트티', 3000, 'sample-store-id-9c12-8fa2b5ef3a72', 2, 'N', 'GREEN', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00005', '에그타르트', 3000, 'sample-store-id-9c12-8fa2b5ef3a72', 3, 'N', 'YELLOW', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00006', '초코타르트', 3300, 'sample-store-id-9c12-8fa2b5ef3a72', 3, 'N', 'YELLOW', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00007', '레드벨벳케이크', 5000, 'sample-store-id-9c12-8fa2b5ef3a72', 4, 'N', 'WHITE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00008', '치즈케이크', 4800, 'sample-store-id-9c12-8fa2b5ef3a72', 4, 'N', 'WHITE', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00009', '초코칩쿠키', 1800, 'sample-store-id-9c12-8fa2b5ef3a72', 5, 'N', 'RED', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    ('P00010', '오트밀쿠키', 2000, 'sample-store-id-9c12-8fa2b5ef3a72', 5, 'N', 'RED', 'Y',
     'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);
INSERT INTO PRODUCT_CD_SEQ (PRODUCT_SEQ)
VALUES (1), (2), (3), (4), (5), (6), (7), (8), (9), (10);

------------------ 옵션 그룹: 사이즈
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('사이즈', 'Y', 'Y', 'Y', 1, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (1, 'Y', 'Y', 'Tall', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (1, 'Y', 'Y', 'Grande', 500, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (1, 'Y', 'Y', 'Venti', 1000, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑: 아메리카노 ↔ 사이즈 그룹
-- P00001 - 아메리카노 (product_id = 1, option_group_id = 1)
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (1, 1, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

------------------ 옵션 그룹: 우유 타입
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('우유 선택', 'Y', 'Y', 'Y', 1, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (2, 'Y', 'Y', '일반우유', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (2, 'Y', 'Y', '저지방우유', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (2, 'Y', 'Y', '귀리우유', 500, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
-- P00002 - 카페라떼 (product_id = 2, option_group_id = 2)
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (2, 2, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-------------------- 옵션 그룹: 당도
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('당도 조절', 'Y', 'N', 'N', 0, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (3, 'Y', 'Y', '기본', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (3, 'Y', 'Y', '조금달게', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (3, 'Y', 'Y', '많이달게', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
-- P00003 - 유자차 (product_id = 3, option_group_id = 3)
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (3, 3, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-------------------- 옵션 그룹: 온도
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('온도 선택', 'Y', 'Y', 'Y', 1, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (4, 'Y', 'Y', 'HOT', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (4, 'Y', 'Y', 'ICED', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
-- P00004 - 페퍼민트티 (product_id = 4, option_group_id = 4)
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (4, 4, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);


-- P00005 - 에그타르트
-- 옵션 그룹: 데움/데우지않음
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('데움/데우지않음', 'N','Y', 'N', 0, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (5, 'N', 'N', '데움', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (5, 'N', 'N', '데우지않음', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (5, 5, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);


-------------------- 옵션 그룹: 초콜릿 추가
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('초콜릿 추가', 'Y', 'N', 'N', 0, 2, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (6, 'Y', 'Y', '초코시럽', 500, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (6, 'Y', 'Y', '초코칩', 700, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
-- P00006 - 초코타르트 (product_id = 6, option_group_id = 6)
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (6, 6, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

---------------------- 옵션 그룹: 크기
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('조각/홀 선택', 'Y', 'Y', 'Y', 1, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (7, 'Y', 'Y', '조각', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (7, 'Y', 'Y', '홀', 15000, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (7, 7, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

---------------------- 옵션 그룹: 수량
INSERT INTO OPTION_GROUP (OPTION_GRP_NM, DELETE_YN, ACTIVE_YN, REQUIRE_YN, MIN_SELECT_CNT, MAX_SELECT_CNT,
                          CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    ('수량 선택', 'Y', 'Y', 'Y', 1, 1, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 옵션 항목
INSERT INTO OPTION (OPTION_GROUP_ID, ACTIVE_YN, DELETE_YN, OPTION_NM, EXTRA_PRICE, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES
    (8, 'Y', 'Y', '1개', 0, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP),
    (8, 'Y', 'Y', '3개 세트', 4000, 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

-- 매핑
INSERT INTO PRODUCT_OPTION_GROUP (product_id, option_group_id, active_yn, delete_yn, CREATED_BY, CREATED_AT, MODIFIED_BY, MODIFIED_AT)
VALUES (9, 8, 'Y', 'N', 'init_data', CURRENT_TIMESTAMP, 'init_data', CURRENT_TIMESTAMP);

