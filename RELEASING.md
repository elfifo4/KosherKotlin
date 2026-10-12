# Releasing

This document is for maintainers. The library is published to Maven Central as
`io.github.elfifo4:kosherkotlin` with the
[Vanniktech Maven Publish plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

## Prerequisites

`~/.gradle/gradle.properties` must contain:

```properties
mavenCentralUsername=<token from central.sonatype.com → View Account → Generate User Token>
mavenCentralPassword=<token password>
signingInMemoryKey=<GPG private key as a single line with \n separators>
signingInMemoryKeyPassword=<GPG key passphrase>
```

To convert a GPG private key to the required single-line format:

```bash
awk '{printf "%s\\n", $0}' ~/private-key.asc
```

## Testing before a release

```bash
# Run the test suite
./gradlew :library:desktopTest

# Verify the JARs and POMs are generated correctly
./gradlew :library:publishToMavenLocal

# Inspect the output
find ~/.m2/repository/io/github/elfifo4 -name "*.pom" | sort
```

## Steps

1. Update `version` in [`library/build.gradle.kts`](library/build.gradle.kts).
2. Update the version in the installation snippet in [`README.MD`](README.MD).
3. Commit, then tag the release:
   ```bash
   git tag v1.0.1
   git push origin v1.0.1
   ```
4. Publish:
   ```bash
   ./gradlew :library:publishAndReleaseToMavenCentral --no-configuration-cache
   ```
5. Verify the deployment at [central.sonatype.com/publishing/deployments](https://central.sonatype.com/publishing/deployments).
6. After 15–30 minutes, verify the artifact is live at
   <https://repo1.maven.org/maven2/io/github/elfifo4/kosherkotlin/>.
