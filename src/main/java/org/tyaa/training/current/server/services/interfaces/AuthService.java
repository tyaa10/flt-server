package org.tyaa.training.current.server.services.interfaces;

import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.repositories.RoleRepository;

import java.util.List;

/**
* Реализация службы аутентификации, использующая РБД-репозитории
* */
@Service
public class AuthService implements IAuthService {
    private final RoleRepository roleRepository;

    public AuthService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public List<RoleModel> getRoles() {
        return roleRepository.findAll().stream()
                .map(roleEntity -> RoleModel.builder().id(roleEntity.getId()).name(roleEntity.getName()).build())
                .toList();
    }
}
