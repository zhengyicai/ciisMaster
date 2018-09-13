package com.qzi.cms.app.controller;

/**
 * Created by Administrator on 2018/9/11.
 */


import com.qzi.cms.common.annotation.SystemControllerLog;
import com.qzi.cms.common.enums.RespCodeEnum;
import com.qzi.cms.common.exception.CommException;
import com.qzi.cms.common.po.UseBuildingPo;
import com.qzi.cms.common.po.UseCommunityPo;
import com.qzi.cms.common.po.UseResidentPo;
import com.qzi.cms.common.resp.RespBody;
import com.qzi.cms.common.util.LogUtils;

import com.qzi.cms.common.util.ToolUtils;
import com.qzi.cms.common.vo.UseResidentVo;
import com.qzi.cms.server.service.app.LoginService;
import com.qzi.cms.server.service.app.RegisterService;
import com.qzi.cms.server.service.web.ResidentService;
import com.qzi.cms.server.service.web.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 注册控制器
 * @author qsy
 * @version v1.0
 * @date 2017年7月31日
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    private RegisterService registerService;


    @Resource(name="appLogin")
    private LoginService loginService;

    @Resource
	private ResidentService residentService;


    @GetMapping("/getCommunity")
    @SystemControllerLog(description="获取小区")
    public RespBody getCommunity(@RequestBody UseCommunityPo po) {
        // 创建返回对象
        RespBody respBody = new RespBody();
        try {


            respBody.add(RespCodeEnum.SUCCESS.getCode(),"获取小区成功", registerService.regfindAll(po));

        }catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户登录失败");
            LogUtils.error("用户登录失败！",ex);
        }
        return respBody;
    }


    @GetMapping("/getBuilding")
    @SystemControllerLog(description="获取楼栋数据")
    public RespBody getBuilding(java.lang.String communityId) {
       // 创建返回对象
       RespBody respBody = new RespBody();
       try {

          List<UseBuildingPo> list =   registerService.findBuilding(communityId);
           respBody.add(RespCodeEnum.SUCCESS.getCode(),"获取楼栋成功",list);

       }catch (Exception ex) {
           respBody.add(RespCodeEnum.ERROR.getCode(), "用户登录失败");
           LogUtils.error("用户登录失败！",ex);
       }
       return respBody;
    }

    @PostMapping("/register")
    	@SystemControllerLog(description="用户登录")
    	public RespBody register(@RequestBody UseResidentVo residentVo) {
    		// 创建返回对象
    		RespBody respBody = new RespBody();
    		try {
    			//验证FormBean
    			if(hasErrors(residentVo,respBody)){
					if(registerService.existsMobile(residentVo.getMobile())){
						UseResidentPo po =  registerService.findMobile(residentVo.getMobile());
						if((new Date().getTime()-po.getCreateTime().getTime())/1000<(60*60*24*7)){
							respBody.add("1000","管理员正在审核中");
							return respBody;
						}else{
							 //7天以上的， 修改用户信息注册时间
							residentVo.setCreateTime(new Date());
							residentService.update(residentVo);
							respBody.add("2000","注册成功，等待管理审核");
						}
					}


    				loginService.register(residentVo);
					
    				respBody.add(RespCodeEnum.SUCCESS.getCode(),"用户注册成功");
    			}
    		} catch (CommException ex) {
    			respBody.add(RespCodeEnum.ERROR.getCode(), ex.getMessage());
    			LogUtils.error("用户注册失败！",ex);
    		} catch (Exception ex) {
    			respBody.add(RespCodeEnum.ERROR.getCode(), "用户注册失败");
    			LogUtils.error("用户注册失败！",ex);
    		}
    		return respBody;
    	}

    /**
     *      2018-09-11
    	 *  验证用户注册信息
    	 * @param residentVo 新用户
    	 * @param respBody
    	 * @return
    	 */
    	private boolean hasErrors(UseResidentVo residentVo, RespBody respBody) {
    		if(StringUtils.isEmpty(residentVo.getName())){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "姓名不能为空");
    			return false;
    		}
    		if(residentVo.getName().length()<2  || residentVo.getName().length()>6){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "姓名必须输入2～6个字符");
    			return false;
    		}
    		if(StringUtils.isEmpty(residentVo.getMobile())){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "手机号不能为空");
    			return false;
    		}
    		if(!ToolUtils.isMobile(residentVo.getMobile())){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "手机号输入有误");
    			return false;
    		}
    		if(StringUtils.isEmpty(residentVo.getSmsCode())){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "手机验证码不能为空");
    			return false;
    		}
    		if(residentVo.getSmsCode().length()!=6){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "手机验证码必须输入6位数字");
    			return false;
    		}
    		if(StringUtils.isEmpty(residentVo.getPassword())){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "密码不能为空");
    			return false;
    		}
    		if(residentVo.getPassword().length()<6  || residentVo.getPassword().length()>20){
    			respBody.add(RespCodeEnum.ERROR.getCode(), "密码必须输入6～20个字符或数字");
    			return false;
    		}
    		return true;
    	}

}
