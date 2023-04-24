package cz.metlicka.server.controller;

import cz.metlicka.server.api.UsersApi;
import cz.metlicka.server.domain.Address;
import cz.metlicka.server.domain.User;
import cz.metlicka.server.model.AddressBuilder;
import cz.metlicka.server.model.UserCreateRequest;
import cz.metlicka.server.model.UserResponse;
import cz.metlicka.server.model.UserResponseBuilder;
import cz.metlicka.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> createUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());

        Address address = new Address();
        address.setStreet(userCreateRequest.getAddress().getStreet());
        address.setCity(userCreateRequest.getAddress().getCity());
        address.setState(userCreateRequest.getAddress().getState());
        address.setZip(userCreateRequest.getAddress().getZip());
        user.setAddress(address);

        user = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userRepository.findAll().stream().map(userToUserResponse()).toList();
        return ResponseEntity.ok(users);
    }

    private static Function<User, UserResponse> userToUserResponse() {
        return user -> {
            cz.metlicka.server.model.Address address = AddressBuilder
                    .address()
                    .street(user.getAddress().getStreet())
                    .city(user.getAddress().getCity())
                    .state(user.getAddress().getState())
                    .zip(user.getAddress().getZip())
                    .build();

            return UserResponseBuilder
                    .userResponse()
                    .id(user.getId())
                    .address(address)
                    .firstName(user.getFirstName())
                    .middleName(user.getMiddleName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .build();
        };
    }

}
