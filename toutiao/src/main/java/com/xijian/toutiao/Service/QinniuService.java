package com.xijian.toutiao.Service;

import com.xijian.toutiao.util.ToutiaoUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class QinniuService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    public String saveImage(MultipartFile file) throws Exception {

        int dopPos=file.getOriginalFilename().lastIndexOf(".");
        if(dopPos<0){
            return  null;
        }
        String fileExt =file.getOriginalFilename().substring(dopPos+1);
        if(!ToutiaoUtil.IsFileAllowed(fileExt.toLowerCase())){
            return null;
        }
        String fileName = UUID.randomUUID().toString().replace("-","")+"."+fileExt;
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "2RR2BBnEphqNK-fsLbdjzrFuH3CP9BlPxW5evyOj";
        String secretKey = "KCG7g8T6_AK0NgipWNq93enCWK3NT3hlZJVWnajg";
        String bucket = "photo";
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            return ToutiaoUtil.QINNIU_DOMAIN+putRet.key;
        } catch (QiniuException ex) {
            logger.error("七牛云上传失败！"+ ex.getMessage());
            Response r = ex.response;
            System.err.println(r.toString());
        }
        return null;
    }
}
