# VectorSim

**vector operation & visualization tool**
*written in java w/ javafx*

## Build Requirements

This project is configured for Java 26 and Maven.

## Windows

Install a JDK 26 distribution and Maven, then verify both are on your `PATH`.

```powershell
winget install EclipseAdoptium.Temurin.26.JDK
winget install Apache.Maven
java -version
javac -version
mvn -version
```

## WSL

Install Maven and a Java 26 runtime in your WSL distro. If your distro package manager does not offer Java 26, use SDKMAN.

```bash
sudo apt update
sudo apt install -y curl zip unzip maven
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 26-tem
java -version
javac -version
mvn -version
```

## Linux

Use your distro package manager if it provides Java 26, or use SDKMAN to install it.

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 26-tem
java -version
javac -version
mvn -version
```

## Compile And Run

From the project root:

```bash
mvn compile
mvn javafx:run
```

## todo
- delete vectors
- custom cone mesh
- cylinder rotation isn't 100% accurate

