package fr.soat.devoxx.game;

import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ServletUtils {
    public static final String ERROR_FORWARD = "/html/error.jsp";

    public static void writeResponse(HttpServletResponse response,
                                        Object valueToWrite) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON);
        Gson gson = new Gson();
        Writer out = response.getWriter();
        out.write(gson.toJson(valueToWrite));
        out.flush();
        out.close();

    }
}
