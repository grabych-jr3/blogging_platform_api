package com.blogging_platform.controllers;

import com.blogging_platform.dto.PostDTO;
import com.blogging_platform.models.Post;
import com.blogging_platform.models.Tag;
import com.blogging_platform.services.PostsService;
import com.blogging_platform.services.TagService;
import com.blogging_platform.util.PostNotCreatedException;
import com.blogging_platform.util.PostNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;
    private final TagService tagService;
    private final ModelMapper modelMapper;

    @Autowired
    public PostsController(PostsService postsService, TagService tagService, ModelMapper modelMapper) {
        this.postsService = postsService;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PostDTO> getAll(@RequestParam(name = "term", required = false) String term){
        if(term != null){
            return postsService.getAllByTerm(term);
        }
        return postsService.getAll();
    }

    @GetMapping("/{id}")
    public PostDTO getOne(@PathVariable int id){
        return postsService.getOne(id);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult){
        getAllFieldErrors(bindingResult);
        postsService.createPost(postDTO);
        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable int id, @RequestBody @Valid PostDTO postDTO, BindingResult bindingResult){
        getAllFieldErrors(bindingResult);
        postsService.updatePost(id, postDTO);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable int id){
        postsService.deletePost(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    private void getAllFieldErrors(BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                errorMsg.append(fieldError.getField())
                        .append(" - ").append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new PostNotCreatedException(errorMsg.toString());
        }
    }
}
