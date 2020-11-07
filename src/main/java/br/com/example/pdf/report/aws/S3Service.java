package br.com.example.pdf.report.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;

public class S3Service {

    public void uploadPdfToS3(File file){
        Regions clientRegion = Regions.US_EAST_1;
        String bucketName = "test-upload-pdf";
        String stringObjKeyName = file.getName() + ".pdf";

        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAQ475PCEFEGES5TFC", "gQ1wR6IhgraCuGcJuw0iDvLrmsgaPMHwwL5qza3Z");

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(clientRegion)
                    .build();

            // Upload a text string as a new object.
            s3Client.putObject(bucketName, stringObjKeyName, file);

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

}
