/*
 * Copyright (c) 2024 - Nathanne Isip
 * This file is part of Ambassador.
 *
 * Ambassador is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Ambassador is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ambassador. If not, see <https://www.gnu.org/licenses/>.
 */
package web.ambassador.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The {@code KernelConfig} class represents the configuration settings required to initialize and run the web server.
 *
 * This class is used to encapsulate various configuration properties such as the server port, database connection details,
 * thread pool settings, and more. These properties are typically read from an external properties file, which can be customized
 * to modify the behavior of the server without needing to modify the code directly.
 *
 * The class is implemented as a {@code record}, which provides a concise way of declaring a class with immutable fields.
 * All fields in this class are final and cannot be modified after they are set during object construction.
 *
 * It supports loading these properties from a properties file, where each key corresponds to a configuration item (e.g., "port",
 * "db_url", etc.). The {@code load} method provides the logic to read the properties file, parse the values, and return an instance
 * of {@code KernelConfig}.
 *
 * <p><b>Usage Example:</b></p>
 * ```java
 *     try {
 *         KernelConfig config = KernelConfig.load("config.properties");
 *         System.out.println("Server will run on port: " + config.port());
 *     } catch (IOException e) {
 *         e.printStackTrace();
 *     }
 * ```
 *
 * @param port The port number that the server will listen on. Default is 80 if not provided in the properties file.
 * @param backlog The maximum number of pending connections in the server's backlog queue. Default is 100 if not provided in the properties file.
 * @param dbUrl The URL of the database the server will connect to. Default is an empty string if not provided in the properties file.
 * @param dbUsername The username for authenticating with the database. Default is an empty string if not provided in the properties file.
 * @param dbPassword The password for authenticating with the database. Default is an empty string if not provided in the properties file.
 * @param dbTimeout The timeout duration (in seconds) for database operations. Default is 60 seconds if not provided in the properties file.
 * @param maxThreads The maximum number of threads that the server will use for processing requests. Default is 8 if not provided in the properties file.
 * @param poolSize The size of the thread pool for handling server requests. Default is 2 if not provided in the properties file.
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public record KernelConfig(
    int port,
    int backlog,
    String dbUrl,
    String dbUsername,
    String dbPassword,
    int dbTimeout,
    int maxThreads,
    int poolSize
) {
    /**
     * Loads the server configuration from a specified properties file.
     * <br/>
     * This method reads the properties file, parses its contents, and creates a {@code KernelConfig} instance.
     * It uses default values for any properties that are not present in the file.
     *
     * @param propertiesFile The path to the properties file containing the server configuration.
     * @return A {@code KernelConfig} instance containing the loaded configuration values.
     * @throws IOException If there is an error reading the properties file.
     *
     * @see Properties
     * @see KernelConfig
     */
    public static KernelConfig load(String propertiesFile) throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream(propertiesFile));

        return new KernelConfig(
            Integer.parseInt(config.getOrDefault("port", "80").toString()),
            Integer.parseInt(config.getOrDefault("backlog", "100").toString()),
            config.getOrDefault("db_url", "").toString(),
            config.getOrDefault("db_username", "").toString(),
            config.getOrDefault("db_password", "").toString(),
            Integer.parseInt(config.getOrDefault("db_timeout", "60").toString()),
            Integer.parseInt(config.getOrDefault("max_threads", "8").toString()),
            Integer.parseInt(config.getOrDefault("pool_size", "2").toString())
        );
    }
}
