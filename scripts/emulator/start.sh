#!/bin/bash


#avdName="Pixel_7_Pro_API_34"
avdName="Nexus_5X_API_28"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if ! $ANDROID_HOME/cmdline-tools/latest/bin/avdmanager list avd | grep -q "$avdName"; then
    echo "No emulator for screenshot tests found, creating one..."
    $DIR/create.sh
fi

if $ANDROID_HOME/platform-tools/adb devices -l | grep -q emulator; then
    echo "Emulator already running"
    exit 0
fi

echo "Starting emulator..."

echo "no" | $ANDROID_HOME/emulator/emulator "-avd" "$avdName" "-wipe-data" "-no-snapshot" "-no-audio" "-no-boot-anim" "-gpu" "swiftshader_indirect" &

#$DIR/wait.sh


echo "Emulator ready, disabling animations!"

$DIR/disable_animations.sh

yes | echo "Emulator started and ready to rock!"

