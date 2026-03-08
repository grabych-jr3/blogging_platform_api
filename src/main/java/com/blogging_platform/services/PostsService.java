package com.blogging_platform.services;

import com.blogging_platform.dto.PostDTO;
import com.blogging_platform.models.Post;
import com.blogging_platform.models.Tag;
import com.blogging_platform.repositories.PostsRepository;
import com.blogging_platform.util.PostNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;

    @Autowired
    public PostsService(PostsRepository postsRepository, ModelMapper modelMapper, TagService tagService) {
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
        this.tagService = tagService;
    }

    public List<PostDTO> getAll(){
        List<Post> posts = postsRepository.findAll();

        return posts.stream()
                .map(this::convertToPostDTO)
                .toList();
    }

    public List<PostDTO> getAllByTerm(String term){
        List<Post> posts = postsRepository.getPostsHavingTerm(term);

        return posts.stream()
                .map(this::convertToPostDTO)
                .toList();
    }

    @Cacheable(value = "posts", key = "#id")
    public PostDTO getOne(int id){
        Post post = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));

        return convertToPostDTO(post);
    }

    @CachePut(value = "posts", key = "#result.id")
    @Transactional
    public PostDTO createPost(PostDTO postDTO){
        Post saved = postsRepository.save(
                enrichPost(convertToPost(postDTO))
        );

        return convertToPostDTO(saved);
    }

    @CachePut(value = "posts", key = "#id")
    @Transactional
    public PostDTO updatePost(int id, PostDTO postDTO){
        Post existingPost = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setCategory(postDTO.getCategory());

        existingPost.setTags(
                postDTO.getTags()
                        .stream()
                        .map(tagService::findOrCreate)
                        .toList()
        );

        existingPost.setUpdatedAt(Instant.now());
        return convertToPostDTO(existingPost);
    }

    @CacheEvict(value = "posts", key = "#id")
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

    private PostDTO convertToPostDTO(Post post){
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        postDTO.setTags(
                post.getTags()
                        .stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
        );
        return postDTO;
    }

    private Post convertToPost(PostDTO postDTO){
        Post post = modelMapper.map(postDTO, Post.class);

        post.setTags(
                postDTO.getTags()
                        .stream()
                        .map(tagService::findOrCreate)
                        .toList()
        );

        return post;
    }
}
