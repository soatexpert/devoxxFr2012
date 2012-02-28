package fr.soat.devoxx.game.servlets;

import fr.soat.devoxx.game.ServletUtils;
import fr.soat.devoxx.game.business.QuestionService;
import fr.soat.devoxx.game.pojo.ResponseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet implementation class Result
 */
public class ResponseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseServlet.class);

    @Inject
    QuestionService questionService;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long userId = Long.parseLong( request.getParameter("userId") );

        String idQuestion = request.getParameter("questionId");

        String[] responsesFromUser = (String[]) request.getParameterMap().get("answers");

        if (userId != null && responsesFromUser != null) {
            List<String> responses = new ArrayList(Arrays.asList(responsesFromUser));

            ResponseResponseDto res = questionService.giveResponse(userId, Integer.valueOf(idQuestion), responses);

            if (res != null) {
                ServletUtils.writeResponse(response, res);
                return;
            }

            request.setAttribute("error", "Error while sending response for user!");

        } else {
            LOGGER.debug("No registered user!");
            request.setAttribute("error", "No registered user!");
        }

        RequestDispatcher requestDispatcher = getServletConfig().getServletContext().getRequestDispatcher(ServletUtils.ERROR_FORWARD);
        requestDispatcher.forward(request, response);
    }
}
