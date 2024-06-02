// package com.projeta.user.domain.model.enuns;

// import lombok.Getter;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;

// import static com.projeta.user.domain.model.enuns.PermissionEnum.ADMIN_CREATE;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.ADMIN_DELETE;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.ADMIN_READ;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.ADMIN_UPDATE;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.MANAGER_CREATE;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.MANAGER_DELETE;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.MANAGER_READ;
// import static com.projeta.user.domain.model.enuns.PermissionEnum.MANAGER_UPDATE;

// import java.util.Collections;
// import java.util.List;
// import java.util.Set;
// import java.util.stream.Collectors;

// @RequiredArgsConstructor
// public enum RoleEnum {

//   USER(Collections.emptySet()),
//   ADMIN(
//           Set.of(
//                   ADMIN_READ,
//                   ADMIN_UPDATE,
//                   ADMIN_DELETE,
//                   ADMIN_CREATE,
//                   MANAGER_READ,
//                   MANAGER_UPDATE,
//                   MANAGER_DELETE,
//                   MANAGER_CREATE
//           )
//   ),
//   MANAGER(
//           Set.of(
//                   MANAGER_READ,
//                   MANAGER_UPDATE,
//                   MANAGER_DELETE,
//                   MANAGER_CREATE
//           )
//   )

//   ;

//   @Getter
//   private final Set<PermissionEnum> permissions;

//   public List<SimpleGrantedAuthority> getAuthorities() {
//     var authorities = getPermissions()
//             .stream()
//             .map(permission -> new SimpleGrantedAuthority(permission.getDescricao()))
//             .collect(Collectors.toList());
//     authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//     return authorities;
//   }
// }
