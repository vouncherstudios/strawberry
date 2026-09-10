/*
 * MIT License
 *
 * Copyright (c) Vouncher Studios <contact@vouncherstudios.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.vouncherstudios.strawberry.minecraft.plugin.generator.velocity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.vouncherstudios.strawberry.Strawberry;
import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.exception.InvalidPluginDescriptionException;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.velocity.VelocityExtension;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class VelocityDescriptionGeneratorTest {
  private static final JsonMapper MAPPER = new JsonMapper();

  @TempDir Path projectDirectory;

  private Project project;
  private VelocityExtension extension;
  private VelocityDescriptionGenerator generator;

  @BeforeEach
  void setUp() {
    this.project = ProjectBuilder.builder().withProjectDir(this.projectDirectory.toFile()).build();
    StrawberryExtension strawberry = Strawberry.extension(this.project.getExtensions());
    this.extension = strawberry.minecraft().plugin().velocity();
    this.generator = new VelocityDescriptionGenerator(strawberry);

    this.extension.id("example-plugin");
    this.extension.name("Example Plugin");
    this.extension.main("com.example.ExamplePlugin");
  }

  @Test
  void generatesConfiguredVelocityDescriptor() throws Exception {
    this.extension.version("2.0.0");
    this.extension.description("Configured description");
    this.extension.authors("Alice", "Bob");
    this.extension.addDependency("required-plugin");
    this.extension.addDependency("optional-plugin", true);

    this.generator.generate(
        "1.2.3", "Project description", this.project.getLayout().getProjectDirectory());

    JsonNode result =
        MAPPER.readTree(Files.readString(this.projectDirectory.resolve("velocity-plugin.json")));
    assertEquals("example-plugin", result.path("id").asText());
    assertEquals("Example Plugin", result.path("name").asText());
    assertEquals("com.example.ExamplePlugin", result.path("main").asText());
    assertEquals("2.0.0", result.path("version").asText());
    assertEquals("Configured description", result.path("description").asText());
    assertEquals(2, result.path("authors").size());
    assertEquals("required-plugin", result.path("dependencies").get(0).path("id").asText());
    assertFalse(result.path("dependencies").get(0).path("optional").asBoolean());
    assertEquals("optional-plugin", result.path("dependencies").get(1).path("id").asText());
    assertTrue(result.path("dependencies").get(1).path("optional").asBoolean());
  }

  @Test
  void acceptsNonBlankOptionalOverrides() {
    this.extension.version("2.0.0");
    this.extension.description("Configured description");

    assertDoesNotThrow(this.generator::validate);
  }

  @Test
  void rejectsBlankOptionalOverrides() {
    this.extension.description("  ");

    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);
  }

  @Test
  void rejectsInvalidIdAndReservedMainNamespace() {
    this.extension.id("Invalid ID");
    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);

    this.extension.id("valid-id");
    this.extension.main("net.minecraft.ExamplePlugin");
    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);
  }
}
