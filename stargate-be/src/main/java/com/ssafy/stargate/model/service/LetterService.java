package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.model.dto.common.LetterDto;
import com.ssafy.stargate.model.dto.request.LetterCreateRequestDto;
import com.ssafy.stargate.model.dto.request.LetterDeleteRequestDto;
import com.ssafy.stargate.model.dto.request.LetterFindRequestDto;
import com.ssafy.stargate.model.dto.request.LetterUpdateRequestDto;
import java.util.List;


/**
 * 편지 서비스 인터페이스
 */
public interface LetterService {

    public LetterDto createLetter(LetterDto dto) throws NotFoundException;

    public LetterDto updateLetter(LetterDto dto) throws NotFoundException;

    public void deleteLetter(LetterDto dto);

    public LetterDto getLetter(Long no) throws NotFoundException;

    public List<LetterDto> getLetterByMeeting(LetterDto dto);

    public List<LetterDto> getLetterByMember(LetterDto memberNo);

    public List<LetterDto> getLetterByFUser(String email);

}
