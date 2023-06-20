package com.example.awss3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AwsS3Application {


	public static void main(String[] args) {
		SpringApplication.run(AwsS3Application.class, args);


	}



}
