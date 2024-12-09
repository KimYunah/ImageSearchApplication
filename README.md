# [김윤아] Android 과제
ImageSearchApplication
- 화면 구성 : Home(Main, Bookmark), Search, Viewer
- 프로젝트 구조
  1. LAUNCHER Activity : HomeActivity (Main 탭과 Bookmark 탭 포함)
  2. MVVM 구조로 *Activity, *Fragment에서 UI관련 동작 수행. *ViewModel에서 로직 처리 수행
  3. 패키지 구성
     1. .framework : 기능 별 구성
            .network : Retrofit API 정의
            .db : Room DB 및 DAO 정의
            .data : 데이터형 정의 
     2. .ui : 화면 구성
            .view : Activity 및 Fragment
            .adapter : RecyclerView Adapter
            .viewModel : ViewModel 클래스
     3. .di : Hilt를 사용한 의존성 주입 설정
     4. .domain : 도메인 구성
  4. 네트워크 관련 라이브러리 : Retrofit, Okhttp,
  5. DB 관련 라이브러리 : Room
  

### 사용 라이브러리와 용도
1. Retrofit + OkHttp : 네이버 이미지 검색 API 호출
2. Room : 북마크 데이터 로컬 DB에 저장 및 관리
3. Hilt : 의존성 주입을 통한 모듈화
4. Glide : 이미지 로딩 및 캐싱
5. Coroutine : 비동기 작업 및 데이터 스트림 처리
6. Jetpack Navigation : 화면 간 이동 처리
7. Material 3 : 디자인 가이드 라인 적용


### 주요 기능
### **1. 메인화면**
- 네이버 이미지 검색 API 사용. (50개씩 데이터 로드)
- 자동 더보기 기능 지원.
- Pull-to-Refresh 기능 추가.
- 아이템 클릭 시 뷰어 페이지로 이동.
- 북마크 추가/삭제 기능 제공.

### **2. 북마크화면**
- 북마크 개별 선택 및 다중 선택 삭제 기능 제공.
- 아이템 클릭 시 뷰어 페이지로 이동.

### **3. 검색 페이지**
- 상단 검색바에서 키워드 입력 후 1초 이상 미입력시 자동 검색 수행.
- 검색 결과가 없거나 오류 발생 시 Toast 알림 표시.
- 검색된 데이터를 메인 탭과 동일한 형식으로 표시.
- 아이템 클릭 시 뷰어 페이지로 이동.

### **4. 뷰어 페이지**
- 클릭된 이미지를 중심으로 위/아래로 30개의 이미지를 세로 스크롤 가능한 형태로 로드.
- Glide를 사용하여 이미지 로드. (메모리 관리를 위해 캐싱 적용)
- 클릭된 이미지의 위치로 이동해서 먼저 노출되도록 구현.


### 이미지 처리
1. Glide 사용
    - `DiskCacheStrategy.AUTOMATIC`: (기본값)메인화면 및 검색화면에서 사용.
    - `DiskCacheStrategy.ALL`: 뷰어화면에서 디스크 캐싱을 통해 네트워크 요청 최소화.
