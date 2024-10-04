package org.tyaa.training.current.server.test.unit.controllers.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.controllers.rest.AuthController;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.services.interfaces.IAuthService;
import org.tyaa.training.current.server.test.dataproviders.RoleProvider;
import org.tyaa.training.current.server.test.dataproviders.UserProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.tyaa.training.current.server.test.dataproviders.UserProvider.getUserModel;

/**
 * Модульный тест контроллера AuthController
 * */
@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    @Mock
    private IAuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @Test
    void getRolesTest() {
        final List<RoleModel> roles = RoleProvider.getAvailableRoleModels();
        when(authService.getRoles())
                .thenReturn(
                        ResponseModel.builder()
                                .status(ResponseModel.SUCCESS_STATUS)
                                .data(roles)
                                .build()
                );
        ResponseEntity<ResponseModel> response = authController.getRoles();
        ResponseModel responseBody = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseModel.SUCCESS_STATUS, responseBody.getStatus());
        assertEquals(roles, responseBody.getData());
    }

    /**
     * Тестовый случай, вызываемый столько раз, сколько строк тестовых данных содержится в указанном csv-файле,
     * и получающих аргументы из текущей строки файла
     * @param givenRoleName Имя роли, которая будет создана
     * @param responseModelMessage Сообщение, которое должно быть возвращено
     * @param responseEntityStatus Название статуса ответа, который должен быть возвращен
     * @param responseModelStatus Статус в теле ответа, который должен быть возвращен
     * */
    @ParameterizedTest
    @CsvFileSource(resources = "/unit/controllers/rest/AuthControllerTest/createRoleTest.csv")
    void createRoleTest(String givenRoleName, String responseModelMessage, String responseEntityStatus, String responseModelStatus) {
        final RoleModel roleModel = new RoleModel();
        roleModel.name = givenRoleName;
        when(authService.createRole(roleModel))
                .thenReturn(
                        ResponseModel.builder()
                                .status(responseModelStatus)
                                .message(responseModelMessage)
                                .build()
                );
        ResponseEntity<ResponseModel> response = authController.createRole(roleModel);
        ResponseModel responseBody = response.getBody();
        assertEquals(HttpStatus.valueOf(responseEntityStatus), response.getStatusCode());
        assertEquals(responseBody.getStatus(), responseModelStatus);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/unit/controllers/rest/AuthControllerTest/createUserTest.csv")
    void createUserTest(String givenUserName, String responseModelMessage, String responseEntityStatus, String responseModelStatus) {
        final UserModel userModel = new UserModel();
        userModel.name = givenUserName;
        userModel.setPassword("test");
        when(authService.createUser(userModel)).thenReturn(
                ResponseModel.builder()
                        .status(responseModelStatus)
                        .message(responseModelMessage)
                        .build()
        );
        ResponseEntity<ResponseModel> response = authController.createUser(userModel);
        ResponseModel responseBody = response.getBody();
        assertEquals(HttpStatus.valueOf(responseEntityStatus), response.getStatusCode());
        assertEquals(responseBody.getStatus(), responseModelStatus);
    }

    @Test
    void getUsersTest() {
        final List<UserModel> userModels = UserProvider.getUserModels();
        when(authService.getUsers())
                .thenReturn(
                        ResponseModel.builder()
                                .status(ResponseModel.SUCCESS_STATUS)
                                .data(userModels)
                                .build()
                );
        ResponseModel responseModel = authController.getUsers().getBody();
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
        assertEquals(userModels, responseModel.getData());
    }

    @Test
    void getUsersByRoleTest() {
        final List<UserModel> userModels = UserProvider.getUserModels();
        when(authService.getRoleUsers(1L))
                .thenReturn(ResponseModel.builder().status(ResponseModel.SUCCESS_STATUS).data(userModels).build());
        ResponseModel responseModel = authController.getUsersByRole(1L).getBody();
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        assertEquals(userModels, responseModel.getData());
    }

    @Test
    void deleteUserTest() {
        when(authService.deleteUser(any()))
                .thenReturn(ResponseModel.builder().status(ResponseModel.SUCCESS_STATUS).build());
        ResponseEntity<ResponseModel> response = authController.deleteUser(1L);
        ResponseModel responseModel = response.getBody();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
        assertEquals(null, responseModel.getData());
    }

    @Test
    void changeUserRoleTest() {
        when(authService.changeUserRole(any(), any()))
                .thenReturn(ResponseModel.builder().status(ResponseModel.SUCCESS_STATUS).build());
        ResponseModel responseModel = authController.changeUserRole(1L, 1L).getBody();
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/unit/controllers/rest/AuthControllerTest/checkUserTest.csv")
    void checkUserTest(boolean isClientAuthenticated, String responseEntityStatus) {
        UserModel userModel = null;
        when(authService.check(any()))
                .thenReturn(
                        ResponseModel.builder()
                                .status(ResponseModel.SUCCESS_STATUS)
                                .data(isClientAuthenticated ? userModel = getUserModel() : null)
                                .build()
                );
        ResponseEntity<ResponseModel> response = authController.checkUser(authentication);
        ResponseModel responseBody = response.getBody();
        assertEquals(HttpStatus.valueOf(responseEntityStatus), response.getStatusCode());
        assertEquals(responseBody.getStatus(), ResponseModel.SUCCESS_STATUS);
        assertEquals(userModel, responseBody.getData());
    }

    @Test
    void signedOutTest() {
        when(authService.onSignOut()).thenReturn(
                ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Signed out")
                .build()
        );
        ResponseEntity<ResponseModel> response = authController.signedOut();
        ResponseModel responseModel = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void onErrorTest() {
        when(authService.onError()).thenReturn(
                ResponseModel.builder()
                        .status(ResponseModel.FAIL_STATUS)
                        .message("Auth error")
                        .build()
        );
        ResponseEntity<ResponseModel> response = authController.onError();
        ResponseModel responseModel = response.getBody();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(responseModel.getStatus(), ResponseModel.FAIL_STATUS);
    }
}