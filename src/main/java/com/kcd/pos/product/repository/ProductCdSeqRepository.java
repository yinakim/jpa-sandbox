package com.kcd.pos.product.repository;

import com.kcd.pos.product.domain.ProductCdSeq;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductCdSeqRepository extends JpaRepository<ProductCdSeq, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 이 쿼리를 실행할 때 쓰기에 대해 락, 다른 트랜잭션 접근시 락 해제까지 대기
    @Query("SELECT MAX(p.productSeq) FROM ProductCdSeq  p")
    Long findMaxSequenceNumberWithLock();
}
