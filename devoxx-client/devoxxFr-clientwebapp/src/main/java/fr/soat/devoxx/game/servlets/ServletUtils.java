package fr.soat.devoxx.game.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class ServletUtils {
	protected static final String ERROR_FORWARD = "/html/error.jsp";

	protected static void writeResponse(HttpServletResponse response,
			Object valueToWrite) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON);
		PrintWriter writer = response.getWriter();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
		Writer strWriter = new StringWriter();
		mapper.writeValue(strWriter, valueToWrite);
		String json = strWriter.toString();

		writer.print(json);
		writer.close();
	}
}
