package com.cronoteSys.model.dao;

import com.cronoteSys.model.vo.AuditLogVO;

public class AuditLogDAO extends GenericsDAO<AuditLogVO, Long> {

	public AuditLogDAO() {
		super(AuditLogVO.class);
	}

	
}
