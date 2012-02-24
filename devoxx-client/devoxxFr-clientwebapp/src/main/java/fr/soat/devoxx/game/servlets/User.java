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
import fr.soat.devoxx.game.pojo.UserResponseDto;

/**
 * Temporary servlet for easily loging
 */
public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER_FORWARD = "/html/index.jsp";
	private static final Logger logger = LoggerFactory.getLogger(User.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String forward = "";

		String userName = (String) request.getParameter("userName");

		if (userName != null) {

			UserResponseDto user = null;
			try {
				user = ControllerBack.getInstance().getUser(userName);
			} catch (ControllerException e) {
				// TODO remove
				// temporary code for prototype purpose
				// If user doesn't exist create one
				try {
					user = ControllerBack.getInstance().createNewUser(userName);
				} catch (ControllerException ex) {
					logger.error("Error while retrieving user");
				}

			}

			if (user != null) {
				// store userName in session
				session.setAttribute("userName", userName);
				request.setAttribute("userResponse", user);
				forward = USER_FORWARD;
			} else {
				request.setAttribute("error", "No registered user!");
				forward = ServletUtils.ERROR_FORWARD;
			}
		} else {
			request.setAttribute("error", "No registered user!");
			forward = ServletUtils.ERROR_FORWARD;
		}

		RequestDispatcher requestDispatcher = getServletConfig()
				.getServletContext().getRequestDispatcher(forward);
		requestDispatcher.forward(request, response);
	}
}
