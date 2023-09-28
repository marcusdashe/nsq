/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.cloudinary.json.JSONObject;
import org.cstemp.nsq.exception.BadRequestException;
import org.cstemp.nsq.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author chibuezeharry & MarcusDashe
 *
 */

@Slf4j
public class NinasUtil {

    public final static int MAX_PAGE_SIZE = 2000;
    public final static String DEFAULT_PAGE_NUMBER = "0";
    public final static String DEFAULT_PAGE_SIZE = "100";

    public static String storeToCloudinary(MultipartFile file, String folderName) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "juvenix",
                "api_key", "237891148546528",
                "api_secret", "qRBsjTP8EFU-ojSryL3z87mBnKw"));
        try {

            File upload = File.createTempFile("image", "jpg");

            file.transferTo(upload);

            Map result = cloudinary.uploader().upload(upload, ObjectUtils.asMap("folder", folderName));
            JSONObject json = new JSONObject(result);

            return json.getString("secure_url");
        } catch (IOException ex) {
            throw new BaseException("The file " + file.getOriginalFilename() + " could not be saved now. \n Try again later", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public static String storeVideoToCloudinary(MultipartFile file, String folderName) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "juvenix",
                "api_key", "237891148546528",
                "api_secret", "qRBsjTP8EFU-ojSryL3z87mBnKw"));
        try {

            File upload = File.createTempFile("image", "jpg");

            file.transferTo(upload);

            Map result = cloudinary.uploader().upload(upload, ObjectUtils.asMap("resource_type", "video",
                     "public_id", folderName));
            JSONObject json = new JSONObject(result);

            return json.getString("secure_url");
        } catch (IOException ex) {
            throw new BaseException("The file " + file.getOriginalFilename() + " could not be saved now. \n Try again later", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public static Boolean deleteFromCloudinary(String url) {

        if (url == null) {
            return false;
        }

        String publicId = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "juvenix",
                "api_key", "237891148546528",
                "api_secret", "qRBsjTP8EFU-ojSryL3z87mBnKw"));
        try {

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            JSONObject json = new JSONObject(result);

            return json.getString("result").equals("ok");

        } catch (IOException ex) {
            throw new BaseException("The file could not be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + MAX_PAGE_SIZE);
        }
    }

    public static String camelToTitleCase(String text) {

        String result = text.replace("/([A-Z])/g", "   $1");

        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }
}
