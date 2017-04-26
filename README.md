# Assignment 3 -OOP

This is an Android application using Bluetooth low energy (BLE) beacons technology to provide proximity-based experiences for users.

It runs on an Android device only and will not run on a virtual device.

[![Video](http://img.youtube.com/vi/fWrekPh4cwc/0.jpg)](https://www.youtube.com/watch?v=fWrekPh4cwc&feature=youtu.be)


Introduction
------------
Due to my interest of the "Internet of things" I decided to create this app usign Bluetooth 4.0 to detect nearby devices called Beacons. So first of all I got one of this beacons on Amazon. use the software that was provided by the beacon manufacturer to do setting it up.

So, what is a beacon?
------------

A beacon is a small device (approx 3cm x 5cm x 2cm) that constantly sends out radio signals to nearby smartphones and tablets, containing a small amount of data.
Whilst beacons can have up to 70m range with no obstructions, this can drop significantly through walls which are made with metal or brick (though thin/stud walls have a much smaller effect).
In addition to the potential range of BLE, most protocols also operate with three ranges of distance: far, near and immediate – and a device can do something different at each range.

What is FindMe?
------------
As part of my Object Oriented Programming Assignment 3 I wanted to learn how to use Android studio and code a more complex Android app than the basic examples I checked in the past.

FindMe reminds you that you left something behind using a beacon that you can hang on you keychain or attach to things you think you might lose. Once you realize you lost something, you can use Bluetooth to help you locate them.

How Does it Work?
------------
FindMe allows you to scan bluetooth devices and display the distance between your smartphone and the beacons. In order to show my understanding of how an Android app works using a Database I created a registration page and a login. Once you have logged in, you can track the beacons devices and find the one you own by its UUID. Each ibeacon has a set of identifying numbers. The UUID is a general device identifier. By clicking on your device you can see how far you are from it. This app calculates proximity based on the power level and signal strentgh of te beacon. If you enable the app to remind you when you are more than 10 meters ways from you device FindMe will send a push notification to your mobile phone letting you know that you left something behind.

The platform & language
---------------------------

Programming this application involved mostly Java code and some xml. I also use PHP files to make requests to the database which is  is hosted on a free hosting site called 000webhost.com

Most of the method used in this app are from Android's libraries. To use this method properly you need to understand what they do and where to use them appropriately.

The application makes use of the Android Beacon Library
(An Android library providing APIs to interact with Beacons). This Api makes your life easier connecting to Ble devices.


Key Notes
---------

* Use of Android Libraries.
* Use of AltBeacons Library fro Android (Involved to learn this API documentation)
* Use of Online Database
* Forms validation (Login,Register)
* App Permissions: Internet connection and Bluetooth
* Use of extending classes and implementation of interfaces.
* Use of Bluetooth 4.0 technology
* Use of Relative Layout gives adaptability to different size screens.
* Use of the background thread to check Internet Connection.
* Use of handler to update UI items on the screen every 3 seconds.