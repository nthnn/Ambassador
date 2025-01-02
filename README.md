<h1 align="center">Ambassador</h1>
<p align="center">Java Lightweight Web Framework</p>

<p align="center">
    <img src="misc/logo.png" width="300" />
</p>

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## Introduction

Ambassador is a lightweight, high-performance web framework designed for building scalable and efficient web applications. It provides robust support for database management, middleware integration, routing, and asynchronous execution, all wrapped in a clean, modular architecture.

Moreover, it is a modern web framework designed to facilitate the creation of performant and scalable web applications. Whether you're building an API server, a full-stack application, or a microservice, Ambassador provides all the essential tools to get your application up and running quickly.

It is built to be:
- **Lightweight**: Focused on performance and minimalism.
- **Extensible**: Easily extensible to meet specific requirements.
- **Asynchronous**: Optimized for asynchronous processing for better scalability.
- **Database-agnostic**: Provides a flexible database layer with built-in connection pooling and query management.

## Features

- **Connection Pooling**: Efficient management of database connections with the `DatabaseManager`.
- **Asynchronous Execution**: Supports asynchronous HTTP handling, ideal for high-concurrency applications.
- **Modular Routing**: Flexible routing mechanism for defining endpoints and middleware.
- **Transaction Management**: Automatic transaction handling within the database layer.
- **Custom Middleware**: Easily extendable middleware framework for request/response processing.
- **Query Builder**: Dynamic and programmatically generated SQL queries with support for parameters and conditions.

## Getting Started

To get started with Ambassador, follow the instructions below. If you are already familiar with web frameworks, you’ll find it easy to integrate Ambassador into your project.

### Prerequisites

- IntelliJ IDEA (version 2024 or above)
- MySQL as supported relational database
- Git Command-Line Interface

### Generating New Project

Run the following command below terminal to pull up the Ambassador project template:

```bash
git clone --depth 1 https://github.com/nthnn/Ambassador
python3 Ambassador/setup.py
```

After the template set-up, open the output project folder in IntelliJ IDEA.

## License

Ambassador is licensed under the GNU GPL Public License v3. See [LICENSE](src/web/ambassador/LICENSE) for more details.
