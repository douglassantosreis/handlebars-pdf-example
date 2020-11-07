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
            BasicAWSCredentials awsCreds = new BasicAWSCredentials("XXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXX");

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(clientRegion)
                    .build();

            s3Client.putObject(bucketName, stringObjKeyName, file);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

}
