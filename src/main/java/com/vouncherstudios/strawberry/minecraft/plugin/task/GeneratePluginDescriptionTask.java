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

package com.vouncherstudios.strawberry.minecraft.plugin.task;

import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.exception.InvalidPluginDescriptionException;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.DescriptionGenerator;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.DescriptionGeneratorType;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

/**
 * The generate plugin description gradle task. It will generate the plugin description based on
 * user defined Strawberry configuration.
 */
public abstract class GeneratePluginDescriptionTask extends DefaultTask {
  // A set to hold the different types of description generators
  private final Set<DescriptionGenerator> generators = new HashSet<>();

  // The output directory for the generated plugin description
  @OutputDirectory private Provider<Directory> outputDirectory;

  /**
   * The task action to generate the plugin description. It iterates over the set of generators and
   * calls the generate method on each one.
   */
  @TaskAction
  public void generate() {
    for (DescriptionGenerator generator : this.generators) {
      generator.generate(getProject(), outputDirectory.get());
    }
  }

  /**
   * Validates the plugin description. It iterates over the set of generators and calls the validate
   * method on each one.
   *
   * @throws InvalidPluginDescriptionException if the plugin description is invalid
   */
  public void validate() throws InvalidPluginDescriptionException {
    for (DescriptionGenerator generator : this.generators) {
      generator.validate();
    }
  }

  /**
   * Sets the generators based on the provided strawberry extension. It checks the generator types
   * from the extension and adds the corresponding generators to the set.
   *
   * @param strawberry the strawberry extension
   */
  public void setGenerators(@Nonnull StrawberryExtension strawberry) {
    Set<DescriptionGenerator> generators = DescriptionGeneratorType.fromExt(strawberry);
    this.generators.addAll(generators);
  }

  /**
   * Sets the output directory for the generated plugin description.
   *
   * @param outputDirectory the output directory
   */
  public void setOutputDirectory(@Nonnull Provider<Directory> outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * Returns the output directory for the generated plugin description.
   *
   * @return the output directory
   */
  @Nonnull
  public Provider<Directory> getOutputDirectory() {
    return this.outputDirectory;
  }
}
