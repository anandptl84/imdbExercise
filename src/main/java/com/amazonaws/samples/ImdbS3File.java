package com.amazonaws.samples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

/**
 * Sample class for downloading name.basics.tsv.gz from the 'current' folder in the
 * imdb-datasets s3 bucket.
 *
 * Use with AWS Java SDK 1.11.156 or later.
 */

public class ImdbS3File {
    private static String bucketName = "imdb-datasets"; 
    private static String key        = "documents/v1/current/title.ratings.tsv.gz";      
    
    public static void main(String[] args) throws IOException, InterruptedException {
        ProfileCredentialsProvider credentialsProvider =
                new ProfileCredentialsProvider("default");

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);

        try {
            // Note: It's necessary to set RequesterPays to true
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key)
                                                    .withRequesterPays(true);

            System.out.println("Downloading object");

            S3Object s3object = s3Client.getObject(getObjectRequest);

            System.out.println("Content-Type: "  + 
                    s3object.getObjectMetadata().getContentType());

            writeFile(s3object.getObjectContent());
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void writeFile(InputStream input) throws IOException, InterruptedException {

    				
        byte[] buf = new byte[1024 * 1024];
        
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(input))); ) {
//             String line;
//             while((line = reader.readLine()) != null) {
//             		System.out.println(Arrays.toString(line.split("\\\t")));
//             }
//         } finally  {
//        	 	input.close();
//         }
        
//        while (String line: rea)
        
        OutputStream out = new FileOutputStream("title.ratings.tsv.gz");
        

       int count;
        while ((count = input.read(buf)) != -1) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            out.write(buf, 0, count);
        }
        out.close();
        input.close();
    }
}