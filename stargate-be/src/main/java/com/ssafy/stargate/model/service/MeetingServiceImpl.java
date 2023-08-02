package com.ssafy.stargate.model.service;

import com.ssafy.stargate.exception.NotFoundException;
import com.ssafy.stargate.exception.CRUDException;
import com.ssafy.stargate.model.dto.common.*;
import com.ssafy.stargate.model.dto.response.MeetingDetailResponseDto;
import com.ssafy.stargate.model.entity.*;
import com.ssafy.stargate.model.repository.*;
import com.ssafy.stargate.util.FileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 미팅 관련 서비스 구현체
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private final MeetingRepository meetingRepository;

    @Autowired
    private final MeetingFUserRepository meetingFUserRepository;

    @Autowired
    private final MeetingMemberRepository meetingMemberRepository;

    @Autowired
    private final PMemberRepository pMemberRepository;

    @Autowired
    private final PUserRepository pUserRepository;

    @Autowired
    private final FUserRepository fUserRepository;

    @Autowired
    private final FileUtil fileUtil;


    @Value("${s3.filepath.meeting}")
    private String filePath;

    /**
     * 미팅 세부정보를 가져온다.
     * ResponseDto에는 기본 정보에 미팅방 ID(roomId), 팬유저 가입 여부(isRegister)가 추가적으로 들어간다.
     * 맴버 미팅방 ID(roomId) 포멧 (String): {미팅UUID}.{유저UUID}
     * 팬유저 가입 여부: 팬 유저 이메일이 DB에 존재하는지 확인
     *
     * @param uuid      [UUID] 미팅 UUID
     * @param principal [Principal] 소속사 email이 포함된 principal 객체
     * @return [MeetingDetailResponseDto] 기본 정보 + 맴버 미팅방 ID, 유저 가입 여부를 포함한 DTO
     * @throws NotFoundException 데이터 찾기 실패 에러
     */

    @Override
    public MeetingDetailResponseDto getMeeting(UUID uuid, Principal principal) throws NotFoundException {
        log.info("data : {}", uuid);
        String email = principal.getName();

        Meeting meeting = getMeeting(uuid, email);

        List<MeetingDetailResponseDto.MeetingMember> meetingMemberDtos = getMeetingDetailMemberDtoList(meeting);

        List<MeetingDetailResponseDto.MeetingFUser> meetingFUserDtos = getMeetingDetailFUserDtoList(meeting);

        SavedFileDto savedFileDto = fileUtil.getFileInfo(filePath, meeting.getImage());

        PGroup group = getGroup(meeting);
        return MeetingDetailResponseDto.builder()
                .uuid(meeting.getUuid())
                .name(meeting.getName())
                .startDate(meeting.getStartDate())
                .waitingTime(meeting.getWaitingTime())
                .meetingTime(meeting.getMeetingTime())
                .groupNo(group.getGroupNo())
                .groupName(group.getName())
                .imageFileInfo(savedFileDto)
                .notice(meeting.getNotice())
                .photoNum(meeting.getPhotoNum())
                .meetingMembers(meetingMemberDtos)
                .meetingFUsers(meetingFUserDtos)
                .build();
    }


    /**
     * 신규 미팅을 생성한다.
     *
     * @param dto       [MeetingDto] 입력받은 Meeting DTO
     * @param principal [Principal] 소속사 이메일이 포함된 객체
     * @return [MeetingDto] 생성한 미팅 정보를 담은 DTO
     * @throws CRUDException     데이터 CRUD 에러
     * @throws NotFoundException 데이터 찾기 실패 에러
     */
    @Transactional
    @Override
    public MeetingDto createMeeting(MeetingDto dto, MultipartFile imageFile, Principal principal) throws CRUDException, NotFoundException {
        log.info("data : {}", dto);
        String email = principal.getName();

        String filename = fileUtil.uploadFile(filePath, imageFile);

        try {

            Meeting meeting = Meeting.builder()
                    .name(dto.getName())
                    .startDate(dto.getStartDate())
                    .waitingTime(dto.getWaitingTime())
                    .meetingTime(dto.getMeetingTime())
                    .notice(dto.getNotice())
                    .photoNum(dto.getPhotoNum())
                    .image(filename)
                    .pUser(pUserRepository.findById(email)
                            .orElseThrow(() -> new NotFoundException("소속사가 존재하지 않습니다.")))
                    .build();


            List<MeetingMemberBridge> meetingMembers = dtoToMeetingMemberList(meeting, dto.getMeetingMembers());
            meetingMemberRepository.saveAll(meetingMembers);
            meeting.setMeetingMembers(meetingMembers);


            List<MeetingFUserBridge> meetingFUsers = dtoToMeetingFUserList(meeting, dto.getMeetingFUsers());
            meetingFUserRepository.saveAll(meetingFUsers);
            meeting.setMeetingFUsers(meetingFUsers);

            meetingRepository.save(meeting);

            return MeetingDto.entityToDto(meeting);
        } catch (Exception e) {
            e.printStackTrace();

            fileUtil.deleteFile(filePath, filename);
            throw new CRUDException("미팅 생성 도중 오류가 발생했습니다.");
        }
    }


    /**
     * 미팅 정보를 수정한다.
     *
     * @param dto       [MeetingDto] 입력받은 Meeting DTO
     * @param principal [Principal] 소속사 이메일이 포함된 객체
     * @throws CRUDException     데이터 CRUD 에러
     * @throws NotFoundException 데이터 찾기 실패 에러
     */
    @Transactional
    @Override
    public void updateMeeting(MeetingDto dto, MultipartFile imageFile, Principal principal) throws CRUDException, NotFoundException {
        log.info("data : {}", dto);
        String email = principal.getName();

        String newFilename = fileUtil.uploadFile(filePath, imageFile);

        try {
            Meeting meeting = getMeeting(dto.getUuid(), email);

            String filename = meeting.getImage();

            meeting.setName(dto.getName());
            meeting.setStartDate(dto.getStartDate());
            meeting.setWaitingTime(dto.getWaitingTime());
            meeting.setMeetingTime(dto.getMeetingTime());
            meeting.setPhotoNum(dto.getPhotoNum());
            meeting.setNotice(dto.getNotice());
            meeting.setImage(newFilename);

            List<MeetingMemberBridge> meetingMembers = meeting.getMeetingMembers();
            List<MeetingMemberBridge> newMeetingMembers = dtoToMeetingMemberList(meeting, dto.getMeetingMembers());
            updateMeetingMemberList(meetingMembers, newMeetingMembers);

            List<MeetingFUserBridge> meetingFUsers = meeting.getMeetingFUsers();
            List<MeetingFUserBridge> newMeetingFUsers = dtoToMeetingFUserList(meeting, dto.getMeetingFUsers());
            updateMeetingFUserList(meetingFUsers, newMeetingFUsers);

            fileUtil.deleteFile(filePath, filename);
        } catch (Exception e) {
            e.printStackTrace();
            fileUtil.deleteFile(filePath, newFilename);
            throw new CRUDException("미팅 수정 도중 오류가 발생했습니다.");
        }

    }

    /**
     * 미팅을 삭제한다.
     *
     * @param dto       [MeetingDto] 입력받은 Meeting DTO
     * @param principal [Principal] 소속사 이메일이 포함된 객체
     * @throws CRUDException     데이터 CRUD 에러
     * @throws NotFoundException 데이터 찾기 실패 에러
     */
    @Transactional
    @Override
    public void deleteMeeting(MeetingDto dto, Principal principal) throws CRUDException, NotFoundException {
        log.info("data : {}", dto);
        String email = principal.getName();
        try {
            Meeting meeting = getMeeting(dto.getUuid(), email);

            meetingRepository.delete(meeting);

            fileUtil.deleteFile(filePath, meeting.getImage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDException("미팅 삭제 도중 오류가 발생했습니다.");
        }
    }

    /**
     * 미팅 정보를 가져온다.
     * 미팅을 만든 해당 소속사만 미팅 정보를 변경할 수 있도록 소속사 email (id)와 함께 찾는다.
     *
     * @param uuid  [UUID] 미팅 uuid (id)
     * @param email [String] 소속사 email (id)
     * @return [Meeting] 미팅 데이터
     * @throws NotFoundException 데이터 찾기 실패 에러
     */
    private Meeting getMeeting(UUID uuid, String email) throws NotFoundException {
        return meetingRepository.findByIdAndEmail(uuid, email)
                .orElseThrow(() -> new NotFoundException("미팅 존재하지 않음"));
    }

    /**
     * 미팅 멤버 상세 정보 DTO 리스트를 가져온다.
     *
     * @param meeting [Meeting] 미팅 데이터
     * @return [List<MeetingDetailResponseDto.MeetingMember>] 미팅 멤버 상세 정보 DTO 리스트
     */
    private List<MeetingDetailResponseDto.MeetingMember> getMeetingDetailMemberDtoList(Meeting meeting) {
        return (List<MeetingDetailResponseDto.MeetingMember>) meeting.getMeetingMembers()
                .stream()
                .map(meetingMember -> MeetingDetailResponseDto.MeetingMember.builder()
                        .uuid(meetingMember.getUuid())
                        .memberNo(meetingMember.getPMember().getMemberNo())
                        .name(meetingMember.getPMember().getName())
                        .orderNum(meetingMember.getOrderNum())
                        .roomId(getRoomId(meeting, meetingMember)) // create roomId
                        .build()).toList();
    }

    /**
     * 미팅 팬 유저 상세 정보 DTO 리스트를 가져온다.
     *
     * @param meeting [Meeting] 미팅 데이터
     * @return [List<MeetingDetailResponseDto.MeetingFUser>] 미팅 팬 유저 상세 정보 DTO 리스트
     */
    private List<MeetingDetailResponseDto.MeetingFUser> getMeetingDetailFUserDtoList(Meeting meeting) {
        return meeting.getMeetingFUsers()
                .stream()
                .map(meetingFUser -> {
                    MeetingDetailResponseDto.MeetingFUser.MeetingFUserBuilder builder = MeetingDetailResponseDto.MeetingFUser.builder();
                    builder.no(meetingFUser.getNo())
                            .email(meetingFUser.getEmail())
                            .orderNum(meetingFUser.getOrderNum());

                    FUser fUser = getFUser(meetingFUser);
                    boolean isRegister = (fUser != null);
                    builder.isRegister(isRegister);
                    builder.name((isRegister) ? fUser.getName() : null);

                    return builder.build();
                }).toList();
    }

    /**
     * 팬 유저 정보를 가져온다.
     *
     * @param meetingFUser [MeetingFUserBridge] 미팅 유저
     * @return [FUser] 팬 유저
     */
    private FUser getFUser(MeetingFUserBridge meetingFUser) {
        Optional<FUser> optionalFUser = fUserRepository.findById(meetingFUser.getEmail());
        return (optionalFUser.isPresent()) ? optionalFUser.get() : null;
    }

    /**
     * 그룹 정보를 가져온다.
     *
     * @param meeting [Meeting] 미팅
     * @return [PGroup] 그룹 정보
     */
    private PGroup getGroup(Meeting meeting) {
        if (meeting.getMeetingMembers().size() < 1) {
            return null;
        }
        MeetingMemberBridge meetingMember = meeting.getMeetingMembers().get(0);
        return meetingMember.getPMember().getPGroup();
    }

    /**
     * 미팅방 id를 생성한다.
     * 미팅 uuid와 미팅멤버 uuid를 이용한다.
     *
     * @param meeting       [Meeting] 미팅
     * @param meetingMember [MeetingMemberBridge] 미팅 멤버
     * @return [String] 생성된 미팅방 id
     */
    private String getRoomId(Meeting meeting, MeetingMemberBridge meetingMember) {
        return meeting.getUuid() + "." + meetingMember.getUuid();
    }


    /**
     * 미팅 멤버 dto 리스트를 통해 미팅 멤버 엔티티 리스트를 만든다.
     *
     * @param meeting           [Meeting] 미팅
     * @param meetingMemberDtos [List<MeetingMemberBridgeDto>] 미팅 멤버 dto 리스트
     * @return List<MeetingMemberBridge> 미팅 멤버 엔티티 리스트
     * @throws NotFoundException NotFoundException 데이터 찾기 실패 에러
     */
    private List<MeetingMemberBridge> dtoToMeetingMemberList(Meeting meeting, List<MeetingMemberBridgeDto> meetingMemberDtos) throws NotFoundException {
        return meetingMemberDtos.stream().map(meetingMemberBridgeDto -> MeetingMemberBridge.builder()
                .meeting(meeting)
                .pMember(pMemberRepository.findById(meetingMemberBridgeDto.getMemberNo())
                        .orElseThrow(() -> new NotFoundException("미팅 멤버가 존재하지 않습니다.")))
                .orderNum(meetingMemberBridgeDto.getOrderNum())
                .build()).toList();
    }

    /**
     * 미팅 팬유저 dto 리스트를 통해 미팅 팬유저 엔티티 리스트를 만든다.
     *
     * @param meeting          [Meeting] 미팅
     * @param meetingFUserDtos [List<MeetingMemberBridgeDto>] 미팅 팬유저 dto 리스트
     * @return List<MeetingMemberBridge> 미팅 팬유저 엔티티 리스트
     */
    private List<MeetingFUserBridge> dtoToMeetingFUserList(Meeting meeting, List<MeetingFUserBridgeDto> meetingFUserDtos) {
        return meetingFUserDtos.stream().map(meetingFUserBridgeDto -> MeetingFUserBridge.builder()
                .meeting(meeting)
                .email(meetingFUserBridgeDto.getEmail())
                .orderNum(meetingFUserBridgeDto.getOrderNum())
                .build()).toList();
    }

    /**
     * 미팅 멤버 리스트를 수정한다.
     * 기존과 수정할 데이터의 멤버 id(no)를 비교하여,
     * - 동일한 멤버: 미팅 멤버 데이터 수정
     * - 수정할 데이터에만 있는 멤버: 해당 미팅 멤버 데이터 생성
     * - 기존 데이터에만 있는 멤버: 해당 미팅 멤버 데이터 삭제
     * 를 진행한다.
     *
     * @param source [List<MeetingMemberBridge>] 기존 데이터 리스트
     * @param target [List<MeetingMemberBridge>] 수정할 데이터 리스트
     * @throws CRUDException 데이터 CRUD 에러
     */
    private void updateMeetingMemberList(List<MeetingMemberBridge> source, List<MeetingMemberBridge> target) throws CRUDException {
        try {
            // create or update (target)
            for (MeetingMemberBridge tg : target) {
                Optional<MeetingMemberBridge> optionalMeetingMember = source.stream()
                        .filter(org -> org.getPMember().getMemberNo() == tg.getPMember().getMemberNo())
                        .findFirst();

                if (optionalMeetingMember.isPresent()) {
                    MeetingMemberBridge meetingMember = optionalMeetingMember.get();
                    // update source
                    meetingMember.setOrderNum(tg.getOrderNum());
                    meetingMemberRepository.save(meetingMember);
                } else {
                    // create target
                    meetingMemberRepository.save(tg);
                }
            }

            // delete (source)
            Iterator<MeetingMemberBridge> iterator = source.iterator();
            while (iterator.hasNext()) {
                MeetingMemberBridge src = iterator.next();
                boolean isExist = false;
                for (MeetingMemberBridge tg : target) {
                    if (src.getPMember().getMemberNo() == tg.getPMember().getMemberNo()) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    meetingMemberRepository.deleteById(src.getUuid());
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CRUDException("미팅 수정(멤버 미팅 수정) 도중 오류가 발생했습니다.");
        }
    }

    /**
     * 팬유저 리스트를 수정한다.
     * 기존과 수정할 데이터의 팬유저 id(email)를 비교하여,
     * - 동일한 팬유저: 미팅 팬유저 데이터 수정
     * - 수정할 데이터에만 있는 팬유저: 해당 미팅 팬유저 데이터 생성
     * - 기존 데이터에만 있는 팬유저: 해당 미팅 팬유저 데이터 삭제
     * 를 진행한다.
     *
     * @param source [List<MeetingMemberBridge>] 기존 데이터 리스트
     * @param target [List<MeetingMemberBridge>] 수정할 데이터 리스트
     * @throws CRUDException 데이터 CRUD 에러
     */
    private void updateMeetingFUserList(List<MeetingFUserBridge> source, List<MeetingFUserBridge> target) throws CRUDException {
        try {
            // create or update (target)
            for (MeetingFUserBridge tg : target) {
                Optional<MeetingFUserBridge> optionalMeetingFUser = source.stream()
                        .filter(org -> org.getEmail().equals(tg.getEmail()))
                        .findFirst();

                if (optionalMeetingFUser.isPresent()) {
                    MeetingFUserBridge meetingFuser = optionalMeetingFUser.get();
                    // update source
                    meetingFuser.setOrderNum(tg.getOrderNum());
                    meetingFUserRepository.save(meetingFuser);
                } else {
                    // create target
                    meetingFUserRepository.save(tg);
                }
            }

            // delete (source)
            Iterator<MeetingFUserBridge> iterator = source.iterator();
            while (iterator.hasNext()) {
                MeetingFUserBridge src = iterator.next();
                boolean isExist = false;
                for (MeetingFUserBridge tg : target) {
                    if (src.getEmail().equals(tg.getEmail())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    meetingFUserRepository.deleteById(src.getNo());
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CRUDException("미팅 수정(유저 미팅 수정) 도중 오류가 발생했습니다.");
        }
    }
}
