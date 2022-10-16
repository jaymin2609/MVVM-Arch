#!/bin/sh

# ps -e

# adb push get_process.sh /data/local/tmp

cmd package list packages

pm uninstall -k app.yulu.bike
