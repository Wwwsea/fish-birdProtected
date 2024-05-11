FROM nginx

MAINTAINER 鸟类数字博物馆后台

RUN rm /etc/nginx/conf.d/default.conf

ADD default.conf /etc/nginx/conf.d/

COPY dist/ /usr/share/nginx/html/
