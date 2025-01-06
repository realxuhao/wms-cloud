package com.bosch.product.service;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class SFTPService {
    @Autowired
    private SFtpConfig sftpConfig;

    /**
     * 创建SFTP连接
     */
    private ChannelSftp createSftp() throws JSchException {
        JSch jsch = new JSch();
        log.info("Try to connect sftp[" + sftpConfig.getUsername() + "@" + sftpConfig.getHost() + "]");

        Session session = createSession(jsch, sftpConfig.getHost(), sftpConfig.getUsername(), sftpConfig.getPort());
        session.setPassword(sftpConfig.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        // 默认情况下，JSch库本身并没有会话超时时间。
        // 为了避免长时间无活动连接占用资源或因网络问题导致连接挂起而不被释放，通常建议设置会话超时，（单位：毫秒）
        session.setTimeout(30000);
        session.connect();

        log.info("Session connected to {}.", sftpConfig.getHost());

        Channel channel = session.openChannel(sftpConfig.getProtocol());
        channel.connect();

        log.info("Channel created to {}.", sftpConfig.getHost());

        return (ChannelSftp) channel;
    }

    /**
     * 创建 Session
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws JSchException {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }

        if (session == null) {
            throw new RuntimeException(host + "session is null");
        }

        return session;
    }

    /**
     * 关闭连接
     */
    private void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    log.error("sftp 连接已关闭");
                }
                if (sftp.getSession() != null) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            log.error("sftp 断开连接失败，原因：{}", e.getMessage(), e);
        }
    }

    /**
     * 判断目录是否存在
     */
    private boolean isFileExist(String sftpPath, ChannelSftp sftp) {
        try {
            // 获取文件信息
            SftpATTRS sftpATTRS = sftp.lstat(sftpPath);
            return sftpATTRS != null;
        } catch (Exception e) {
            log.error("判断文件是否存在失败，原因：{}", e.getMessage(), e);
            return false;
        }
    }


    /**
     * 上传文件
     *
     * @param localFilePath
     * @param remoteFileName
     */
    public void uploadFile(String localFilePath, String remoteFileName) {
        // 上传文件
        ChannelSftp sftp = null;
        try (FileInputStream inputStream = new FileInputStream(localFilePath)) {
            // 开启sftp连接
            sftp = createSftp();
            // 进入sftp文件目录
            sftp.cd(sftpConfig.getRemoteDirectory());
            log.info("修改目录为：{}", sftpConfig.getRemoteDirectory());

            // 上传文件
            sftp.put(inputStream, remoteFileName);
            log.info("上传文件成功，目标目录：{}", sftpConfig.getRemoteDirectory());
        } catch (SftpException | JSchException | IOException e) {
            log.error("上传文件失败，原因：{}", e.getMessage(), e);
            throw new RuntimeException("上传文件失败");
        } finally {
            // 关闭sftp
            disconnect(sftp);
            File file = new File(localFilePath);
            if (file.exists() && file.isFile()) {
                boolean success = file.delete();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param remoteFileName
     * @param localFilePath
     */
    public void downloadFile(String remoteFileName, String localFilePath) {
        long start = System.currentTimeMillis();
        ChannelSftp sftp = null;
        String sftpPath = sftpConfig.getRemoteDirectory() + "/" + remoteFileName;
        try {
            // 开启sftp连接
            sftp = createSftp();
            // 判断sftp文件存在
            boolean isExist = isFileExist(sftpPath, sftp);
            if (isExist) {
                // 下载文件
                FileOutputStream outputStream = new FileOutputStream(localFilePath);
                sftp.get(sftpPath, outputStream);
                outputStream.close();
                // 记录日志
                long time = System.currentTimeMillis() - start;
                log.info("sftp文件下载成功，目标文件：{}，总耗时：{}ms.", sftpPath, time);
            } else {
                log.error("sftp文件下载失败，sftp文件不存在：" + sftpPath);
                throw new RuntimeException("sftp文件下载失败，sftp文件不存在：" + sftpPath);
            }
        } catch (SftpException | JSchException | IOException e) {
            log.error("sftp文件下载失败，目标文件名：{}，原因：{}", sftpPath, e.getMessage(), e);
            throw new RuntimeException("sftp文件下载失败");
        } finally {
            // 关闭sftp
            disconnect(sftp);
        }
    }
}
