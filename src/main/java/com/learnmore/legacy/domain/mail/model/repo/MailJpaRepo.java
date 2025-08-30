package com.learnmore.legacy.domain.mail.model.repo;

import com.learnmore.legacy.domain.mail.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailJpaRepo extends JpaRepository<Mail, Long> {
    List<Mail> findAllByUser_UserId(Long userId);
}
