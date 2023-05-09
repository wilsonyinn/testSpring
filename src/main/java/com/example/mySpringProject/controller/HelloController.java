package com.example.mySpringProject.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.mySpringProject.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
@RestController
@RequestMapping("/file")
public class HelloController {
    @Autowired
    private StorageService service;
    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @GetMapping("/hello")
    public ArrayList<String> listBucket() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix("testUser01").withDelimiter("");;

        ObjectListing objects = s3Client.listObjects(listObjectsRequest);

        List<S3ObjectSummary> summaries = objects.getObjectSummaries();
        ArrayList<String> obj_keys = new ArrayList<>();
        for (S3ObjectSummary objectSummary : summaries){
            obj_keys.add(objectSummary.getKey());
        }
        return obj_keys;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "path") String path) {
        return service.uploadFile(file, path);
    }

    @PostMapping("/uploadFolder")
    public String uploadFolder(@RequestParam(value = "path") String path){
        return service.uploadFolderToDestination(path);
    }

    @PutMapping("/rename")
    public String rename(@RequestParam(value = "key1") String fileKey, @RequestParam(value = "key2") String newFileKey)
    {
        service.rename(fileKey, newFileKey);
        return "Successful Rename: " + fileKey + " to " + newFileKey;
    }

}