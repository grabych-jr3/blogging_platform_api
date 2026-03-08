package com.blogging_platform.services;

import com.blogging_platform.dto.PostDTO;
import com.blogging_platform.models.Post;
import com.blogging_platform.models.Tag;
import com.blogging_platform.repositories.PostsRepository;
import com.blogging_platform.util.PostNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PostDTO getOne(int id){
        Post post = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found!"));

        return convertToPostDTO(post);
    }

    @Transactional
    public void createPost(PostDTO postDTO){
        postsRepository.save(enrichPost(convertToPost(postDTO)));
    }

    @Transactional
    public void updatePost(int id, PostDTO postDTO){
        Post existingPost = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        Post post = convertToPost(postDTO);

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
