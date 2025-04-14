package io.mateus.blogapi.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.mateus.blogapi.exception.AccessDeniedException;
import io.mateus.blogapi.exception.AppException;
import io.mateus.blogapi.exception.BadRequestException;
import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.exception.UnauthorizedException;
import io.mateus.blogapi.model.role.Role;
import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.model.user.Address;
import io.mateus.blogapi.model.user.Company;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.UserIdentityAvailability;
import io.mateus.blogapi.payload.UserProfile;
import io.mateus.blogapi.payload.UserSummary;
import io.mateus.blogapi.payload.request.InfoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.repositories.PostRepository;
import io.mateus.blogapi.repositories.RoleRepository;
import io.mateus.blogapi.repositories.UserRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para obter informações do usuário atual
    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
                currentUser.getLastName());
    }

    // Método para verificar a disponibilidade do nome de usuário
    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    // Método para verificar a disponibilidade do email
    @Override
    public UserIdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    // Método para obter o perfil do usuário(informações publicas)
    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.getUserByName(username);

        Long postCount = postRepository.countByCreatedBy(user.getId());

        return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(),
                user.getWebsite(), user.getCompany(), postCount);
    }

    // Método para adicionar um novo usuário
    @Override
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
            throw new BadRequestException(apiResponse);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
            throw new BadRequestException(apiResponse);
        }

        List<Role> roles = new ArrayList<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                roles.add(roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new AppException("Role not set.")));
            }
        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User role not set.")));
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public boolean existsByRole(RoleName roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException("Role not found."));
        return userRepository.existsByRolesContaining(role);
    }

    // Método para atualizar um usuário
    @Override
    public User updateUser(User newUser, String username, UserPrincipal currentUser) {
        User user = userRepository.getUserByName(username);

        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(newUser.getPassword());
            user.setAddress(newUser.getAddress());
            user.setPhone(newUser.getPhone());
            user.setWebsite(newUser.getWebsite());
            user.setCompany(newUser.getCompany());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
                "You don't have permission to update profile of: " + username);
        throw new UnauthorizedException(apiResponse);
    }

    // Método para deletar um usuário
    @Override
    public ApiResponse deleteUser(String username, UserPrincipal currentUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", username));

        if (!user.getId().equals(currentUser.getId())
                || !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,
                    "You don't have permission to delete profile of: " + username);
            throw new UnauthorizedException(apiResponse);
        }

        userRepository.delete(user);

        return new ApiResponse(Boolean.TRUE, "User deleted successfully");
    }

    // Método para dar permissão de admin a um usuário
    @Override
    public ApiResponse giveAdmin(String Username) {
        User user = userRepository.getUserByName(Username);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("User role not set.")));
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set.")));
        user.setRoles(roles);

        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You gave ADMIN role to user: " + Username);
    }

    // Método para remover a role de ADMIN de um usuário
    @Override
    public ApiResponse removeAdmin(String Username) {
        User user = userRepository.getUserByName(Username);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User role not set.")));
        user.setRoles(roles);

        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "You removed ADMIN role from user: " + Username);
    }

    // Método para atualizar ou definir informações do usuário
    @Override
    public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        Address address = new Address(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(),
                infoRequest.getZipcode());
        Company company = new Company(infoRequest.getCompanyName(), infoRequest.getCatchPhrase(), infoRequest.getBs());

        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setAddress(address);
            user.setCompany(company);
            user.setWebsite(infoRequest.getWebsite());
            user.setPhone(infoRequest.getPhone());

            User updatedUser = userRepository.save(user);

            Long postCount = postRepository.countByCreatedBy(updatedUser.getId());

            return new UserProfile(updatedUser.getId(), updatedUser.getUsername(),
                    updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getCreatedAt(),
                    updatedUser.getEmail(), updatedUser.getAddress(), updatedUser.getPhone(), updatedUser.getWebsite(),
                    updatedUser.getCompany(), postCount);
        }

        // Se o usuário não tiver permissão, lança uma exceção AccessDeniedException
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile",
                HttpStatus.FORBIDDEN);
        throw new AccessDeniedException(apiResponse);
    }
}
