package com.seesun.service.lecture.session;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seesun.dto.lecture.session.ActivateSessionDTO;
import com.seesun.dto.lecture.session.LectureSessionHistoryDTO;
import com.seesun.dto.lecture.session.LectureSessionPoolDTO;
import com.seesun.dto.lecture.session.LectureSessionResponse;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.global.uuid.UUIDUtil;
import com.seesun.mapper.lecture.LectureMapper;
import com.seesun.mapper.lecture.session.ActivateSessionMapper;
import com.seesun.mapper.lecture.session.LectureSessionHistoryMapper;
import com.seesun.service.order.OrdersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureSessionService {
	private final LectureMapper lectureMapper;
	private final LectureSessionHistoryMapper historyMapper;
	private final ActivateSessionMapper activateSessionMapper;
	private final LectureSessionPoolSerivce sessionPoolService;
	private final OrdersService ordersService;
	
	
	@Transactional
	public LectureSessionResponse create(Long mbId, Long leId) {
		
		// 강의 세션이 열려있는지 확인
		if(activateSessionMapper.checkExistsSessionByleId(leId) == 1)
			throw new GlobalException(ErrorCode.SESSION_EXISTS);
		
		// 개설자와 강의 ID 매칭 확인
		if(lectureMapper.checkLectureMember(mbId, leId) != 1)
			throw new GlobalException(ErrorCode.LECTURE_MENTO_NOT_MATCH);
		
		// 잔여 강의 풀 확인
		LectureSessionPoolDTO pool = sessionPoolService.getPoolData();
		if(pool == null)
			throw new GlobalException(ErrorCode.LECTURE_POOL_IS_FULL);
		
		// 강의 세션 활성화 시도
		try {
			// 강의 히스토리 생성
			LectureSessionHistoryDTO hist = new LectureSessionHistoryDTO();
			hist.setLe_id(leId);
			hist.setMb_id(mbId);
			hist.setRoom_id(pool.getRoom_id());
			
			historyMapper.insertSessionData(hist);
			
			// 활성 세션에 등록
			ActivateSessionDTO ssData = new ActivateSessionDTO();
			ssData.setRoom_id(pool.getRoom_id());
			ssData.setMb_id(mbId);
			ssData.setLe_id(leId);
			ssData.setHistory_id(hist.getHistory_id());
			ssData.setUuid(UUIDUtil.generate());
			
			activateSessionMapper.insertSessionData(ssData);
			
			// 세션 퓰에 등록
			sessionPoolService.activateSessionByRoomId(pool.getRoom_id());
			
			// 결과 리턴
			// LeId, UUID, HistoryId
			return new LectureSessionResponse(
					hist.getHistory_id(),
					ssData.getUuid()
				);
		} catch(Exception e) {
			// 추후 exception 별 구별 예정
//			e.printStackTrace();
			
			throw new GlobalException(ErrorCode.DATABASE_UNKNOWN_ERROR);
		}
	}
	
	@Transactional
	public void close(Long mbId, String uuid) {
		// 현재 활성 세션 데이터 가져오기
		ActivateSessionDTO ssData = getSessionDataByUuid(uuid);
		if(ssData == null)
			throw new GlobalException(ErrorCode.SESSION_NOT_FOUND);
		
		// 세션 소유자인지 확인
		if(activateSessionMapper.checkSessionOwner(mbId) != 1)
			throw new GlobalException(ErrorCode.NOT_SESSION_OWNER);
		
		try {
			// 활성 세션에서 삭제
			activateSessionMapper.deleteSessionData(ssData.getRoom_id());
			
			// 세션 퓰에서 비활성화 해제
			sessionPoolService.deactivateSessionByRoomId(ssData.getRoom_id());
		} catch(Exception e) {
//			e.printStackTrace();
			
			throw new GlobalException(ErrorCode.DATABASE_DELETE_ERROR);
		}
	}
	
	@Transactional
	public void validate(Long mbId, String uuid) {
		// 현재 활성 세션 데이터 가져오기
		ActivateSessionDTO ssData = getSessionDataByUuid(uuid);
		if(ssData == null)
			throw new GlobalException(ErrorCode.SESSION_NOT_FOUND);
		
		// 멘토일 경우 무시 (세션 소유자)
		if(activateSessionMapper.checkSessionOwner(mbId) == 1)
			return;
		
		// 정상 결제 상태를 찾을 수 없는 경우
		if(ordersService.checkMemberOrderStatus(mbId, ssData.getLe_id()) != 1)
			throw new GlobalException(ErrorCode.LECTURE_INVALID_PAYMENT_STATUS);
	}
	
	@Transactional
	public void start(Long mbId, String uuid) {
		// 현재 활성 세션 데이터 가져오기
		ActivateSessionDTO ssData = getSessionDataByUuid(uuid);
		if(ssData == null)
			throw new GlobalException(ErrorCode.SESSION_NOT_FOUND);
		
		// 세션 소유자인지 확인
		if(activateSessionMapper.checkSessionOwner(mbId) != 1)
			throw new GlobalException(ErrorCode.NOT_SESSION_OWNER);
		
		// 세션 활성 상태로 변경
		activateSessionMapper.setActivateStatus(mbId);
	}
	
	@Transactional
	public String check(Long leId) {
		ActivateSessionDTO ssData = activateSessionMapper.getSessionDataByLeId(leId);
		
		if(ssData.getStatus() == 0)
			throw new GlobalException(ErrorCode.SESSION_NOT_STARTED);
		
		return ssData.getUuid();
	}
	
	
	// 다른 service에서 사용 필요
	public ActivateSessionDTO getSessionDataByUuid(String uuid) {
		return activateSessionMapper.getSessionDataByUuid(uuid);
	}
}
