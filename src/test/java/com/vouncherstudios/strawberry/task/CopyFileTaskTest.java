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

package com.vouncherstudios.strawberry.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

final class CopyFileTaskTest {

  @TempDir Path projectDirectory;

  @Test
  void createsParentDirectoriesAndCopiesTheSourceFile() throws Exception {
    Project project =
        ProjectBuilder.builder().withProjectDir(this.projectDirectory.toFile()).build();
    CopyFileTask task = project.getTasks().register("copyFile", CopyFileTask.class).get();
    Path source = this.projectDirectory.resolve("source.jar");
    Path destination = this.projectDirectory.resolve("nested/build/copied.jar");
    Files.writeString(source, "first content");
    task.getSourceFile().set(source.toFile());
    task.getDestinationFile().set(destination.toFile());

    task.copy();

    assertEquals("first content", Files.readString(destination));
  }

  @Test
  void replacesAnExistingDestinationFile() throws Exception {
    Project project =
        ProjectBuilder.builder().withProjectDir(this.projectDirectory.toFile()).build();
    CopyFileTask task = project.getTasks().register("copyFile", CopyFileTask.class).get();
    Path source = this.projectDirectory.resolve("source.jar");
    Path destination = this.projectDirectory.resolve("destination.jar");
    Files.writeString(source, "new content");
    Files.writeString(destination, "old content");
    task.getSourceFile().set(source.toFile());
    task.getDestinationFile().set(destination.toFile());

    task.copy();

    assertEquals("new content", Files.readString(destination));
  }
}
