package cz.metlicka.server.mapper;

import cz.metlicka.server.domain.User;
import cz.metlicka.server.model.Address;
import cz.metlicka.server.model.UserCreateRequest;
import cz.metlicka.server.model.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserResponse map(User user);

    @Mapping(target = "id", ignore = true)
    User map(UserCreateRequest user);

    List<UserResponse> mapToUserResponse(List<User> users);

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "user" })
    Address map(cz.metlicka.server.domain.Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    cz.metlicka.server.domain.Address map(Address address);

}
