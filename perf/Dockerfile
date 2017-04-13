FROM java:8

COPY ./build /build
COPY ./conf /conf

VOLUME /results

CMD java -Dlogback.configurationFile=/build/resources/test/logback-gatling.xml -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant -cp "/build/deps/*:/conf" io.gatling.app.Gatling --simulation simulations.${simulation}  --results-folder /results --binaries-folder /build/classes/test/ --bodies-folder /build/resources/test/bodies --data-folder /build/resources/test/data




