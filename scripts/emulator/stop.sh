#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#$DIR/enable_animations.sh

echo "Stopping emulator..."
$ANDROID_HOME/platform-tools/adb emu kill || true

yes | echo "Emulator stopped!"
