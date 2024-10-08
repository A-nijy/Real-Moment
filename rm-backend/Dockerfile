# 빌드 이미지로 OpenJDK 11 & Gradle을 지정
FROM gradle:jdk17-alpine AS build

# 소스코드를 복사할 작업 디렉토리를 생성
WORKDIR /app

# 라이브러리 설치에 필요한 파일만 복사
COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

# 호스트 머신의 소스코드를 작업 디렉토리로 복사
COPY . /app

# Gradle 빌드를 실행하여 JAR 파일 생성!!
#RUN gradle clean build --no-daemon

# Gradle 빌드를 실행하여 JAR 파일 생성 (이때 테스트는 생략 : ddl을 none에서 update로 하니 데이터베이스와 연결을 해야하는데 연결을 못해서 에러남)
RUN gradle clean build -x test --no-daemon

# 런타임 이미지로 OpenJDK 11-jre-slim 지정
FROM openjdk:17-jdk-slim

# Timezone 설정을 위해 tzdata 패키지 설치
RUN apt-get update && apt-get install -y tzdata

# Timezone 설정
ENV TZ=Asia/Seoul

# 애플리케이션을 실행할 작업 디렉토리를 생성
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사!
COPY --from=build /app/build/libs/*.jar /app/leafy.jar

EXPOSE 8080 
ENTRYPOINT ["java"] 
CMD ["-jar", "leafy.jar"]
