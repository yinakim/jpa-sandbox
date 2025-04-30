/* 카테고리 */
CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_nm VARCHAR(255),
    store_id VARCHAR(36) NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',

    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255)
);
COMMENT ON TABLE category IS '카테고리 테이블';

/* 상품 */
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_cd VARCHAR(20) NOT NULL UNIQUE,
    product_nm VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    store_id VARCHAR(36) NOT NULL,
    bg_color VARCHAR(20) NOT NULL,
    tax_yn VARCHAR(1) NOT NULL,

    category_id BIGINT NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',

    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),

    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id)
);
COMMENT ON TABLE product IS '상품 테이블';
COMMENT ON COLUMN product.product_id IS '상품Id(pk)';
COMMENT ON COLUMN product.product_cd IS '상품고유ID';



/* 상품 ID SEQ 자체관리 */
CREATE TABLE IF NOT EXISTS product_cd_seq (
--     product_seq BIGINT AUTO_INCREMENT PRIMARY KEY
    product_seq BIGINT PRIMARY KEY
);
COMMENT ON TABLE product_cd_seq IS '상품고유아이디(ex. P00001) 생성용  SEQ 자체관리 테이블';


/* 옵션그룹 */
CREATE TABLE IF NOT EXISTS option_group (
    option_grp_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_grp_nm VARCHAR(50) NOT NULL,
    depth INT NOT NULL,
    parent_id BIGINT,
    product_id BIGINT NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),

    CONSTRAINT fk_optiongroup_parent FOREIGN KEY (parent_id) REFERENCES option_group(option_grp_id),
    CONSTRAINT fk_optiongroup_product FOREIGN KEY (product_id) REFERENCES product(id)
);
COMMENT ON TABLE option_group IS '옵션그룹 테이블';


/* 옵션 */
CREATE TABLE IF NOT EXISTS option (
    option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_nm VARCHAR(50) NOT NULL,
    extra_price INT NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255)
);
COMMENT ON TABLE option IS '옵션 테이블';

/* 상품-옵션 매핑 */
CREATE TABLE IF NOT EXISTS product_option_group (
    pog_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    option_group_id BIGINT NOT NULL,
    active_yn CHAR(1) NOT NULL DEFAULT 'N',
    delete_yn CHAR(1) NOT NULL DEFAULT 'N',
    created_by VARCHAR(255) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_by VARCHAR(255) DEFAULT NULL,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_option_group_product FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT fk_product_option_group_option_group FOREIGN KEY (option_group_id) REFERENCES option_group(option_grp_id)
);
COMMENT ON TABLE product_option_group IS '상품-옵션 매핑 테이블';