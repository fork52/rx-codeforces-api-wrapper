
# rx-codeforces-api

A reactive api wrapper for codeforces api. 

At its core, the library uses **Spring WebFlux's** reactive web client to make api requests. 
This allows for a non-blocking, asynchronous usage.

It also uses **Project Reactor's** reactive streams to handle the responses.

<p align="center">
  <a href="https://github.com/fork52/chess_gif/blob/master/LICENSE">
      <img alt="Apache License" src="https://img.shields.io/badge/License-Apache_2.0-blue" />
  </a>
</p>

## Installation
### Maven
```xml
<groupId>io.github.fork52.rx-codeforces-api-wrapper</groupId>
<artifactId>rx-codeforces-api</artifactId>
<version>1.0.0</version>
```

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