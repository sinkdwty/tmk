package com.hjzddata.core.common.exception;

import com.hjzddata.core.exception.ServiceExceptionEnum;

/**
 * @Description 所有业务异常的枚举
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum implements ServiceExceptionEnum {

	/**
	 * 字典
	 */
	DICT_EXISTED(400,"字典已经存在"),
	ERROR_CREATE_DICT(500,"创建字典失败"),
	ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),
	ERROR_CODE_EMPTY(500,"字典类型不能为空"),

	/**
	 * 文件上传
	 */
	FILE_READING_ERROR(400,"FILE_READING_ERROR!"),
	FILE_NOT_FOUND(400,"FILE_NOT_FOUND!"),
	UPLOAD_ERROR(500,"上传出错"),

	/**
	 * 权限和数据问题
	 */
	DB_RESOURCE_NULL(400,"数据库中没有该资源"),
	NO_PERMITION(405, "权限异常"),
	REQUEST_INVALIDATE(400,"请求数据格式不正确"),
	INVALID_KAPTCHA(400,"验证码不正确"),
	CANT_DELETE_ADMIN(600,"不能删除超级管理员"),
	CANT_FREEZE_ADMIN(600,"不能禁用超级管理员"),
	CANT_CHANGE_ADMIN(600,"不能修改超级管理员角色"),

	CANT_UPDATE_PRIV(400,"无法操作此用户，请检查是否有该权限"),

	/**
	 * 账户问题
	 */
	USER_ALREADY_REG(401,"该用户已经注册"),
	NO_THIS_USER(400,"没有此用户"),
	USER_NOT_EXISTED(400, "没有此用户"),
	ACCOUNT_FREEZED(401, "账号被禁用"),
	OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
	TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

	/**
	 * 角色问题
	 */
	Role_ALREADY_REG(401,"该角色名已经存在"),

	/**
	 * 项目问题
	 */
	PRODUCT_ALREADY_REG(401,"该项目名已经存在"),
	NO_THIS_PRODUCT(400,"没有此项目"),
	PRODUCT_NOT_EXISTED(400, "没有此项目"),
	PRODUCT_FREEZED(401, "项目被冻结"),

	/**
	 * 客户问题
	 */
	CUSTOM_ALREADY_REG(401,"该手机号已经存在"),
	CUSTOM_List_REG(401,"数据为空"),

	/**
	 * 公司问题
	 */
	DEPT_ALREADY_REG(401,"系统中该公司已经存在"),

	/**
	 * 错误的请求
	 */
	MENU_PCODE_COINCIDENCE(400,"菜单编号和副编号不能一致"),
	EXISTED_THE_MENU(400,"菜单编号重复，不能添加"),
	DICT_MUST_BE_NUMBER(400,"字典的值必须为数字"),
	REQUEST_NULL(400, "请求有错误"),
	REQUEST_DICTNAME(400, "字典名称不能为空"),
	SESSION_TIMEOUT(400, "会话超时"),
	SERVER_ERROR(500, "服务器异常");

	BizExceptionEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	private Integer code;

	private String message;

	@Override
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
