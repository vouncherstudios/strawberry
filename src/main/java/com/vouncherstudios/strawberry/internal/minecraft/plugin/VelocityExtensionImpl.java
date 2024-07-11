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

package com.vouncherstudios.strawberry.internal.minecraft.plugin;

import com.vouncherstudios.strawberry.minecraft.plugin.dependency.Dependency;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.velocity.VelocityExtension;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;

public class VelocityExtensionImpl implements VelocityExtension {
  private final Property<String> main;
  private final Property<String> version;
  private final Property<String> description;
  private final Property<String> id;
  private final Property<String> name;
  private final SetProperty<String> authors;
  private final SetProperty<Dependency> dependencies;

  @Inject
  public VelocityExtensionImpl(@Nonnull ObjectFactory objects) {
    this.main = objects.property(String.class);
    this.version = objects.property(String.class);
    this.description = objects.property(String.class);
    this.id = objects.property(String.class);
    this.name = objects.property(String.class);
    this.authors = objects.setProperty(String.class);
    this.dependencies = objects.setProperty(Dependency.class);
  }

  @Nonnull
  @Override
  public Property<String> main() {
    return this.main;
  }

  @Nonnull
  @Override
  public Property<String> version() {
    return this.version;
  }

  @Nonnull
  @Override
  public Property<String> description() {
    return this.description;
  }

  @Nonnull
  @Override
  public Property<String> id() {
    return this.id;
  }

  @Nonnull
  @Override
  public Property<String> name() {
    return this.name;
  }

  @Nonnull
  @Override
  public SetProperty<String> authors() {
    return this.authors;
  }

  @Nonnull
  @Override
  public SetProperty<Dependency> dependencies() {
    return this.dependencies;
  }
}
