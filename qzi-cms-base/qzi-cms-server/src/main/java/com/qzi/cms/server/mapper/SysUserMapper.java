/* 
 * 文件名：SysUserMapper.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年6月6日
 * 版本号：v1.0
*/
package com.qzi.cms.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.qzi.cms.common.po.SysUserPo;
import com.qzi.cms.common.vo.CommunityAdminVo;
import com.qzi.cms.common.vo.SysUserVo;
import com.qzi.cms.server.base.BaseMapper;

/**
 * 用户信息DAO
 * @author qsy
 * @version v1.0
 * @date 2017年6月6日
 */
public interface SysUserMapper extends BaseMapper<SysUserPo>{

	/**
	 * 用户登录
	 * @param loginName
	 * @return
	 */
	@Select("select * from sys_user where loginName=#{ln}")
	public SysUserVo findByloginName(@Param("ln") String loginName);

	/**
	 * 分页查询
	 * @param rwoBounds
	 * @return
	 */
	@Select("select id,userName,loginName,mobile,roleId,roleName,userIcon,createTime,lastTime,state from sys_user order by createTime desc")
	public List<SysUserVo> findAll(RowBounds rwoBounds);

	/**
	 * 查总记录数
	 * @return
	 */
	@Select("select count(1) from sys_user")
	public long findCount();

	/**
	 * 查找登录名是否存在
	 * @param loginName
	 * @return
	 */
	@Select("select count(1) from sys_user where loginName=#{ln}")
	public int findLoginName(@Param("ln") String loginName);

	/**
	 * @param communityId
	 * @return
	 */
	@Select("SELECT su.userName,su.id userId,uru.userId is not null checked from sys_user su LEFT JOIN use_community_user uru on su.id = uru.userId and uru.communityId=#{cid} where su.state='10'")
	public List<CommunityAdminVo> findAdmin(@Param("cid")String communityId);

}
