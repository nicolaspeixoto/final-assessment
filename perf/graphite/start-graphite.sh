#!/bin/sh
docker rm -f graphite
docker run -d \
  --name graphite \
  -p 88:80 \
  -p 2003:2003 \
  10.95.27.40/graphite