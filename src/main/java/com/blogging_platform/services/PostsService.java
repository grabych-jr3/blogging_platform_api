package com.blogging_platform.services;

import com.blogging_platform.models.Post;
import com.blogging_platform.repositories.PostsRepository;
import com.blogging_platform.util.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public List<Post> getAll(){
        return postsRepository.findAll();
    }

    public List<Post> getAllByTerm(String term){
        return postsRepository.getPostsHavingTerm(term);
    }

    public Post getOne(int id){
        Optional<Post> post = postsRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));
    }

    @Transactional
    public void createPost(Post post){
        postsRepository.save(enrichPost(post));
    }

    @Transactional
    public void updatePost(int id, Post post){
        Post existingPost = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setCategory(post.getCategory());
        existingPost.setTags(post.getTags());
        existingPost.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void deletePost(int id){
        Post post = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));
        postsRepository.delete(post);
    }

    private Post enrichPost(Post post){
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        return post;
    }
}
