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
    product_pk_id BIGINT NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),

    CONSTRAINT fk_optiongroup_parent FOREIGN KEY (parent_id) REFERENCES option_group(option_grp_id),
    CONSTRAINT fk_optiongroup_product FOREIGN KEY (product_pk_id) REFERENCES product(id)
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

/* 카테고리 별 옵션  */
CREATE TABLE IF NOT EXISTS category_option_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_pk_id BIGINT NOT NULL,
    option_group_pk_id BIGINT NOT NULL,
    mandatory BOOLEAN DEFAULT TRUE NOT NULL,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    modified_at TIMESTAMP,
    modified_by VARCHAR(255),

    CONSTRAINT fk_cog_category FOREIGN KEY (category_pk_id) REFERENCES category(category_id),
    CONSTRAINT fk_cog_optiongroup FOREIGN KEY (option_group_pk_id) REFERENCES option_group(option_grp_id)
);
COMMENT ON TABLE category_option_group IS '카테고리 별 지정옵션 매핑 테이블';
