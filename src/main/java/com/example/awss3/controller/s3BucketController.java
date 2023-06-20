package com.example.awss3.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class s3BucketController {
    private final AmazonS3 amazonS3;

    private String bucketName;


    public s3BucketController(AmazonS3 amazonS3, @Value("${aws.bucket.name}") String bucketName) {
        this.bucketName = bucketName;

        this.amazonS3 = amazonS3;
    }
    @GetMapping("getBucketList")
    public List<Bucket> getBucketList(){
       return amazonS3.listBuckets();
    }

    @PostMapping("uploadFile")
    public void uploadFile(@RequestPart(value = "file") @NonNull MultipartFile multipartFile) throws IOException {

        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        String generatedFileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");

        amazonS3.putObject(new PutObjectRequest(bucketName, generatedFileName, file)
                .withCannedAcl(CannedAccessControlList.Private));
        file.delete();
    }

    @GetMapping("getFiles")
    public void getFilesInBucket(){
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        List<String> keys = objectSummaries.stream().map(S3ObjectSummary::getKey).toList();

        keys.forEach(key-> {
            S3Object object = amazonS3.getObject(bucketName, key);
            System.out.println(object.toString());
        });

    }

    @DeleteMapping("deleteFile")
    public void deleteFile(@RequestParam String key){
        amazonS3.deleteObject(bucketName,key);
        getFilesInBucket();
    }

}
