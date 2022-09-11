#!/bin/bash

#TIMESTAMP 변수 생성 후 현재 시각으로 초기화
TIMESTAMP=`date +%s`

#echo : 문자열이나 변수를 출력
echo ">>> server will run!!!!"

DOCKER_ID=Docker_Hub_ID

#태그값을 마지막 시각값으로 초기화
DOCKER_TAG=latest-${TIMESTAMP}

#도커이미지명
DOCKER_IMAGE=$ID/$DOCKER_REPOSITORY_NAME:${DOCKER_TAG}

#sudo : 변경된 계정으로 명령어만 실행하는 명령어입니다.
# A|B(파이프) : A의 내용을 읽어서, B의 입력으로 전달
# -u : 로그인할 때 사용할 docker registry 계정 설정
# -p : 로그인할 때 사용할 비밀번호 설정
echo Docker_Hub_PW |  sudo -S docker login -u ${DOCKER_ID} -p Docker_Hub_PW
echo "docker login completed"

#Docker build
sudo docker build -t ${DOCKER_IMAGE} .
echo "docker build completed"

#Docker hub에 push
sudo docker push ${DOCKER_IMAGE}
echo "docker push completed"