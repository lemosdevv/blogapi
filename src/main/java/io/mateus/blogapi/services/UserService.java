package io.mateus.blogapi.services;

import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.UserIdentityAvailability;
import io.mateus.blogapi.payload.UserProfile;
import io.mateus.blogapi.payload.UserSummary;
import io.mateus.blogapi.payload.request.InfoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface UserService {

    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfile getUserProfile(String username);

    User addUser(User user);

    boolean existsByRole(RoleName roleName);

    User updateUser(User newUser, String username, UserPrincipal currentUser);

    ApiResponse deleteUser(String username, UserPrincipal currentUser);

    ApiResponse giveAdmin(String Username);

    ApiResponse removeAdmin(String Username);

    UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);
}
