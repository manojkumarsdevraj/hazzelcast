package com.unilognew.db.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.database.ConnectionManager;
import com.unilognew.exception.DBServiceException;

public abstract class BaseDAOService {

	private Logger logger = LoggerFactory.getLogger(BaseDAOService.class);

	public void executeBatchQueries(List<String> queryList)
			throws DBServiceException {
		Connection conn = null;
		Statement stmt = null;

		if (queryList == null || queryList.isEmpty()) {
			logger.debug("There are not sql queries to execute...");
			return;
		}

		try {
			conn = ConnectionManager.getDBConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (String query : queryList) {
				logger.info("Query to be added to the batch : " + query);
				stmt.addBatch(query);
			}

			int result[] = stmt.executeBatch();

			for (int i = 0; i < result.length; i++) {
				if (result[i] == Statement.EXECUTE_FAILED) {
					logger.error("Some error occured while executing the queries...");
					throw new DBServiceException(
							"Some error occured while executing the queries...");
				}
			}

			conn.commit();

		} catch (Exception e) {
			try {
				logger.error("Exception occured while executing the queries..."
						+ e.getMessage());
				conn.rollback();
			} catch (Exception e1) {
				// ignore
			}
			throw new DBServiceException(e.getMessage());
		} finally {
			ConnectionManager.closeDBStatement(stmt);
			ConnectionManager.closeDBConnection(conn);
		}
	}

}
