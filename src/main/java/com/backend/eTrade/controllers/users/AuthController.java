package com.backend.eTrade.controllers.users;

import com.backend.eTrade.services.cart.CartService;
import com.backend.eTrade.services.user.dtos.UserDTO;
import com.backend.eTrade.repositories.users.UserRepository;
import com.backend.eTrade.security.jwt.JwtUtils;
import com.backend.eTrade.security.request.LoginRequest;
import com.backend.eTrade.security.request.SignupRequest;
import com.backend.eTrade.security.request.UpdateUserInfoRequest;
import com.backend.eTrade.security.response.LoginResponse;
import com.backend.eTrade.security.response.MessageResponse;
import com.backend.eTrade.security.response.UserInfoResponse;
import com.backend.eTrade.security.service.UserDetailsImpl;
import com.backend.eTrade.services.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CartService cartService;

    @PostMapping("/public/sign-in")
    private ResponseEntity<?> authenticateUser(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "Cart-Identifier", required = false) String cartIdentifier
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (cartIdentifier != null) {
            cartService.updateCartAfterUserLogIn(cartIdentifier, userDetails.getEmail());
        }

        LoginResponse loginResponse = new LoginResponse(jwtToken, userDetails.getFirstName(), roles);
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/public/signup")
    private ResponseEntity<?> register(
            @Valid @RequestBody SignupRequest signupRequest
    ) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        userService.createAUser(signupRequest);

        return new ResponseEntity<>
                (new MessageResponse("User registered successfully! We have sent a verification link to your email. Please verify to continue."),
                        HttpStatus.CREATED);
    }

    @GetMapping("/public/verify")
    private ResponseEntity<?> verifyEmail(
            @RequestParam @Email @NotBlank String email
    ) {
        userService.getAnotherVerifyToken(email);
        return ResponseEntity.ok("If the email address matches an account in our system, we will send you a verification email.");
    }

    @GetMapping("/getMe")
    private ResponseEntity<UserInfoResponse> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDTO userDTO = userService.getMe(userDetails.getEmail());
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getRole(),
                userDTO.isVerified(),
                userDTO.isBlocked()
        );

        return ResponseEntity.ok().body(userInfoResponse);
    }

    @PutMapping("/update-password")
    private ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String password
    ) {
        userService.updatePassword(userDetails.getEmail(), password);
        return ResponseEntity.ok().body("Password Successfully Updated");
    }

    @PostMapping("/public/verify-user")
    private ResponseEntity<?> verifyUserEmail(
            @RequestParam String token
    ) {
        userService.verifyUser(token);
        return ResponseEntity.ok().body("User verified successfully");
    }

    @PutMapping("/update-user")
    private ResponseEntity<LoginResponse> updateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UpdateUserInfoRequest updateUserInfoRequest
    ) {
        userService.updateUserInfo(userDetails.getEmail(), updateUserInfoRequest.getEmail(),
                updateUserInfoRequest.getFirstName(), updateUserInfoRequest.getLastName());

        userDetails.setEmail(updateUserInfoRequest.getEmail());
        userDetails.setFirstName(updateUserInfoRequest.getFirstName());
        userDetails.setLastName(updateUserInfoRequest.getLastName());
        String token = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        LoginResponse response = new LoginResponse(
                token,
                updateUserInfoRequest.getFirstName(),
                roles
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam @Email @NotBlank String email
    ) {
        userService.getPasswordResetToken(email);
        return ResponseEntity.ok().body("If the email address matches an account in our system, we will send you a password reset email.");
    }

    @PostMapping("/public/password-reset")
    public ResponseEntity<?> passwordReset(
            @RequestParam String token,
            @RequestParam String password
    ) {
        userService.resetPassword(token, password);
        return ResponseEntity.ok().body("Password Reset Successfully");
    }

    @GetMapping("/admin/getAllUser")
    private ResponseEntity<Page<UserDTO>> getAllUser(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNumber
    ) {
        Page<UserDTO> users = userService.getAllUsers(pageNumber, pageSize);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/admin/update-user-role")
    private ResponseEntity<?> updateUserRole(
            @RequestParam String email,
            @RequestParam String role
    ) {
        userService.updateUserRole(email, role);
        return ResponseEntity.ok().body("User Role Updated Successfully");
    }

    @PostMapping("/admin/toggle-block-user")
    private ResponseEntity<?> toggleBlockUser(
            @RequestParam String email,
            @RequestParam boolean block
    ) {
        userService.toggleBlockUser(email, block);
        String blockText = block ? "blocked" : "unblocked";
        return ResponseEntity.ok().body("User " + blockText + " successfully");
    }

    @GetMapping("/admin/get-user-by-email")
    private ResponseEntity<UserDTO> getUserByEmail(
            String email
    ) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(user);
    }
}
