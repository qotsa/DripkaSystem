package ru.otr.integration.smev3client.smev3mock2.ftp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3mock2.config.AppProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tartanov.mikhail on 10.10.2016.
 */

@Component
public class FtpServerSmevMock {

    private final Logger logger = LoggerFactory.getLogger(FtpServerSmevMock.class);

    @Autowired
    AppProperties appProperties;

    @PostConstruct
    public void createFtp() {

        logger.debug("-----Starting createFtp()-----");
        FakeFtpServer fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(appProperties.getPort());

        FileSystem fileSystem = new UnixFakeFileSystem();
        fakeFtpServer.addUserAccount(new UserAccount("user_STUB2", "qwerty123", "/"));
        logger.debug("-----Creating file entry-----");
        fileSystem.add(new FileEntry("/data/file1.txt", "file1"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
        logger.debug("-----Leaving createFtp()-----");
    }

    public String writeToFtp(String localURI, String targetURI)
            throws Exception {
        logger.debug("-----Leaving writeToFtp with parameters:\n" +
                "localURI: " + localURI + "\n" +
                "targetURI: " + targetURI + "\n" + "-----");
        if (StringUtils.isEmpty(localURI) || StringUtils.isEmpty(targetURI)) {
            throw new IllegalArgumentException(
                    "fromLocalToFtp() : arguments cannot be null or empty");
        }

        FileSystemOptions targetOpts = new FileSystemOptions();
        FileSystemOptions localOpts = new FileSystemOptions();

        FtpFileSystemConfigBuilder targetConfigBuilder = FtpFileSystemConfigBuilder
                .getInstance();

        FileSystemConfigBuilder localConfigBuilder = FtpFileSystemConfigBuilder
                .getInstance();


        targetConfigBuilder.setPassiveMode(targetOpts, true);
        targetConfigBuilder.setControlEncoding(targetOpts, "UTF-8");

        FileSystemManager e = VFS.getManager();
        //StandardFileSystemManager e = new StandardFileSystemManager();
        //e.init();
        FileObject lPath = e.resolveFile(localURI);
        FileObject tPath = e.resolveFile(targetURI, targetOpts);
        //e.close();
        if (lPath == null) {
            throw new IOException("Local file cannot be resolved: "
                    + localURI);
        }
        if (tPath == null) {
            throw new IOException("Target file cannot be resolved: "
                    + targetURI);
        }
        if (!lPath.exists()) {
            throw new Exception("File does not exist: " + localURI);
        }
        tPath.copyFrom(lPath, Selectors.SELECT_SELF);

        return targetURI;
    }

    public Boolean writeToFtp(InputStream inputStream, String targetURI) throws IOException {

        logger.debug("-----Starting writeToFtp(InputStream inputStream, String targetURI)-----");

        String server = "localhost";
        int port = appProperties.getPort();
        String user = appProperties.getUser();
        String pass = appProperties.getPassword();

        FTPClient ftpClient = new FTPClient();

        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        boolean done = ftpClient.storeFile(targetURI, inputStream);
        inputStream.close();

        return done;
    }


}
