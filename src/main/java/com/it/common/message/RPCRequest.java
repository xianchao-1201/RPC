package com.it.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPCRequest implements Serializable {

	//服务类名
	private String interfaceName;
	//方法名
	private String methodName;
	//参数列表
	private Object[] params;
	//参数类型
	private Class<?>[] paramsType;
}
