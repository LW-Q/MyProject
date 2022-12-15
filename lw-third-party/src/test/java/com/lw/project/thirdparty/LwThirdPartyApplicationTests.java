package com.lw.project.thirdparty;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class LwThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Test
    void testUpload() {
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessId = "yourAccessKeyId";
        String accessKey = "yourAccessKeySecret";
// Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
// 填写Bucket名称，例如examplebucket。
        String bucket = "examplebucket";
// 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://examplebucket.oss-cn-hangzhou.aliyuncs.com";
// 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
        String callbackUrl = "https://192.168.0.0:8888";
// 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = "exampledir/";
        String filePath = "C:\\Users\\Over\\Pictures\\head.jpg";
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest("lw-project", "picture.jps", new File(filePath));

        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
    }

}
