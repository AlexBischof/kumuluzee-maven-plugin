/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p>
 * Copyright 2012-2015 the original author or authors.
 */
package de.bischinger.kumuluzee;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Mojo for
 * <ul>
 *     <li>copying dependencies to target/dependency</li>
 *     <li>copying src/main/webapp to target/classes/webapp</li>
 *</ul>
 *
 * @author Alexander Bischof
 */
@Mojo(name = "copy-dependencies", defaultPhase = PACKAGE, requiresDependencyResolution = COMPILE)
public class KumuluzEECopyDependenciesAndResources extends AbstractMojo {
    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject mavenProject;

    @Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    public void execute() throws MojoExecutionException {
        copyDependencies();

        copyWebapp();
    }

    private void copyWebapp() throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-resources-plugin"),
                        version("2.7")
                ),
                goal("copy-resources"),
                configuration(
                        element(name("outputDirectory"), "${basedir}/target/classes/webapp"),
                        element(name("resources"),
                                element(name("resource"),
                                        element(name("directory"), "src/main/webapp"),
                                        element(name("filtering"), "true")
                                ))
                ),
                executionEnvironment(
                        mavenProject,
                        mavenSession,
                        pluginManager
                )
        );
    }

    private void copyDependencies() throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version("2.10")
                ),
                goal("copy-dependencies"),
                configuration(
                        element(name("outputDirectory"))
                ),
                executionEnvironment(
                        mavenProject,
                        mavenSession,
                        pluginManager
                )
        );
    }
}