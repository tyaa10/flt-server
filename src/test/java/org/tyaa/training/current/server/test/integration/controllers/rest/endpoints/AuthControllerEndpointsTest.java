package org.tyaa.training.current.server.test.integration.controllers.rest.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.test.dataproviders.RoleProvider;
import org.tyaa.training.current.server.test.dataproviders.UserProvider;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private static class GetUsersResponseModel extends ResponseModel<List<UserModel>> {}

    @Nested
    class GetRolesTest {

        private void getRolesTest(ResultMatcher expectedStatus, ResultMatcher expectedContent) throws Exception {
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/admin/roles")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent);
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetRoles_thenOk() throws Exception {
            getRolesTest(status().isOk(), content().string(containsString("Roles successfully retrieved")));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenGetRoles_thenForbidden() throws Exception {
            getRolesTest(status().isForbidden(), content().string(containsString("")));
        }

        @Test
        public void givenNoUser_whenGetRoles_thenUnauthorized() throws Exception {
            getRolesTest(status().isUnauthorized(), content().string(containsString("")));
        }
    }

    @Nested
    class CreateRoleTest {

        private final String TEST_ROLE = "TEST_ROLE";

        private void createRoleTest(ResultMatcher expectedStatus, ResultMatcher expectedContent) throws Exception {
            final String roleJson = format("{\"name\":\"%s\"}", TEST_ROLE);
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/admin/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent);
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenCreateRole_thenOk() throws Exception {
            createRoleTest(status().isCreated(), content().string(containsString(format("Role %s created", TEST_ROLE))));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenCreateRole_thenForbidden() throws Exception {
            createRoleTest(status().isForbidden(), content().string(containsString("")));
        }

        @Test
        public void givenNoUser_whenCreateRole_thenUnauthorized() throws Exception {
            createRoleTest(status().isUnauthorized(), content().string(containsString("")));
        }
    }

    @Nested
    class GetUsersByRoleTest {

        private void getUsersByRoleTest(RoleModel roleModel, ResultMatcher expectedStatus, ResultMatcher expectedContent) throws Exception {
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.get(format("/api/auth/admin/roles/%d/users", roleModel.getId()))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent)
                    .andDo(result -> {
                        if (result.getResponse().getContentType() != null && expectedStatus.equals(status().isOk())) {
                            System.out.println("getContentType() = " + result.getResponse().getContentType());
                            final GetUsersResponseModel responseModel =
                                objectMapper.readValue(result.getResponse().getContentAsString(), GetUsersResponseModel.class);
                            if (responseModel != null) {
                                assertNotNull(responseModel.getData());
                                assertFalse(responseModel.getData().isEmpty());
                                responseModel.getData().forEach(firstRoleUserModel -> {
                                    assertEquals(firstRoleUserModel.getRoleId(), roleModel.getId());
                                    assertEquals(firstRoleUserModel.getRoleName(), roleModel.getName());
                                });
                            }
                        }
                    });
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithCorrectId_thenOk() throws Exception {
            final RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(1L);
            getUsersByRoleTest(roleModel, status().isOk(), content().string(containsString(format("List of %s Role Users Retrieved Successfully", roleModel.getName()))));
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithWrongId_thenNotFound() throws Exception {
            final RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(Long.MAX_VALUE);
            getUsersByRoleTest(roleModel, status().isNotFound(), content().string(containsString(format("No Users: Role #%d Not Found", roleModel.getId()))));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithCorrectId_thenForbidden() throws Exception {
            final RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(1L);
            getUsersByRoleTest(roleModel, status().isForbidden(), content().string(""));
        }

        @Test
        public void givenAdminUserAuthenticated_whenGetUsersByRoleWithCorrectId_thenUnauthorized() throws Exception {
            final RoleModel roleModel = RoleProvider.getAvailableRoleModel();
            roleModel.setId(1L);
            getUsersByRoleTest(roleModel, status().isUnauthorized(), content().string(""));
        }
    }

    @Nested
    class CreateUserTest {

        @ParameterizedTest
        @CsvFileSource(resources = "/integration/controllers/rest/endpoints/AuthControllerTest/createUserTest.csv")
        public void createUserTest(String name, String password, int expectedStatus, String expectedResponseMessage) throws Exception {
            String userModelJson = format("{\"name\":\"%s\",\"password\":\"%s\",\"roleId\":%s}", name, password, 1L);
            System.out.printf(
                    "UserModel with name: %s, password: %s, userModel: %s, expectedStatus: %s, expectedResponseMessage: %s\n",
                    name,
                    password,
                    userModelJson,
                    expectedStatus,
                    format(expectedResponseMessage, name)
            );
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(userModelJson))
                    .andExpect(status().is(expectedStatus))
                    .andExpect(content().string(containsString(format(expectedResponseMessage, name))));
        }
    }

    @Nested
    class DeleteUserTest {

        private void deleteUserTest(UserModel userModel, Long userId, ResultMatcher expectedStatus, ResultMatcher expectedContent) throws Exception {
            String userModelJson =
                    format(
                            "{\"name\":\"%s\",\"password\":\"%s\",\"roleId\":%s}",
                            userModel.getName(), userModel.getPassword(), userModel.getRoleId()
                    );
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(userModelJson));
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.delete(format("/api/auth/users/%s", userId))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectedContent)
                    .andExpect(expectedStatus);
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenDeleteUserWithCorrectId_thenNoContent() throws Exception {
            final UserModel userModel = UserProvider.getUserModel();
            deleteUserTest(userModel, userModel.getId(), status().isNoContent(), content().string(containsString(format("User #%s Deleted", userModel.getId()))));
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenDeleteUserWithCorrectId_thenForbidden() throws Exception {
            final UserModel userModel = UserProvider.getUserModel();
            deleteUserTest(userModel, userModel.getId(), status().isForbidden(), content().string(containsString("")));
        }


        @Test
        public void givenNoUser_whenDeleteUserWithCorrectId_thenUnauthorized() throws Exception {
            final UserModel userModel = UserProvider.getUserModel();
            deleteUserTest(userModel, userModel.getId(), status().isUnauthorized(), content().string(containsString("")));
        }
    }

    @Nested
    class ChangeUserRoleTestCases {

        private void changeUserRoleTest(
                Long newRoleId,
                ResultMatcher expectedStatus,
                ResultMatcher expectedContent
        ) throws Exception {
            AuthControllerEndpointsTest.this.mockMvc
                    .perform(
                            MockMvcRequestBuilders.patch(format("/api/auth/admin/users/2/change-role/%s", newRoleId))
                                .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(expectedStatus)
                    .andExpect(expectedContent);
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void givenAdminUserAuthenticated_whenChangeUserRole_thenOk() throws Exception {
            changeUserRoleTest(
                    3L,
                    status().isOk(),
                    content().string(containsString("user role successfully changed to"))
            );
            changeUserRoleTest(
                    2L,
                    status().isOk(),
                    content().string(containsString("user role successfully changed to"))
            );
        }

        @Test
        @WithMockUser(username = "one", roles = { "CUSTOMER" })
        public void givenCustomerUserAuthenticated_whenChangeUserRole_thenForbidden() throws Exception {
            changeUserRoleTest(
                    3L,
                    status().isForbidden(),
                    content().string(containsString(""))
            );
            changeUserRoleTest(
                    2L,
                    status().isForbidden(),
                    content().string(containsString(""))
            );
        }

        @Test
        public void givenNoUser_whenChangeUserRole_thenUnauthorized() throws Exception {
            changeUserRoleTest(
                    3L,
                    status().isUnauthorized(),
                    content().string(containsString(""))
            );
            changeUserRoleTest(
                    2L,
                    status().isUnauthorized(),
                    content().string(containsString(""))
            );
        }
    }

    @Nested
    class CheckUserTest {

        private void checkUserTest(ResultMatcher expectedStatus, ResultMatcher expectedContent) throws Exception {
            AuthControllerEndpointsTest.this.mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/users/check")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent);
        }

        @Test
        public void givenNoUser_whenCheck_thenUnauthorized() throws Exception {
            checkUserTest(status().isUnauthorized(), content().string(containsString("User is a Guest")));
        }

        @Test
        @WithMockUser(username = "testuser", password = "testpassword")
        public void givenTestuserAuthenticated_whenCheck_thenOk() throws Exception {
            checkUserTest(status().isOk(), content().string(containsString("User testuser Signed In")));
        }
    }
}
