package org.edu.user.service.imp;

import org.apache.dubbo.config.annotation.Service;
import org.edu.common.domain.DatoDTO.UserDataDTO;
import org.edu.common.service.UserServiceRPC;
import org.edu.common.domain.UserDTO;
import org.edu.user.infra.dao.UserMapper;
import org.edu.user.infra.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = UserServiceRPC.class)
public class UserServiceImp  implements UserServiceRPC {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpInner userServiceImpInner;

    public UserDataDTO getById(Long id) {
        // 调用 service 层的方法获取 UserEntity
        UserEntity userEntity = userServiceImpInner.getById(id);

        // 将 UserEntity 转换为 UserDataDTO
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setId(userEntity.getId());
        userDataDTO.setUsername(userEntity.getUsername());
        userDataDTO.setEmail(userEntity.getEmail());
        userDataDTO.setPhone(userEntity.getPhone());
        userDataDTO.setAddress(userEntity.getAddress());
        userDataDTO.setPassword(userEntity.getPassword());
        userDataDTO.setCreated_at(userEntity.getCreated_at());

        return userDataDTO;
    }

    public void updateById(UserDataDTO userDataDTO) {
        // 转换成UserEntity后更新：
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDataDTO.getUsername());
        userEntity.setPassword(userDataDTO.getPassword());
        userEntity.setEmail(userDataDTO.getEmail());
        userEntity.setPhone(userDataDTO.getPhone());
        userEntity.setAddress(userDataDTO.getAddress());

        userMapper.updateById(userEntity);
    }


    @Override
    public int saveUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        // password should be encrypted:
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(user.getPassword());
        userEntity.setPassword(encryptedPassword);
        userEntity.setEmail(user.getEmail());
        // TODO：插入数据库做幂等判断：
        return userMapper.insert(userEntity);
    }

    @Override
    public void updateUser(UserDTO user) {
        // 转换成UserEntity后更新：
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userMapper.updateById(userEntity);
    }

    @Override
    public void deleteUser(UserDTO user) {
        // 转换成UserEntity后删除：
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        // 只需要id即可：
        userMapper.deleteById(userEntity);
    }

    @Override
    public List<UserDTO> getUserList() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<UserEntity> userEntityList = userMapper.selectList(null);
        for (UserEntity userEntity : userEntityList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userEntity.getId());
            userDTO.setUsername(userEntity.getUsername());
            userDTO.setPassword(userEntity.getPassword());
            userDTO.setEmail(userEntity.getEmail());
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
