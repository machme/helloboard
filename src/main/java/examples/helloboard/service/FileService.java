package examples.helloboard.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void fileUpload(MultipartHttpServletRequest request) throws Exception;
}
