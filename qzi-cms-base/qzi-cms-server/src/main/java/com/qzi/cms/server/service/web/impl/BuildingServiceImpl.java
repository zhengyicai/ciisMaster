/* 
 * 文件名：BuildingServiceImpl.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年7月5日
 * 版本号：v1.0
*/
package com.qzi.cms.server.service.web.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.qzi.cms.common.enums.StateEnum;
import com.qzi.cms.common.po.UseBuildingPo;
import com.qzi.cms.common.po.UseCommunityPo;
import com.qzi.cms.common.po.UseRoomPo;
import com.qzi.cms.common.resp.Paging;
import com.qzi.cms.common.util.ToolUtils;
import com.qzi.cms.common.vo.SysUserVo;
import com.qzi.cms.common.vo.TreeVo;
import com.qzi.cms.common.vo.UseBuildingVo;
import com.qzi.cms.server.mapper.UseBuildingMapper;
import com.qzi.cms.server.mapper.UseCommunityMapper;
import com.qzi.cms.server.mapper.UseRoomMapper;
import com.qzi.cms.server.service.common.CommonService;
import com.qzi.cms.server.service.web.BuildingService;

/**
 * 楼栋业务层实现类
 * @author qsy
 * @version v1.0
 * @date 2017年7月5日
 */
@Service
public class BuildingServiceImpl implements BuildingService {
	@Resource
	private UseCommunityMapper communityMapper;
	@Resource
	private UseBuildingMapper buildMapper;
	@Resource
	private UseRoomMapper roomMapper;
	@Resource
	private CommonService commonService;

	@Override
	public List<TreeVo> findTree() throws Exception {
		//读取用户信息
		SysUserVo userVo = commonService.findUser();
		return communityMapper.findTree(userVo.getId());
	}


	@Override
	public List<UseBuildingVo> findBuilding(String communityId, Paging paging) {
		RowBounds rowBounds = new RowBounds(paging.getPageNumber(),paging.getPageSize());
		return buildMapper.findBuilding(communityId,rowBounds);
	}

	@Override
	public long findCount(String communityId) {
		return buildMapper.findCount(communityId);
	}


	@Override
	public void updateState(UseBuildingVo buildingVo) {
		UseBuildingPo buildingPo = buildMapper.selectByPrimaryKey(buildingVo.getId());
		buildingPo.setState(buildingVo.getState());
		buildMapper.updateByPrimaryKey(buildingPo);
	}

	@Override
	public void createRoom(UseBuildingVo buildingVo) {
		//修改楼栋参数信息
		UseBuildingPo buildingPo = buildMapper.selectByPrimaryKey(buildingVo.getId());
		buildingPo.setUnitNumber(buildingVo.getUnitNumber());
		buildingPo.setFloorNumber(buildingVo.getFloorNumber());
		buildingPo.setRoomNumber(buildingVo.getRoomNumber());
		buildMapper.updateByPrimaryKey(buildingPo);
		//查找小区
		UseCommunityPo communityPo = communityMapper.selectByPrimaryKey(buildingPo.getCommunityId());
		//生成房间
		for(int u=1;u<=buildingPo.getUnitNumber();u++){
			for(int f=1;f<=buildingPo.getFloorNumber();f++){
				for(int r=1;r<=buildingPo.getRoomNumber();r++){
					UseRoomPo roomPo = new UseRoomPo();
					roomPo.setUnitName(String.format("%02d", u));
					roomPo.setId(ToolUtils.getUUID());
					roomPo.setBuildingId(buildingPo.getId());
					roomPo.setState(StateEnum.NORMAL.getCode());
					roomPo.setRoomNo(communityPo.getCommunityNo()+buildingPo.getBuildingNo()+String.format("%02d", u)+String.format("%02d", f)+String.format("%02d", r));
					roomPo.setRoomName(f+String.format("%02d", r));
					roomMapper.insert(roomPo);
				}
			}
		}
	}

}
