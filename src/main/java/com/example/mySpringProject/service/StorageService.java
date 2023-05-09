package com.example.mySpringProject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service

public class StorageService {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

//    public String uploadFileToDestination(MultipartFile file, String keyName)
//    {
//        File fileObject = convertMultiPartFileToFile(file);
//
//        s3Client.putObject(new PutObjectRequest(bucketName, keyName, fileObject));
//        fileObject.delete();
//        return("File uploaded to " + keyName);
//    }
    public String uploadFile(MultipartFile file, String filePath) {
        File fileObject = convertMultiPartFileToFile(file);

        s3Client.putObject(new PutObjectRequest(bucketName, filePath, fileObject));
        fileObject.delete();

        return "File uploaded to: " + filePath;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println("Error"); // add exception logging for this service later //this line could be issue
        }

        return convertedFile;
    }

    public String uploadFolderToDestination(String path){
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        s3Client.putObject(new PutObjectRequest(bucketName, path, "/"));
        return "Folder created at " + path;
    }

    public void rename(String fileKey, String newFileKey) {
        s3Client.copyObject(bucketName, fileKey, bucketName, newFileKey);
        s3Client.deleteObject(bucketName, fileKey);
    }

}

