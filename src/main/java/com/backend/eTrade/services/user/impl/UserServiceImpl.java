package com.backend.eTrade.services.user.impl;

import com.backend.eTrade.error.user.UserException;
import com.backend.eTrade.services.user.dtos.UserDTO;
import com.backend.eTrade.models.users.*;
import com.backend.eTrade.repositories.users.PasswordResetTokenRepository;
import com.backend.eTrade.repositories.users.RoleRepository;
import com.backend.eTrade.repositories.users.UserRepository;
import com.backend.eTrade.repositories.users.UserVerifyTokenRepository;
import com.backend.eTrade.security.request.SignupRequest;
import com.backend.eTrade.services.user.UserService;
import com.backend.eTrade.utils.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserVerifyTokenRepository userVerifyTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService,
                           UserVerifyTokenRepository userVerifyTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userVerifyTokenRepository = userVerifyTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void createAUser(SignupRequest signupRequest) {

        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Role role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new UserException("Error: Role is not found", HttpStatus.INTERNAL_SERVER_ERROR));

        user.setRole(role);
        User saveduser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        UserVerifyToken userVerifyToken = new UserVerifyToken(
                token, expiryDate, saveduser
        );
        userVerifyTokenRepository.save(userVerifyToken);

        String url = frontendUrl + "/verify-account?token=" + token;

        emailService.sendUserVerifyEmail(signupRequest.getEmail(), url);
    }

    @Override
    public void getAnotherVerifyToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Please enter a valid email address", HttpStatus.CONFLICT));

        if (user.isVerified()) {
            throw new UserException("User Already Verified", HttpStatus.BAD_REQUEST);
        }

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        UserVerifyToken userVerifyToken = new UserVerifyToken(
                token,
                expiryDate,
                user
        );
        userVerifyTokenRepository.save(userVerifyToken);

        String url = frontendUrl + "/verify-account?token=" + token;

        emailService.sendUserVerifyEmail(email, url);
    }

    @Override
    public UserDTO getMe(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return convertUserToUserDTO(user);
    }

    @Override
    public void updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void verifyUser(String token) {
        UserVerifyToken userVerifyToken = userVerifyTokenRepository.findByToken(token)
                .orElseThrow(() -> new UserException("Invalid Verify Token", HttpStatus.BAD_REQUEST));

        if (userVerifyToken.isUsed()) {
            throw new UserException("User verified token has already used", HttpStatus.BAD_REQUEST);
        }

        if (userVerifyToken.getExpiryDate().isBefore(Instant.now())) {
            throw new UserException("User verified token has expired", HttpStatus.BAD_REQUEST);
        }

        User user = userVerifyToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        userVerifyToken.setUsed(true);
        userVerifyTokenRepository.save(userVerifyToken);
    }

    @Override
    public void updateUserInfo(String email, String newEmail, String firstName, String lastName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void getPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(3, ChronoUnit.HOURS);
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                expiryDate,
                user
        );

        passwordResetTokenRepository.save(passwordResetToken);

        String url = frontendUrl + "/password-reset/token?token=" + token;

        emailService.sendPasswordResetEmail(email, url);
    }

    @Override
    public void resetPassword(String token, String password) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new UserException("Invalid password reset token", HttpStatus.BAD_REQUEST));

        if (passwordResetToken.isUsed()) {
            throw new UserException("Password reset token has already used", HttpStatus.BAD_REQUEST);
        }

        if (passwordResetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new UserException("Password reset token has expired", HttpStatus.BAD_REQUEST);
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        Page<UserDTO> userDTOPage = usersPage.map(this::convertUserToUserDTO);

        return userDTOPage;
    }

    @Override
    public void updateUserRole(String email, String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        AppRole appRole;
        try {
            appRole = AppRole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException("Invalid role name: " + roleName, HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new UserException("Role not found: " + roleName, HttpStatus.NOT_FOUND));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void toggleBlockUser(String email, boolean toggleBlock) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        user.setBlocked(toggleBlock);
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        return convertUserToUserDTO(user);
    }


    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().getRoleName().name());
        userDTO.setVerified(user.isVerified());
        userDTO.setBlocked(user.isBlocked());

        return userDTO;
    }
}
