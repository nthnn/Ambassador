package com.example.app.controllers;

import web.ambassador.annotations.Controller;
import web.ambassador.annotations.Method;
import web.ambassador.db.DatabaseManager;
import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.Component;
import web.ambassador.view.Dom;
import web.ambassador.view.ViewContent;

@Controller
public class HomeController implements Component {
    @Override
    @Method
    public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
        response.getHeaders().put("Cookie", "test=hello");
        return Dom.createPage(
            Dom.head(
                Dom.title("Your Ambassador homepage!"),
                Dom.stylesheet("assets/styles/bootstrap.min.css")
            ),
            Dom.body(
                Dom.br(),
                Dom.div(
                    Dom.img()
                        .src("assets/images/logo.png")
                        .attr("alt", "Logo")
                        .attr("width", "300")
                        .addClass("mt-4"),
                    Dom.h2("Welcome to your Ambassador homepage!")
                        .addClass("mt-4"),
                    Dom.p(
                        Dom.noTag("Server date and time is: "),
                        Dom.span().id("datetime")
                    ),
                    Dom.div(
                        Dom.a(
                            "https://nthnn.github.io/Ambassador",
                            "Learn More",
                            "_blank"
                        ).addClass(
                            "btn",
                            "btn-primary",
                            "d-block-inline",
                            "mx-1"
                        ),
                        Dom.a(
                            "https://github.com/nthnn/Ambassador",
                            "GitHub",
                            "_blank"
                        ).addClass(
                            "btn",
                            "btn-primary",
                            "d-block-inline",
                            "mx-1"
                        )
                    ),
                    Dom.script(
                        "javascript",
                        """
                        const fetchDatetime = async ()=> {
                            const response = await fetch('/datetime', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({})
                            });

                            if(!response.ok)
                                return;
                
                            const data = await response.json();
                            const datetimeElement = document.getElementById('datetime');
                
                            datetimeElement.textContent = data.datetime || "No data received";
                        };

                        fetchDatetime();
                        setInterval(fetchDatetime, 1000);
                        """
                    )
                ).attr("align", "center")
                    .addClass("mt-4")
            )
        );
    }
}
