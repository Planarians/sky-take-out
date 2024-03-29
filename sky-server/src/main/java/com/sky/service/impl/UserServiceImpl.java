package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.BusinessException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.repository.UserRepository;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {



    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    // 微信登录
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        // 1.参数校验
        if (StrUtil.isBlank(userLoginDTO.getCode())) {
            throw new BusinessException("code不能为空");
        }
        // 2.微信登录
        // 2-1 url（地址）
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        // 2-2 param（参数）
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",userLoginDTO.getCode());
        paramMap.put("grant_type","authorization_code");
        // 2-3 发送请求
        String json = HttpClientUtil.doGet(url, paramMap);
        // 2-4 json转map
        Map<String,String> map = JSON.parseObject(json, Map.class);
        // 2-5 校验是否有openid
        String openid = map.get("openid");
        if (StrUtil.isBlank(openid)) {
            throw new BusinessException("微信登录失败");
        }
        // 3.根据openid查询user表
       // User user = userMapper.getByOpenid(openid);
        User user = userRepository.findUserByOpenid(openid);

        // 4.判断是否为新用户
        if (user==null) {
            // 注册
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            //userMapper.insert(user);  // 开启主键返回
            userRepository.save(user);
        }
        // 5.返回user
        return user;
    }
}
