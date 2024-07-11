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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.gradle.utils.GradlePropertyUtils;
import com.vouncherstudios.strawberry.minecraft.plugin.dependency.Dependency;
import com.vouncherstudios.strawberry.minecraft.plugin.exception.InvalidPluginDescriptionException;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.velocity.VelocityExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.DescriptionGenerator;
import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Property;

/** Represents a VelocityExtension plugin description generator. */
public final class VelocityDescriptionGenerator implements DescriptionGenerator {
  private static final Pattern ID_PATTERN = Pattern.compile("[a-z][a-z0-9-_]{0,63}");
  private static final String FILE_NAME = "velocity-plugin.json";
  private static final ObjectMapper MAPPER =
      JsonMapper.builder().addModule(new SimpleModule()).build();

  private final StrawberryExtension strawberry;

  public VelocityDescriptionGenerator(@Nonnull StrawberryExtension strawberry) {
    this.strawberry = strawberry;
  }

  @Override
  public void generate(@Nonnull Project project, @Nonnull Directory directory) {
    VelocityExtension extension = this.strawberry.minecraft().plugin().velocity();

    ObjectNode node = MAPPER.createObjectNode();

    node.put("id", extension.id().get());
    node.put("name", extension.name().get());
    node.put("main", extension.main().get());

    String version = project.getVersion().toString();

    Property<String> versionProp = extension.version();
    if (versionProp.isPresent()) {
      version = versionProp.get();
    }

    node.put("version", version);

    String description = project.getDescription();

    Property<String> descriptionProp = extension.description();
    if (descriptionProp.isPresent()) {
      description = descriptionProp.get();
    }

    if (description != null && !description.isBlank()) {
      node.put("description", description);
    }

    ArrayNode authorsNode = MAPPER.createArrayNode();

    Set<String> authors = extension.authors().get();
    if (!authors.isEmpty()) {
      for (String author : authors) {
        authorsNode.add(author);
      }
    }
    node.set("authors", authorsNode);

    ArrayNode dependencies = MAPPER.createArrayNode();
    for (Dependency dependency : extension.dependencies().get()) {
      ObjectNode dep = MAPPER.createObjectNode();
      dep.put("id", dependency.getId());
      dep.put("optional", dependency.isOptional());

      dependencies.add(dep);
    }
    node.set("dependencies", dependencies);

    try {
      File file = directory.file(FILE_NAME).getAsFile();
      file.createNewFile();

      MAPPER.writeValue(file, node);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void validate() throws InvalidPluginDescriptionException {
    VelocityExtension extension = this.strawberry.minecraft().plugin().velocity();

    String id = extension.id().get();
    if (!ID_PATTERN.matcher(id).matches()) {
      throw new InvalidPluginDescriptionException("Invalid plugin id, should match " + ID_PATTERN);
    }

    String main = extension.main().get().toLowerCase();
    for (String invalidNamespace : INVALID_NAMESPACES) {
      if (main.startsWith(invalidNamespace)) {
        throw new InvalidPluginDescriptionException(
            main + " may not be within the " + invalidNamespace + " namespace");
      }
    }

    if (GradlePropertyUtils.isNotEmpty(extension.version())) {
      throw new InvalidPluginDescriptionException("Version can't be empty if present");
    }

    if (GradlePropertyUtils.isNotEmpty(extension.description())) {
      throw new InvalidPluginDescriptionException("Description can't be empty if present");
    }
  }
}
