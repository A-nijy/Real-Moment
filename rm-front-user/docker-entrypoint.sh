#!/bin/sh
set -e

# default.conf.template 파일에서 환경 변수를 대체하고 결과를 default.conf에 저장
envsubst '${BACKEND_HOST} ${BACKEND_PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

# 환경 변수를 JavaScript 파일에 주입
if [ -n "$PORTONE_CODE" ]; then
  sed -i "s|PORTONE_CODE|$PORTONE_CODE|g" /usr/share/nginx/html/static/js/*.js
fi


# 다음 명령어를 실행
exec "$@"