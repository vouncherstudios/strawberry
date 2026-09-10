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

package com.vouncherstudios.strawberry;

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin;
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import com.vouncherstudios.strawberry.internal.StrawberryExtensionImpl;
import com.vouncherstudios.strawberry.minecraft.plugin.task.GeneratePluginDescriptionTask;
import com.vouncherstudios.strawberry.shadow.Relocation;
import com.vouncherstudios.strawberry.task.CopyFileTask;
import javax.annotation.Nonnull;
import net.kyori.indra.IndraPlugin;
import net.kyori.mammoth.ProjectPlugin;
import net.kyori.mammoth.Properties;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.util.GradleVersion;

/** The Strawberry plugin providing project configuration. */
public final class StrawberryPlugin implements ProjectPlugin {

  @Override
  public void apply(
      @Nonnull Project project,
      @Nonnull PluginContainer plugins,
      @Nonnull ExtensionContainer extensions,
      @Nonnull TaskContainer tasks) {
    // Apply gradle plugins
    plugins.apply(IndraPlugin.class);
    plugins.apply(ShadowPlugin.class);

    StrawberryExtensionImpl strawberry = (StrawberryExtensionImpl) Strawberry.extension(extensions);

    // Configure shadow
    tasks
        .withType(ShadowJar.class)
        .configureEach(
            shadowJar -> {
              // Add relocations
              SetProperty<Relocation> relocationsProp =
                  Properties.finalized(strawberry.relocations());
              for (Relocation relocation : relocationsProp.get()) {
                shadowJar.relocate(
                    relocation.getPattern(),
                    relocation.getDestination(),
                    relocation.getConfiguration());
              }

              // Remove archive classifier from output jar
              shadowJar.getArchiveClassifier().set("");
            });
    TaskProvider<ShadowJar> shadowJarTask = tasks.named("shadowJar", ShadowJar.class);
    DirectoryProperty rootBuildDirectory = project.getObjects().directoryProperty();
    rootBuildDirectory.set(project.file(project.getRootDir() + "/build"));
    TaskProvider<CopyFileTask> copyShadowJarTask =
        tasks.register(
            "copyShadowJarToRootBuild",
            CopyFileTask.class,
            copy -> {
              copy.setDescription("Copies the final shaded jar to the root build directory.");
              copy.setGroup("build");
              copy.dependsOn(tasks.named("jar"));
              copy.getSourceFile().set(shadowJarTask.flatMap(ShadowJar::getArchiveFile));
              copy.getDestinationFile()
                  .set(
                      rootBuildDirectory.file(
                          shadowJarTask
                              .flatMap(ShadowJar::getArchiveFile)
                              .map(file -> file.getAsFile().getName())));
            });
    // Add the final jar copy task as dependency on build task
    tasks.named("build", DefaultTask.class).configure(build -> build.dependsOn(copyShadowJarTask));
    // Add shadowJar task as dependency on other projects shadowJar task
    shadowJarTask.configure(
        shadowJar ->
            project
                .getConfigurations()
                .forEach(
                    configuration ->
                        configuration
                            .getDependencies()
                            .forEach(
                                dependency -> {
                                  if (dependency instanceof ProjectDependency) {
                                    String dependentProjectPath =
                                        ((ProjectDependency) dependency).getPath();
                                    String dependentShadowJarPath =
                                        dependentProjectPath.equals(":")
                                            ? ":shadowJar"
                                            : dependentProjectPath + ":shadowJar";
                                    shadowJar.dependsOn(dependentShadowJarPath);
                                  }
                                })));

    // Create minecraft generate plugin description task
    TaskProvider<GeneratePluginDescriptionTask> generatePluginDescriptionTask =
        tasks.register(
            "minecraftGeneratePluginDescription",
            GeneratePluginDescriptionTask.class,
            task -> {
              String name = "minecraft-plugin-description";
              task.setDescription(
                  "Generate the plugin description based on user defined strawberry configuration.");
              task.setGroup(name);
              task.getOutputDirectory()
                  .set(
                      project
                          .getLayout()
                          .getBuildDirectory()
                          .dir("generated/" + Strawberry.EXTENSION_NAME + "/" + name));
            });
    project.afterEvaluate(
        ignored ->
            generatePluginDescriptionTask.configure(
                task -> {
                  task.setGenerators(strawberry);
                  task.getProjectVersion().set(project.getVersion().toString());
                  if (project.getDescription() != null) {
                    task.getProjectDescription().set(project.getDescription());
                  }
                }));
    // Add generate plugin description task as dependency on build task
    tasks
        .named("build", DefaultTask.class)
        .configure(build -> build.dependsOn(generatePluginDescriptionTask));
    // Copy plugin description output to our final jar
    extensions
        .getByType(SourceSetContainer.class)
        .named(SourceSet.MAIN_SOURCE_SET_NAME)
        .configure(sourceSet -> sourceSet.getResources().srcDir(generatePluginDescriptionTask));
  }

  @Nonnull
  @Override
  public GradleVersion minimumGradleVersion() {
    return Strawberry.MINIMUM_SUPPORTED;
  }
}
