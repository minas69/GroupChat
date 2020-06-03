package group.chat.server;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ErrorHandler")
public class ErrorHandler extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer httpStatus = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Integer code = (Integer) request.getAttribute("code");
        if (code == null) {
            code = httpStatus;
        }
        String message = (String) request.getAttribute("javax.servlet.error.message");

        response.setContentType("application/json");

        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (httpStatus != null) {
            response.setStatus(httpStatus);
            builder.add("code", code);
            builder.add("message", message);
        }

        JsonObject jo = builder.build();

        PrintWriter out = response.getWriter();
        out.print(jo.toString());
        out.flush();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
