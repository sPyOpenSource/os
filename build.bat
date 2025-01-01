@echo off
if "%1"=="b64" goto b64
if "%1"=="b32" goto b32
if "%1"=="b32opt" goto b32opt
if "%1"=="qemu" goto qemu
if "%1"=="sun" goto sun
echo Invalid call, try "b32opt", "b32", "b64" or "qemu" or "sun"
goto ende

:b32opt
echo ---------- Compiler-Output ----------
compile %2 %3 %4 %5 %6 %7 %8 %9 -t ia32opt -p out\ -o boot -O bootconf.txt#floppy32 work
echo ---------- End of C-Output ----------
goto ende

:b32
echo ---------- Compiler-Output ----------
compile %2 %3 %4 %5 %6 %7 %8 %9 -t ia32 -p out\ -o boot -O bootconf.txt#floppy32 work
echo ---------- End of C-Output ----------
goto ende

:b64
echo ---------- Compiler-Output ----------
compile %2 %3 %4 %5 %6 %7 %8 %9 -t amd64 -p out\ -o boot -O bootconf.txt#floppy64 work
echo ---------- End of C-Output ----------
goto ende

:qemu
"%ProgramFiles%\Qemu\qemu-system-x86_64.exe" -L "%ProgramFiles%\Qemu" -m 32 -boot a -fda "%CD%\out\BOOT_FLP.IMG" -localtime
goto ende

:sun
javac -sourcepath work -d clss sun\sun\SunRaytrace.java && java -cp clss sun.SunRaytrace
goto ende

:ende
