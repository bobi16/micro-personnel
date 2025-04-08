package com.bobiko.common.core.constants;

/**
 * @author bobiko
 * @date 2025-4-7 15:00
 * @description 必须描述类做什么事情, 实现什么功能
 */
public interface CaptchaConstants
{
    /**
     * 验证码长
     */
    int CAPTCHA_WIDTH = 200;
    /**
     * 验证码款
     */
    int CAPTCHA_HEIGHT = 100;
    /**
     * 验证码字符个数
     */
    int CAPTCHA_LENGTH = 4;
    /**
     * 验证码干扰线/圈数量
     */
    int CAPTCHA_CIRCLE_NUM = 20;
    /**
     * 验证码保存有效时间
     */
    int LOGIN_CAPTCHA_CODE_TTL = 60;
}
