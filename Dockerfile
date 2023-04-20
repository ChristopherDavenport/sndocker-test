FROM virtuslab/scala-cli

RUN apt-get update
RUN apt-get -y install build-essential git cmake openssl libssl-dev
RUN git clone https://github.com/aws/s2n-tls.git && cmake -S s2n-tls -B build/shared -DBUILD_SHARED_LIBS=OFF && cmake --build build/shared && cmake --install build/shared
RUN ldconfig

COPY joke.scala .
RUN scala-cli compile joke.scala
RUN scala-cli --power package joke.scala

RUN ls

FROM ubuntu:22.04

COPY --from=0 ./Joke .

ENV S2N_DONT_MLOCK=1

CMD ./Joke
