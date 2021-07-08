package com.unilog.ecommerce.user.external;

import javax.ws.rs.HttpMethod;

import org.apache.log4j.Logger;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.user.model.ExternalToken;
import com.unilog.ecommerce.user.model.User;

public class Cimm2bcExternalUserSerice implements ExternalUserService {
	
	protected static ExternalUserService externalUserService;
	private static final Logger logger = Logger.getLogger(Cimm2bcExternalUserSerice.class);
	
	public static ExternalUserService getInstance() {
			synchronized (externalUserService) {
				if(externalUserService == null) {
					externalUserService = new Cimm2bcExternalUserSerice();
				}
			}
		return externalUserService;
	}
	@Override
	public ExternalToken authenticate(User user) {
		return null;
	}

	@Override
	public String createUser(User user) {
		String externalUserId = null;
		try {
			String createUserApi = CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API");
			Cimm2BCentralUser rawUser = UserMappingUtil.mapUserToRequestBody(user);
			Cimm2BCentralResponseEntity response = Cimm2BCentralClient.getInstance().getDataObject(createUserApi, HttpMethod.POST, rawUser, String.class);
			if(response != null && response.getData() != null && response.getStatus().getCode() == 200) {
				externalUserId = response.getData().toString();
			}
		}catch (Exception e) {
			logger.error("Exception occured while creating user in External System", e);
		}
		return externalUserId;
	}

	@Override
	public String updateUser(User user) {
		return null;
	}

}
