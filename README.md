# Strawberry

## Introduction

Strawberry is a collection of Gradle plugins designed to streamline and apply common build settings across projects
within an organization. These plugins aim to enforce consistency, reduce boilerplate, and automate routine tasks.

## Features

- **Indra and Shadow Plugin Integration**: Seamlessly integrates with Indra and Shadow plugins for advanced project configurations and artifact shading.
- **Easy Shadow Relocation**: Simplifies the process of relocating packages within shaded jars, ensuring minimal conflicts with other plugins.
- **Final Jar Copying**: Automatically copies the final shaded jar to the build directory, facilitating easier distribution and deployment.
- **Archive Classifier Removal**: Removes the archive classifier from the shaded jar, creating cleaner artifact names.
- **Minecraft Plugin Description Generation**: Supports the generation of plugin description files for both Paper and Velocity platforms, streamlining the development of Minecraft plugins.

## Requirements

- Gradle 7.5 or higher
- Java 11 or higher

## Installation

To use the Strawberry plugins, add the following to your project's `build.gradle` file:

```groovy
plugins {
    id 'com.vouncherstudios.strawberry' version '1.0.0'
}
```

## Usage

After installing the plugin, you can configure it in your `build.gradle`:

```groovy
strawberry {
    // Configuration options here
}
```

For more detailed usage instructions, refer to the [documentation](https://github.com/vouncherstudios/strawberry/wiki).


## License

Strawberry is released under the MIT License. See the [LICENSE](LICENSE) file for more details.