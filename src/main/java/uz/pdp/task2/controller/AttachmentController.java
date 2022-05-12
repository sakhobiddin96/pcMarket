package uz.pdp.task2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.task2.entity.Attachment;
import uz.pdp.task2.entity.AttachmentContent;
import uz.pdp.task2.repository.AttachmentContentRepository;
import uz.pdp.task2.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @PostMapping
    public HttpEntity<?> addAttachment(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        Attachment attachment= new Attachment();
        attachment.setFileOriginalName(file.getOriginalFilename());
        attachment.setContentType(file.getContentType());
        attachment.setSize(file.getSize());
        Attachment savedAttachment = attachmentRepository.save(attachment);
        AttachmentContent attachmentContent=new AttachmentContent();
        attachmentContent.setAttachment(savedAttachment);
        attachmentContent.setBytes(file.getBytes());
        attachmentContentRepository.save(attachmentContent);
        return ResponseEntity.ok("File saved");

    }
    @GetMapping("/{id}")
    public void getAttachment(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()){
            Attachment attachment = optionalAttachment.get();
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachment_Id(id);
            if (optionalAttachmentContent.isPresent()){
                AttachmentContent attachmentContent = optionalAttachmentContent.get();
                response.setHeader("Content-Disposition","attachment; filename=\""+attachment.getFileOriginalName()+"\"");
                response.setContentType(attachment.getContentType());
                FileCopyUtils.copy(attachmentContent.getBytes(),response.getOutputStream());

            }
        }


    }
    @DeleteMapping("/{id}")
    public String deleteAttachment(@PathVariable Integer id){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()){
            attachmentRepository.deleteById(id);
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachment_Id(id);
            if (optionalAttachmentContent.isPresent()){
                attachmentContentRepository.deleteByAttachment_Id(id);
                return "Deleted";
            }
        }
        return "Attachment not found";
    }
    @PutMapping("/{id}")
    public String editAttachment(MultipartHttpServletRequest request,@PathVariable Integer id) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()){
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachment_Id(id);
            if (optionalAttachmentContent.isPresent()){
                Attachment attachment = optionalAttachment.get();
                AttachmentContent attachmentContent = optionalAttachmentContent.get();
                Iterator<String> fileNames = request.getFileNames();
                MultipartFile file = request.getFile(fileNames.next());
                attachment.setName(file.getName());
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());
                attachment.setFileOriginalName(file.getOriginalFilename());
                attachmentRepository.save(attachment);
                attachmentContent.setBytes(file.getBytes());
                attachmentContentRepository.save(attachmentContent);
                return "Attachment edited";
            }
            return "ATTACHMENT NOT FOUND";
        }
        return "ATTACHMENT NOT FOUND";
    }
}
