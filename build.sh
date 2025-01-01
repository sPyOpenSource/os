#!/bin/bash

if [ "$1" == "b32" ] ; then
  ./compile $2 $3 $4 $5 $6 $7 $8 $9 -t ia32 -p out/ -o boot -O "bootconf.lin#floppy32" work
  exit
fi

if [ "$1" == "b32opt" ] ; then
  ./compile $2 $3 $4 $5 $6 $7 $8 $9 -t ia32opt -p out/ -o boot -O "bootconf.lin#floppy32" work
  exit
fi

if [ "$1" == "b64" ] ; then
  ./compile $2 $3 $4 $5 $6 $7 $8 $9 -t amd64 -p out/ -o boot -O "bootconf.lin#floppy64" work
  exit
fi

if [ "$1" == "qemu" ] ; then
  qemu-system-x86_64 -boot a -m 32 -fda ./out/BOOT_FLP.IMG -localtime &
  exit
fi

if [ "$1" == "sun" ] ; then
  javac -sourcepath work -d clss sun/sun/SunRaytrace.java && java -cp clss sun.SunRaytrace
  exit
fi

echo "try b32, b32opt, b64 or qemu for PicOS-test"
echo "try sun for raytrace-test with SunJava"
