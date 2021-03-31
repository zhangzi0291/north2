package com.north.sys.controller;


import cn.hutool.crypto.digest.MD5;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.file.FileControlHandler;
import com.north.sys.dto.UploadDto;
import com.north.sys.entity.SysFile;
import com.north.sys.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-18
 */
@Tag(name = "sys-file-controller 文件相关")
@RestController
@RequestMapping("/sysFile")
public class SysFileController extends BaseController<SysFile, ISysFileService> {

    @Resource
    private ISysFileService sysFileService;
    @Resource
    private FileControlHandler fileControlHandler;


    /**
     * 上传文件
     *
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @Operation(summary = "上传文件", description = "md5相同的文件直接返回记录")
    @RequestMapping(path = "upload", method = {RequestMethod.GET, RequestMethod.POST})
    public R<UploadDto> upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        //校验文件md5，如果md5相同是同一个文件不需要重复保存
        String md5 = MD5.create().digestHex(file.getInputStream());
        SysFile sysFile = sysFileService.getSysFileByMD5(md5);
        if (sysFile == null) {
            //保存文件
            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileName = uuid + originalFilename.substring(originalFilename.lastIndexOf("."));
            fileControlHandler.saveFile(file.getInputStream(), fileName);
            //记录文件记录
            sysFile = new SysFile();
            sysFile.setFileName(fileName);
            sysFile.setOriginalName(originalFilename);
            sysFile.setFileSize(file.getSize());
            sysFile.setUploadTime(LocalDateTime.now());
            sysFile.setFilePath(fileName);
            sysFile.setMd5Value(md5);
            super.addJson(sysFile);
        }
        //返回访问的Url和ID
        UploadDto dto = new UploadDto();
        dto.setFileId(sysFile.getId());
        String http = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        dto.setDownloadUrl(http + "/sysFile/download?id=" + sysFile.getId());

        return R.ok(dto);
    }

    /**
     * 下载文件
     *
     * @param id       SysFile的ID
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @Operation(summary = "下载文件", description = "下载文件")
    @RequestMapping(path = "download", method = {RequestMethod.GET, RequestMethod.POST})
    public R download(String id, HttpServletResponse response) throws UnsupportedEncodingException {
        SysFile file = this.get(id).getData();
        if (file == null) {
            return R.failed("文件不存在");
        }
        Path filePath = Paths.get(file.getFilePath());

        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(file.getOriginalName(), "UTF-8"));

        try {
            InputStream inputStream = fileControlHandler.loadFileInputStream(file.getFilePath());
            OutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024 * 1024];
            int len;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("download file error:", e);
            return R.failed("下载错误");
        }

        return null;
    }

    public static void main(String[] args) {
        Path saveDir = Paths.get("./upload");
        System.out.println(saveDir.toUri().toString());
    }
}
