#!/bin/bash
javac -cp .:./src:./lib/1.8/jfxrt.jar:./lib/vlcj/jna-3.5.2.jar:./lib/vlcj/platform-3.5.2.jar:./lib/vlcj/vlcj-3.8.0.jar:./lib/slf4j-api-1.7.21.jar -d ./bin ./src/VoxspellPrototype/VoxspellPrototype.java

if [ $? -eq 0 ] ; then
    echo "Compilation Successful!";
else
    echo "Compilation Failed"
fi

java -cp .:./bin:./lib/1.8/jfxrt.jar:./lib/vlcj/jna-3.5.2.jar:./lib/vlcj/platform-3.5.2.jar:./lib/vlcj/vlcj-3.8.0.jar:./lib/slf4j-api-1.7.21.jar VoxspellPrototype.VoxspellPrototype
