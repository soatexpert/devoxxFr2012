package fr.soat.devoxx.game.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.soat.devoxx.game.controllerback.ControllerBack;
import fr.soat.devoxx.game.exceptions.ControllerException;
import fr.soat.devoxx.game.pojo.AllQuestionResponseDto;

/**
 * Servlet implementation class AllQuestions
 */
public class AllQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(AllQuestions.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");

		if (userName != null) {
			AllQuestionResponseDto allQuestions = null;
			try {
				allQuestions = ControllerBack.getInstance().getAllQuestions(
						userName);

				if (allQuestions != null) {
					ServletUtils.writeResponse(response,
							allQuestions.getQuestions());
					return;
				}

			} catch (ControllerException e) {
				logger.error("Error in getting questions for user!");
			}

			request.setAttribute("error",
					"Error in getting questions for user!");

		} else {
			logger.error("No registered user!");
			request.setAttribute("error", "No registered user!");
		}

		RequestDispatcher requestDispatcher = getServletConfig()
				.getServletContext().getRequestDispatcher(
						ServletUtils.ERROR_FORWARD);
		requestDispatcher.forward(request, response);
	}
}
