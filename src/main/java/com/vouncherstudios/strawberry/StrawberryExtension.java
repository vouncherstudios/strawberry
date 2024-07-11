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

import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator;
import com.vouncherstudios.strawberry.minecraft.extension.MinecraftExtension;
import com.vouncherstudios.strawberry.shadow.Relocation;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.gradle.api.Action;
import org.gradle.api.provider.SetProperty;

/** Extension exposing extra functionality provided by Strawberry. */
public interface StrawberryExtension {

  /**
   * The relocations to be made in this project.
   *
   * @return a set property providing the relocations
   */
  @Nonnull
  SetProperty<Relocation> relocations();

  /**
   * Add a class relocator that maps each class in the pattern to the provided destination.
   *
   * @param pattern the source pattern to relocate
   * @param destination the destination package
   */
  default void relocate(@Nonnull String pattern, @Nonnull String destination) {
    relocate(pattern, destination, null);
  }

  /**
   * Add a class relocator that maps each class in the pattern to the provided destination.
   *
   * @param pattern the source pattern to relocate
   * @param destination the destination package
   * @param configure the configuration of the relocator
   */
  default void relocate(
      @Nonnull String pattern,
      @Nonnull String destination,
      @Nullable Action<SimpleRelocator> configure) {
    relocations().add(new Relocation(pattern, destination, configure));
  }

  @Nonnull
  MinecraftExtension minecraft();

  void minecraft(@Nonnull Action<MinecraftExtension> action);
}
