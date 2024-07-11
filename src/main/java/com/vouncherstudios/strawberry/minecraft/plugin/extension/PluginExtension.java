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

package com.vouncherstudios.strawberry.minecraft.plugin.extension;

import com.vouncherstudios.strawberry.minecraft.plugin.extension.paper.PaperExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.velocity.VelocityExtension;
import javax.annotation.Nonnull;
import org.gradle.api.Action;

/** A minecraft plugin description configurable interface. */
public interface PluginExtension {

  @Nonnull
  VelocityExtension velocity();

  void velocity(@Nonnull Action<VelocityExtension> action);

  @Nonnull
  PaperExtension paper();

  void paper(@Nonnull Action<PaperExtension> action);
}
