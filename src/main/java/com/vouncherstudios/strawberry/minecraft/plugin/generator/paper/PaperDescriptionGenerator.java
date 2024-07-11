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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.dependency.Dependency;
import com.vouncherstudios.strawberry.minecraft.plugin.exception.InvalidPluginDescriptionException;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.PaperExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.DescriptionGenerator;
import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Property;

/** Represents a Paper plugin description generator. */
public final class PaperDescriptionGenerator implements DescriptionGenerator {
  private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
  private static final String FILE_NAME = "plugin.yml";
  private static final ObjectMapper MAPPER =
      YAMLMapper.builder()
          .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
          .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
          .enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
          .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
          .build();
  private final StrawberryExtension strawberry;

  public PaperDescriptionGenerator(@Nonnull StrawberryExtension strawberry) {
    this.strawberry = strawberry;
  }

  @Override
  public void generate(@Nonnull Project project, @Nonnull Directory directory) {
    PaperExtension paper = this.strawberry.minecraft().plugin().paper();

    ObjectNode node = MAPPER.createObjectNode();

    String name = paper.name().get();

    node.put("name", name);
    node.put("main", paper.main().get());
    node.put("version", project.getVersion().toString());
    node.put("description", project.getDescription());
    node.put("load", paper.load().get().toString());

    Property<String> apiVersionProp = paper.apiVersion();
    if (apiVersionProp.isPresent()) {
      node.put("api-version", apiVersionProp.get());
    }

    Set<String> authors = paper.authors().get();
    if (!authors.isEmpty()) {

      // Single author
      if (authors.size() == 1) {
        for (String author : authors) {
          node.put("author", author);
        }
      } else {
        // Multiple authors
        ArrayNode authorsNode = MAPPER.createArrayNode();
        for (String author : authors) {
          authorsNode.add(author);
        }

        node.set("authors", authorsNode);
      }
    }

    ArrayNode depend = MAPPER.createArrayNode();
    ArrayNode softDepend = MAPPER.createArrayNode();

    for (Dependency dependency : paper.dependencies().get()) {
      String dependencyId = dependency.getId();

      if (dependency.isOptional()) {
        softDepend.add(dependencyId);
      } else {
        depend.add(dependencyId);
      }
    }

    if (!depend.isEmpty()) {
      node.set("depend", depend);
    }
    if (!softDepend.isEmpty()) {
      node.set("softdepend", softDepend);
    }

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
    PaperExtension extension = this.strawberry.minecraft().plugin().paper();

    String main = extension.main().get().toLowerCase();
    for (String invalidNamespace : INVALID_NAMESPACES) {
      if (main.startsWith(invalidNamespace)) {
        throw new InvalidPluginDescriptionException(
            main + " may not be within the " + invalidNamespace + " namespace");
      }
    }

    String name = extension.name().get();
    if (!VALID_NAME.matcher(name).matches()) {
      throw new InvalidPluginDescriptionException(
          "Invalid plugin name, should match " + VALID_NAME);
    }
  }
}
