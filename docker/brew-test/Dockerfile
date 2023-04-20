FROM ubuntu:22.04


RUN apt-get update
# Baseline for linuxbrew
RUN apt-get -y install build-essential procps curl file git

RUN /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

RUN /home/linuxbrew/.linuxbrew/bin/brew install s2n

COPY sndocker-test-out .

ENV LD_LIBRARY_PATH "/home/linuxbrew/.linuxbrew/Cellar/openssl@3/3.1.0/lib:/home/linuxbrew/.linuxbrew/Cellar/s2n/1.3.42/lib"

CMD ./sndocker-test-out