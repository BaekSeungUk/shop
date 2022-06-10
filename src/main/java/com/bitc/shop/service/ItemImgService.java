package com.bitc.shop.service;

import com.bitc.shop.entity.ItemImg;
import com.bitc.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

//    @Value 어노테이션을 사용하여 application.properties 파일에 설정해 놓은 경로를 가져옴
    @Value("${itemImgLocation}")
    private String itemImgLocation;

//    데이터 베이스에 파일 정보를 저장하기 위해서 지정
//    private final 로 선언을 하였지만 @Autowired로 지정하여 사용해도 됨
    private  final ItemImgRepository itemImgRepository;

//    업로드된 파일을 서버에 저장 및 파일 이름을 반환
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oriImgName)) {
//            FileService 의 uploadFile 메서드를 실행하여 실제 저장 및 고유한 파일명을 반환
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
//            등록된 외부 폴더에 생성된 고유파일명을 추가하여 프로젝트에서 사용하는 전체 파일 경로를 생성함
            imgUrl = "/images/item/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg); // 데이터 베이스에 업로드된 파일정보 저장
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
//         이미지가 수정되었는지 확인
        if (!itemImgFile.isEmpty()) {
//            지정한 이미지를 데이터베이스에서 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            
//            조회된 이미지 데이터를 데이터 베이스에서 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

//            신규 이미지 데이터를 데이터베이스에 저장
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

        }
    }
}
