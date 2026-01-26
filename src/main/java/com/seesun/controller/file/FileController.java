package com.seesun.controller.file;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.service.file.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
	// 디버그 테스트 용도
	private final FileService fm;
	
	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> save(@RequestPart("file") MultipartFile f) {
		
		fm.save(1L, f);
		
		return ResponseEntity.ok("성공");
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam("path") String p) {
		fm.delete(p);
		
		return ResponseEntity.ok("성공");
	}
}
