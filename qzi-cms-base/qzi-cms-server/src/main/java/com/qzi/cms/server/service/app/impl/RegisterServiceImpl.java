package com.qzi.cms.server.service.app.impl;

import com.qzi.cms.common.po.UseBuildingPo;
import com.qzi.cms.common.po.UseCommunityPo;
import com.qzi.cms.common.resp.Paging;
import com.qzi.cms.common.vo.UseBuildingVo;
import com.qzi.cms.server.mapper.UseBuildingMapper;
import com.qzi.cms.server.mapper.UseCommunityMapper;
import com.qzi.cms.server.service.app.RegisterService;
import org.springframework.stereotype.Service;
import sun.jvm.hotspot.asm.Register;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhengyicai on 2018/9/8.
 */
@Service("registerservice")
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private UseCommunityMapper useCommunityMapper;

    @Resource
    private UseBuildingMapper buildMapper;
    @Override
    public List<UseCommunityPo> regfindAll(UseCommunityPo po) {
        return useCommunityMapper.regfindAll(po);
    }

    @Override
    public Integer regfindCount(UseCommunityPo po) {
        return useCommunityMapper.regfindCount(po);
    }

    @Override
    public List<UseBuildingPo> findBuilding(String communityId) {
        return buildMapper.findByCommunityId(communityId);
    }


    @Override
    public long findCount(String communityId) {
        return buildMapper.findByCount(communityId);
    }


}
