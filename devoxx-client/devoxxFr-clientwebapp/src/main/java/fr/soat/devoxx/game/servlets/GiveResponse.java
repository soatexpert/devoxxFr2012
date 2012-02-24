package fr.soat.devoxx.game.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import fr.soat.devoxx.game.pojo.ResponseResponseDto;
import fr.soat.devoxx.game.pojo.question.ResponseType;

/**
 * Servlet implementation class Result
 */
public class GiveResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(GiveResponse.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");

		String idQuestion = request.getParameter("questionId");

		// request.getParameter("answers") only returns the first string of the
		// list !
		String[] responsesFromUser = (String[]) request.getParameterMap().get(
				"answers");

		if (userName != null && responsesFromUser != null) {
			try {
				// Generic ArrayList used to bypass Jersey bug
				List responses = new ArrayList(Arrays.asList(responsesFromUser));
				ResponseResponseDto res = ControllerBack.getInstance()
						.sendReplyForQuestion(userName,
								Integer.valueOf(idQuestion), responses);

				if (res != null) {
					ServletUtils.writeResponse(response, res);
					return;
				}

			} catch (ControllerException e) {
				logger.error("Error while sending response");
				// TODO remove
				// temporary code to bypass exception on the jersey side
				ResponseResponseDto res = new ResponseResponseDto();
				res.setResponseType(ResponseType.INVALID);
				res.setId(Integer.valueOf(idQuestion));
				ServletUtils.writeResponse(response, res);
				return;
			}

			request.setAttribute("error",
					"Error while sending response for user!");

		} else {
			logger.debug("No registered user!");
			request.setAttribute("error", "No registered user!");
		}

		RequestDispatcher requestDispatcher = getServletConfig()
				.getServletContext().getRequestDispatcher(
						ServletUtils.ERROR_FORWARD);
		requestDispatcher.forward(request, response);
	}
}
