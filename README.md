<p align="center">
  <img width="128" height="128" alt="image" src="https://github.com/user-attachments/assets/c4120222-8826-43ef-802f-8721b64b0f80" />
</p>
<h3 align="center">nicotine</h3>
<p align="center">Minecraft utility mod for anarchy servers</p>


## About

Not a fork! nicotine is a utility mod with its own click GUI, event system, command system and plenty of modules to enhance your gameplay

nicotine doesn't make direct OpenGL calls, making it compatible with mods that change the rendering system, such as [VulkanMod](https://modrinth.com/mod/vulkanmod)

Despite being a Fabric mod, nicotine doesn't depend on the Fabric API and it uses the official Mojang mappings, making it easily portable to other mod loaders

## Preview
<p align="center">
  <img src="./preview.gif" />
</p>

## Usage

Download the jar file from the [releases](https://github.com/tranarchy/nicotine/releases) section or build the project from source yourself

Then simply just move the jar file to your Minecraft mods folder

Since nicotine is a Fabric mod you will also need the [Fabric Loader](https://fabricmc.net/)

## Building

Linux, macOS and *BSD
```
./gradlew build
```

Windows
```
.\gradlew.bat build
```

You will find the built jar in `build/libs`

## GUI

The default keybind for the click GUI is **Right Shift**, you can change this any time

## Other versions

nicotine is currently made for version **1.21.11**

If you want to use nicotine on a server that runs on another version you can use the [ViaFabricPlus](https://modrinth.com/mod/viafabricplus) mod
