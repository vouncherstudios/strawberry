# Strawberry

Strawberry is a gradle plugin designed to streamline and apply common build settings across projects
within an organization. This plugin aim to enforce consistency, reduce boilerplate, and automate routine tasks.

## Features

- **Indra and Shadow Plugin Integration**: Seamlessly integrates with Indra and Shadow plugins for advanced project
  configurations and artifact shading.
- **Easy Shadow Relocation**: Simplifies the process of relocating packages within shaded jars, ensuring minimal
  conflicts with other plugins.
- **Final Jar Copying**: Automatically copies the final shaded jar to the build directory, facilitating easier
  distribution and deployment.
- **Archive Classifier Removal**: Removes the archive classifier from the shaded jar, creating cleaner artifact names.
- **Minecraft Plugin Description Generation**: Supports the generation of plugin description files for both Paper and
  Velocity platforms, streamlining the development of Minecraft plugins.

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

### Relocation Example

This example demonstrates how to relocate a package from `com.vouncherstudios.nexus` to `com.example.libs.nexus` to
avoid classpath conflicts.

```groovy
strawberry {
    relocate('com.vouncherstudios.nexus', 'com.example.libs.nexus')
}
```

### Plugin Descriptions Example for Paper

This example configures a Paper plugin description. It specifies the main class and name of the plugin. Optionally,
version and description can be specified; if not, they default to the project's version and description.

```groovy
strawberry {
    minecraft {
        plugin {
            paper {
                main("com.example.ExamplePlugin")
                name("ExamplePlugin")
                // Optional: If not specified, the project's version and description are used
                // version("1.0.0")
                // description("An example plugin for Paper")
            }
        }
    }
}
```

For more detailed usage instructions, refer to the [documentation](https://github.com/vouncherstudios/strawberry/wiki).

## License

Strawberry is released under the MIT License. See the [LICENSE](license_header.txt) file for more details.