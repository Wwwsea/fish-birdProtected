package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.constants.Const;
import com.fish.birdProducted.domain.dto.LoginLogDTO;
import com.fish.birdProducted.domain.dto.LoginLogDeleteDTO;
import com.fish.birdProducted.domain.entity.LoginLog;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.LoginLogVO;
import com.fish.birdProducted.mapper.LoginLogMapper;
import com.fish.birdProducted.service.LoginLogService;
import com.fish.birdProducted.utils.AddressUtils;
import com.fish.birdProducted.utils.BrowserUtil;
import com.fish.birdProducted.utils.IpUtils;
import com.fish.birdProducted.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * (LoginLog)表服务实现类
 *
 * @author fish
 * @since 2024-2-08 14:38:44
 */
@Slf4j
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingKey.log-login}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange.log}")
    private String exchange;

    @Override
    public void loginLog(HttpServletRequest request, String userName, Integer state, String message) {
        try {
            String browserName = BrowserUtil.browserName(request);
            String ipAddress = IpUtils.getIpAddr(request);
            String os = BrowserUtil.osName(request);
            String realAddressByIP = AddressUtils.getRealAddressByIP(ipAddress);
            int requestType;
            String typeHeader = request.getHeader(Const.TYPE_HEADER);
            if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.FRONTEND_REQUEST)) {
                requestType = 0;
            } else if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.BACKEND_REQUEST)) {
                requestType = 1;
            } else {
                requestType = 2;
            }
            if (userName == null) {
                userName = "未知用户";
            }
            LoginLog logEntity = LoginLog.builder()
                    .userName(userName)
                    .ip(ipAddress)
                    .address(realAddressByIP)
                    .browser(browserName)
                    .os(os)
                    .type(requestType)
                    .state(state)
                    .message(message)
                    .build();

            rabbitTemplate.convertAndSend(exchange, routingKey, logEntity);
            log.info("发送登录日志信息到RabbitMQ");
        } catch (Exception e) {
            log.error("发送登录日志信息到RabbitMQ时发生异常: {}", e.getMessage());
        }
    }

    @Override
    public List<LoginLogVO> searchLoginLog(LoginLogDTO loginLogDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(loginLogDTO)) {
            wrapper.like(StringUtils.isNotEmpty(loginLogDTO.getAddress()), LoginLog::getAddress, loginLogDTO.getAddress())
                    .like(StringUtils.isNotEmpty(loginLogDTO.getUserName()), LoginLog::getUserName, loginLogDTO.getUserName())
                    .eq(StringUtils.isNotNull(loginLogDTO.getState()), LoginLog::getState, loginLogDTO.getState());
            if (StringUtils.isNotNull(loginLogDTO.getLoginTimeStart()) && StringUtils.isNotNull(loginLogDTO.getLoginTimeEnd())) {
                wrapper.gt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeStart()).and(a -> a.lt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeEnd()));
            }
        }
        wrapper.orderByDesc(LoginLog::getCreateTime);
        return loginLogMapper.selectList(wrapper).stream().map(loginLog -> loginLog.asViewObject(LoginLogVO.class,v -> v.setLoginTime(loginLog.getCreateTime()))).toList();
    }

    @Transactional
    @Override
    public ResponseResult<Void> deleteLoginLog(LoginLogDeleteDTO loginLogDeleteDTO) {
        if (this.removeByIds(loginLogDeleteDTO.getIds())) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
