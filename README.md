
# rx-codeforces-api

A reactive api wrapper for codeforces api :)

At its core, the library uses **Spring WebFlux's** reactive web client to make api requests. 
This allows for a non-blocking, asynchronous usage.

It also uses **Project Reactor's** reactive streams to handle the responses.

<p align="center">
  <a href="https://github.com/fork52/chess_gif/blob/master/LICENSE">
      <img alt="Apache License" src="https://img.shields.io/badge/License-Apache_2.0-blue" />
  </a>
  <a href="https://javadoc.io/doc/io.github.fork52.rx-codeforces-api-wrapper/rx-codeforces-api">
      <img alt="Java Doc" src="https://javadoc.io/badge2/io.github.fork52.rx-codeforces-api-wrapper/rx-codeforces-api/javadoc.svg" />
  </a>
</p>

## Installation
### Maven
The library is available on Maven Central. [Maven Central Link](https://central.sonatype.com/artifact/io.github.fork52.rx-codeforces-api-wrapper/rx-codeforces-api).

To use it, add the following dependency to your pom.xml file:

Latest Version: 1.0.2

```xml
<groupId>io.github.fork52.rx-codeforces-api-wrapper</groupId>
<artifactId>rx-codeforces-api</artifactId>
<version>1.0.2</version>
```

## Documentation
- CodeForces Api Documentation: [cf-api-docs](https://codeforces.com/apiHelp)
- Javadocs: [Official documentation](https://javadoc.io/doc/io.github.fork52.rx-codeforces-api-wrapper/rx-codeforces-api/latest/com/rxcodeforces/api/CodeforcesWebClient.html)

## Usage

### CodeforcesWebClient
The `CodeforcesWebClient` class is the main entry point for the library. It is used to make api requests to codeforces-api.

Example:

```java
Mono<CFResponseList<User>> cfResponseListMono = codeforcesWebClient
    .getUserInfo(Arrays.asList("DmitriyH", "Fefer_Ivan"));

System.out.println(cfResponseListMono.block());
```

## Formatting
Please make sure to
follow [Google CodeStyle](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
before raising any pull requests.


## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Fork the repository and clone it to your local machine. Create a new branch for your feature.

```bash
git checkout -b my-new-feature
```