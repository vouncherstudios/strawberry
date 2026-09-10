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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.vouncherstudios.strawberry.Strawberry;
import com.vouncherstudios.strawberry.StrawberryExtension;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.paper.PaperDescriptionGenerator;
import com.vouncherstudios.strawberry.minecraft.plugin.generator.velocity.VelocityDescriptionGenerator;
import java.util.Set;
import java.util.stream.Collectors;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

final class DescriptionGeneratorTypeTest {

  @Test
  void createsOnlyGeneratorsWithAllRequiredProperties() {
    Project project = ProjectBuilder.builder().build();
    StrawberryExtension strawberry = Strawberry.extension(project.getExtensions());

    assertEquals(0, DescriptionGeneratorType.fromExt(strawberry).size());

    strawberry.minecraft().plugin().paper().name("PaperPlugin");
    assertEquals(0, DescriptionGeneratorType.fromExt(strawberry).size());

    strawberry.minecraft().plugin().paper().main("com.example.PaperPlugin");
    Set<DescriptionGenerator> paperOnly = DescriptionGeneratorType.fromExt(strawberry);
    assertEquals(1, paperOnly.size());
    assertInstanceOf(PaperDescriptionGenerator.class, paperOnly.iterator().next());

    strawberry.minecraft().plugin().velocity().id("velocity-plugin");
    strawberry.minecraft().plugin().velocity().name("Velocity Plugin");
    strawberry.minecraft().plugin().velocity().main("com.example.VelocityPlugin");
    Set<DescriptionGenerator> both = DescriptionGeneratorType.fromExt(strawberry);
    assertEquals(2, both.size());
    assertEquals(
        Set.of(PaperDescriptionGenerator.class, VelocityDescriptionGenerator.class),
        both.stream().map(Object::getClass).collect(Collectors.toSet()));
  }
}
