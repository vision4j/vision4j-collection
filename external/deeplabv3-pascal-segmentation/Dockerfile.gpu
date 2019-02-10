FROM tensorflow/tensorflow:1.8.0-gpu

RUN apt-get update

RUN apt-get install -y \
        build-essential \
        cmake \
        git \
        wget \
        unzip \
        yasm \
        pkg-config \
        libswscale-dev \
        libtbb2 \
        libtbb-dev \
        libjpeg-dev \
        libpng-dev \
        libtiff-dev \
        # libjasper-dev \ this library seems to not be installable at the moment
        libavformat-dev \
        libhdf5-dev \
        libpq-dev

WORKDIR /


RUN apt-get update && apt-get install -y wget && wget https://github.com/opencv/opencv/archive/3.4.1.zip \
    && unzip 3.4.1.zip \
    && mkdir /opencv-3.4.1/cmake_binary \
    && cd /opencv-3.4.1/cmake_binary \
    && cmake -DBUILD_TIFF=ON \
    -DBUILD_opencv_java=OFF \
    -DWITH_CUDA=OFF \
    -DENABLE_AVX=ON \
    -DWITH_OPENGL=ON \
    -DWITH_OPENCL=ON \
    -DWITH_IPP=ON \
    -DWITH_TBB=ON \
    -DWITH_EIGEN=ON \
    -DWITH_V4L=ON \
    -DBUILD_TESTS=OFF \
    -DBUILD_PERF_TESTS=OFF \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DCMAKE_INSTALL_PREFIX=$(python -c "import sys; print(sys.prefix)") \
    -DPYTHON_EXECUTABLE=$(which python) \
    -DPYTHON_INCLUDE_DIR=$(python -c "from distutils.sysconfig import get_python_inc; print(get_python_inc())") \
    -DPYTHON_PACKAGES_PATH=$(python -c "from distutils.sysconfig import get_python_lib; print(get_python_lib())") .. \
    && make install \
    && rm /3.4.1.zip \
    && rm -r /opencv-3.4.1


RUN pip install grpcio-tools==1.12.0 

RUN mkdir -p /app
RUN wget http://download.tensorflow.org/models/deeplabv3_pascal_trainval_2018_01_04.tar.gz -O /app/deeplabv3_pascal_trainval_2018_01_04.tar.gz


RUN mkdir -p /app/proto
COPY segmentation.proto /app/proto
COPY *.py /app/
RUN python -m grpc_tools.protoc -I/app/proto --python_out=/app --grpc_python_out=/app /app/proto/segmentation.proto

CMD ["python", "/app/segmentation_server.py"]

