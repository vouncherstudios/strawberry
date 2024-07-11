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
import com.vouncherstudios.strawberry.minecraft.plugin.generator.paper.LoadOrder;
import javax.annotation.Nonnull;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;

/** A paper plugin description configurable interface. */
public interface PaperExtension {
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
   * The name of the plugin. This name should be unique as to not conflict with other plugins. The
   * plugin name may contain alphanumeric characters, dashes, and underscores.
   *
   * @return a property providing the name for this plugin
   */
  @Nonnull
  Property<String> name();

  /**
   * Sets the name of the plugin.
   *
   * @param name the name of the plugin
   */
  default void name(@Nonnull String name) {
    name().set(name);
  }

  /**
   * The version of the plugin. If not set, the version will be set to project's version.
   *
   * @return a property providing the version of the plugin
   */
  @Nonnull
  Property<String> version();

  /**
   * Sets the version of the plugin.
   *
   * @param version the version of the plugin
   */
  default void version(@Nonnull String version) {
    version().set(version);
  }

  /**
   * The description of the plugin. If not set, the description will be set to project's
   * description.
   *
   * @return a property providing the description of the plugin
   */
  @Nonnull
  Property<String> description();

  /**
   * Sets the description of the plugin.
   *
   * @param description the description of the plugin
   */
  default void description(@Nonnull String description) {
    description().set(description);
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
   * The phase of server-startup this plugin will load during.
   *
   * @return a property providing the plugin's load order
   */
  @Nonnull
  Property<LoadOrder> load();

  /**
   * Sets the plugin's load order.
   *
   * @param loadOrder the plugin's load order
   */
  default void load(@Nonnull LoadOrder loadOrder) {
    load().set(loadOrder);
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
   * @param pluginName the plugin name of the dependency
   */
  default void addDependency(@Nonnull String pluginName) {
    addDependency(pluginName, false);
  }

  /**
   * Adds a dependency to load before this plugin.
   *
   * @param pluginName the plugin name of the dependency
   * @param optional whether the dependency is not required to enable this plugin
   */
  default void addDependency(@Nonnull String pluginName, boolean optional) {
    dependencies().add(new Dependency(pluginName, optional));
  }

  /**
   * Adds dependencies to load before this plugin.
   *
   * <p>By default these dependencies are not optional, it will be hard-dependencies. If you want to
   * add soft-dependencies, use other {@link #addDependencies(boolean, String...) addDependencies}
   * method.
   *
   * @param pluginNames the plugin name of the dependencies
   */
  default void addDependencies(@Nonnull String... pluginNames) {
    addDependencies(false, pluginNames);
  }

  /**
   * Adds dependencies to load before this plugin.
   *
   * @param pluginNames the plugin name of the dependencies
   * @param optional whether the dependencies are not required to enable this plugin
   */
  default void addDependencies(boolean optional, @Nonnull String... pluginNames) {
    for (String pluginName : pluginNames) {
      dependencies().add(new Dependency(pluginName, optional));
    }
  }

  /**
   * The API version which this plugin is designed to support.
   *
   * @return a property providing the api version
   */
  @Nonnull
  Property<String> apiVersion();

  /**
   * Sets the API version.
   *
   * @param apiVersion the API version
   */
  default void apiVersion(@Nonnull String apiVersion) {
    apiVersion().set(apiVersion);
  }
}
