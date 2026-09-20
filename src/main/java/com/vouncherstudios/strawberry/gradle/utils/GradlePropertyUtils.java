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

package com.vouncherstudios.strawberry.gradle.utils;

import javax.annotation.Nonnull;
import org.gradle.api.provider.Property;

/** Utilities for inspecting Gradle string properties. */
public final class GradlePropertyUtils {
  private GradlePropertyUtils() {}

  /**
   * Checks whether a property is present and contains a non-blank value.
   *
   * @param property the property to inspect
   * @return whether the property contains a non-blank value
   */
  public static boolean isNotEmpty(@Nonnull Property<String> property) {
    return property.isPresent() && !property.get().isBlank();
  }

  /**
   * Checks whether a property is present and contains a blank value.
   *
   * @param property the property to inspect
   * @return whether the property contains a blank value
   */
  public static boolean isEmpty(@Nonnull Property<String> property) {
    return property.isPresent() && property.get().isBlank();
  }
}
