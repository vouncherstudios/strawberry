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

import com.vouncherstudios.strawberry.internal.StrawberryExtensionImpl;
import javax.annotation.Nonnull;
import net.kyori.mammoth.Extensions;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.util.GradleVersion;

/** Information about the plugin. */
public final class Strawberry {
  /** The minimum supported Gradle version for the Strawberry suite. */
  public static final GradleVersion MINIMUM_SUPPORTED = GradleVersion.version("7.5");

  public static final String EXTENSION_NAME = "strawberry";

  /**
   * Gets or creates the {@code strawberry} extension for a project.
   *
   * @param extensions the extensions container
   * @return the appropriate extension instance
   */
  @Nonnull
  public static StrawberryExtension extension(@Nonnull ExtensionContainer extensions) {
    return Extensions.findOrCreate(
        extensions, EXTENSION_NAME, StrawberryExtension.class, StrawberryExtensionImpl.class);
  }
}
