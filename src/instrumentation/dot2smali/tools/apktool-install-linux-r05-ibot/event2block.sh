#!/bin/sh
apktool b $1 unsignedEDGExample.apk
java -jar signapk.jar  platform.x509.pem platform.pk8  unsignedEDGExample.apk signedEDGExample.apk
#adb install signedEDGExample.apk