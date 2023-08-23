package com.wr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wr.domain.SysConfigPojo.*;

import java.util.List;

public interface ISysConfigService extends IService<SysConfigPo> {

    List<SysConfigVo> selectConfigList(SysConfigDto sysConfigDto);

    SysConfigVo selectConfigById(Long configId);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    boolean install(AddSysConfigDto addSysConfigDto);

    boolean updateInfo(UpSysConfigDto upSysConfigDto);

    public boolean selectCaptchaEnabled();

    boolean resetConfigCache();

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache();

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache();
}
