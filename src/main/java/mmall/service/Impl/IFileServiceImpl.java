package mmall.service.Impl;

import com.google.common.collect.Lists;
import com.sun.jnlp.FileSaveServiceImpl;
import mmall.service.IFileService;
import mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class IFileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileSaveServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("." + 1));
        String uplloadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：(),上传路径：（），新的文件名：（）", fileName, path, uplloadFileName);
        //目录文件
        File file1Dir = new File(path);
        if (!file1Dir.exists()) {
            file1Dir.setWritable(true);
            file1Dir.mkdir();
        }
        File targetFile = new File(path, uplloadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功了
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件上传异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
