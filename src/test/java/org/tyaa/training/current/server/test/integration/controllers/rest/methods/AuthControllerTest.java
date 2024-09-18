package org.tyaa.training.current.server.test.integration.controllers.rest.methods;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.controllers.rest.AuthController;
import org.tyaa.training.current.server.test.dataproviders.RoleProvider;
import org.tyaa.training.current.server.test.dataproviders.UserProvider;

import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест контроллера AuthController на уровне методов
 */
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Nested
    class GetRolesTestCases {

        private void getRolesTest(String expectedResponseMessage) {
            ResponseModel responseModel = authController.getRoles().getBody();
            assertNotNull(responseModel);
            assertNotNull(responseModel.getData());
            assertEquals(expectedResponseMessage, responseModel.getMessage());
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetRoles_thenSuccess() {
            getRolesTest("Roles successfully retrieved");
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenGetRoles_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getRolesTest(""));
        }

        @Test
        @WithMockUser(username = "cm_user_1", roles = { "CONTENT_MANAGER" })
        public void givenContentManagerUserAuthenticated_whenGetRoles_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getRolesTest(""));
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenGetRoles_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getRolesTest(""));
        }

        @Test
        public void givenNoUser_whenGetRoles_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> getRolesTest(""));
        }
    }

    @Nested
    class CreateRoleTestCases {

        private void createRoleTest(RoleModel roleModel, String expectedResponseMessage) {
            ResponseModel responseModel = authController.createRole(roleModel).getBody();
            assertNotNull(responseModel);
            assertNotNull(responseModel.getMessage());
            assertNull(responseModel.getData());
            System.out.printf("CreateRoleTestCases - roleName: %s, expectedResponseMessage: %s, actualResponseMessage: %s", roleModel.getName(), expectedResponseMessage, responseModel.getMessage());
            assertTrue(responseModel.getMessage().contains(format(expectedResponseMessage, roleModel.getName())));
        }

        private void createRoleTest(String expectedResponseMessage) {
            RoleModel roleModel = RoleProvider.getRoleModel();
            createRoleTest(roleModel, expectedResponseMessage);
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenCreateRole_thenSuccess() {
            createRoleTest("Role %s created");
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenCreateRoleTwice_thenFailure() {
            RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setName("ROLE_TEST");
            // создание роли
            createRoleTest(roleModel, "Role %s created");
            // попытка повторного создания роли с тем же именем
            createRoleTest(roleModel, "Unique index or primary key violation");
        }

        @Test
        @WithMockUser(username = "non-admin", roles = { "NON_ADMIN" })
        public void givenNonAdminUserAuthenticated_whenCreateRole_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> createRoleTest(""));
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenCreateRole_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> createRoleTest(""));
        }

        @Test
        public void givenNoUser_whenCreateRole_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> createRoleTest(""));
        }
    }

    @Nested
    class GetUsersTestCases {

        public void getUsersTest( String expectedResponseMessage) {
            ResponseModel responseModel = authController.getUsers().getBody();
            assertNotNull(responseModel);
            assertNotNull(responseModel.getData());
            assertTrue(!((List<UserModel>) responseModel.getData()).isEmpty());
            assertEquals(expectedResponseMessage, responseModel.getMessage());
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetUsers_thenSuccess() {
            getUsersTest("Users retrieved successfully");
        }

        @Test
        @WithMockUser(username = "non-admin", roles = { "NON_ADMIN" })
        public void givenNonAdminUserAuthenticated_whenGetUsers_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersTest(""));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenGetUsers_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersTest(""));
        }

        @Test
        @WithMockUser(username = "cm_user_1", roles = { "CONTENT_MANAGER" })
        public void givenContentManagerUserAuthenticated_whenGetUsers_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersTest(""));
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenGetUsers_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersTest(""));
        }

        @Test
        public void givenNoUser_whenGetUsers_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> getUsersTest(""));
        }
    }

    @Nested
    class GetUsersByRoleTestCases {

        public void getUsersByRoleTest(RoleModel roleModel, String expectedResponseMessage) {
            ResponseModel responseModel = authController.getUsersByRole(roleModel.getId()).getBody();
            assertNotNull(responseModel);
            if (!expectedResponseMessage.endsWith("Not Found")) {
                assertNotNull(responseModel.getData());
                assertTrue(!((List<UserModel>) responseModel.getData()).isEmpty());
                ((List<UserModel>) responseModel.getData()).forEach(firstRoleUserModel -> {
                    assertEquals(firstRoleUserModel.getRoleId(), roleModel.getId());
                    assertEquals(firstRoleUserModel.getRoleName(), roleModel.getName());
                });
            }
            assertEquals(format(expectedResponseMessage, roleModel.getName()), responseModel.getMessage());
        }

        public void getUsersByRoleTest(String expectedResponseMessage) {
            final RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(1L);
            getUsersByRoleTest(
                    roleModel,
                    expectedResponseMessage
            );
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithCorrectId_thenSuccess() {
            getUsersByRoleTest("List of %s Role Users Retrieved Successfully");
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithWrongId_thenFailure() {
            RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(Long.MAX_VALUE);
            getUsersByRoleTest(roleModel, String.format("No Users: Role #%d Not Found", roleModel.getId()));
        }

        @Test
        @WithMockUser(username = "non-admin", roles = { "NON_ADMIN" })
        public void givenNonAdminUserAuthenticated_whenGetUsersByRoleWithCorrectId_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersByRoleTest(""));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenGetUsersByRole_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersByRoleTest(""));
        }

        @Test
        @WithMockUser(username = "cm_user_1", roles = { "CONTENT_MANAGER" })
        public void givenContentManagerUserAuthenticated_whenGetUsersByRole_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersByRoleTest(""));
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenGetUsersByRole_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> getUsersByRoleTest(""));
        }

        @Test
        public void givenNoUser_whenGetUsersByRole_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> getUsersByRoleTest(""));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/integration/controllers/rest/methods/AuthControllerTest/createUserTest.csv")
    public void createUserTest(String name, String password, String expectedResponseMessage) {
        UserModel userModel = UserModel.builder().name(name).password(password).roleId(1L).build();
        ResponseModel responseModel = authController.createUser(userModel).getBody();
        assertNotNull(responseModel);
        assertNotNull(responseModel.getMessage());
        System.out.printf(
                "UserModel with name: %s, password: %s, expectedResponseMessage: %s, actualResponseMessage: %s, result: %s\n",
                name,
                password,
                format(expectedResponseMessage, userModel.getName()),
                responseModel.getMessage(),
                responseModel.getMessage().contains(format(expectedResponseMessage, userModel.getName()))
        );
        assertTrue(responseModel.getMessage().contains(format(expectedResponseMessage, userModel.getName())));
    }

    @Nested
    class DeleteUserTestCases {

        public void deleteUserTest(Long userId, String expectedResponseMessage) {
            ResponseModel responseModel = authController.deleteUser(userId).getBody();
            assertNotNull(responseModel);
            assertEquals(format(expectedResponseMessage, userId), responseModel.getMessage());
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenDeleteUserWithCorrectId_thenSuccess() {
            final UserModel userModel = UserProvider.getUserModel();
            authController.createUser(userModel);
            Long userId = ((List<UserModel>) authController.getUsers().getBody().getData())
                    .stream().filter(u -> u.getName().equals(userModel.getName())).findFirst().orElse(null).getId();
            deleteUserTest(userId, "User #%s Deleted");
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenDeleteUserWithNullId_thenFailure() {
            deleteUserTest(null, "The given id must not be null");
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenDeleteUser_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> deleteUserTest(Long.MAX_VALUE, ""));
        }

        @Test
        @WithMockUser(username = "cm_user_1", roles = { "CONTENT_MANAGER" })
        public void givenContentManagerUserAuthenticated_whenDeleteUser_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> deleteUserTest(Long.MAX_VALUE, ""));
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenDeleteUser_thenFailure() {
            assertThrows(AccessDeniedException.class, () -> deleteUserTest(Long.MAX_VALUE, ""));
        }

        @Test
        public void givenNoUser_whenDeleteUser_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> deleteUserTest(Long.MAX_VALUE, ""));
        }
    }

    @Nested
    class ChangeUserRoleTestCases {

        public void changeUserRoleTest() {
            final RoleModel oldRoleModel =
                    ((List<RoleModel>) authController.getRoles().getBody().getData()).get(0);
            final RoleModel newRoleModel =
                    ((List<RoleModel>) authController.getRoles().getBody().getData()).get(1);
            final Long oldUserRoleId = oldRoleModel.getId();
            final Long newUserRoleId = newRoleModel.getId();
            UserModel userModel = UserProvider.getUserModel();
            userModel.setRoleId(oldUserRoleId);
            authController.createUser(userModel);
            final String userName = userModel.getName();
            userModel = ((List<UserModel>) authController.getUsers().getBody().getData()).stream()
                    .filter(u -> u.getName().equals(userName))
                    .findFirst().orElse(null);
            ResponseModel responseModel = authController.changeUserRole(userModel.getId(), newUserRoleId).getBody();
            assertNotNull(responseModel);
            assertNotNull(responseModel.getMessage());
            assertEquals(
                    format("%s user role successfully changed to %s", userModel.getName(), newRoleModel.getName()),
                    responseModel.getMessage()
            );
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenChangeUserRole_thenSuccess() {
            changeUserRoleTest();
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenChangeUserRole_thenFailure() {
            assertThrows(AccessDeniedException.class, this::changeUserRoleTest);
        }

        @Test
        @WithMockUser(username = "cm_user_1", roles = { "CONTENT_MANAGER" })
        public void givenContentManagerUserAuthenticated_whenChangeUserRole_thenFailure() {
            assertThrows(AccessDeniedException.class, this::changeUserRoleTest);
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenChangeUserRole_thenFailure() {
            assertThrows(AccessDeniedException.class, this::changeUserRoleTest);
        }

        @Test
        public void givenNoUser_whenChangeUserRole_thenFailure() {
            assertThrows(AuthenticationCredentialsNotFoundException.class, this::changeUserRoleTest);
        }
    }

    @Nested
    class CheckUserTestCases {

        public void checkUserTest(boolean authenticated, String expectedResponseMessage) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ResponseModel responseModel = authController.checkUser(authentication).getBody();
            assertNotNull(responseModel);
            if (authenticated) {
                final UserModel userModel = (UserModel) responseModel.getData();
                assertNotNull(userModel);
                assertEquals(authentication.getName(), userModel.getName());
                assertEquals(authentication.getAuthorities().iterator().next().getAuthority(), userModel.getRoleName());
                assertEquals(format(expectedResponseMessage, authentication.getName()), responseModel.getMessage());
            } else {
                assertNull(responseModel.getData());
                assertEquals(expectedResponseMessage, responseModel.getMessage());
            }
        }

        @Test
        @WithMockUser(username = "test-user", roles = { "TEST" })
        public void givenAdminUserAuthenticated_whenCheckUser_thenSuccessWithUserData() {
            checkUserTest(true, "User %s Signed In");
        }

        @Test
        @WithAnonymousUser
        public void givenAnonymousUser_whenCheckUser_thenSuccessWithUserData() {
            checkUserTest(true, "User %s Signed In");
        }

        @Test
        public void givenNoUser_whenCheckUser_thenSuccessWithoutUserData() {
            checkUserTest(false, "User is a Guest");
        }
    }

    @Test
    public void signedOutTest() {
        final ResponseEntity responseEntity = authController.signedOut();
        ResponseModel responseModel = (ResponseModel) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseModel);
        assertEquals("Signed out", responseModel.getMessage());
    }

    @Test
    public void onErrorTest() {
        final ResponseEntity responseEntity = authController.onError();
        ResponseModel responseModel = (ResponseModel) responseEntity.getBody();
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseModel);
        assertEquals("Auth error", responseModel.getMessage());
    }
}
