package com.example.app;

import com.example.app.controllers.DateTime;
import com.example.app.controllers.HomeController;

import web.ambassador.core.Kernel;
import java.io.IOException;

public class WebApplication {
    public static void main(String[] args) {
        try(Kernel framework = new Kernel(8080)) {
            framework.setupShutdownHook(
                ()-> System.out.println("Server stopped successfully!"),
                ()-> System.out.println("Cannot shutdown server!")
            );

            framework.registerController(HomeController.class);
            framework.registerController(DateTime.class);

            // Configure custom error pages
            // framework.setErrorController(404, new CustomNotFoundController());
            // framework.setErrorController(500, new CustomServerErrorController());

            framework.setMaxThreads(Runtime.getRuntime().availableProcessors() * 2);
            framework.start();
        }
        catch(IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            System.exit(1);
        }
        catch(Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }
}
