package com.ssafy.stargate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tests/mp")
public class MultipartController {

    @GetMapping("/get")
    public void test() {
        System.out.println("QWER");
    }

    @PostMapping("/upload")
    public void test2(@RequestParam("file") MultipartFile file) throws IOException {
        /*
        System.out.println(file.getName());
        System.out.println(file.getBytes());
        System.out.println(file.getContentType());
        System.out.println(file.getSize());
        System.out.println(file.getResource());
        */

        // set path (stargate-be부터)
        Path root = Paths.get("public/uploads");
        String filename = "testimage.png";


        // create upload
        Files.createDirectories(root);

        // upload files
//        Files.copy(file.getInputStream(), root.resolve(filename));

        // delete files
        Files.deleteIfExists(root.resolve(filename));

        // load files
        System.out.println(root.resolve("seqeuuu.drawio3.png").toUri());
        Resource resource = new UrlResource(root.resolve("seqeuuu.drawio3.png").toUri());
        System.out.println(resource);
        System.out.println(resource.exists());
        System.out.println(resource.isReadable());
    }
}
