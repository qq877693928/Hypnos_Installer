# Hypnos_Installer

[中文](https://github.com/qq877693928/Hypnos_Installer/edit/master/README_cn.md) | [English](https://github.com/qq877693928/Hypnos_Installer/edit/master/README.md)

App update installer, to simply use

## Feature
- Three ways of silent install app: `Root` , `AccessibilityService` and `Auto`, to chose these way by enum Mode of Installer

```java
Installer.getInstance().install(getActivity(), Installer.MODE.AUTO, APK_DOWNLOAD_URL);
```

![image](https://github.com/qq877693928/Hypnos_Installer/blob/master/art/screen.gif)

## Example
- Get the Installer instance
```java
Installer installer = Installer.getInstance();
```

- Use install two params, the first parameter is install apk way : `ROOT_ONLY` , `ACCESSIBILITY_ONLY` and `AUTO`. the second parameter is file path : `file path` or `http url`.
```java
//file path
installer.install(getActivity(), Installer.MODE.AUTO, file);

//http url
installer.install(getActivity(), Installer.MODE.AUTO, APK_DOWNLOAD_URL);
```

# Tools
* Android Stuido 2.2 preview 6
* build SDK version: 24
