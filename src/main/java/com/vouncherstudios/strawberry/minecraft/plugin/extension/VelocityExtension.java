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

import com.vouncherstudios.strawberry.minecraft.plugin.dependency.Dependency;
import javax.annotation.Nonnull;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;

/** A velocity plugin description configurable interface. */
public interface VelocityExtension {

  /**
   * The plugin's initial class file. This main can't start with minecraft packages or derivative.
   *
   * @return a property providing the plugin's initial class file
   */
  @Nonnull
  Property<String> main();

  /**
   * Sets the plugin's initial class file.
   *
   * @param main the plugin's initial class file
   */
  default void main(@Nonnull String main) {
    main().set(main);
  }

  /**
   * The id of the plugin. This id should be unique as to not conflict with other plugins. The
   * plugin id may contain alphanumeric characters, dashes, and underscores, and be a maximum of 64
   * characters long.
   *
   * @return a property providing the id for this plugin
   */
  @Nonnull
  Property<String> id();

  /**
   * Sets the id of the plugin.
   *
   * @param id the id of the plugin
   */
  default void id(@Nonnull String id) {
    id().set(id);
  }

  /**
   * The human-readable name of the plugin as to be used in descriptions and similar things.
   *
   * @return a property providing the plugin name
   */
  @Nonnull
  Property<String> name();

  /**
   * Sets the human-readable name of the plugin.
   *
   * @param name the human-readable name of the plugin
   */
  default void name(@Nonnull String name) {
    name().set(name);
  }

  /**
   * The authors of the plugin.
   *
   * @return a property providing the author of the plugin
   */
  @Nonnull
  SetProperty<String> authors();

  /**
   * Sets the authors of the plugin.
   *
   * @param authors the authors of the plugin
   */
  default void authors(@Nonnull String... authors) {
    for (String author : authors) {
      authors().add(author);
    }
  }

  /**
   * The dependencies required to load before this plugin.
   *
   * @return a property providing the plugin dependencies
   */
  @Nonnull
  SetProperty<Dependency> dependencies();

  /**
   * Adds a dependency to load before this plugin.
   *
   * <p>By default this dependency is not optional, it will be a hard-dependency. If you want to add
   * a soft-dependency, use other {@link #addDependency(String, boolean) addDependency} method.
   *
   * @param id the plugin id of the dependency
   */
  default void addDependency(@Nonnull String id) {
    addDependency(id, false);
  }

  /**
   * Adds a dependency to load before this plugin.
   *
   * @param id the plugin id of the dependency
   * @param optional whether the dependency is not required to enable this plugin
   */
  default void addDependency(@Nonnull String id, boolean optional) {
    dependencies().add(new Dependency(id, optional));
  }

  /**
   * Adds dependencies to load before this plugin.
   *
   * <p>By default these dependencies are not optional, it will be hard-dependencies. If you want to
   * add soft-dependencies, use other {@link #addDependencies(boolean, String...) addDependencies}
   * method.
   *
   * @param ids the plugin id of the dependencies
   */
  default void addDependencies(@Nonnull String... ids) {
    addDependencies(false, ids);
  }

  /**
   * Adds dependencies to load before this plugin.
   *
   * @param ids the plugin id of the dependencies
   * @param optional whether the dependencies are not required to enable this plugin
   */
  default void addDependencies(boolean optional, @Nonnull String... ids) {
    for (String id : ids) {
      dependencies().add(new Dependency(id, optional));
    }
  }
}
