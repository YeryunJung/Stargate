package com.ssafy.stargate.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataUtil {
    /**
     * Boolean데이터를 문자열로 바꿔준다.
     * null값이면 false로 처리한다.
     * 
     * @param data [Boolean] 변환할 데이터
     * @return [String] 변환된 문자열
     */
    public String booleanToString(Boolean data) {
        return (data != null) ? String.valueOf(data) : "false";
    }

    /**
     * 문자열 데이터를 Boolean으로 바꿔준다.
     * null값이면 false로 처리한다.
     *
     * @param data [String] 변환할 문자열
     * @return [Boolean] 변환된 데이터
     */
    public Boolean stringToBoolean(String data) {
        return (data != null) ? Boolean.valueOf(data) : false;
    }
}
