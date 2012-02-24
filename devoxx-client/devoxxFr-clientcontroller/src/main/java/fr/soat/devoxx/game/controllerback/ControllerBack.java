package fr.soat.devoxx.game.controllerback;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import fr.soat.devoxx.game.WebClientUtils;
import fr.soat.devoxx.game.exceptions.ControllerException;
import fr.soat.devoxx.game.pojo.AllQuestionResponseDto;
import fr.soat.devoxx.game.pojo.ResponseRequestDto;
import fr.soat.devoxx.game.pojo.ResponseResponseDto;
import fr.soat.devoxx.game.pojo.UserRequestDto;
import fr.soat.devoxx.game.pojo.UserResponseDto;

public class ControllerBack {
	public static ControllerBack INSTANCE;

	public static final String SERVICE_PATH = "services";

	private ControllerBack() {

	}

	public static ControllerBack getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ControllerBack();
		}
		return INSTANCE;
	}

	private URI getBaseURI() {
		return UriBuilder.fromUri(WebClientUtils.INSTANCE.getBaseUri()).build();
	}

	public AllQuestionResponseDto getAllQuestions(String userName)
			throws ControllerException {

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		AllQuestionResponseDto res = null;
		try {
			res = service.path(SERVICE_PATH)
					.path("/admin/question/" + userName)
					.type(MediaType.APPLICATION_JSON)
					.get(AllQuestionResponseDto.class);
		} catch (UniformInterfaceException e) {
			throw new ControllerException();
		}

		return res;
	}

	public UserResponseDto getUser(String userName) throws ControllerException {

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		UserResponseDto res = null;
		try {
			res = service.path(SERVICE_PATH).path("/admin/user/" + userName)
					.type(MediaType.APPLICATION_JSON)
					.get(UserResponseDto.class);
		} catch (UniformInterfaceException e) {
			throw new ControllerException();
		}

		return res;
	}

	public UserResponseDto createNewUser(String userName)
			throws ControllerException {
		// User does not exist, create a new one
		UserRequestDto requestDto = new UserRequestDto();
		requestDto.setName(userName);
		requestDto.setMail(userName + "@gmail.com"); // TODO

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		UserResponseDto res = null;
		try {
			res = service.path(SERVICE_PATH).path("/admin/user")
					.type(MediaType.APPLICATION_JSON)
					.post(UserResponseDto.class, requestDto);
		} catch (UniformInterfaceException e) {
			throw new ControllerException();
		}

		return res;
	}

	public ResponseResponseDto sendReplyForQuestion(String userName, int id,
			List responses) throws ControllerException {

		ResponseRequestDto requestDto = new ResponseRequestDto();
		requestDto.setUserName(userName);
		requestDto.setId(id);
		requestDto.setResponses(responses);

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		ResponseResponseDto res = null;
		try {
			res = service.path(SERVICE_PATH)
					.path("/admin/question/" + userName + "/reply")
					.type(MediaType.APPLICATION_JSON)
					.post(ResponseResponseDto.class, requestDto);
		} catch (UniformInterfaceException e) {
			throw new ControllerException();
		}

		return res;
	}
}
