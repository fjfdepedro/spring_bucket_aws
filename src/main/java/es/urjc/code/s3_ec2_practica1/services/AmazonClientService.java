package es.urjc.code.s3_ec2_practica1.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AmazonClientService {

    public static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();

    public List<String> getBuckets() {
        List<String> bucketList = new ArrayList<String>();
        for (Bucket s : s3.listBuckets()) {
            bucketList.add(s.getName());
        }
        return bucketList;
    }

    public List<String> getObjectBuckets(String bucketName) {
        List<String> objectBucketList = new ArrayList<String>();
        ObjectListing objectListing = s3.listObjects(bucketName);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            objectBucketList.add(os.getKey());
        }
        return objectBucketList;
    }

    public String createBucket(String bucketName) {
        if(s3.doesBucketExistV2(bucketName)) {
            return "Error: bucket name: " + bucketName + " is not available";
        }
        s3.createBucket(bucketName);
        return "New bucket created!";
    }

    public void createObjectBucket(String bucketName, MultipartFile multiPartFile) throws IllegalStateException, IOException {
        String fileName = multiPartFile.getOriginalFilename();
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multiPartFile.transferTo(file);

        PutObjectRequest por = new PutObjectRequest(
          bucketName, 
          fileName,
          file
        );
        por.setCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(por);
    }

    public ResponseEntity<String> deleteBucket(String bucketName){
        try {
            s3.deleteBucket(bucketName);
            return new ResponseEntity<>("Bucket:" + bucketName + " borrado correctamente",HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("No se ha podido borrar el bucket:" + bucketName + ", el error es:" + e,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> deleteObjectBucket(String bucketName, String objectName){
        try {
            s3.deleteObject(bucketName, objectName);
            return new ResponseEntity<>("Borrado objecto:" + objectName + " del bucket:"  + bucketName + "correctamente",HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("No se ha podido borrar el objecto:" + objectName + " del bucket:"  + bucketName +  ", el error es:" + e,HttpStatus.NOT_FOUND);
        }
    }
}