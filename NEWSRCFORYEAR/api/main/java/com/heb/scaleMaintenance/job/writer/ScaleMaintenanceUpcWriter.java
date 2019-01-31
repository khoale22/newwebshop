package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceUpcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for a scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceUpcWriter implements ItemWriter<List<ScaleMaintenanceUpc>> {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceUpcWriter.class);
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called writer with null or empty list.";

	@Autowired
	private ScaleMaintenanceUpcRepository repository;

	@Override
	public void write(List<? extends List<ScaleMaintenanceUpc>> items) throws Exception {

		if (items == null || items.isEmpty()) {
			ScaleMaintenanceUpcWriter.logger.info(ScaleMaintenanceUpcWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}
		for(List<ScaleMaintenanceUpc> scaleMaintenanceUpcs : items){
			if (scaleMaintenanceUpcs == null || scaleMaintenanceUpcs.isEmpty()) {
				ScaleMaintenanceUpcWriter.logger.info(ScaleMaintenanceUpcWriter.EMPTY_LIST_LOGGER_MESSAGE);
			} else {
				this.repository.save(scaleMaintenanceUpcs);
			}
		}
	}
}
