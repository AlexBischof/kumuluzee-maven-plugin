# kumuluzee-maven-plugin

This repository provides a maven-plugin for [KumuluzEE](https://ee.kumuluz.com) to combine the following packaging tasks:

1. Explicit usage of **maven-dependency-plugin** to copy all dependencies into default output directory which is
*target/dependency*. Looking at the following snippet 

``` xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.10</version>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

2. **Copy** webapp directory from standard directory *src/main/webapp* to *target/classes*

``` xml
<plugin>
    <artifactId>maven-resources-plugin</artifactId>
    <version>2.7</version>
    <executions>
        <execution>
            <id>copy-resources</id>
            <!-- here the phase you need -->
            <phase>validate</phase>
            <goals>
              <goal>copy-resources</goal>
            </goals>
            <configuration>
              <outputDirectory>${basedir}/target/extra-resources</outputDirectory>
              <resources>          
                <resource>
                  <directory>src/non-packaged-resources</directory>
                  <filtering>true</filtering>
                </resource>
              </resources>              
            </configuration>            
        </execution>
    </executions>
</plugin>
```

## Usage

``` xml
<build>
    <plugins>
        <plugin>
            <groupId>de.bischinger</groupId>
            <artifactId>kumuluzee-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                   <id>copy-resources</id>
                   <phase>package</phase>
                   <goals>
                    <goal>copy-dependencies</goal>
                   </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```