package com.revature.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revature.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer>{

}
