package com.exrates.inout.service.impl;

import com.exrates.inout.service.UserFilesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Arrays.asList;

@Service
@PropertySource("classpath:/uploadfiles.properties")
public class UserFilesServiceImpl implements UserFilesService {

    private @Value("${upload.userFilesDir}")
    String userFilesDir;
    @Value("${upload.userFilesLogicalDir}")
    private String userFilesLogicalDir;

    private final Set<String> contentTypes;

    private static final Logger LOG = LogManager.getLogger(UserFilesServiceImpl.class);

    public UserFilesServiceImpl() {
        contentTypes = new HashSet<>();
        contentTypes.addAll(asList("image/jpg", "image/jpeg", "image/png"));
    }

    public boolean checkFileValidity(final MultipartFile file) {
        return !file.isEmpty() && contentTypes.contains(extractContentType(file));
    }

    public String saveReceiptScan(final int userId, final int invoiceId, final MultipartFile file) throws IOException {
        final Path path = Paths.get(userFilesDir + userId, "receipts");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String baseFilename = new StringJoiner("_").add("receipt")
                .add(String.valueOf(invoiceId))
                .add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")))
                .toString();
        Path logicalPath = writeUserFile(path, Paths.get(userFilesLogicalDir, String.valueOf(userId), "receipts"), baseFilename, file);
        return logicalPath.toString();
    }

    private Path writeUserFile(Path initialPath, Path logicalPath, String baseFilename, MultipartFile file) throws IOException {
        Path realPath = null;
        Path logicalFilePath;
        try {
            final String name = baseFilename + "." + extractFileExtension(file);
            realPath = Paths.get(initialPath.toString(), name);
            logicalFilePath = Paths.get(logicalPath.toString(), name);
            Files.write(realPath, file.getBytes());
            return logicalFilePath;
        } catch (final IOException e) {
            if (realPath != null) {
                final List<IOException> exceptions = new ArrayList<>();
                try {
                    Files.delete(realPath);
                } catch (final IOException ex) {
                    ex.initCause(e);
                    exceptions.add(ex);
                }
                if (!exceptions.isEmpty()) {
                    LOG.error("Exceptions during deleting uploaded files " + exceptions);
                }
            }
            throw e;
        }
    }


    private String extractContentType(final MultipartFile file) {
        return file.getContentType().toLowerCase();
    }

    /**
     * @param file - Uploaded file
     * @return - Uploaded files extension
     */
    private String extractFileExtension(final MultipartFile file) {
        return extractContentType(file).substring(6); //Index of dash in Content-Type
    }
}
