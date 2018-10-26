package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.UploadDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	// 다운로드
	@GetMapping(value="/download/{fileName}",produces= {MediaType.APPLICATION_OCTET_STREAM_VALUE})//octet stream : 문자열 마임타입
	@ResponseBody // 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
	public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName) { // 파일웅웅웅: 파일 이름을 받는 것
		

		String fName = fileName.substring(0, fileName.lastIndexOf("_"));// aaa_jpg
		log.info("FName: " + fName);
		
		String ext = fileName.substring(fileName.lastIndexOf("_") + 1); // 확장자
		log.info("ext : " + ext);

		String total = fName + "." + ext; // 오리지날 파일 이름
		
		//다운로드 위해 uuid끊기
		int under = total.indexOf("_");
		String totalOrigin = total.substring(under+1);//
	
		ResponseEntity<byte[]> result = null;

		try {
			File target = new File("C:\\upload\\" + total);

			//파일 다운로드
			String downName = new String(totalOrigin.getBytes("UTF-8"),"ISO-8859-1");
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Disposition","attachment; filename=" + downName);//

			byte[] arr = FileCopyUtils.copyToByteArray(target); // 바이트의 배열
			result = new ResponseEntity<>(arr,header, HttpStatus.OK); //다운로드 위해 해더 걸기
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return result;
	}

	
	
	
	
	
	
	
	
	@GetMapping("/viewFile/{fileName}") // 다운로드. '.'은 확장자를 못받음.
	@ResponseBody // 순수한 데이터 반환
	public ResponseEntity<byte[]> viewFile(@PathVariable("fileName") String fileName) {// 바이트의 배열 . 패스 웅엥웅: 경로 설정

		log.info("filename : " + fileName);

		String fName = fileName.substring(0, fileName.lastIndexOf("_"));// aaa_jpg
		log.info("FName: " + fName);
		String ext = fileName.substring(fileName.lastIndexOf("_") + 1); // 확장자
		log.info("ext : " + ext);

		String total = fName + "." + ext; // 오리지날 파일 이름

		ResponseEntity<byte[]> result = null;

		try {
			File target = new File("C:\\upload\\" + total);

			HttpHeaders header = new HttpHeaders();
			header.add("Content-type", Files.probeContentType(target.toPath()));

			byte[] arr = FileCopyUtils.copyToByteArray(target); // 바이트의 배열
			result = new ResponseEntity<>(arr, header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return result;

	}

	
	
	
	
	
	
	
	
	@PostMapping(value = "/upload", produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<UploadDTO> upload(MultipartFile[] files) {

		List<UploadDTO> result = new ArrayList<>();

		for (MultipartFile file : files) {

			log.info(file.getOriginalFilename());
			log.info(file.getContentType());
			log.info(file.getSize());

			UUID uuid = UUID.randomUUID();// 중복 막기 위함
			String saveFileName = uuid.toString() + "_" + file.getOriginalFilename();
			String thumbFileName = "s_" + saveFileName; // 섬네일 이름

			File saveFile = new File("C:\\upload\\" + saveFileName);
			FileOutputStream thumbFile = null;

			try {

				thumbFile = new FileOutputStream("C:\\upload\\" + thumbFileName);

				Thumbnailator.createThumbnail(file.getInputStream(), thumbFile, 100, 100);

				thumbFile.close();// 썸네일 파일이 내 드라이브에 저장

				result.add(new UploadDTO(saveFileName, file.getOriginalFilename(),
						thumbFileName.substring(0, thumbFileName.lastIndexOf(".")),
						thumbFileName.substring(thumbFileName.lastIndexOf(".") + 1)));

				file.transferTo(saveFile); // 파일 저장

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;

	}
}
