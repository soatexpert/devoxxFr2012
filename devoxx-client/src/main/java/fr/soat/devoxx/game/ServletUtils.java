package fr.soat.devoxx.game;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ServletUtils {
    public static final String ERROR_FORWARD = "/html/error.jsp";

    public static void writeResponse(HttpServletResponse response,
                                     Object valueToWrite) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        Writer out = response.getWriter();
        out.write(gson.toJson(valueToWrite));
        out.flush();
        out.close();

    }
}
