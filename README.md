#Crossfader  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/crossfader/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/crossfader)
[![Join the chat at https://gitter.im/mikepenz/crossfader](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mikepenz/crossfader?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**Crossfader** is a small library to provide an easy to use and fast helper class for the great CrossFadeSlidingPaneLayout by @chiuki. 

It comes with following features:
* Easy to use API
* Lightweight
 * No additional dependencies
* No need to modify your layouts

#Preview
##Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/Crossfader/develop/DEV/screenshots/screenshot1_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/Crossfader/develop/DEV/screenshots/screenshot2_small.png)

![Image](https://raw.githubusercontent.com/mikepenz/Crossfader/develop/DEV/screenshots/screenshot3_small.png)


#Include in your project
##Using Maven
The Crossfader Library is pushed to [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.mikepenz%22), so you just need to add the following dependency to your `build.gradle`.

```javascript
compile('com.mikepenz:crossfader:1.1.2@aar') {
	transitive = true
}
```

##How to use

Initialize and create the Crossfader
```java
crossFader = new Crossfader()
	//provide the view which should be the main content
        .withContent(findViewById(R.id.crossfade_content))
        //provide the view and it's width (in pixels) which should be displayed first
        .withFirst(result.getSlider(), firstWidth)
        //provide the view and it's width (in pixels) which should be displayed after fading
        .withSecond(miniResult.build(this), secondWidth)
        //provde the saved instance state to store the previous state of the crossfade view
        .withSavedInstance(savedInstanceState)
        //build and inflate everything
        .build();
        //Awesome everything is set and working
```

Programmatically crossFade the view
```java
//check if the view is crossFaded
if (crossFader.isCrossFaded()) {
	//crossFade the view
	crossFader.crossFade();
}
```

Save the instance state
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
	//add the values which need to be saved from the crossFader to the bundle
	outState = crossFader.saveInstanceState(outState);
	super.onSaveInstanceState(outState);
}
```

#Developed By

* Mike Penz - http://mikepenz.com - <mikepenz@gmail.com>


#License

    Copyright 2015 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
