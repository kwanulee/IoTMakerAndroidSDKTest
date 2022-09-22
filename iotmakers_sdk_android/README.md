# IoTMakers Android SDK

IoTMakers Android 모바일 앱 제작용 라이브러리 모듈

## Getting Started
### 모바일 앱 프로젝트에 IoTMakers Android SDK 참조하기
1. 작업 중인 Android Project 최상단에서 우클릭하여 Open Module Setting 선택
2. Modules에서 '+' 버튼 클릭하여 Import Gradle Project 선택 후 'Next'
3. Source directory에 압축 해제한 SDK 파일 선택 후 'OK'
4. Open Module Setting의 Dependencies에서 app 선택 후 Declared Dependencies '+' > Module Dependency
5. 추가한 모듈 선택하고 'OK'
6. 작업 중인 Android Project 최상단 내 settings.gradle 파일의 import 부분에 해당 모듈이 정상적으로 선언되어 있는지 확인

## Development Guide
### 모바일 앱 프로젝트에서...
com.kt.gigaiot_sdk에 구현되어 있는 메소드를 이용하여 개발

**서비스 대상 목록 조회 예시**
```
import com.kt.gigaiot_sdk.SvcTgtNewApiNew;

SvcTgtNewApiNew svcTgtApi = new SvcTgtNewApiNew(
        ApplicationPreference.getInstance().getPrefAccessToken(),
        new APICallback<SvcTgtApiResponseNew>() {
            @Override
            public void onStart() {
                // 통신 전 구현 필요한 것들
            }

            @Override
            public void onDoing(SvcTgtApiResponseNew response) {
                // response에 따른 구현
            }

            @Override
            public void onFail() {
                // 통신 실패 시 처리할 것들
            }
        });

svcTgtApi.getNewSvcTgtSeqList();
```

### com.kt.gigaiot_sdk
com.kt.gigaiot_sdk.network에 정의되어 있는 메소드가 구현되어 있다.

### com.kt.gigaiot_sdk.data
APIList 내 메소드의 response에 매핑되는 데이터 구조가 정의되어 있다.

### com.kt.gigaiot_sdk.network
#### APIClient
iotmakers 플랫폼의 base address(iotmakers.kt.com)가 선언되어 있다.
getAUTHClient 메소드를 이용하여 Retrifit 객체를 생성하고, HTTP 메소드를 이용한다.

#### ApiConstants
API 통신에서 공통으로 사용되는 String이 선언되어 있다.

#### APIList
모바일 앱 프로젝트에 구현 가능한 메소드가 작성되어 있다.

1. doPostOauthToken
> 로그인을 위한 토큰 인증
2. doPostDeviceStatus
> 디바이스 상태 정보 조회
3. doPostDeviceRegistration
> 디바이스 등록 (미사용)
4. doPostNewDeviceModify
> 디바이스 정보 수정
5. doPostDeviceUploadImage
> 디바이스 이미지 최초 업로드
6. doPostDeviceUpdateImage
> 디바이스 이미지 수정
7. getImageBase64
> 디바이스 이미지 조회
8. doGetNewDeviceList
> 디바이스 목록 조회
9. doPutNewDeviceCtrl
> 디바이스 제어
10. doGetProtocols
> 프로토콜 조회
11. doGetBindtypes
> 바인드 유형 조회
12. doGetRootgwcncid
> 상위 게이트 연결 id 조회
13. doGetNewSvctgtSeqList
> 서비스 대상 목록 조회
14. doGetTagstreamList
> 태그 스트림 목록 조회
15. doGetTagstreamLog
> 태그 스트림 로그 조회
16. doGetTagstreamLogLast
> 태그 스트림 마지막 로그 조회
17. doPostTagstreamCtrl
> 태그 스트림 제어
18. doGetEventList
> 이벤트 목록 조회
19. doGetEventLogList
> 이벤트 로그 목록 조회
20. doGetEventLogListToStart
> 이벤트 로그 특정 시간 내 목록 조회
21. doGetMemberInfo
> 회원 정보 조회
22. doPostPushSessionReg
> 푸시 서버 등록 (미사용)
23. doPostPushSessionDel
> 푸시 서버 삭제 (미사용)
24. doGetPublicDeviceList
> 공개 디바이스 목록 조회

## Readme History
* 2020-04-08 **초안 작성 - 모바일 앱 개발 프로젝트 셋팅 방법 및 개발 가이드**