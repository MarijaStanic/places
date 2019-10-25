[Link to my Diploma thesis](https://drive.google.com/file/d/1bLfsI-5TaIZjYOvH8ahNjU1e9HmYfhG5/view?usp=sharing)

The project consists of two modules: app, which is an Android library and core, which is a Java library.
The app module contains the Android MVP view layer while the remaining two layers are in the core module.
This ensures that the Context or any other Android objects are not used in the presenter or model. Also, this makes it possible to reuse the core with a different user interface (for example, an iOS app).
