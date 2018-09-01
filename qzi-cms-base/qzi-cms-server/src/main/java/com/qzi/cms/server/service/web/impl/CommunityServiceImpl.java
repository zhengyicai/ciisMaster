/* 
 * 文件名：CommunityServiceImpl.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年6月28日
 * 版本号：v1.0
*/
package com.qzi.cms.server.service.web.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.qzi.cms.common.enums.StateEnum;
import com.qzi.cms.common.po.UseBuildingPo;
import com.qzi.cms.common.po.UseCommunityPo;
import com.qzi.cms.common.po.UseCommunityUserPo;
import com.qzi.cms.common.resp.Paging;
import com.qzi.cms.common.util.ToolUtils;
import com.qzi.cms.common.util.YBBeanUtils;
import com.qzi.cms.common.vo.AdminVo;
import com.qzi.cms.common.vo.CommunityAdminVo;
import com.qzi.cms.common.vo.SysCityVo;
import com.qzi.cms.common.vo.UseCommunityVo;
import com.qzi.cms.server.mapper.SysCityMapper;
import com.qzi.cms.server.mapper.SysUserMapper;
import com.qzi.cms.server.mapper.UseBuildingMapper;
import com.qzi.cms.server.mapper.UseCommunityMapper;
import com.qzi.cms.server.mapper.UseCommunityUserMapper;
import com.qzi.cms.server.service.web.CommunityService;

/**
 * 住宅小区业务层实现类
 * @author qsy
 * @version v1.0
 * @date 2017年6月28日
 */
@Service
public class CommunityServiceImpl implements CommunityService {
	@Resource
	private UseCommunityMapper communityMapper;
	@Resource
	private SysCityMapper cityMapper;
	@Resource
	private SysUserMapper userMapper;
	@Resource
	private UseCommunityUserMapper communityUserMapper;
	@Resource
	private UseBuildingMapper buildMapper;

	@Override
	public List<UseCommunityVo> findAll(Paging paging) {
		RowBounds rwoBounds = new RowBounds(paging.getPageNumber(),paging.getPageSize());
		return communityMapper.findAll(rwoBounds);
	}

	@Override
	public long findCount() {
		return communityMapper.findCount();
	}

	@Override
	public void add(UseCommunityVo communityVo) throws Exception {
		//保存小区信息
		UseCommunityPo ucPo = YBBeanUtils.copyProperties(communityVo, UseCommunityPo.class);
		ucPo.setId(ToolUtils.getUUID());
		ucPo.setCommunityNo(getCommunityNo());
		ucPo.setCreateTime(new Date());
		communityMapper.insert(ucPo);
		//保存楼栋信息
		for(int n=1;n<=communityVo.getBuildingNum();n++){
			UseBuildingPo buildPo = new UseBuildingPo();
			buildPo.setId(ToolUtils.getUUID());
			buildPo.setBuildingName(n+"栋");
			buildPo.setBuildingNo(String.format("%02d", n));
			buildPo.setCommunityId(ucPo.getId());
			buildPo.setState(StateEnum.NORMAL.getCode());
			buildMapper.insert(buildPo);
		}
	}

	@Override
	public void update(UseCommunityVo communityVo) throws Exception {
		UseCommunityPo raPo = YBBeanUtils.copyProperties(communityVo, UseCommunityPo.class);
		communityMapper.updateByPrimaryKey(raPo);
	}
	
	/**
	 * 获取小区编号
	 * @return 6位字符编号
	 */
	private String getCommunityNo(){
		String no = communityMapper.findMaxCommunityNo();
		int tno=1;
		if(StringUtils.isNotEmpty(no)){
			tno = Integer.parseInt(no)+1;
		}
		return String.format("%06d", tno);
	}

	@Override
	public List<SysCityVo> findCitys(String parentCode) {
		return cityMapper.findCitys(parentCode);
	}

	@Override
	public List<CommunityAdminVo> findAdmin(String communityId) {
		return userMapper.findAdmin(communityId);
	}

	@Override
	public void add(AdminVo adminVo) {
		//删除当前小区关联的数据
		communityUserMapper.deleteForRid(adminVo.getCommunityId());
		//保存当前小区关系的数据
		for(String userId:adminVo.getUserIds()){
			UseCommunityUserPo ucuPo = new UseCommunityUserPo();
			ucuPo.setCommunityId(adminVo.getCommunityId());
			ucuPo.setUserId(userId);
			communityUserMapper.insert(ucuPo);
		}
	}

}
