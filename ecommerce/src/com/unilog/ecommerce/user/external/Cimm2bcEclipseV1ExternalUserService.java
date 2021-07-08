package com.unilog.ecommerce.user.external;

import javax.ws.rs.HttpMethod;

import org.apache.log4j.Logger;

import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralClient;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralResponseEntity;
import com.erp.service.cimm2bcentral.utilities.Cimm2BCentralUser;
import com.unilog.database.CommonDBQuery;
import com.unilog.ecommerce.user.model.ExternalToken;
import com.unilog.ecommerce.user.model.User;

public class Cimm2bcEclipseV1ExternalUserService implements ExternalUserService {
	protected static ExternalUserService externalUserService;
	private static final Logger logger = Logger.getLogger(Cimm2bcEclipseV1ExternalUserService.class);
	public static ExternalUserService getInstance() {
			synchronized (Cimm2bcEclipseV1ExternalUserService.class) {
				if(externalUserService == null) {
					externalUserService = new Cimm2bcEclipseV1ExternalUserService();
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
		String createUserApi = CommonDBQuery.getSystemParamtersList().get("CREATE_USER_API");
		Cimm2BCentralUser rawUser = UserMappingUtil.mapUserToRequestBody(user);
		try {
			Cimm2BCentralResponseEntity userDetailsResponseEntity = Cimm2BCentralClient.getInstance().getDataObject(createUserApi, HttpMethod.POST, rawUser, String.class);
			if(userDetailsResponseEntity!=null && userDetailsResponseEntity.getData() != null &&  userDetailsResponseEntity.getStatus().getCode() == 200 ){  
				externalUserId = userDetailsResponseEntity.getData().toString();
			}
		}catch(Exception e) {
			logger.error("Unable to create user", e);
		}
		return externalUserId;
	}

	@Override
	public String updateUser(User user) {
		return null;
	}

}
