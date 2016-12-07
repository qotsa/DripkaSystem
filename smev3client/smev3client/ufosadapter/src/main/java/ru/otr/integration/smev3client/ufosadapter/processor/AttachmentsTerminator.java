package ru.otr.integration.smev3client.ufosadapter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

/**
 * Created by nikitin.maksim on 30.11.2016.
 */
public class AttachmentsTerminator implements Processor {

    private String server;
    private int port;
    private String user;
    private String pass;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            String dirPath = "/replication/fs/" + exchange.getIn().getHeader("breadcrumbId");
            removeDirectory(ftpClient, dirPath, "");

            dirPath = "/replication/embedded/" + exchange.getIn().getHeader("breadcrumbId");
            removeDirectory(ftpClient, dirPath, "");

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void removeDirectory(FTPClient ftpClient, String parentDir,
                                       String currentDir) throws IOException {
        String dirToList = parentDir;

        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] files = ftpClient.listFiles(dirToList);

        if (files != null && files.length > 0) {
            for (FTPFile file : files) {
                String currentFileName = file.getName();

                if (currentFileName.equals(".") || currentFileName.equals("..")) continue;

                String filePath = parentDir + "/" + currentDir + "/" + currentFileName;

                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                if (file.isDirectory()) {
                    removeDirectory(ftpClient, dirToList, currentFileName);
                } else {
                    boolean deleted = ftpClient.deleteFile(filePath);
                }
            }

            boolean removed = ftpClient.removeDirectory(dirToList);
        }
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
