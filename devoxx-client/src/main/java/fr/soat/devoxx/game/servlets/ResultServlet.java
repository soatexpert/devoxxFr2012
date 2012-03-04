package fr.soat.devoxx.game.servlets;

import fr.soat.devoxx.game.business.ResultService;
import fr.soat.devoxx.game.pojo.ResultResponseDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class Result
 */
public class ResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String RESULT_FORWARD = "/result.jsp";
    private static final String ERROR_FORWARD = "/error.jsp";

    @Autowired
    ResultService resultService;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String forward = "";

        Long userId = Long.parseLong(request.getParameter("userId"));

        if (userId != null) {
            ResultResponseDto result = resultService.getResultForUser(userId);

            if (result != null) {
                request.setAttribute("nbFail", result.getNbFail());
                request.setAttribute("nbInvalid", result.getNbInvalid());
                request.setAttribute("nbSuccess", result.getNbSuccess());
                forward = RESULT_FORWARD;
            } else {
                request.setAttribute("error", "No results found for user!");
                forward = ERROR_FORWARD;
            }
        } else {
            request.setAttribute("error", "No user!");
            forward = ERROR_FORWARD;
        }

        RequestDispatcher requestDispatcher = getServletConfig().getServletContext().getRequestDispatcher(forward);
        requestDispatcher.forward(request, response);
    }
}
