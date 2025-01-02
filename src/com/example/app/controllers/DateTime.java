package com.example.app.controllers;

import web.ambassador.annotations.Controller;
import web.ambassador.annotations.Method;
import web.ambassador.db.DatabaseManager;
import web.ambassador.enums.HttpStatusCode;
import web.ambassador.enums.MethodType;
import web.ambassador.http.Request;
import web.ambassador.http.Response;
import web.ambassador.view.Component;
import web.ambassador.view.EmptyView;
import web.ambassador.view.JsonContent;
import web.ambassador.view.ViewContent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller(path="/datetime")
public class DateTime implements Component {
    @Override
    @Method(MethodType.POST)
    public ViewContent index(Request request, Response response, DatabaseManager dbManager) {
        Map<String, Object> data = new HashMap<>();
        data.put("datetime", new Date());

        return JsonContent.fromMap(data);
    }

    @Method
    public ViewContent redirect(Request request, Response response, DatabaseManager dbManager) {
        response.setStatusCode(HttpStatusCode.PERMANENT_REDIRECT);
        response.getHeaders().put("Location", "/");

        return new EmptyView();
    }
}
