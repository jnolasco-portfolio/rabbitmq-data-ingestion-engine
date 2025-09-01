package cloud.jnolasco.rabbitmq.file_upload_service.controller;

import cloud.jnolasco.rabbitmq.file_upload_service.config.StorageProperties;
import cloud.jnolasco.rabbitmq.file_upload_service.dto.FileUploadEvent;
import cloud.jnolasco.rabbitmq.file_upload_service.publisher.UpLoadProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@EnableConfigurationProperties(StorageProperties.class)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/upload")
public class FileUploadController {

    private final StorageProperties storageProperties;
    private final UpLoadProducer upLoadProducer;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("jobId") UUID jobId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            String fileName = UUID.randomUUID() + ".csv";
            String tempFilePath = storageProperties.path() + "/" + fileName;

            file.transferTo(new java.io.File(tempFilePath));

            FileUploadEvent event = FileUploadEvent.builder()
                    .id(UUID.randomUUID())
                    .jobId(jobId)
                    .originalName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .filePath(tempFilePath)
                    .build();
            upLoadProducer.sendMessage(event);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
