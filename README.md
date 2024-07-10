# Real Moment

Real Moment 는 화장품을 판매하는 온라인 쇼핑몰입니다.

<br>
<br>

- Real Moment 쇼핑몰 URL  :  
- Real Moment 관리자 URL  :  

<br>
<br>


## :book: 프로젝트 개요

Spring Boot 학습 후 처음 처음 진행하는 프로젝트로 새롭고 다양한 기술 스택을 추가로 학습하며 이해하고 적용하는 데 중점을 두었습니다.

그리고 고민끝에 이에 적합한 주제가 온라인 쇼핑몰이라고 판단하여 프로젝트를 진행하였습니다.

주제를 선정 후 기본적인 ERD와 FlowChart를 설계하고 이에 따라 API 명세서를 작성하여 웹 서버 개발을 진행하였습니다.

<br>

- [rm-front-user] - GitHub 주소 :  https://github.com/pado232/RealMoment
- [rm-front-admin] - GitHub 주소 :  https://github.com/pado232/ShopAdmin
- [rm-backend] - GitHub 주소 :  https://github.com/A-nijy/rm-backend

<br>
<br>

## :busts_in_silhouette: 참여 인원

- FE 1명 (팀원) : React를 이용한 전체적인 클라이언트 구현

- BE 1명 (본인) : ERD,  API 명세서,  서버,  데이터베이스,  API 개발

<br>
<br>

## :alarm_clock: 개발 기간

2024.01.10 ~ 2024.07.07

<br>
<br>

## :wrench: 기술 스택

- 백엔드  :  Spring Boot,  String Security,  Spring Data JPA,  QueryDSL
- 데이터베이스  :  PostgreSQL
- 인프라  :  AWS EC2,  AWS ELB,  AWS S3,  Docker,  Nginx
- CI/CD  :  GitHub Actions
- 보안  :  JWT

<br>
<br>

## :file_folder: [rm-backend] Project Structure

```sh
└─src
    └─main
       └─java/rm/backend
          ├─adminController
          ├─adminService
          ├─component
          ├─config
          ├─controller
          ├─domain
          │  ├─AutoCheck
          │  ├─entity
          │  └─repository
          │      ├─custom
          │      └─impl
          ├─dto
          │  ├─commonDto
          │  └─innerDto
          ├─enumeration
          ├─exception
          ├─jwt
          │  └─config
          │      ├─auth
          │      ├─filter
          │      └─util
          ├─PortOne
          │  └─config
          ├─scheduled
          ├─service
          └─smtp
```

<br>
<br>

## :mag: Real Moment 아키텍처

![github-image-001](https://github.com/A-nijy/Real-Moment/assets/150596887/22de766b-a1e8-4469-a18e-d3a177bb4b64)

<br>
<br>

## :mag: AWS 아키텍처

![github-image-006](https://github.com/A-nijy/Real-Moment/assets/150596887/b9bd35d4-201f-435e-8af6-0daabadd1cb6)

<br>
<br>

## :mag: 한국 전자결제 서비스 흐름 & 포트원 동작 구조

![github-image-002](https://github.com/A-nijy/Real-Moment/assets/150596887/a55ef2ab-38a5-4c94-ab35-77fc097b717d)

![github-image-003](https://github.com/A-nijy/Real-Moment/assets/150596887/37997ba4-85f6-4d16-8900-51c1817815ff)

![github-image-004](https://github.com/A-nijy/Real-Moment/assets/150596887/6a3197d4-13c0-4824-bd7e-ebaa090e9694)

<br>
<br>

## :floppy_disk: ERD

![github-image-005](https://github.com/A-nijy/Real-Moment/assets/150596887/ee23822d-79c4-4fd0-a791-4ef0daa15b54)

<br>
<br>

## :clipboard: Real Moment 프로젝트 주요 기능

<br>

### :sunny: JWT 인증 및 인가 시스템

#### 기능
- Spring Security에서 JWT용 커스텀 Filter Chain을 추가하여 사용자 인증 및 인가를 관리하도록 구현하였습니다. 사용자 로그인 시 검증을 거쳐 Access Token과 Refresh Token을 발급하고, Access Token 만료 시 Refresh Token으로 재발급을 진행하여 지속적인 로그인 상태를 유지하도록 했습니다.

#### 보안 강화
- 로그아웃이나 회원 탈퇴 시 Refresh Token을 DB에서 제거하여 해당 토큰 도용 방지 및 세션 무효화를 통해 보안을 강화했습니다.

#### JWT 선택 이유
- JWT는 Stateless 특성으로 인해 확장성이 높고 서버 부하가 줄어들며, 인증 및 인가 처리 속도가 빠르기 때문에 선택하였습니다.
- 다만 Refresh Token을 DB에 저장하기 때문에 완벽하게 Stateless라고 부르기에는 부족하지만 매번 Refresh Token을 사용하는 것이 아니기 때문에 Session 보다는 여전히 확장성과 서버 부하를 크게 줄일 수 있습니다.

<br>

---

### :sunny: 회원가입 유효성 검사

#### 기능
- 회원가입에 입력하는 모든 정보(아이디, 비밀번호, 이메일 등)에 대해 정해진 규칙에 따른 유효성 검사를 수행합니다.

#### 보안 향상
- 유효성 검사를 통과하지 못한 데이터는 회원가입을 진행할 수 없어, 데이터의 정확성과 보안을 향상시킵니다.

<br>

---

### :sunny: 동적 데이터 조회

#### 기능
- QueryDSL을 사용하여 상품, 주문 내역, 회원 정보 등을 다양한 검색 조건에 따라 조회합니다.
- 검색 결과는 페이징을 통해 효율적으로 데이터를 분리 후 제공하여 사용자가 해당 서비스를 더욱 편리하게 이용할 수 있습니다.

<br>

---

### :sunny: 회원, 관리자 페이지 분리

#### 기능
- 사용자와 관리자 기능을 분리하여 서로 다른 인터페이스와 접근 권한을 제공합니다.

#### 보안 및 관리 효율성 향상
- 사용자와 관리자 별로 접근하는 페이지를 분리하고 각각 역할에 맞는 기능을 제공하여 시스템의 보안과 관리 효율성을 증가시킵니다.

<br>

---

### :sunny: 권한에 따라 API 접근 분리

#### 기능
- JWT의 Access Token에 포함된 권한 정보를 가지고 API 접근을 제어합니다.

#### 세분화된 접근 관리
- 회원 권한과 관리자 권한을 세분화하고 여기서 관리자는 대표, 운영관리, 회원관리, 일반관리로 분리하여 각 권한에 알맞는 API 접근 권한을 제공합니다.

<br>

---

### :sunny: 이미지 관리

#### 기능
- AWS S3를 이용하여 쇼핑몰의 모든 이미지(로고, 홍보 이미지, 상품 이미지 등)관리합니다.

#### 효율적 관리
- 이미지는 AWS S3에 저장하고 해당 이미지에 대한 정보를 DB에 저장하여 AWS S3 접근을 최소화하여 관리 효율성을 높이고 이미지 로딩 시간을 단축시켰습니다.
- 이미지의 쓰임새에 따라 이미지에 대한 정보를 분리하여 관리합니다.

<br>

---

### :sunny: PortOne 결제 시스템

#### 기능
- PortOne을 통해 PG사(이니시스)와 연동하여 온라인 카드 결제를 진행합니다.
- 결제 전에는 최종적으로 결제 금액을 검증하고, 결제 취소(환불) 시에는 모든 상태를 구매 이전의 상태로 복원합니다.

#### 사용자 경험 및 보안
- 안전한 결제 프로세스를 제공하여 사용자의 신뢰를 높이고, 결제 취소 및 환불 과정에서의 PortOne으로부터 토큰을 발급받아 진행하는 것으로 보안을 강화합니다.

<br>

---

### :sunny: 이메일 발송 인증

#### 기능
- Google SMTP Server를 이용하여 회원가입 시 인증 코드를 사용자가 입력한 이메일로 발송합니다.

#### 보안 인증 강화
- 사용자가 작성한 이메일 주소를 검증하여 회원가입할 계정의 안정성을 향상시킵니다.

<br>
<br>


## :memo: 트러블 슈팅

<br>

### [ DTO 관리의 복잡성 해결 ]

#### :rotating_light: 문제 발생
클라이언트와의 통신을 위한 DTO의 수가 많아지면서, DTO 관리의 복잡성이 증가했습니다.

#### :white_check_mark: 해결 방안
DTO를 기능별로 묶어, 하나의 클래스 내에서 여러 개의 내부 클래스(inner)로 구성함으로써 관리를 용이하게 했습니다.

#### :heavy_exclamation_mark: 느낀점
기능에 따라 DTO를 분류하고 구조화하는 것이 유지보수 효율성을 높인다는 점을 확인했습니다.

<br>

---

### [ 회원 정보 접근 보안 강화 ]

#### :rotating_light: 문제 발생
API 접근 시 사용되는 Access Token과 회원 고유번호 파라미터 불일치로 인해 발생하는 보안 문제가 있었습니다.

#### :white_check_mark: 해결 방안
Spring Security의 커스텀 filter Chain을 활용하여, API 접근 시 AccessToken에 담긴 회원 고유번호와 URL의 파라미터를 자동 비교 검증을 함으로써, 일치하지 않는 사용자의 접근을 차단했습니다.

#### :heavy_exclamation_mark: 느낀점
API 설계 시 보안을 철저히 고려해야 하며, 자동화된 검증 메커니즘을 통해 안전성을 보장받을 수 있음을 알았습니다.

<br>

---

### [ 회원 정보 접근 보안 강화 ]

#### :rotating_light: 문제 발생
QueryDSL을 사용하여 다양한 검색 조건에 따라 데이터를 동적으로 조회할 때, 'where' 문에 모든 조건을 명시하게 되어 코드의 가독성과 재사용성이 떨어지는 문제가 발생했습니다.

#### :white_check_mark: 해결 방안
QueryDSL의 'BooleanExpression' 기능을 활용하여, 각 검색 조건을 별도으 ㅣ조건 메서드로 분리했습니다.
해당 접근 방식을 통해 코드의 가독성을 향상시키고, 필요할 때 재사용할 수 있게 만들어 유지보수를 용이하게 했습니다.

#### :heavy_exclamation_mark: 느낀점
QueryDSL을 사용한 조건 메서드의 분리는 쿼리의 가독성과 유지보수성을 대폭 향상시킬 수 있다는 것을 알았습니다.


