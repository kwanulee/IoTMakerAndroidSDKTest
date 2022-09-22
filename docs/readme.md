# IoTMakers Android SDK를 통한 IoT 애플리케이션 개발
## 목표
- [1. IoTMakers 포털에 IoT 애플리케이션 등록](#1)
- [2. Android 앱 프로젝트 설정](#2)
- [3. IoTMakers 연동 Android 앱 예제 설명](#3)
- [4. 예제 프로젝트 코드 다운로드](#4)

---
<a name="1"></a>
## 1. IoTMakers 포털에 IoT 애플리케이션 등록

- IoT 애플리케이션이 **IoTMakers** 플랫폼을 이용하기 위해서는 앱 인증에 필요한 **App ID**와 **App Secret**을 발급 받아야 한다. 
- **App ID**와 **App Secret**은 다음 **앱 등록 절차**를 통해서 등록된 앱을 통해 확인할 수 있다.
- **앱 등록 절차**
	1. 포탈 메뉴 상단의 IoT 개발 탭 내 앱 등록 메뉴에서 아래와 같이 [**App 등록**] 버튼을 클릭합니다.

		<img src="https://iotmakers.kt.com/openp/assets/images/guide_app_reg_and1.png" width = 500> 
			
	2. 기본정보 및 상세정보를 입력 후, **저장** 버튼을 클릭합니다.

		- 기본정보 입력
			- \+ 첨부 : 앱 아이콘 이미지 파일 등록
			- **App name**: 등록 할 앱 이름입력 (필수)
			- OAuth 설정: 3rd Party 개발사가 플랫폼 사용자 인증 시 OAuth 인증 사용 유무 선택
			- OAuth redirect url: Oauth 인증 시 Access token 전송을 위한 URL 입력
			<img src="https://iotmakers.kt.com/openp/assets/images/guide_app_reg_and2.png" width = 500>

		- 상세정보 입력
			- **카테고리**: 앱의 카테고리 유형을 선택
			- 기타 나머지 정보는 필요에 따라 입력
			
			<img src="https://iotmakers.kt.com/openp/assets/images/guide_app_reg_and3.png" width = 500>
			
	3. 등록된 앱의  **App ID**와 **App Secret**를 확인한다.


<a name="2"></a>
## 2. Android 앱 프로젝트 설정
- 사용 Android Studio 버전: **Android Studio Dolphin | 2021.3.1** 

### 2.1 Android 앱 프로젝트에 IoTMakers Android SDK 설정

1. Android Studio를 통해서 **Android 프로젝트를 생성**
	- *IoTMakerAndroidSDKTest* 이름의 Android 프로젝트를 생성한다.

2. **IoTMakers Android SDK 다운로드**
	- **IoTMakers 포탈**에서 [**IoT개발** > **SDK 다운로드**](https://iotmakers.kt.com/openp/index.html#/sdk) 를 클릭합니다.
	- [**Android SDK**](https://iotmakers.kt.com/openp/assets/files/iotmakers_sdk_android_1.3.3.zip)를 다운로드한 후, 압축을 해제합니다.

3. 작업 중인 Android Project(*IoTMakerAndroidSDKTest*) 최상단에서 우클릭하여 **Open Module Setting** 선택
2. Modules에서 '**+**' 버튼 클릭하여 **Import Gradle Project** 선택 후 '**Next**'
3. Source directory에 압축 해제한 SDK 파일(*iotmakers\_sdk\_android*) 선택 후 '**OK**'
4. **Open Module Setting**의 **Dependencies**에서 app 선택 후 **Declared Dependencies** '**+**' > **Module Dependency**
5. 추가한 모듈 선택하고 '**OK**'
6. 작업 중인 Android Project 최상단 내 **settings.gradle** 파일의 import 부분에 해당 모듈이 정상적으로 선언되어 있는지 확인


### 2.2 최신 Android 플랫폼 버전 반영을 위한 설정 변경
- **IoTMakers** 포탈에 배포된 SDK는 Android4.0 기반으로 만들어진 것이므로, **최신 Android 플랫폼 버전 (API level 31 이상)**에서 정상적으로 동작시키기 위해서 다음과 같은 추가작업을 수행해야 합니다.
	- **build.gradle (Module: iotmakers\_sdk\_android)** 스크립트 수정 후, 상단의 **Sync Now** 클릭
		- **allprojects** {..} 주석처리
		- "buildToolsVersion "28.0.3" 삭제
	- **AndroidManifest.xml** 수정
		- \<application\> 부분에 **android:usesCleartextTraffic="true"** 로 설정
		
			```xml
			<application
				android:allowBackup="true"
				android:icon="@mipmap/ic_launcher"
				android:label="@string/app_name"
				android:roundIcon="@mipmap/ic_launcher_round"
				android:supportsRtl="true"
				android:theme="@style/AppTheme"
				android:usesCleartextTraffic="true">  
			```	
		   	
		   	- Android 9.0(API level 28) 부터 강화된 네트워크 보안정책으로 인해 특정 도메인에 대해 일반적인 텍스트 사용을 명시적으로 선언한 경우에만 허용함

	
<a name="3"></a>
## 3. IoTMakers 연동 Android 앱 예제

- 이하 모든 **IoTMakers** 연동 Android 얩 예제 프로젝트에서는 **IoTMakers** 서버와 Android 앱이 인터넷 통신을 위해서 **AndroidManifest.xml** 파일에 다음 권한을 추가해야 합니다.
	- \<manifest\> 태크 안에 다음 **android.permission.INTERNET** 권한을 추가

		```xml
		<manifest ...>
		
			<uses-permission android:name="android.permission.INTERNET" />
			
		</manifest>
		```
		
### 3.1 [플랫폼 로그인](login.md)

### 3.2 [디바이스 목록 및 디바이스 상세 조회](devices.md)

### 3.3 [디바이스 태그스트림 목록, 태그스트림 로그 정보조회와 제어 명령 보내기](tagStrm.md)
	
<a name="4"></a>
## 4. 예제 프로젝트 코드 다운로드
[Github Repository](https://github.com/kwanulee/IoTMakerAndroidSDKTest)의 **Code** 버튼을 클릭한후 [**Download ZIP**](https://github.com/kwanulee/IoTMakerAndroidSDKTest/archive/refs/heads/main.zip) 메뉴를 선택하여 Android 앱 프로젝트를 다운받고 압축을 해제한다. 

