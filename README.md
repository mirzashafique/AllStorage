# AllStorage
AllStorage is a library used for selecting different files from your smart-phone's storage. It provides a tabbed interface which allows you to select files of different types eg. images, videos, sound files etc. You can limitize the maximum number of files which the user can select. This library provides run-time permissions for newer android versions.

# Screenshot

<details>
	<summary>Click to see how image picker looks…</summary>
  
  
  <div class="row">
  <div class="column">
    <img 
src="https://user-images.githubusercontent.com/36470821/50398508-f139d280-0799-11e9-82d6-d475cc0ab319.jpg" height="460" width="284"/>
  </div>
  <div class="column">
    <img 
src="https://user-images.githubusercontent.com/36470821/50398533-2cd49c80-079a-11e9-92ab-a688b9baccab.jpg" height="460" width="284"/>
  </div>
  <div class="column">
    <img src="https://user-images.githubusercontent.com/36470821/50398666-28f54a00-079b-11e9-8476-a3e9dd6ed576.jpg" height="460" width="284"/>
  </div>
  <div class="column">
    <img src="https://user-images.githubusercontent.com/36470821/50398673-36123900-079b-11e9-815a-8245cd555325.jpg" height="460" width="284"/>
  </div>
	
<div class="column">
    <img src=" https://user-images.githubusercontent.com/36470821/50398720-a15c0b00-079b-11e9-93ef-a4631565390d.jpg" height="460" width="284"/>
  </div>
  
</div>
  
  
 
  
</details>

# Download
 [![](https://jitpack.io/v/esafirm/android-image-picker.svg)](https://jitpack.io/#esafirm/android-image-picker)

Add this to your project's `build.gradle`

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add this to your module's `build.gradle` 

```groovy
dependencies {
 	implementation 'com.github.pakbachelors:AllStorage:1.0.0'
}
```



# Usage

For full example, please refer to the `sample` app. 


## Start AllStorage activity

The simplest way to select items from different media types where the limit for number of items of each media is 5

```java
Storager.create(context) // Activity or Fragment
	    .all().start();
``` 

## Recieving Result

```java
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Storage.shouldHandle(requestCode, resultCode, data)) {
            List<SelectedFiles> selectedFiles = Storage.getResults();
            Toast.makeText(getApplicationContext(), selectedFiles.size() + "", Toast.LENGTH_LONG).show();
            Log.d("www", ""+selectedFiles.size());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
``` 

## Handling Runtime permissions

```java
@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Storage.handlePermissions(requestCode, grantResults);
    }
``` 





Complete features of what you can do with AllStorage

```java
Storage.create(context)
.showImages(int n) //allows the user to pick "n" number of items from image type media stored in user's smartphone
.showCamera(int n) //allows the user to snap multiple pics and select "n" number of those items 
.showAudios(int n) //allows the user to pick "n" number of items from audio type media stored in user's smartphone
.showVideos(int n)  //allows the user to pick "n" number of items from video type media stored in user's smartphone
.showFiles(int n) //allows the user to pick "n" number of items from user storage files eg. pdf, zip, txt etc.
.all() //allows the user to pick maximum of 5 items from each of the media type
.start(); // start Storage activity with request code

	
```   

## Images Only

```java
Storage.create(context)
.showImages(int n) //maximum "n" number of images can be selected by the user
.start(); // Could be Activity, Fragment, Support Fragment 

```

## Camera Only

```java
Storage.create(context)
.showCamera(int n) //allows he user to snap pics and maximum "n" number of snapped images can be selected by the user
.start(); // Could be Activity, Fragment, Support Fragment 

```

## Audio files Only

```java
Storage.create(context)
.showAudios(int n) //maximum "n" number of audio files can be selected by the user
.start(); // Could be Activity, Fragment, Support Fragment 

```

## Video files Only

```java
Storage.create(context)
.showVideos(int n) //maximum "n" number of videos can be selected by the user
.start(); // Could be Activity, Fragment, Support Fragment 

```

## Other files

```java
Storage.create(context)
.showFiles(int n) //maximum "n" number of files(pdf, txt, ppt etc.) can be selected by the user
.start(); // Could be Activity, Fragment, Support Fragment 

```


# Contributions




# Modification License

```
Copyright (c) 2018 PakBachelors

The Permission is granted, free of charge, to anyone obtaining a copy of this software, to deal in the Software without
any restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, and/or sell
copies of the Software. 
For suggestions or improvements in the software, feel free to contact at mirza.shf121@gmail.com



