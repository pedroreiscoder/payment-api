package io.ezycollect.paymentapi.repository;

import io.ezycollect.paymentapi.entity.WebHook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebHookRepository extends JpaRepository<WebHook, Long> {
}
