package es.urjc.code.s3_ec2_practica1.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.code.s3_ec2_practica1.services.AmazonClientService;

@RestController
@RequestMapping("/api/")
public class BucketController {

	@Autowired
	private AmazonClientService amazonClient;

	@GetMapping("/buckets")
	public List<String> getBuckets() {
		return amazonClient.getBuckets();
	}

	@GetMapping("/buckets/{bucket_name}")
	public List<String> getObjectBuckets(@PathVariable String bucket_name) {
		return amazonClient.getObjectBuckets(bucket_name);
	}

	@PostMapping("/buckets/{bucket_name}")
	@ResponseStatus(HttpStatus.CREATED)
	public String createBucket(@PathVariable String bucket_name) {
		return amazonClient.createBucket(bucket_name);
	}

	@PostMapping("/buckets/{bucket_name}/uploadObject")
	@ResponseStatus(HttpStatus.CREATED)
	public void createObjectBucket(@PathVariable String bucket_name,
			@RequestParam("file") MultipartFile multiPartFile) throws IllegalStateException, IOException {
				amazonClient.createObjectBucket(bucket_name, multiPartFile);
	}


	@DeleteMapping("/buckets/{bucket_name}")
	public ResponseEntity<String> deleteBucket(@PathVariable String bucket_name) {
		return amazonClient.deleteBucket(bucket_name);
    }
    
    @DeleteMapping("/buckets/{bucket_name}/{object_name}")
	public ResponseEntity<String> delete(@PathVariable String bucket_name, @PathVariable String object_name) {
		return amazonClient.deleteObjectBucket(bucket_name, object_name);
	}

}