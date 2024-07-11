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

package com.vouncherstudios.strawberry.internal.minecraft;

import com.vouncherstudios.strawberry.internal.minecraft.plugin.PluginExtensionImpl;
import com.vouncherstudios.strawberry.minecraft.extension.MinecraftExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.PluginExtension;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import net.kyori.mammoth.Configurable;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;

public class MinecraftExtensionImpl implements MinecraftExtension {
  private final PluginExtensionImpl plugin;

  @Inject
  public MinecraftExtensionImpl(@Nonnull ObjectFactory objects) {
    this.plugin = objects.newInstance(PluginExtensionImpl.class);
  }

  @Nonnull
  @Override
  public PluginExtensionImpl plugin() {
    return this.plugin;
  }

  @Override
  public void plugin(@Nonnull Action<PluginExtension> action) {
    Configurable.configure(this.plugin, action);
  }
}
