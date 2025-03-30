package com.example.demo.repository;

import com.example.demo.domain.model.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Modifying
    @Transactional // su dụng Transaction vì các method này ta tự impl không có transaction sẵn như các method được JpaRepository impl
    // nếu không có transaction thì việc thay đổi không được cập nhật dẫn đến lỗi jakarta.persistence.TransactionRequiredException: Executing an update/delete query
    @Query("DELETE FROM RefreshToken r WHERE r.userId = :userId")
    void deleteByUserId(@Param("userId") int userId);
}
