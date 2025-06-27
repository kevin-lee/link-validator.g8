# link-validator.g8

A [Giter8](http://www.foundweekends.org/giter8/) template for creating a link validator application in Scala.

## Overview

This template provides a starting point for building a link validator application that can extract and validate links from Markdown and HTML documents. It helps identify broken links, ensuring your documentation remains accurate and reliable.

## Usage

### Using sbt

```bash
sbt new kevin-lee/link-validator.g8
```

### Using g8 command line

```bash
g8 kevin-lee/link-validator.g8
```

## Template Parameters

| Parameter             | Description                        | Default Value    |
| --------------------- | ---------------------------------- | ---------------- |
| `sbt_version`         | The version of sbt to use          | `1.11.2`         |
| `scalaVersion`        | The version of Scala to use        | `3.7.1`          |
| `organization`        | Your organization domain           | `your.domain`    |
| `organizationName`    | Your organization name             | -                |
| `author_name`         | Your name                          | `Your Name`      |
| `author_email`        | Your email                         | `some@email`     |
| `github_username`     | Your GitHub username               | -                |
| `project_name`        | The name of your project           | `link-validator` |
| `package`             | The base package name              | `linkvalidator`  |
| `cats_version`        | Cats library version               | `2.13.0`         |
| `cats_effect_version` | Cats Effect library version        | `3.6.1`          |
| `refined4s_version`   | Refined4s library version          | `1.1.0`          |
| `hedgehog_version`    | Hedgehog testing framework version | `0.12.0`         |
| `decline_version`     | Decline CLI library version        | `2.5.0`          |
| `extras_version`      | Extras library version             | `0.45.0`         |

## Features

The generated project will include:

- A modular structure for building a link validator application

## Development

### Requirements

- JDK 17 or higher
- sbt 1.11.2 or higher

## License

This template is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
