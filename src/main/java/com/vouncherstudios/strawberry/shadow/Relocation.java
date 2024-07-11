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

package com.vouncherstudios.strawberry.shadow;

import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.gradle.api.Action;

public final class Relocation {
  private final String pattern;
  private final String destination;
  private final Action<SimpleRelocator> configuration;

  /**
   * Create a new relocation.
   *
   * @param pattern the source pattern to relocate
   * @param destination the destination package
   * @param configuration the configuration of the relocator
   */
  public Relocation(
      @Nonnull String pattern,
      @Nonnull String destination,
      @Nullable Action<SimpleRelocator> configuration) {
    this.pattern = pattern;
    this.destination = destination;
    this.configuration = configuration;
  }

  /**
   * Get the source pattern to relocate.
   *
   * @return the source pattern
   */
  @Nonnull
  public String getPattern() {
    return this.pattern;
  }

  /**
   * Get the destination package.
   *
   * @return the destination package
   */
  @Nonnull
  public String getDestination() {
    return this.destination;
  }

  /**
   * Get the configuration of the relocator.
   *
   * @return the configuration
   */
  @Nullable
  public Action<SimpleRelocator> getConfiguration() {
    return this.configuration;
  }

  @Override
  public boolean equals(@Nonnull Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Relocation that = (Relocation) o;
    return Objects.equals(this.pattern, that.pattern)
        && Objects.equals(this.destination, that.destination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pattern, this.destination);
  }
}
