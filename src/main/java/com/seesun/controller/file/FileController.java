package com.seesun.controller.file;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.seesun.config.security.SecurityConfig;
import com.seesun.dto.file.response.FileDataResponseDTO;
import com.seesun.dto.file.response.FileDownloadDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.global.file.FileProperties;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.file.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
	// 디버그 테스트 용도
	// 실제로는 controller 대신 service layer에서 호출
	
//	private final FileService fileService;
//	private final FileProperties fileProperties;


//	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<?> save(@RequestPart("file") MultipartFile f) {
//		fileService.save(1L, "MENTO_REQUEST", f);
//		
//		return ResponseEntity.ok("성공");
//	}
	
	// 디버그 전용
//	@PostMapping("/delete")
//	public ResponseEntity<?> delete() {
//		fileService.delete(1L, "MENTO_REQUEST");
//		
//		return ResponseEntity.ok("성공");
//	}
	
	// 파일 목록 조회
	// 나중에 용도에 따라 Admin쪽 Service로 이동.
//	@GetMapping("/list")
//	public ResponseEntity<List<FileDataResponseDTO>> list(@RequestParam("tid") short typeId) {
//
//		return ResponseEntity.ok(fileService.list(typeId));
//		return ResponseEntity.ok("성공");
//	}
	
	// 파일 다운로드
//	@GetMapping("/download/{fileId}")
//	public ResponseEntity<Resource> download(@PathVariable("fileId") Long fid) {
//		FileDownloadDTO file = fileService.downloadData(fid);
//		
//		Path basePath = fileProperties.getBasePath();
//		Path path = null;
//		Resource resource = null;
//		String encodedName = null;
//
//		try {
//        path = basePath
//                .resolve(file.getRelative_path())
//                .toAbsolutePath()
//                .normalize();
//
//        if (!Files.exists(path) || !Files.isReadable(path)) {
//            throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
//        }
//
//        resource = new FileSystemResource(path);
//
//        encodedName = URLEncoder.encode(file.getOriginal_name(), StandardCharsets.UTF_8)
//                .replaceAll("\\+", "%20");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + encodedName + "\"")
//                .contentLength(file.getSize() != null ? file.getSize() : path.toFile().length())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//	}
}
