package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezoneParam = req.getParameter("timezone");
        String timezone = "UTC";

        if (timezoneParam != null && !timezoneParam.isBlank()) {
            timezone = convertTimezone(timezoneParam);
        }

        ZonedDateTime now = ZonedDateTime.now(TimeZone.getTimeZone(timezone).toZoneId());
        String formattedTime = now.format(FORMATTER);

        resp.setContentType("text/html; charset=UTF-8");

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head><title>Time</title></head>");
        out.println("<body>");
        out.println("<h1>" + formattedTime + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }

    private String convertTimezone(String timezoneParam) {
        if (timezoneParam.startsWith("UTC")) {
            return "GMT" + timezoneParam.substring(3);
        }
        return timezoneParam;
    }
}