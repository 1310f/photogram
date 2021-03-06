package pl.tscript3r.photogram.services.beans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.user.UserService;
import pl.tscript3r.photogram.user.role.RoleRepository;
import pl.tscript3r.photogram.user.role.RoleService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram.Consts.SECOND_ROLE;
import static pl.tscript3r.photogram.domains.RoleTest.getAdminRole;
import static pl.tscript3r.photogram.domains.RoleTest.getDefaultRole;

@DisplayName("Role service")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    UserService userService;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @Test
    @DisplayName("Get default role")
    void getDefault() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(getDefaultRole()));
        assertNotNull(roleService.getDefault());
    }

    @Test
    @DisplayName("Get existing by firstname")
    void getByName() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(getAdminRole()));
        assertNotNull(roleService.getByName(SECOND_ROLE));
    }

    @Test
    @DisplayName("Get non existing by firstname")
    void getNonExistingByName() {
        when(roleRepository.findByName(any())).thenThrow(NotFoundPhotogramException.class);
        assertThrows(NotFoundPhotogramException.class, () -> roleService.getByName(""));
    }

}