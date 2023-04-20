FROM ubuntu:22.04


RUN apt-get update
RUN apt-get -y install build-essential git cmake openssl libssl-dev
RUN git clone https://github.com/aws/s2n-tls.git && cmake -S s2n-tls -B build/shared -DBUILD_SHARED_LIBS=ON && cmake --build build/shared && cmake --install build/shared
RUN ldconfig
COPY sndocker-test-out .

ENV S2N_DONT_MLOCK=1

CMD ./sndocker-test-out