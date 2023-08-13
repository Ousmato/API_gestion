package com.tpAPI_gestion.Repository;

import com.tpAPI_gestion.Entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Notification_repository extends JpaRepository<Notifications, Integer> {
    Notifications save(Notifications notifications);
}
