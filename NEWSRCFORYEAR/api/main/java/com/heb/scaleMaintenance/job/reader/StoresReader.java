package com.heb.scaleMaintenance.job.reader;

import com.heb.util.controller.IntegerListFromStringFormatter;
import com.heb.util.controller.LongListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Reader for a list of stores that returns one store.
 *
 * @author m314029
 * @since 2.17.8
 */
public class StoresReader implements ItemReader<Integer>, StepExecutionListener {

	@Value("#{jobParameters['stores']}")
	private String stores;

	private Iterator<Integer> data;

	private IntegerListFromStringFormatter integerListFromStringFormatter = new IntegerListFromStringFormatter();

	@Override
	public Integer read() throws Exception {

		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}

		// we're at the end of the data.
		return null;
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		List<Integer> storeList = this.integerListFromStringFormatter.parse(stores, Locale.US);
		this.data = storeList.iterator();
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
