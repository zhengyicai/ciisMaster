/* 
 * 文件名：UseBuildingMapper.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年7月4日
 * 版本号：v1.0
*/
package com.qzi.cms.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.qzi.cms.common.po.UseBuildingPo;
import com.qzi.cms.common.vo.OptionVo;
import com.qzi.cms.common.vo.UseBuildingVo;
import com.qzi.cms.server.base.BaseMapper;

/**
 * 楼栋DAO
 * @author qsy
 * @version v1.0
 * @date 2017年7月4日
 */
public interface UseBuildingMapper extends BaseMapper<UseBuildingPo>{

	/**
	 * @param communityId
	 * @param rwoBounds 
	 * @return
	 */
	@Select("select * from use_building where communityId=#{communityId} order by buildingNo")
	public List<UseBuildingVo> findBuilding(@Param("communityId") String communityId, RowBounds rwoBounds);

	/**
	 * @param communityId
	 * @return
	 */
	@Select("select count(1) from use_building where communityId=#{communityId}")
	public long findCount(@Param("communityId") String communityId);

	/**
	 * @param id
	 * @return
	 */
	@Select("SELECT * from use_building where communityId=#{communityId} and state='10' ORDER BY buildingNo")
	public List<UseBuildingPo> findByCommunityId(@Param("communityId") String id);


	@Select("select count(1) from use_building where communityId=#{communityId} and state='10'")
	public long findByCount(@Param("communityId") String communityId);



	/**
	 * @param communityId
	 * @return
	 */
	@Select("SELECT id value,buildingName name from use_building where communityId=#{communityId} and roomNumber>0 and state='10' ORDER BY buildingNo")
	public List<OptionVo> findBuildings(@Param("communityId") String communityId);
	
}
