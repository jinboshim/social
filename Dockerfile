# 베이스 이미지 설정
FROM ubuntu:22.04

# 기본 패키지 설치
RUN apt-get -y update
RUN apt-get -y upgrade

# 시간대 설정 (Asia/Seoul 시간대로)
RUN DEBIAN_FRONTEND=noninteractive TZ=Asia/Seoul apt-get -y install tzdata
RUN ln -fs /usr/share/zoneinfo/Asia/Seoul /etc/localtime && dpkg-reconfigure -f noninteractive tzdata

# OpenJDK 설치 !!!필요한 JDK 버전에 맞는 설치!!!
RUN apt install -y openjdk-8-jdk

# 프로젝트 jar 파일 복사
# !!!jar 파일 이름 수정!!!
COPY ./target/social.network-0.0.1-SNAPSHOT.jar /app/

# 작업 디렉토리 생성 및 이동
WORKDIR /app

# Spring Boot 애플리케이션 실행
# !!!jar 파일 이름 수정!!!
#CMD ["java", "-jar", "-Dspring.profiles.active=dev8080", "/app/springboot_mvc_template-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "/app/social.network-0.0.1-SNAPSHOT.jar"]


# build 하여 image 만들기
#docker image build --pull=true -f "Dockerfile" -t social:latest .
#docker build -t social-test .

# image 실행
#docker run -p 8080:8080 social

# log 확인
#docker logs -f --tail 100 social

# Container stop
#docker stop [container id] or docker kill [container id]

# Container 제거
#docker rm [container name or id]

# image 제거
#docker rmi [image name]

# container 접속
#docker exec -it CONTAINER_ID /bin/bash