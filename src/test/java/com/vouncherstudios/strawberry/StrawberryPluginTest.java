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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class StrawberryPluginTest {

  @TempDir Path projectDirectory;

  @Test
  void appliesExpectedConfigurationAndTaskWiring() throws Exception {
    Files.writeString(
        this.projectDirectory.resolve("settings.gradle"), "rootProject.name = 'test'\n");
    Files.writeString(
        this.projectDirectory.resolve("build.gradle"),
        """
        plugins {
            id 'com.vouncherstudios.strawberry'
        }

        tasks.register('verifyStrawberry') {
            dependsOn 'copyShadowJarToRootBuild'

            doLast {
                assert project.extensions.findByName('strawberry') != null

                def shadowJar = project.tasks.named('shadowJar').get()
                def copyShadowJar = project.tasks.named('copyShadowJarToRootBuild').get()
                def generateDescription = project.tasks.named('minecraftGeneratePluginDescription').get()
                def buildTask = project.tasks.named('build').get()

                assert shadowJar.archiveClassifier.get() == ''
                assert copyShadowJar.sourceFile.get() == shadowJar.archiveFile.get()
                assert copyShadowJar.destinationFile.get().asFile.parentFile == project.file('build')
                assert buildTask.taskDependencies.getDependencies(buildTask).contains(copyShadowJar)
                assert buildTask.taskDependencies.getDependencies(buildTask).contains(generateDescription)
            }
        }
        """);

    BuildResult result =
        GradleRunner.create()
            .withProjectDir(this.projectDirectory.toFile())
            .withPluginClasspath()
            .withArguments("verifyStrawberry", "--stacktrace")
            .build();

    assertEquals(TaskOutcome.SUCCESS, result.task(":verifyStrawberry").getOutcome());
  }

  @Test
  void exposesDeclaredMinimumGradleVersion() {
    assertEquals(Strawberry.MINIMUM_SUPPORTED, new StrawberryPlugin().minimumGradleVersion());
  }
}
