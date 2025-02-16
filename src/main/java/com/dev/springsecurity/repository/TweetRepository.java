package com.dev.springsecurity.repository;

import com.dev.springsecurity.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
