package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.util.controller.StreamingExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service that generates reports for the scale system.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class ReportsService {

	private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

	private static final String INGREDIENT_REPORT_HEADER= "\"UPC\",\"PLU\",\"Description Line One\",\"Description Line Two\",\"Ingredient Statement Number\",\"Ingredients\"";
	private static final String INGREDIENT_EXPORT_FORMAT = "\"%d\",\"%d\",\"%s\",\"%s\",\"%d\",\"%s\"";

	private static final int PAGE_SIZE = 100;

	private static List<Long> invalidIngredientStatements;

	static {
		ReportsService.invalidIngredientStatements = new LinkedList<>();
		ReportsService.invalidIngredientStatements.add(0l);
		ReportsService.invalidIngredientStatements.add(9999l);
	}

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	/**
	 * Streams a report of UPCs whose ingredient statements match a supplied pattern.
	 *
	 * @param outputStream The output stream to write the report to.
	 * @param ingredientPattern The pattern to match in the ingredient statement.
	 */
	public void streamIngredientReport(ServletOutputStream outputStream, String ingredientPattern){

		// Print out the header
		try {
			outputStream.println(ReportsService.INGREDIENT_REPORT_HEADER);
			outputStream.flush();
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		// Build the object that will match the user's requested pattern.
		Pattern pattern = Pattern.compile(ingredientPattern, Pattern.CASE_INSENSITIVE);

		int page = 0;
		int sinceLastPrint = 0;
		boolean dataFound;
		// Will pull all the UPCs with valid ingredient statements in pages of 100.
		do {
			dataFound = false;

			//If the program has not output to the file in 50 iterations, output "loading" to mitigate timeout issues.
			try {
				if (sinceLastPrint >= 50) {
					outputStream.flush();
					outputStream.println("Loading...");
					sinceLastPrint = 0;
				}
			}catch (IOException e){
				throw new StreamingExportException(e.getMessage(), e.getCause());
			}
			sinceLastPrint++;

			// Get the next page of data.
			ReportsService.logger.debug(String.format("Fetching page %d", page));

			Pageable pageRequest = new PageRequest(page++, ReportsService.PAGE_SIZE);
			List<ScaleUpc> scaleUpcs =
					scaleUpcRepository.findByIngredientStatementNotIn(
							ReportsService.invalidIngredientStatements, pageRequest);

			// After the last one is pulled, the list will be empty.
			if (!scaleUpcs.isEmpty()) {
				dataFound = true;
				for (ScaleUpc scaleUpc : scaleUpcs) {
					String ingredientStatement = scaleUpc.getIngredientStatementHeader().getIngredientsText();
					// If the ingredient statement matches the user's supplied pattern, print it out to the
					// output stream.
					if (pattern.matcher(ingredientStatement).find()) {
						try {
							outputStream.println(String.format(ReportsService.INGREDIENT_EXPORT_FORMAT,
									scaleUpc.getUpc(), scaleUpc.getPlu(), scaleUpc.getEnglishDescriptionOne(),
									scaleUpc.getEnglishDescriptionTwo(), scaleUpc.getIngredientStatement(),
									ingredientStatement));
							sinceLastPrint = 0;
						} catch (IOException e) {
							throw new StreamingExportException(e.getMessage(), e.getCause());
						}
					}
				}
			}
		} while (dataFound);
	}
}
