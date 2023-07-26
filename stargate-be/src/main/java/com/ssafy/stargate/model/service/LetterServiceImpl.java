package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.LetterException;
import com.ssafy.stargate.model.dto.common.LetterDto;
import com.ssafy.stargate.model.dto.request.LetterCreateRequestDto;
import com.ssafy.stargate.model.dto.request.LetterDeleteRequestDto;
import com.ssafy.stargate.model.entity.FUser;
import com.ssafy.stargate.model.entity.Letter;
import com.ssafy.stargate.model.entity.Meeting;
import com.ssafy.stargate.model.entity.PMember;
import com.ssafy.stargate.model.repository.FUserRepository;
import com.ssafy.stargate.model.repository.LetterRepository;
import com.ssafy.stargate.model.repository.MeetingRepository;
import com.ssafy.stargate.model.repository.PMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 편지 서비스 구현체
 */
@Service
@Slf4j
public class LetterServiceImpl implements LetterService{

    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private FUserRepository fUserRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private PMemberRepository pMemberRepository;

    /**
     * 편지 작성해서 저장
     * @param dto LetterCreateRequestDto 팬유저가 작성한 편지 정보 담는 dto
     * @return LetterDto 저장된 편지 정보 dto
     */
    @Override
    public LetterDto createLetter(LetterCreateRequestDto dto) {

        FUser fUser = fUserRepository.findById(dto.getEmail()).orElseThrow();
        PMember pMember = pMemberRepository.findById(dto.getMemberNo()).orElseThrow();
        Meeting meeting = meetingRepository.findById(dto.getUuid()).orElseThrow();

        Letter letter = Letter.builder()
                .contents(dto.getContents())
                .fUser(fUser)
                .pMember(pMember)
                .meeting(meeting)
                .build();

        Letter savedLetter = letterRepository.save(letter);

        log.info("저장된 편지 번호 {}", savedLetter.getNo());

        return LetterDto.builder()
                .no(savedLetter.getNo())
                .contents(savedLetter.getContents())
                .email(savedLetter.getFUser().getEmail())
                .memberNo(savedLetter.getPMember().getMemberNo())
                .uuid(savedLetter.getMeeting().getUuid())
                .createDate(savedLetter.getCreateDate())
                .build();
    }

    /**
     * 편지 수정
     * @param dto 편지 정보가 저장된 dto
     * @return LetterDto 수정 완료된 편지 정보 담는 dto
     */
    @Override
    public LetterDto updateLetter(LetterDto dto) throws LetterException {

        Letter letter = letterRepository.findById(dto.getNo()).orElseThrow();

        if(letter != null){
            letter.setContents(dto.getContents());

        }else{
            throw new LetterException("해당하는 편지가 없습니다.");
        }

        Letter updatedLetter = letterRepository.save(letter);

        return LetterDto.builder()
                .no(updatedLetter.getNo())
                .contents(updatedLetter.getContents())
                .email(updatedLetter.getFUser().getEmail())
                .memberNo(updatedLetter.getPMember().getMemberNo())
                .uuid(updatedLetter.getMeeting().getUuid())
                .createDate(updatedLetter.getCreateDate())
                .editDate(updatedLetter.getEditDate())
                .build();
    }

    /**
     * 편지 삭제
     * @param dto LetterDeleteRequestDto 삭제하려는 편지 번호 정보를 담는 dto
     * @throws LetterException 삭제하려는 편지 번호가 존재하지 않을 때 발생
     */
    @Override
    public void deleteLetter(LetterDeleteRequestDto dto) throws LetterException {
        try{
            letterRepository.deleteById(dto.getNo());
        }catch (Exception e){
            throw new LetterException("삭제하려는 편지가 존재하지 않습니다.");
        }
    }

    /**
     * 편지 번호로 조회해서 해당하는 편지 정보 반환
     * @param no Long 편지 번호
     * @return LetterDto 편지 정보가 담긴 dto
     */
    @Override
    public LetterDto getLetter(Long no) {

        Letter letter = letterRepository.findById(no).orElseThrow();

        return LetterDto.builder()
                .no(letter.getNo())
                .contents(letter.getContents())
                .createDate(letter.getCreateDate())
                .editDate(letter.getEditDate())
                .memberNo(letter.getPMember().getMemberNo())
                .email(letter.getFUser().getEmail())
                .uuid(letter.getMeeting().getUuid())
                .build();
    }

    /**
     * 팬미팅 번호를 기준으로 편지들 정보 조회
     * @param uuid UUID 팬미팅 번호
     * @return List<LetterDto> 팬미팅에 보내진 편지 목록
     */
    @Override
    public List<LetterDto> getLetterByMeeting(UUID uuid) {

        List<Letter> letters = letterRepository.findByMeeting_Uuid(uuid);

        return letters.stream().map((Letter -> LetterDto.builder()
                .no(Letter.getNo())
                .contents(Letter.getContents())
                .email(Letter.getFUser().getEmail())
                .memberNo(Letter.getPMember().getMemberNo())
                .uuid(Letter.getMeeting().getUuid())
                .createDate(Letter.getCreateDate())
                .editDate(Letter.getEditDate())
                .build()
                )).toList();
    }

    /**
     * 연예인 번호를 기준으로 편지들 정보 조회
     * @param memberNo long 연예인 번호
     * @return List<LetterDto> 연예인에게 보내진 편지 목록
     */
    @Override
    public List<LetterDto> getLetterByMember(long memberNo) {

        List<Letter> letters = letterRepository.findBypMemberMemberNo(memberNo);

        return letters.stream().map((Letter -> LetterDto.builder()
                .no(Letter.getNo())
                .contents(Letter.getContents())
                .email(Letter.getFUser().getEmail())
                .memberNo(Letter.getPMember().getMemberNo())
                .uuid(Letter.getMeeting().getUuid())
                .createDate(Letter.getCreateDate())
                .editDate(Letter.getEditDate())
                .build()
                )).toList();
    }

    /**
     * 팬유저가 작성한 편지 목록 조회
     * @param email String 팬유저 이메일
     * @return List<LetterDto> 팬유저가 작성한 편지 목록
     */
    @Override
    public List<LetterDto> getLetterByFUser(String email) {

        List<Letter> letters = letterRepository.findByfUserEmail(email);

        return letters.stream().map((Letter -> LetterDto.builder()
                .no(Letter.getNo())
                .contents(Letter.getContents())
                .email(Letter.getFUser().getEmail())
                .memberNo(Letter.getPMember().getMemberNo())
                .uuid(Letter.getMeeting().getUuid())
                .createDate(Letter.getCreateDate())
                .editDate(Letter.getEditDate())
                .build()
                )).toList();
    }


}