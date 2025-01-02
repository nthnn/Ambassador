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

import com.sun.net.httpserver.HttpServer;
import web.ambassador.db.DatabaseManager;

import java.lang.ref.Cleaner;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Primary orchestrator for HTTP server management, routing, and database connection pooling.
 * The Kernel class encapsulates the core logic of starting and managing an HTTP server using Java's
 * built-in {@code HttpServer} class. It handles request routing, manages thread pools for request execution,
 * and facilitates interaction with the database layer via the {@code DatabaseManager}.
 *
 * <p><b>Key features include:</b></p>
 * <ul>
 *     <li>Configurable thread pool management</li>
 *     <li>Dynamic controller registration for routing</li>
 *     <li>Graceful shutdown with automatic resource cleanup</li>
 *     <li>Customizable error handling for HTTP responses</li>
 *     <li>Database connection pooling and management</li>
 * </ul>
 *
 * @author <a href="https://github.com/nthnn" target="_blank">Nathanne Isip</a> &lt;<a href="mailto:nathanneisip@gmail.com">nathanneisip@gmail.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public class Kernel implements AutoCloseable {
    /**
     * A Cleaner instance used to manage and automate resource cleanup during shutdown.
     * Cleaner ensures that resource management logic (like database connection closures or thread pool shutdowns)
     * is executed reliably upon application termination.
     */
    private final Cleaner cleaner;

    /** Cleanable instance to handle cleanup logic upon failure during shutdown. */
    private Cleaner.Cleanable cleanableFailRunner = null;

    /** Cleanable instance to handle cleanup logic upon successful shutdown. */
    private Cleaner.Cleanable cleanableSuccessRunner = null;

    /** The port on which the HTTP server will listen for incoming connections. */
    private final int port;

    /** The maximum backlog for incoming socket connections. */
    private final int backlog;

    /** The size of the database connection pool. */
    private final int poolSize;

    /** Router responsible for handling and dispatching HTTP requests to controllers. */
    private final Router router;

    /** Manages database connectivity and connection pooling. */
    private final DatabaseManager dbManager;

    /**
     * Stores error controllers mapped to specific HTTP status codes.
     * This allows custom error handling for different types of HTTP errors by associating status codes
     * (e.g., 404, 500) with corresponding ErrorController instances.
     */
    private final Map<Integer, ErrorController> errorControllers;

    /** Maximum number of threads available for the thread pool. */
    private int maxThreads;

    /** Thread pool executor that handles incoming HTTP requests concurrently. */
    private ExecutorService executor;

    /**
     * Constructs a basic Kernel instance with a specified port and default configuration.
     * This constructor sets up an HTTP server with minimal configuration.
     * No database connections are initialized in this mode.
     *
     * @param port The port on which the server will run.
     */
    public Kernel(int port) {
        this.port = port;
        this.poolSize = 0;
        this.backlog = 100;
        this.dbManager = null;
        this.router = new Router();
        this.cleaner = Cleaner.create();
        this.errorControllers = new HashMap<>();
        this.maxThreads = Runtime.getRuntime().availableProcessors() * 2;
    }

    /**
     * Constructs a Kernel with database connectivity and custom connection pooling.
     * This constructor initializes the web server with database integration, allowing efficient connection pooling.
     *
     * @param poolSize Size of the database connection pool.
     * @param backlog Maximum backlog of incoming connections.
     * @param port Server port for listening.
     * @param url Database connection URL.
     * @param username Database username.
     * @param password Database password.
     */
    public Kernel(int poolSize, int backlog, int port, String url, String username, String password) {
        this.port = port;
        this.backlog = backlog;
        this.poolSize = poolSize;
        this.router = new Router();
        this.cleaner = Cleaner.create();
        this.errorControllers = new HashMap<>();
        this.maxThreads = Runtime.getRuntime().availableProcessors() * 2;

        this.dbManager = new DatabaseManager(
            url,
            username,
            password,
            this.poolSize,
            60
        );
    }

    /**
     * Constructs a Kernel instance using a pre-defined configuration object.
     * This allows flexible initialization by passing a KernelConfig object, which encapsulates various
     * server and database configuration parameters.
     *
     * @param config KernelConfig object containing server and database configuration.
     */
    public Kernel(KernelConfig config) {
        this.port = config.port();
        this.router = new Router();
        this.backlog = config.backlog();
        this.cleaner = Cleaner.create();
        this.poolSize = config.poolSize();
        this.maxThreads = config.maxThreads();
        this.errorControllers = new HashMap<>();

        if(!config.dbUrl().isEmpty())
            this.dbManager = new DatabaseManager(
                config.dbUrl(),
                config.dbUsername(),
                config.dbPassword(),
                this.poolSize,
                config.dbTimeout()
            );
        else this.dbManager = null;
    }

    /**
     * Registers shutdown hooks to ensure clean server termination.
     *
     * @param onSuccess Runnable to execute upon successful shutdown.
     * @param onFail Runnable to execute if shutdown encounters an error.
     */
    public void setupShutdownHook(Runnable onSuccess, Runnable onFail) {
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            this.cleanableSuccessRunner = this.cleaner.register(this, onSuccess);
            this.cleanableFailRunner = this.cleaner.register(this, onFail);

            try {
                Kernel.this.stop();
                onSuccess.run();
            }
            catch(Exception e) {
                onFail.run();
            }
        }));
    }

    /**
     * Sets the maximum number of threads for the thread pool.
     *
     * @param threads Maximum number of threads.
     */
    public void setMaxThreads(int threads) {
        this.maxThreads = threads;
    }

    /**
     * Registers custom error controllers for handling specific HTTP errors.
     *
     * @param statusCode HTTP status code (e.g., 404, 500).
     * @param controller ErrorController to handle the error.
     */
    public void setErrorController(int statusCode, ErrorController controller) {
        this.errorControllers.put(statusCode, controller);
        this.errorControllers.putIfAbsent(0, new DefaultErrorController());
    }

    /**
     * Registers a controller class with the router for handling HTTP requests.
     * This method dynamically registers a controller class, which contains request-handling
     * methods mapped to specific routes. The controller must follow the expected structure
     * to be properly recognized by the router.
     *
     * @param controllerClass Class object representing the controller to be registered.
     *
     * <p><b>Example:</b></p>
     * ```java
     * kernel.registerController(MyController.class);
     * ```
     */
    public void registerController(Class<?> controllerClass) {
        this.router.registerController(controllerClass);
    }

    /**
     * Starts the HTTP server and initializes the request processing pipeline.
     *
     * This method sets up the HTTP server to listen on the specified port, creates a thread pool
     * for handling incoming requests, and registers the root request handler (`KernelHandler`)
     * to manage routing and error handling.
     *
     * The server operates asynchronously, processing multiple requests concurrently based on
     * the configured thread pool size.
     *
     * @throws IOException if the server fails to start or encounters binding issues.
     *
     * <p><b>Example:</b></p>
     * ```java
     * kernel.start();
     * ```
     */
    public void start() throws IOException {
        HttpServer server = HttpServer.create(
            new InetSocketAddress(this.port),
            this.backlog
        );

        this.executor = new ThreadPoolExecutor(
            this.maxThreads / 2,
            this.maxThreads,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        server.createContext("/", KernelHandler.build(
            this.router,
            this.dbManager,
            this.errorControllers
        ));
        server.setExecutor(this.executor);
        server.start();

        System.out.println(
            "Server started on port " + this.port +
                " with " + this.maxThreads + " threads"
        );
    }

    /**
     * Stops the HTTP server and shuts down associated resources gracefully.
     *
     * This method initiates the shutdown of the thread pool and closes all active database connections.
     * It ensures that ongoing tasks are completed within a specified timeout period (60 seconds).
     * If tasks do not terminate within this period, a forceful shutdown is triggered.
     *
     * This method is invoked automatically during shutdown or can be called explicitly.
     *
     * <p><b>Example:</b></p>
     * ```java
     * kernel.stop();
     * ```
     */
    public void stop() {
        if(this.executor == null)
            return;
        this.executor.shutdown();

        if(this.dbManager != null)
            this.dbManager.shutdown();

        try {
            if(!this.executor.awaitTermination(60, TimeUnit.SECONDS) &&
                !this.executor.isShutdown())
                this.executor.shutdownNow();
        }
        catch(InterruptedException e) {
            this.executor.shutdownNow();
        }
    }

    /**
     * Closes the Kernel and performs resource cleanup.
     *
     * This method is part of the AutoCloseable interface, ensuring that resources are cleaned
     * up properly when the Kernel instance is closed. It triggers any registered success or
     * failure cleanup actions through the Cleaner API.
     *
     * <p><b>Example:</b></p>
     * ```java
     * try (Kernel kernel = new Kernel(8080)) {
     *     kernel.start();
     * } // Kernel will be automatically closed here
     * ```
     */
    @Override
    public void close() {
        if(this.cleanableFailRunner != null)
            this.cleanableFailRunner.clean();

        if(this.cleanableSuccessRunner != null)
            this.cleanableSuccessRunner.clean();
    }
}
