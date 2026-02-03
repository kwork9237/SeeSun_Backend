package com.seesun.service.webrtc;

import com.seesun.dto.lecture.session.ActivateSessionDTO;
import com.seesun.dto.member.response.MemberSessionInfo;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.janus.JanusRestClient;
import com.seesun.service.lecture.session.LectureSessionService;
import com.seesun.service.member.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JanusRoomService {

    private final JanusRestClient janusRestClient;
    private final LectureSessionService sessionService;
    private final MemberService mbService;
    
    @Value("${seesun.janus.base-url}")
    private String janusUrl;

    /** 방 존재 확인 → 없으면 생성 */
    public BootstrapResponseDTO ensureRoomExists(Long mbId, String uuid) {
    	ActivateSessionDTO ssData = sessionService.getSessionDataByUuid(uuid);
    	MemberSessionInfo mbInfo = mbService.getMemberSessionInfo(mbId);
    	
    	if(ssData == null || mbInfo == null)
    		throw new GlobalException(ErrorCode.INVALID_REQUIRE_DATA);

        long sessionId = janusRestClient.createSession();
        long handleId = janusRestClient.attachPlugin(sessionId);
        long lectureId = ssData.getLe_id();
        short roomId = ssData.getRoom_id();
        String role = switchRole(mbInfo.getMb_type_id());
        String displayName = mbInfo.getNickname();

        // exists 체크
        Map<?, ?> existsRes = janusRestClient.sendMessage(sessionId, handleId,
                Map.of("request", "exists", "room", roomId)
        );

        if (!exists(existsRes)) {
            janusRestClient.sendMessage(sessionId, handleId, Map.of(
                    "request", "create",
                    "room", roomId,
                    "description", "SeeSun lecture " + lectureId,
                    "publishers", 10,
                    "permanent", false
            ));
        }

        return new BootstrapResponseDTO(
    			String.valueOf(roomId),
    			janusUrl,
    			role,
    			displayName,
    			role.equals("MENTOR") ? displayName : null
			);
    }

    private boolean exists(Map<?, ?> res) {
        try {
            Map<?, ?> plugindata = (Map<?, ?>) res.get("plugindata");
            Map<?, ?> data = (Map<?, ?>) plugindata.get("data");
            return (boolean) data.get("exists");
        } catch (Exception e) {
            return false;
        }
    }
    
    // role 반환
    private String switchRole(short mbTypeId) {
    	switch (mbTypeId) {
    		case 2 : return "MENTOR";
    		case 1 : return "MENTEE";
    		case 0 : return "ADMIN";
	    	default : throw new IllegalStateException("Invalid member type");
    	}
    }
}
