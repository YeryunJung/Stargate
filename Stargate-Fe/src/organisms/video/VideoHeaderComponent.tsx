import TimeLeftComponent from '@/atoms/common/TimeLeftComponent';
import ProFileIcon from '@/atoms/video/ProFileIcon';
import React, { useEffect, useState } from 'react';

interface VideoHeaaderProps {
  min: number;
  sec: number;
  type: string;
  participantsData:
    | {
        email: string;
        name: string;
        nickname: string;
        birthday: string;
        isPolaroidEnable: boolean;
        postitContents: string;
        totalMeetingCnt: number;
      }[]
    | null;
  meetingIdx: number;
}

// Props 에 유저 데이터 넣는 부분 추가해주기
// type.ts에 유저 데이터 타입 추가해주고 전역으로 사용하기
const VideoHeaderComponent: React.FC<VideoHeaaderProps> = ({
  min,
  sec,
  type,
  participantsData,
  meetingIdx,
}) => {
  const [nextUser, setNextUser] = useState<string | null>(null);

  useEffect(() => {
    if (participantsData) {
      if (participantsData[meetingIdx + 1]) {
        setNextUser(participantsData[meetingIdx + 1].name);
      } else {
        setNextUser(null);
      }
    }
  }, [participantsData]);

  return type == 'star' && participantsData != undefined ? (
    <div className="flex flex-row w-screen my-5">
      <div className="flex items-center basis-1/2 w-full text-white ml-5">
        <ProFileIcon />
        <div className="mx-auto">
          <p>이름: {participantsData[meetingIdx].name}</p>
          <p>생일: {participantsData[meetingIdx].birthday}</p>
          <p>촬영 여부: {participantsData[meetingIdx].isPolaroidEnable}</p>
        </div>
      </div>
      <div className="flex basis-1/4 justify-end mr-5">
        <TimeLeftComponent min={min} sec={sec} />
      </div>
      {nextUser ? (
        <div className="flex basis-1/4 justify-center text-white">
          <p className="text-xl mt-1">NEXT</p>
          <p className="modal-title">{nextUser}</p>
        </div>
      ) : (
        <div className="flex basis-1/4"></div>
      )}
    </div>
  ) : (
    <div className="flex flex-row w-screen my-5">
      <div className="basis-1/2 w-full text-white ml-5">
        <ProFileIcon />
      </div>
      <div className="flex basis-1/4 justify-end mr-5">
        <TimeLeftComponent min={min} sec={sec} />
      </div>
      {nextUser ? (
        <div className="flex basis-1/4 justify-center text-white">
          <p className="text-xl mt-1">NEXT</p>
          <p className="modal-title">{nextUser}</p>
        </div>
      ) : (
        <div className="flex basis-1/4"></div>
      )}
    </div>
  );
};

export default VideoHeaderComponent;