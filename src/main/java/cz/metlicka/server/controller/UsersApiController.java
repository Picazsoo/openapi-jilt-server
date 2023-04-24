package cz.metlicka.server.controller;

import cz.metlicka.server.api.UsersApi;
import cz.metlicka.server.domain.User;
import cz.metlicka.server.mapper.UserMapper;
import cz.metlicka.server.model.UserCreateRequest;
import cz.metlicka.server.model.UserResponse;
import cz.metlicka.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.userServiceContract.base-path:}")
public class UsersApiController implements UsersApi {

    private final NativeWebRequest request;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> createUser(UserCreateRequest userCreateRequest) {
        User user = userRepository.save(userMapper.map(userCreateRequest));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(
                userMapper.mapToUserResponse(
                        userRepository.findAll()
                )
        );
    }

}
