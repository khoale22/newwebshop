package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.service.AuthorizationAndRetailService;
import com.heb.util.controller.LongListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Processor for converting a long into ScaleMaintenanceAuthorizeRetails.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceAuthorizeRetailProcessor implements ItemProcessor<Integer, List<ScaleMaintenanceAuthorizeRetail>>, StepExecutionListener {

	@Value("#{jobParameters['upcs']}")
	private String upcs;

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	private List<Long> upcList;
	private final static int PAGE_SIZE = 100;

	private LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();

	@Autowired
	private AuthorizationAndRetailService authorizationAndRetailService;

	@Override
	public List<ScaleMaintenanceAuthorizeRetail> process(Integer store) throws Exception {

		List<ScaleMaintenanceAuthorizeRetail> toReturn = new LinkedList<>();

		List<Long> currentUpcs = new LinkedList<>();
		for (Long upc : this.upcList) {
			currentUpcs.add(upc);
			if (currentUpcs.size() >= PAGE_SIZE) {
				// get authorization and retails for {PAGE_SIZE} upcs
				toReturn.addAll(this.authorizationAndRetailService
						.getAuthorizedAndRetailForUpcsByStore(transactionId, store, currentUpcs));
				currentUpcs.clear();
			}

		}
		// get authorization and retail for the remainder of current upcs
		if(currentUpcs.size() > 0) {
			toReturn.addAll(this.authorizationAndRetailService
					.getAuthorizedAndRetailForUpcsByStore(transactionId, store, currentUpcs));
		}
		return toReturn;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.upcList = this.longListFromStringFormatter.parse(upcs, Locale.US);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
