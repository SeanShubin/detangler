# Detangler

Analyzes dependency structure and cycles

## Levels
- there must be at least 1 level
- the last level is always the class level
- the second to last level is the package level
- the first x levels greater than 2 establish a hierarchy
- every class will be broken down into the same number of levels
    - this makes sensible graphs possible without losing information
    - placeholders will be created if necessary


## 3 level example

Suppose you had the following configuration

```text
{
  level 3
  startsWith {
    include [[com seanshubin]]
    drop [[com seanshubin]]
  }
}
```

And the following (contrived) classes

```text
com.seanshubin.detangler.analysis.Detangler
com.seanshubin.detangler.analysis.tree.DetanglerBackedByTree
com.seanshubin.detangler.Metrics
com.seanshubin.CollectionUtil
```

Here is how they would be broken down into levels

| drop           | top       | package           | class                 |
| -------------- | --------- | ----------------- | --------------------- |
| com.seanshubin | detangler | analysis          | Detangler             |
| com.seanshubin | detangler | analysis.tree     | DetanglerBackedByTree |
| com.seanshubin | detangler | root-of-detangler | Metrics               |
| com.seanshubin | -root-    | -root-            | CollectionUtil        |


## Configuration File
```
{
  reportDir console/target/detangled
  searchPaths [
    console/target/detangler.jar
  ]
  level 2
  startsWith {
    include [[com seanshubin detangler]]
    drop [[com seanshubin detangler]]
    exclude []
  }
  allowedInCycle console/detangler-allowed-in-cycle.txt
  canFailBuild true
}
```

## Running from the command line
`java -jar console/target/detangler.jar console/detangler.txt`

## Running from maven
In the file maven-sample/pom.xml
```xml
<plugin>
    <groupId>com.seanshubin.detangler</groupId>
    <artifactId>detangler-maven-plugin</artifactId>
    <version>0.9.4</version>
    <executions>
        <execution>
            <phase>verify</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <detanglerConfig>detangler.txt</detanglerConfig>
    </configuration>
</plugin>
```

At the console
```text
mvn -f maven-sample/ detangler:report -DdetanglerConfig=detangler.txt
```
