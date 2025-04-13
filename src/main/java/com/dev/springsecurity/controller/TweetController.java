package com.dev.springsecurity.controller;

import com.dev.springsecurity.controller.dto.CreateTweetDto;
import com.dev.springsecurity.entities.Tweet;
import com.dev.springsecurity.entities.User;
import com.dev.springsecurity.repository.TweetRepository;
import com.dev.springsecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
public class TweetController {


    private TweetRepository tweetRepository;

    private UserRepository userRepository; //Saber o usuario

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(CreateTweetDto dto, JwtAuthenticationToken token) { //Saber o usuario que esta vindo no token

        Optional<User> user = userRepository.findById(UUID.fromString(token.getName()));
        //busca o usuario pelo id UUID
        // pega o ID como String, então você precisa converter para UUID

        Tweet tweet = new Tweet();
        tweet.setUser(user.get()); //associa ao usuario autenticado
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId,
                                            JwtAuthenticationToken token) {  //representa o token JWT do usuário autenticado

        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Verifica se o dono do tweet (tweet.getUser().getUserId()) é igual ao usuário autenticado (UUID.fromString(token.getName()))
        //token.getName() retorna o ID do usuário que fez login, extraído do JWT.
        if (tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            tweetRepository.deleteById(tweetId);


        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        return ResponseEntity.ok().build();
    }

}
