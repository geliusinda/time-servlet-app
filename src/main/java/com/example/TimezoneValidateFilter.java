package com.example;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String timezoneParam = req.getParameter("timezone");

        if (timezoneParam != null && !timezoneParam.isBlank()) {
            String convertedTimezone = convertTimezone(timezoneParam);

            if (!isValidTimezone(convertedTimezone)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html; charset=UTF-8");

                PrintWriter out = resp.getWriter();
                out.println("<html>");
                out.println("<head><title>Error</title></head>");
                out.println("<body>");
                out.println("<h1>Invalid timezone</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String convertTimezone(String timezoneParam) {
        if (timezoneParam.startsWith("UTC")) {
            return "GMT" + timezoneParam.substring(3);
        }
        return timezoneParam;
    }

    private boolean isValidTimezone(String timezone) {
        String[] availableIds = TimeZone.getAvailableIDs();
        for (String id : availableIds) {
            if (id.equals(timezone)) {
                return true;
            }
        }

        if (timezone.startsWith("GMT+") || timezone.startsWith("GMT-")) {
            try {
                TimeZone tz = TimeZone.getTimeZone(timezone);
                return !tz.getID().equals("GMT") || timezone.equals("GMT") || timezone.equals("UTC");
            } catch (Exception e) {
                return false;
            }
        }

        return timezone.equals("UTC") || timezone.equals("GMT");
    }
}