package org.zerock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadDTO {
	
	private String uploadName;
	private String originName;//원본파일 이름
	private String thumbName;//섬네일 이름
	private String ext; //이미지인지 체크

}
