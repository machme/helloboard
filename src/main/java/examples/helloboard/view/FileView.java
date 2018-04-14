package examples.helloboard.view;


import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@Component("fileView")
public class FileView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = (File)map.get("file");

        response.setContentType(getContentType());
        response.setContentLength((int)file.length());

        String userAgent = request.getHeader("User-Agent");
        String filename = file.getName();

        if (userAgent.contains("Trident")||(userAgent.indexOf("MSIE")>-1)) {
            logger.debug("user-agent:IE under 10 : "+userAgent );
            filename= URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if(userAgent.contains("Chrome")) {
            logger.debug("user-agent:Chrome : "+userAgent);
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        } else if(userAgent.contains("Opera")) {
            logger.debug("user-agent:Opera : "+userAgent);
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        } else if(userAgent.contains("Firefox")) {
            logger.debug("user-agent:Firefox : "+userAgent);
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(Exception e){}
            }
        }
        out.flush();
    }
}
