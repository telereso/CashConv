#!/bin/bash

#avdName="Pixel_7_Pro_API_34"
avdName="Nexus_5X_API_28"

if $ANDROID_HOME/cmdline-tools/latest/bin/avdmanager list avd | grep -q "$avdName"; then
    echo "There is an existing an emulator to run screenshot tests"
    exit 0;
fi

#echo "Creating a brand new SDCard..."
#rm -rf sdcard.img
#$ANDROID_HOME/emulator/mksdcard -l e 1G sdcard.img
#echo "SDCard created!"

kern=$(sysctl -n kern.version | tr '[:upper:]' '[:lower:]')

if [[ $kern == *"arm64"* ]]; then
  abi="arm64-v8a"
else
  abi="x86_64"
fi

#image="system-images;android-34;google_apis_playstore;arm64-v8a"
image="system-images;android-28;google_apis_playstore;$abi"

echo "Downloading the image to create the emulator..."
echo y | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "$image"
echo "Image downloaded!"

echo "Creating the emulator to run screenshot tests..."
echo no | $ANDROID_HOME/cmdline-tools/latest/bin/avdmanager create avd -n "$avdName" -k "$image"
echo "Emulator created!"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#cp $DIR/config.ini ~/.android/avd/"$avdName".avd/config.ini
#cp sdcard.img ~/.android/avd/"$avdName".avd/sdcard.img
#cp sdcard.img.qcow2 ~/.android/avd/"$avdName".avd/sdcard.img.qcow2
