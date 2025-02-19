package server.api.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import server.api.domain.user.User;
import server.api.mapper.user.UserMapper;
import server.api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.framework.custom.CustomMap;

import java.util.Optional;

/**
 * @project: amo
 * @description: ✅ mypage
 * @title: UserController
 *
 * @ROLE: ROLE_USER
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/my_page")
    public String MyPage() {
        return "/user/my_page"; // ✅ mypage
    }

    // 여기에서 findUsername 을 해서 username 받고, json 형식으로 춣력했으면 한다.
    @PostMapping("/select/username")
    @ResponseBody
    public ResponseEntity<?> findUsername(@RequestBody FindUsernameRequest request) throws Exception {

        CustomMap user = userMapper.findByUsername(request.getUsername());

        return null;
    }

    // ✅ 요청 DTO
    public static class FindUsernameRequest {
        private String username;

        public String getUsername() {
            return username;
        }
    }

    // ✅ 응답 DTO
    public static class FindUsernameResponse {
        private final String username;
        private final String message;

        public FindUsernameResponse(String username, String message) {
            this.username = username;
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }
    }
}

