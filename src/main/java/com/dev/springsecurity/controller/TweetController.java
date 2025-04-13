package com.dev.springsecurity.controller;

import com.dev.springsecurity.controller.dto.CreateTweetDto;
import com.dev.springsecurity.controller.dto.FeedDto;
import com.dev.springsecurity.controller.dto.FeedItemDto;
import com.dev.springsecurity.entities.Role;
import com.dev.springsecurity.entities.Tweet;
import com.dev.springsecurity.entities.User;
import com.dev.springsecurity.repository.TweetRepository;
import com.dev.springsecurity.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));


        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))){

            tweetRepository.deleteById(tweetId);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        return ResponseEntity.ok().build();
    }

    //Listar tweets por paginação
    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0")int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {


        var tweets = tweetRepository.findAll(

                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet ->
                        new FeedItemDto(
                                tweet.getTweetId(),
                                tweet.getContent(),
                                tweet.getUser().getName())
                );

        return ResponseEntity.ok(new FeedDto(
                tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));

    }


}




