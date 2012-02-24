package fr.soat.devoxx.game.servlets;

import fr.soat.devoxx.game.ServletUtils;
import fr.soat.devoxx.game.business.QuestionService;
import fr.soat.devoxx.game.pojo.AllQuestionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AllQuestions
 */
public class QuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionServlet.class);

    @Inject
    QuestionService questionService;


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("userName");

        if (userName != null) {
            AllQuestionResponseDto allQuestions = questionService.getAllQuestions(userName);

            //TODO : utiliser un patron de méthode !!!!!!!!!!
            if (allQuestions != null) {
                ServletUtils.writeResponse(response, allQuestions.getQuestions());
                return;
            }

            request.setAttribute("error", "Error in getting questions for user!");

        } else {
            LOGGER.error("No registered user!");
            request.setAttribute("error", "No registered user!");
        }

        RequestDispatcher requestDispatcher = getServletConfig().getServletContext().getRequestDispatcher(ServletUtils.ERROR_FORWARD);
        requestDispatcher.forward(request, response);
    }
}
