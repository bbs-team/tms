package com.bbs.tms.controller;

import com.bbs.tms.entity.Video;
import com.bbs.tms.repository.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@RequestMapping("/api/video")
public class videoController{

    @Autowired
    private VideoRepository videoRepo;

    @GetMapping(value = "")
    public Iterable<Video> getallvedio(){
        return videoRepo.findAll();
    }

    @GetMapping(value = "/{kind}")
    public Object getKindOfVedio(@PathVariable String kind){
        return videoRepo.findByKind(kind);
    }

}