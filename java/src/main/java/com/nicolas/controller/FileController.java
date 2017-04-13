package com.nicolas.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by nsanto1 on 4/2/17.
 */
@Controller
public class FileController {
    final String UPLOAD_DIR = "/tmp/";

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        InputStream is = new FileInputStream(UPLOAD_DIR + id);
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> postFile(@RequestParam("data") MultipartFile file) throws IOException{
        String id = UUID.randomUUID().toString();
        file.transferTo(new File(UPLOAD_DIR + id));
        return ResponseEntity
                .ok()
                .body(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeFile(@PathVariable("id") String id){
        File deleteFile = new File(UPLOAD_DIR + id);
        deleteFile.delete();
        return ResponseEntity
                .ok()
                .body("");

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> putFile(@RequestParam("data") MultipartFile file, @PathVariable("id") String id, HttpServletResponse response) throws IOException {
        file.transferTo(new File(UPLOAD_DIR + id));
        return ResponseEntity
                .ok()
                .body(id);
    }
}
