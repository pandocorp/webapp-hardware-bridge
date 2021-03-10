package tigerworkshop.webapphardwarebridge.services;

import java.io.*;

import org.bouncycastle.util.encoders.Base64;

import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import tigerworkshop.webapphardwarebridge.Config;
import tigerworkshop.webapphardwarebridge.responses.PrintDocument;
import tigerworkshop.webapphardwarebridge.utils.DownloadUtil;

public class DocumentService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DocumentService.class.getName());
    private static final DocumentService instance = new DocumentService();
    private static final SettingService settingService = SettingService.getInstance();

    private DocumentService() {
        File directory = new File(Config.DOCUMENT_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static DocumentService getInstance() {
        return instance;
    }

    public static void extract(String base64, String urlString) throws Exception {
        byte[] bytes = Base64.decode(base64);

        try (OutputStream stream = new FileOutputStream(getPathFromUrl(urlString))) {
            stream.write(bytes);
        }
    }

    public static void download(String urlString) throws Exception {
        DownloadUtil.file(urlString, getPathFromUrl(urlString), true, settingService.getSetting().getIgnoreTLSCertificateErrorEnabled(), settingService.getSetting().getDownloadTimeout());
    }

    public static File getFileFromUrl(String urlString) {
        return new File(getPathFromUrl(urlString));
    }

    public static void deleteFileFromUrl(String urlString) {
        getFileFromUrl(urlString).delete();
    }

    public static String getPathFromUrl(String urlString) {
        if(!urlString.contains("http"))
        {
            return urlString;
        }
        urlString = urlString.replace(" ", "%20");
        String filename = urlString.substring(urlString.lastIndexOf("/") + 1);
        return Config.DOCUMENT_PATH + filename;
    }

    public void prepareDocument(PrintDocument printDocument) throws Exception {
        if (printDocument.getRawContent() != null && !printDocument.getRawContent().isEmpty()) {
            return;
        }

        if (printDocument.getUrl() == null && printDocument.getFileContent() == null) {
            throw new Exception("URL is null");
        }

        if (printDocument.getFileContent() != null) {
            extract(printDocument.getFileContent(), printDocument.getUrl());
        } else {
            download(printDocument.getUrl());
        }
    }

    public static List<String> getFilesFromLocal(String folderPath, String filter) throws IOException {
        logger.info("Get files from local started");
        List<String> localFiles = new ArrayList<String>();
        if (folderPath.isEmpty()) {
            logger.info("While getting files from Shared drive since path is sent as empty getting path from Settings as->" + settingService.getSetting().getSharedDriveLocation());
            folderPath = settingService.getSetting().getSharedDriveLocation();
        }
        File directory = new File(folderPath);
        System.out.println(directory);
        logger.info(String.valueOf(directory));
        try (Stream<Path> walk = Files.walk(Paths.get(
                folderPath))) {
            List<Path> result = walk.filter(Files::isRegularFile)
                    .filter(x -> x.getFileName().toString().startsWith(filter)).collect(Collectors.toList());
            if (result.isEmpty()) {
                logger.info("List of files with specified delivery number criteria is empty in " + folderPath);
                throw new FileNotFoundException("No pdf files start with delivery number " + filter + " in shared drive path");
            }
            result.forEach(file -> {
                        file = file.normalize();
                        System.out.println("Document Print isLocal from Folder " + file.toString());
                        logger.info("Document Print isLocal from Folder " + file.toString());
                        System.out.println(file.getFileName());
                        localFiles.add(file.toString());
                    }
            );
        } catch (IOException e) {

            logger.info(e.getMessage());
            e.printStackTrace();
            if (e instanceof FileSystemException) {
                throw new FileSystemException("Shared drive path not found. Please check the path specified in Pando Print Bridge Configurator " + folderPath);
            }
            throw e;
        }
        logger.info("Get files from local completed");
        return localFiles;
    }

/*
	public static List<String> getFilesFromLocal(String folderPath, String filter) throws IOException {
		List<String> localFiles = new ArrayList<String>();
		File directory = new File(folderPath);
		 for (Path path : Files.newDirectoryStream(Paths.get(folderPath),
                 path -> path.toFile().isFile()
//                         && path.getFileName().startsWith(filter)
         )) {
			 path = path.normalize();
			 logger.info("Document Print isLocal from Folder " + path.toString());
             System.out.println(path.getFileName());
             localFiles.add(path.toString());
		 }

		return localFiles;
	}

 */
    public static void main(String[] args) throws Exception {
        download("https://abc-test-pando.s3.ap-south-1.amazonaws.com/123_packing_list.pdf");
    }
}
