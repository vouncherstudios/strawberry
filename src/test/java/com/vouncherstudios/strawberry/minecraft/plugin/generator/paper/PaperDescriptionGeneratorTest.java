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

package com.vouncherstudios.strawberry.minecraft.plugin.generator.paper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.vouncherstudios.strawberry.Strawberry;
import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.exception.InvalidPluginDescriptionException;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.paper.PaperExtension;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class PaperDescriptionGeneratorTest {
  private static final YAMLMapper MAPPER = new YAMLMapper();

  @TempDir Path projectDirectory;

  private Project project;
  private PaperExtension extension;
  private PaperDescriptionGenerator generator;

  @BeforeEach
  void setUp() {
    this.project = ProjectBuilder.builder().withProjectDir(this.projectDirectory.toFile()).build();
    StrawberryExtension strawberry = Strawberry.extension(this.project.getExtensions());
    this.extension = strawberry.minecraft().plugin().paper();
    this.generator = new PaperDescriptionGenerator(strawberry);

    this.extension.name("ExamplePlugin");
    this.extension.main("com.example.ExamplePlugin");
  }

  @Test
  void generatesConfiguredPluginYaml() throws Exception {
    this.extension.version("1.0.0");
    this.extension.description("Configured description");
    this.extension.load(LoadOrder.STARTUP);
    this.extension.apiVersion("1.21");
    this.extension.authors("Alice", "Bob");
    this.extension.addDependency("RequiredPlugin");
    this.extension.addDependency("OptionalPlugin", true);

    this.generator.generate(
        "1.2.3", "Project description", this.project.getLayout().getProjectDirectory());

    JsonNode result =
        MAPPER.readTree(Files.readString(this.projectDirectory.resolve("plugin.yml")));
    assertEquals("ExamplePlugin", result.path("name").asText());
    assertEquals("com.example.ExamplePlugin", result.path("main").asText());
    assertEquals("STARTUP", result.path("load").asText());
    assertEquals("1.0.0", result.path("version").asText());
    assertEquals("Configured description", result.path("description").asText());
    assertEquals("1.21", result.path("api-version").asText());
    assertEquals(2, result.path("authors").size());
    assertEquals("RequiredPlugin", result.path("depend").get(0).asText());
    assertEquals("OptionalPlugin", result.path("softdepend").get(0).asText());
    assertFalse(result.has("author"));
  }

  @Test
  void usesProjectDefaultsAndSingularAuthorKey() throws Exception {
    this.extension.authors("Alice");

    this.generator.generate(
        "1.2.3", "Project description", this.project.getLayout().getProjectDirectory());

    JsonNode result =
        MAPPER.readTree(Files.readString(this.projectDirectory.resolve("plugin.yml")));
    assertEquals("1.2.3", result.path("version").asText());
    assertEquals("Project description", result.path("description").asText());
    assertEquals("Alice", result.path("author").asText());
    assertFalse(result.has("authors"));
  }

  @Test
  void acceptsNonBlankOptionalOverrides() {
    this.extension.version("1.0.0");
    this.extension.description("Configured description");

    assertDoesNotThrow(this.generator::validate);
  }

  @Test
  void rejectsBlankOptionalOverrides() {
    this.extension.version("  ");

    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);
  }

  @Test
  void rejectsInvalidNameAndReservedMainNamespace() {
    this.extension.name("invalid/name");
    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);

    this.extension.name("Valid Name");
    this.extension.main("org.bukkit.ExamplePlugin");
    assertThrows(InvalidPluginDescriptionException.class, this.generator::validate);
  }
}
