package de.bischinger.kumuluzee;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "copy-dependencies", defaultPhase = PACKAGE, requiresDependencyResolution = COMPILE)
public class KumuluzEECopyDependenciesAndResources extends AbstractMojo {
    @Component
    private MavenProject mavenProject;

    @Component
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
                        element(name("outputDirectory"),"${basedir}/target/classes/webapp"),
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