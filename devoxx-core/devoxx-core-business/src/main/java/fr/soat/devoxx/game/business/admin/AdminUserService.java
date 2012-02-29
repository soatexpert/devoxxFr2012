/*
 * Copyright (c) 2011 Khanh Tuong Maudoux <kmx.petals@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.soat.devoxx.game.business.admin;

import fr.soat.devoxx.game.admin.pojo.GameUserDataManager;
import fr.soat.devoxx.game.admin.pojo.dto.AllUserResponseDto;
import fr.soat.devoxx.game.business.exception.InvalidUserException;
import fr.soat.devoxx.game.persistent.User;
import fr.soat.devoxx.game.pojo.UserRequestDto;
import fr.soat.devoxx.game.pojo.UserResponseDto;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

/**
 * User: khanh
 * Date: 20/12/11
 * Time: 14:12
 */
@Component
@Singleton
@Path("/admin/user")
public class AdminUserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminUserService.class);

    private String PERSISTENCE_UNIT_NAME = "devoxx";

    @Inject
    private GameUserDataManager gameUserDataManager;

    private final Validator validator;

    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final Mapper dozerMapper = new DozerBeanMapper();

    EntityManagerFactory emf;

    //    @javax.enterprise.inject.Produces
//    @PersistenceContext
    EntityManager em;

    private void init() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
    }

    public AdminUserService() {
    	init();
    }

    AdminUserService(String persistenceUnitName,
                     GameUserDataManager gameUserDataManager) {
        this.PERSISTENCE_UNIT_NAME = persistenceUnitName;
        this.gameUserDataManager = gameUserDataManager;
        init();
    }

    private void close() {
        if (em != null) {
            em.close();
        }
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AllUserResponseDto getAllUsers() {
        AllUserResponseDto allUsersDto = new AllUserResponseDto();
        try {
            @SuppressWarnings("unchecked")
            List<User> users = em.createQuery("from User u").getResultList();
            for (User user : users) {
                allUsersDto.addUserResponse(this.dozerMapper.map(user, UserResponseDto.class));
            }
        } catch (RuntimeException e) {
        	LOGGER.debug("Get all users failed", e);
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
        return allUsersDto;
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponseDto createUser(UserRequestDto userRequestDto)
            throws InvalidUserException {
        try {
            User user = dozerMapper.map(userRequestDto, User.class);

            Set<ConstraintViolation<User>> constraintViolations = validator
                    .validate(user);

            if (constraintViolations.size() != 0) {
                LOGGER.error("Invalid input for user creation {}",
                        userRequestDto);
                throw new InvalidUserException(constraintViolations);
            }
            /*
                * final String token = generateToken(); user.setToken(token);
                */

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            LOGGER.debug("User creation successful: {}", userRequestDto);

            this.gameUserDataManager.registerUser(user.getId());

            return dozerMapper.map(user, UserResponseDto.class);
        } catch (RuntimeException e) {
        	LOGGER.debug("Post new user failed", e);
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
    }

    @Path("/{userId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public UserResponseDto getUser(@PathParam("userId") Long userId) {
        try {
            User user = getUserById(userId);

            if (null != user) {
                LOGGER.debug("get user {} successful", userId);
                UserResponseDto response = dozerMapper.map(user, UserResponseDto.class);
                response.setToken(null);
                return response;
            } else {
                LOGGER.debug("get user {} failed: not found", userId);
                throw new WebApplicationException(Status.NOT_FOUND);
            }
        } catch (PersistenceException e) {
            LOGGER.debug("get user failed: PersistenceException", e);
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }
    
    @Path("/openId/{urlId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public UserResponseDto getUserByOpenId(@PathParam("urlId") String urlId) {
        try {
        	urlId = URLDecoder.decode(urlId, "UTF-8");
            User user = getUserByUrlId(urlId);

            if (null != user) {
                LOGGER.debug("get user {} successful", urlId);
                UserResponseDto response = dozerMapper.map(user, UserResponseDto.class);
                response.setToken(null);
                return response;
            } else {
                LOGGER.debug("get user {} failed: not found", urlId);
                throw new WebApplicationException(Status.NOT_FOUND);
            }
        } catch (UnsupportedEncodingException e) {
        	LOGGER.error("UrlDecoding '/openId/"+urlId+"' error", e);
        	throw new WebApplicationException(Status.BAD_REQUEST);
        } catch (PersistenceException e) {
            LOGGER.debug("get user failed: PersistenceException", e);
            throw new WebApplicationException(Status.NOT_FOUND);
        } 
    }

    @Path("/{userId}/games")
    @DELETE
    public void cleanUserGames(@PathParam("userId") Long userId) {
        this.gameUserDataManager.cleanUser(userId);
    }

    @Path("/{userId}")
    @DELETE
    public void deleteUser(@PathParam("userId") Long userId) {
        try {
//			List<User> users = getUsers(em, userName);
            User user = getUserById(userId);

            if (null != user) {
                em.getTransaction().begin();
                em.remove(user);
                em.getTransaction().commit();
            } else {
                LOGGER.debug("delete user {} failed: not found", userId);
                throw new WebApplicationException(Status.NOT_FOUND);
            }
            this.gameUserDataManager.destroyUser(userId);

            LOGGER.debug("delete user {} successful", userId);
        } catch (PersistenceException e) {
            LOGGER.debug("delete user failed: PersistenceException", e);
//			throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    /*private List<User> getUsers(EntityManager em, String userName) {
         // CriteriaQuery<User> criteriaQuery = createSimpleUserCriteriaQuery(em,
         // userName);
         // return em.createQuery(criteriaQuery).setParameter("name",
         // userName).getResultList();
         return em.createQuery("select g from User g where g.name = :name")
                 .setParameter("name", userName).getResultList();
     }*/

    private User getUserById(Long userId) throws PersistenceException {
        return (User) em.createQuery("select u from User u where u.id = :id")
                .setParameter("id", userId).getSingleResult();
//        CriteriaQuery<User> criteriaQuery = createSimpleUserCriteriaQuery(em,
//                userName);
//                return em.createQuery(criteriaQuery).setParameter("name",
//                userName).getSingleResult();
    }
    
    private User getUserByUrlId(String urlId) throws PersistenceException {
        return (User) em.createQuery("select u from User u where u.urlId = :urlId")
                .setParameter("urlId", urlId).getSingleResult();
    }

	@Override
    protected void finalize() throws Throwable {
	    close();
	    super.finalize();
    }

//    private CriteriaQuery<User> createSimpleUserCriteriaQuery(EntityManager em,
//                                                              String userName) {
//        // List<User> users = em.createQuery(
//        // "select g from User g where g.name = :name")
//        // .setParameter("name", userName).getResultList();
//
//        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<User> criteriaQuery = queryBuilder
//                .createQuery(User.class);
//
//        Root<User> root = criteriaQuery.from(User.class);
//
//        criteriaQuery.select(root).where(
//                queryBuilder.equal(root.get("name"), userName));
//        return criteriaQuery;
//    }
    
}
