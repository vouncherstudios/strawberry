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

package com.vouncherstudios.strawberry.minecraft.plugin.generator;

import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.PaperExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.extension.VelocityExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.paper.PaperDescriptionGenerator;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.velocity.VelocityDescriptionGenerator;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.gradle.api.provider.Property;

/** The implemented generators type. */
public enum DescriptionGeneratorType {
  PAPER {
    @Override
    public DescriptionGenerator getGenerator(@Nonnull StrawberryExtension extension) {
      return new PaperDescriptionGenerator(extension);
    }

    @Override
    public boolean isAvailable(@Nonnull StrawberryExtension extension) {
      PaperExtension paper = extension.minecraft().plugin().paper();
      return isNotEmpty(paper.name()) && isNotEmpty(paper.main());
    }
  },
  VELOCITY {
    @Override
    public DescriptionGenerator getGenerator(@Nonnull StrawberryExtension extension) {
      return new VelocityDescriptionGenerator(extension);
    }

    @Override
    public boolean isAvailable(@Nonnull StrawberryExtension extension) {
      VelocityExtension velocity = extension.minecraft().plugin().velocity();
      return isNotEmpty(velocity.main()) && isNotEmpty(velocity.id());
    }
  };

  public abstract DescriptionGenerator getGenerator(@Nonnull StrawberryExtension extension);

  public abstract boolean isAvailable(@Nonnull StrawberryExtension extension);

  /**
   * Returns a set of generator types based on the extension.
   *
   * @param extension the minecraft extension
   * @return the set of generator types
   */
  @Nonnull
  public static Set<DescriptionGenerator> fromExt(@Nonnull StrawberryExtension extension) {
    HashSet<DescriptionGenerator> types = new HashSet<>();

    for (DescriptionGeneratorType generatorType : values()) {
      if (generatorType.isAvailable(extension)) {
        types.add(generatorType.getGenerator(extension));
      }
    }
    return types;
  }

  private static boolean isNotEmpty(@Nonnull Property<String> prop) {
    return prop.isPresent() && !prop.get().isEmpty() && !prop.get().isBlank();
  }
}
