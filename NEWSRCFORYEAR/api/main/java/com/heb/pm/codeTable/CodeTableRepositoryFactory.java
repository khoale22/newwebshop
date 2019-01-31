package com.heb.pm.codeTable;

import com.heb.pm.codeTable.factory.CodeTableFrontEnd;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Constructs repositories to use for code table CRUD operations.
 *
 * @author m314029
 * @since 2.21.0
 */
@Service
public class CodeTableRepositoryFactory implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(CodeTableRepositoryFactory.class);

	// logs
	private static final String CODE_TABLE_FOUND_MESSAGE = "Code table repository: %s will be used for code table: %s.";

	// errors
	private static final String UNKNOWN_CODE_TABLE_ERROR = "%s is an unknown code table. Please contact " +
			"production support.";

	// code table constants
	private static final String PACKAGING_TYPE_TABLE = "PKG_TYP";
	private static final String CODE_DATE_TYPE_TABLE = "CODE_DATED";
	private static final String PACK_CONFIGURATION_TABLE = "PK_CFG";
	private static final String STATE_TABLE = "STATE";
	private static final String MASTER_PACK_MATERIAL_TABLE = "MST_PK_MATRL";

	private Map<String, JpaRepository> codeTableRepositories;

	@Autowired
	private PackagingTypeRepository packagingTypeRepository;

	@Autowired
	private PackConfigurationRepository packConfigurationRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CodeDateTypeRepository codeDateTypeRepository;

	@Autowired
	private MasterPackMaterialRepository masterPackMaterialRepository;

	/**
	 * Getter for a repository linked to the given table name.
	 *
	 * @param tableName Table name to get the repository for.
	 * @return Repository to be used for CRUD operations.
	 */
	public <T extends CodeTable> JpaRepository<T, String> getRepository(String tableName){
		JpaRepository<T, String> toReturn = codeTableRepositories.get(tableName);

		if(toReturn != null) {
			logger.info(String.format(CODE_TABLE_FOUND_MESSAGE, toReturn.getClass().getInterfaces()[0], tableName));
			return toReturn;
		} else {
			logger.error(String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
			throw new IllegalArgumentException(
					String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
		}
	}

	/**
	 * After class is built, builds map of table name => repository to use.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.codeTableRepositories = new HashMap<>();
		codeTableRepositories.put(PACKAGING_TYPE_TABLE, this.packagingTypeRepository);
		codeTableRepositories.put(CODE_DATE_TYPE_TABLE, this.codeDateTypeRepository);
		codeTableRepositories.put(PACK_CONFIGURATION_TABLE, this.packConfigurationRepository);
		codeTableRepositories.put(STATE_TABLE, this.stateRepository);
		codeTableRepositories.put(MASTER_PACK_MATERIAL_TABLE, this.masterPackMaterialRepository);
	}

	/**
	 * Converts a list of code table front end objects to a list of the respective entities (based off table name).
	 *
	 * @param tableName the tableName.
	 * @param entities the entities.
	 * @return returns a list of the respective code table entities based off the table name.
	 */
	public List<? extends CodeTable> toEntities(String tableName, List<CodeTableFrontEnd> entities) {
		switch (tableName) {
			case PACKAGING_TYPE_TABLE: {
				return entities.stream().map(PackagingType::new).collect(Collectors.toList());
			}
			case CODE_DATE_TYPE_TABLE: {
				return entities.stream().map(CodeDateType::new).collect(Collectors.toList());
			}
			case PACK_CONFIGURATION_TABLE: {
				return entities.stream().map(PackConfiguration::new).collect(Collectors.toList());
			}
			case STATE_TABLE: {
				return entities.stream().map(State::new).collect(Collectors.toList());
			}
			case MASTER_PACK_MATERIAL_TABLE: {
				return entities.stream().map(MasterPackMaterial::new).collect(Collectors.toList());
			}
			default: {
				logger.error(String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
				return null;
			}
		}
	}
}
