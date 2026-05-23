package com.sky.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private static final String WECHAT_LOGIN_URL =
            "https://api.weixin.qq.com/sns/jssession?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserLoginVO login(com.sky.dto.UserLoginDTO dto) {
        String openid = getOpenid(dto.getCode());

        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }

        String token = JwtUtil.createUserToken(
                jwtProperties.getUserSecretKey(), user.getId());

        return UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
    }

    private String getOpenid(String code) {
        String url = String.format(WECHAT_LOGIN_URL,
                weChatProperties.getAppid(), weChatProperties.getSecret(), code);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());
            String openid = jsonNode.get("openid").asText();
            if (openid == null || openid.isEmpty()) {
                throw new RuntimeException("微信登录失败: " + response.body());
            }
            return openid;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("微信登录失败", e);
        }
    }
}
