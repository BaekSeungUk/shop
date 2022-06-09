package com.bitc.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileDate) throws Exception {
//        UUID 클래스 : 고유한 값을 자동으로 생성해주는 클래스
        UUID uuid = UUID.randomUUID();
//        파일의 확ㅇ자 이름만 가져오기
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        uuid를 가지고 생성된 고유한 이름 + 확장자명 으로 고유한 파일명을 생성함
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
//        파일 스트림을 통해서 파일을 생성
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
//        해당 파일에 파일 내용 입력
        fos.write(fileDate);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

//        파일이 존재하는지 확인 후 삭제
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }
        else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
