package com.exrates.inout.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserFilesService {

    boolean checkFileValidity(MultipartFile file);

    String saveReceiptScan(int userId, int invoiceId, MultipartFile file) throws IOException;
}
