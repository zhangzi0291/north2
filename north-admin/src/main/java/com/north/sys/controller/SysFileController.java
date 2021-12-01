package com.north.sys.controller;


import cn.hutool.crypto.digest.MD5;
import com.north.base.BaseController;
import com.north.base.Constant;
import com.north.base.api.R;
import com.north.file.FileControlHandler;
import com.north.sys.dto.UploadDto;
import com.north.sys.entity.SysFile;
import com.north.sys.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
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
    public R<UploadDto> upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, String moduleName, String relationId) throws IOException {
        return uploadWithMd5Check(request, file, moduleName, relationId, true);
    }


    /**
     * 上传文件
     *
     * @param request
     * @param file
     * @param checkmd5 是否校验MD5,是的话md5重复不再重新保存
     * @return
     * @throws IOException
     */
    @RequestMapping(path = "uploadWithMd5Check", method = {RequestMethod.GET, RequestMethod.POST})
    public R<UploadDto> uploadWithMd5Check(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, String moduleName, String relationId, Boolean checkmd5) throws IOException {
        //校验文件md5，如果md5相同是同一个文件不需要重复保存
        String md5 = MD5.create().digestHex(file.getInputStream());
        SysFile sysFile = sysFileService.getSysFileByMD5(md5);
        //文件不存在或者不校验MD5重复的话就保存，否则直接返回已知的文件ID
        if (sysFile == null || Boolean.FALSE.equals(checkmd5)) {
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
            if (StringUtils.hasLength(moduleName)) {
                sysFile.setModuleName(moduleName);
            }
            if (StringUtils.hasLength(relationId)) {
                sysFile.setRelationId(relationId);
            }
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
     * 下载文件 通过ID
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
            if (inputStream == null) {
                return R.failed("下载错误,文件不存在");
            }
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


    /**
     * 调用saveFile接口保存上传的文件
     *
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    public UploadDto saveFile(HttpServletRequest request, MultipartFile file) throws IOException {
        R<UploadDto> r = this.upload(request, file, Constant.SYS_MODULE_NAME, null);
        return r.getData();
    }

}
