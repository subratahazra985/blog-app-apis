package com.subro.blog.services.impl;

import com.subro.blog.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    /**
     * Uploads a file to the specified path and returns the name of the file which can be used to retrieve the file.
     *
     * @param path the path where the file is to be uploaded
     * @param multipartFile the file which is to be uploaded
     * @return the name of the uploaded file
     * @throws IOException
     */
    @Override
    public String uploadImage(String path, MultipartFile multipartFile) throws IOException {
        String name=multipartFile.getOriginalFilename();
        String randomId= UUID.randomUUID().toString();
        String fileName=randomId.concat(name.substring(name.lastIndexOf(".")));
        String fullPath=path + File.separator +fileName;
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        Files.copy(multipartFile.getInputStream(), Paths.get(fullPath));
        return fileName;
    }

    /**
     * Returns an InputStream of a file from the specified path.
     *
     * @param path the path where the file is located
     * @param fileName the name of the file which is to be retrieved
     * @return an InputStream of the file
     * @throws FileNotFoundException if the file is not found
     */
    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
