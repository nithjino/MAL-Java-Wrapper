# MAL-Java-Wrapper
Main repo here: https://gitlab.com/bunu/MAL-API-Java-Wrapper

This repo is just a copy of my gitlab repo. There is no guarantee that this repo will be up-to-date

Check out my gitlab if you want that.

### What this is 
This is a simple wrapper for the [official API for MyAnimeList](https://myanimelist.net/modules.php?go=api "MAL API Doc")
It was a pain trying to work with the API since there was so little documentation and sometimes details the API would give wasn't correct
so I made this small wrapper for any other projects that would need it.

An extra thing I added that isn't part of the offical MAL API is the ability to get list of shows with a certain status such as Plan to Watch or Completed

### Things that need to be done if I find the time
+ Add methods to add/remove/update manga

### How to Use
You can either add the java file to whatever project you are using or add the jar file as an external library and add
`import com.bunu.MAL.MAL_API` to the list of imports

In eclipse, you can go to `Projects -> Properties -> Java Build Path` and add an external jar file from there.

There will be a sample class with how to use the methods found in the wrapper

[Here is an example project I made using this wrapper.](https://github.com/nithjino/MAL-Show-Randomizer)

![alt text](http://i.imgur.com/mwHrHWm.png)
