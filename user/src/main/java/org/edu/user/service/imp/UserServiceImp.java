package org.edu.user.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.Service;
import org.edu.commom.Service.UserServiceRPC;
import org.edu.commom.domain.UserDTO;
import org.edu.user.infra.dao.UserMapper;
import org.edu.user.infra.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = UserServiceRPC.class)
public class UserServiceImp extends ServiceImpl<UserMapper, UserEntity> implements UserServiceRPC {

    @Autowired
    private UserMapper userMapper;

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
