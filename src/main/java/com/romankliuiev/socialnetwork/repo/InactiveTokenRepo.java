package com.romankliuiev.socialnetwork.repo;

import com.romankliuiev.socialnetwork.data.InactiveToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InactiveTokenRepo extends JpaRepository<InactiveToken, String> {
}
