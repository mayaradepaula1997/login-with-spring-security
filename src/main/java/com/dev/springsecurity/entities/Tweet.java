package com.dev.springsecurity.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tb_tweets")
public class Tweet {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //será gerado automaticamente pelo banco de dados, usando uma sequência
    @Column(name = "tweet_id") //nome da coluna, que vai armazenar o valor do tweetid
    private Long tweetId;

    @ManyToOne  //Um usuario pode ter varios tweet, mas um tweet está vinculado apenas a um usuário
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @CreationTimestamp  //timestamp da data e hora da criação do tweet
    private Instant creationTimestamp;


    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
