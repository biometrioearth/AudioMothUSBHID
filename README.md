# AudioMothUSBHID

Android aar module to ease the AudioMoth configuration via Android USB interfaces.
Currently works for Firmware version 1.4.4 

Portions from https://github.com/452/USBHIDTerminal/
    
# Compile guide

 1. Clone this repo.
 2. Open it in Android Studio
 3. Connect your phone to WiFi
 4. Connect your phone to PC
 5. Open adb (from command line)
 6. Issue the command  >adb tcpip 5555
 7. Issue the command >adb connect  &#91;your-phohe-ip-address&#93;:5555	
 8. Open testapp
 9. Press debug button
 10. Connect your AudioMoth to the mini-usb to the phone
 11. Press detect button, select a device and accept the permission request
 12. Press configure button
 13. Press set date button 
 14. On Android Studio press Alt+6 Logcat console
 15. Select your phone if it's not already selected
 16. Select the running process com.undercurrency.audiomoth.test
 17. Then, you will see the log messages, try to notice the byte arrays printed on the log, this byte arrays confirm the correct send/receive of the configuration

# Super quick start guide
This code includes a Test App (testapp), it contains a main activity with 4 buttons to test all the functions
included in the audiomoth-usbhid aar library, in the code, inside the assets directory, there's a json file called Ultrasonic.json, 
this file was generated by the AudioMoth desktop application, if you like you are free to change the contents of this json file, compile and install the testapp
to your favorite Android Phone.

1. Run the AudioMoth test demo.
2. Plug AudioMoth device to the phone.
3. Tap Detect button.
3.1 Select the device from the list.
3.2 Accept the permission request to let the app use USB communication.
  This will send the correct packet to the AudioMoth and get back the Device Info (Firmware, Date and Serial number)
  and display it in the screen.
4. Tap Configure button.
 This will send a Json file called Ultrasonic.json to AudioMoth, then will receive the configuration bytes on return from the AudioMoth, and convert it to json again, and show it in the screen.
5. Tap SetDate button.
This will send the correct packet to set the current date to AudioMoth, then will receive the same bytes in return, convert it to a Date and print it on the screen.


 # Developer Guide
 The audiomoth-usbhid library works as an Android Service, this means that the only way to use it is througth events in and events out.
 If you send an event in, this event have its corresponding event out

 | Event In | Event Out  |
 |--|--|
 |PrepareDevicesListEvent  | AudioMothPacketReceiveEvent  |
 |AudioMothSetDateEvent|AudioMothSetDateReceiveEvent|
 |AudioMothConfigEvent|AudioMothConfigReceiveEvent|

This library also have the capability to seriallize and deserialize an AudioMoth binary arrays to the corresponding Java Object called RecordingSettings
Please review the RecordingSettings class and their corresponding methods:
serializeToBytes and the corresponding RecordingSettings(byte[] array) constructor.

To use this aar you may compile it, and then add it in your module gradle.properties like this:

In project structure: 

1. Create a folder in libs directory, such as aars.
2. Put your aar lib into the aars folder.
3. Add the code snippet

repositories {
    flatDir {
        dirs 'libs/aars'
    }
}

to your gradle.properties app module. 

4. Add a dependency in the build.gradle file that belongs to your app module.
dependencies {
    ...
    implementation (name:'audiomoth-usbhid-1.0-debug', ext:'aar')
    implementation 'de.greenrobot:eventbus:2.4.0'
}



In an Activity

1. Add an event bus in onCreate method
2. Write a function called startService with a new Intent to all USBHidTool.class this call start up
the android service responsible for calling AudioMoth
3. In onStart method add a call to startService() and a call to eventBus.register(this)
4. The way to interact with audiomoth is by calling events to configure it:

4.1 If you want to receive the date, firmware and serial number you must send a eventBus.post(PrepareDevicesListEvent()) and write the corresponding code to handle an AudioMothPacketReceiveEvent, inside this event an object called DeviceConfig is instantiated.

4.2 If you want to set the Date to the device, you must send the corresponding event eventBus.post(new AudioMothSetDateEvent(new Date())); and write the corresponding code to receive the response AudioMothSetDateReceiveEvent, inside this event lives the Date from the device.

4.3 If you want to set the AudioMothConfig , you must send the corresponding event eventBus.post(new AudioMothConfigEvent(rs)); with the RecordingSettings object set,
you must create a RecordingSettings object with all the values you want. The RecordingSettings must include an instance of DeviceInfo, so  by example, before that you need to call at least the PrepareDevicesListEvent and receive the DeviceConfig and then set it to the new RecordingSettings. Then you must receive the event onEvent(AudioMothConfigReceiveEvent event), inside this event lives a regenerated RecordingSettings reconstructed from the response back from AudioMoth.

