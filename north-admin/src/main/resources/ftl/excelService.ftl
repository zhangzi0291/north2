package com.north.sys.service;

import com.north.base.excel.service.ExcelServiceAbstract;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ${entityName}ExcelService extends ExcelServiceAbstract<${entityName}, ${serviceName}> {

@Override
protected void checkValue(${entityName} bean, Map
<String, String> errorMap) {

}

}
