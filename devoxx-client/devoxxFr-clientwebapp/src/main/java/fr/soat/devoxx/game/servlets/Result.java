package fr.soat.devoxx.game.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.soat.devoxx.game.controllerback.ControllerBack;
import fr.soat.devoxx.game.pojo.ResultResponseDto;

/**
 * Servlet implementation class Result
 */
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String RESULT_FORWARD = "/result.jsp";
	private static final String ERROR_FORWARD = "/error.jsp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String forward = "";

		// Object userName = session.getAttribute("userName");
		Object userName = "toto";
		if (userName != null) {
			ResultResponseDto result = null ; //ControllerBack.getInstance().getResultForUser((String) userName);

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

		RequestDispatcher requestDispatcher = getServletConfig()
				.getServletContext().getRequestDispatcher(forward);
		requestDispatcher.forward(request, response);
	}
}
