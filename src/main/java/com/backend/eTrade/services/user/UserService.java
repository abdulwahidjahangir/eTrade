package com.backend.eTrade.services.user;

import com.backend.eTrade.security.request.SignupRequest;
import com.backend.eTrade.services.user.dtos.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    void createAUser(SignupRequest signupRequest);

    void getAnotherVerifyToken(String email);

    UserDTO getMe(String email);

    void updatePassword(String email, String password);

    void verifyUser(String token);

    void updateUserInfo(String email, String newEmail, String firstName, String lastName);

    void getPasswordResetToken(String email);

    void resetPassword(String token, String password);

    Page<UserDTO> getAllUsers(int page, int size);

    void updateUserRole(String email, String Role);

    void toggleBlockUser(String email, boolean toggleBlock);

    UserDTO getUserByEmail(String email);
}
