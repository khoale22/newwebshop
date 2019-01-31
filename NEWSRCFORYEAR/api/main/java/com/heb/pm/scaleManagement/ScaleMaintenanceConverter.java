package com.heb.pm.scaleManagement;

import com.heb.pm.entity.*;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrient;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrientStatement;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for converting scale management entities to scale maintenance java objects.
 *
 * @author m314029
 * @since 2.17.8
 */
@Component
public class ScaleMaintenanceConverter {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceConverter.class);

	// log messages
	private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product master not found for upc: %d. Setting to null so" +
			" calling method can handle.";
	private static final String NUTRIENT_NOT_FOUND_MESSAGE = "Nutrient code: %d not found.";

	/**
	 * Converts a list of upcs into a list of scale maintenance products.
	 *
	 * @param scaleUpcs Scale upcs to convert.
	 * @return List of scale maintenance products.
	 */
	public List<ScaleMaintenanceProduct> convertScaleUpcsToScaleMaintenanceProducts(List<ScaleUpc> scaleUpcs) {

		List<ScaleMaintenanceProduct> toReturn = new ArrayList<>();
		ScaleMaintenanceProduct currentScaleMaintenanceProduct;
		for(ScaleUpc scaleUpc : scaleUpcs){

			// convert the scale upc into a scale maintenance product
			currentScaleMaintenanceProduct = this.convertScaleUpcToScaleMaintenanceProduct(scaleUpc);
			if(currentScaleMaintenanceProduct != null){
				toReturn.add(currentScaleMaintenanceProduct);
			}
		}
		return toReturn;
	}

	/**
	 * Converts a scale upcs into a scale maintenance product.
	 *
	 * @param scaleUpc Scale upc to convert.
	 * @return Scale maintenance product.
	 */
	private ScaleMaintenanceProduct convertScaleUpcToScaleMaintenanceProduct(ScaleUpc scaleUpc) {
		ScaleMaintenanceProduct toReturn = this.setPLUInformation(scaleUpc);
		if(toReturn != null) {
			this.setIngredientInformation(toReturn, scaleUpc);
			this.setNutritionInformation(toReturn, scaleUpc);
		}
		return toReturn;
	}

	/**
	 * Sets nutrition information onto a scale maintenance product given a scale upc.
	 *
	 * @param toReturn Scale maintenance product to update nutrition values on.
	 * @param scaleUpc Scale upc with nutrition information.
	 */
	private void setNutritionInformation(ScaleMaintenanceProduct toReturn, ScaleUpc scaleUpc) {
		if(scaleUpc.getNutrientStatementHeader() != null){
			if(scaleUpc.getNutrientStatementHeader().getNutrientStatementDetailList() != null){
				this.setNutritionValuesByCode(toReturn, scaleUpc.getNutrientStatementHeader().getNutrientStatementDetailList());
			}
		}
	}

	/**
	 * Sets nutrition values on a given scale maintenance product based on a list of nutrient statement details.
	 *
	 *  @param scaleMaintenanceProduct Scale maintenance product to set nutrition values on.
	 * @param details List of nutrient statement details.
	 */
	private void setNutritionValuesByCode(ScaleMaintenanceProduct scaleMaintenanceProduct, List<NutrientStatementDetail> details) {
		Nutrient.Codes nutrientCode;
		for(NutrientStatementDetail detail : details){
			nutrientCode = Nutrient.Codes.valueOf(detail.getKey().getNutrientLabelCode());
			switch (nutrientCode){
				case ASCORBIC_ACID : {
					scaleMaintenanceProduct.setAscorbicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case BIOTIN : {
					scaleMaintenanceProduct.setBiotin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALCIUM : {
					scaleMaintenanceProduct.setCalcium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALORIES : {
					scaleMaintenanceProduct.setCalories(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CALORIES_FROM_FAT : {
					scaleMaintenanceProduct.setCaloriesFromFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case CHOLESTEROL : {
					scaleMaintenanceProduct.setCholesterol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case COPPER : {
					scaleMaintenanceProduct.setCopper(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case DIETARY_FIBER : {
					scaleMaintenanceProduct.setDietaryFiber(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case FOLACIN : {
					scaleMaintenanceProduct.setFolacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case FOLIC_ACID : {
					scaleMaintenanceProduct.setFolicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case IODINE : {
					scaleMaintenanceProduct.setIodine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case IRON : {
					scaleMaintenanceProduct.setIron(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case MAGNESIUM : {
					scaleMaintenanceProduct.setMagnesium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case NIACIN : {
					scaleMaintenanceProduct.setNiacin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PANTOTHENIC_ACID : {
					scaleMaintenanceProduct.setPantothenicAcid(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PHOSPHORUS : {
					scaleMaintenanceProduct.setPhosphorus(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case POTASSIUM : {
					scaleMaintenanceProduct.setPotassium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case PROTEIN : {
					scaleMaintenanceProduct.setProtein(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case RIBOFLAVIN : {
					scaleMaintenanceProduct.setRiboflavin(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SATURATED_FAT : {
					scaleMaintenanceProduct.setSaturatedFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SODIUM : {
					scaleMaintenanceProduct.setSodium(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SUGAR_ALCOHOL : {
					scaleMaintenanceProduct.setSugarAlcohol(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case SUGARS : {
					scaleMaintenanceProduct.setSugar(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case THIAMINE : {
					scaleMaintenanceProduct.setThiamine(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TOTAL_CARBOHYDRATE : {
					scaleMaintenanceProduct.setTotalCarbohydrates(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TOTAL_FAT : {
					scaleMaintenanceProduct.setTotalFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case TRANS_FAT : {
					scaleMaintenanceProduct.setTransFat(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_A : {
					scaleMaintenanceProduct.setVitaminA(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B1 : {
					scaleMaintenanceProduct.setVitaminB1(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B2 : {
					scaleMaintenanceProduct.setVitaminB2(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B6 : {
					scaleMaintenanceProduct.setVitaminB6(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_B12 : {
					scaleMaintenanceProduct.setVitaminB12(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_C : {
					scaleMaintenanceProduct.setVitaminC(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_D : {
					scaleMaintenanceProduct.setVitaminD(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case VITAMIN_E :{
					scaleMaintenanceProduct.setVitaminE(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				case ZINC : {
					scaleMaintenanceProduct.setZinc(
							this.createScaleMaintenanceNutrientFromValueAndPercent(
									detail.getNutrientStatementQuantity(), detail.getNutrientDailyValue()));
					break;
				}
				default: {
					logger.error(String.format(NUTRIENT_NOT_FOUND_MESSAGE, nutrientCode.getCode()));
					break;
				}
			}
		}
	}

	/**
	 * Creates and returns a new scale maintenance nutrient with the given value and percent.
	 *
	 * @param value Value to set on the nutrient.
	 * @param percent Percent to set on the nurtrient.
	 * @return New scale maintenance nutrient.
	 */
	private ScaleMaintenanceNutrient createScaleMaintenanceNutrientFromValueAndPercent(Double value, long percent) {
		return new ScaleMaintenanceNutrient(value, percent);
	}

	/**
	 * Sets ingredient information on a scale maintenance product given a scale upc.
	 *
	 * @param scaleMaintenanceProduct Scale maintenance product to set ingredient information on.
	 * @param scaleUpc Scale upc with the ingredient information.
	 */
	private void setIngredientInformation(ScaleMaintenanceProduct scaleMaintenanceProduct, ScaleUpc scaleUpc) {
		if(scaleUpc.getIngredientStatementHeader() != null) {
			scaleMaintenanceProduct.setIngredientText(scaleUpc.getIngredientStatementHeader().getIngredientsText());
		}
	}

	/**
	 * Sets PLU information on a scale maintenance product given a scale upc.
	 *
	 * @param scaleUpc Scale upc with the PLU information.
	 */
	private ScaleMaintenanceProduct setPLUInformation(ScaleUpc scaleUpc) {
		ScaleMaintenanceProduct scaleMaintenanceProduct = null;
		try{
			ProductMaster product = null;
			if(scaleUpc.getAssociateUpc().getSellingUnit() != null){
				product = scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster();
			}
			if(product != null) {
				scaleMaintenanceProduct = new ScaleMaintenanceProduct();
				scaleMaintenanceProduct.setDepartment(product.getDepartmentCode());
				scaleMaintenanceProduct.setSubDepartment(product.getSubDepartmentCode());
			} else {
				throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, scaleUpc.getUpc()));
			}
		} catch (EntityNotFoundException e){
			logger.error(e.getLocalizedMessage());
			return scaleMaintenanceProduct;
		}
		scaleMaintenanceProduct
				.setActionCode(scaleUpc.getActionCode())
				.setEatByDays(scaleUpc.getEatByDays())
				.setEnglishDescriptionOne(scaleUpc.getEnglishDescriptionOne())
				.setEnglishDescriptionTwo(scaleUpc.getEnglishDescriptionTwo())
				.setEnglishDescriptionThree(scaleUpc.getEnglishDescriptionThree())
				.setEnglishDescriptionFour(scaleUpc.getEnglishDescriptionFour())
				.setSpanishDescriptionOne(scaleUpc.getSpanishDescriptionOne())
				.setSpanishDescriptionTwo(scaleUpc.getSpanishDescriptionTwo())
				.setSpanishDescriptionThree(scaleUpc.getSpanishDescriptionThree())
				.setSpanishDescriptionFour(scaleUpc.getSpanishDescriptionFour())
				.setForceTare(scaleUpc.isForceTare())
				.setFreezeByDays(scaleUpc.getFreezeByDays())
				.setGrade(scaleUpc.getGrade())
				.setGraphicsCode(scaleUpc.getGraphicsCode())
				.setIngredientStatement(scaleUpc.getIngredientStatement())
				.setLabelFormatOne(scaleUpc.getLabelFormatOne())
				.setLabelFormatTwo(scaleUpc.getLabelFormatTwo())
				.setNetWeight(scaleUpc.getNetWeight())
				.setNutrientStatementCode(scaleUpc.getNutrientStatement())
				.setNutrientStatement(this.getNutrientStatementFromNutrientStatementHeader(scaleUpc.getNutrientStatementHeader()))
				.setPrePackTare(scaleUpc.getPrePackTare())
				.setPriceOverride(scaleUpc.isPriceOverride())
				.setServiceCounterTare(scaleUpc.getServiceCounterTare())
				.setShelfLifeDays(scaleUpc.getShelfLifeDays())
				.setUpc(scaleUpc.getUpc())
				.setPlu(scaleUpc.getPlu());
		return scaleMaintenanceProduct;
	}

	/**
	 * This method gets a scale maintenance nutrient given a nutrient statement header.
	 *
	 * @param nutrientStatementHeader Nutrient statement header with required information.
	 * @return New scale maintenance nutrient statement with information from given nutrient statement header.
	 */
	private ScaleMaintenanceNutrientStatement getNutrientStatementFromNutrientStatementHeader(NutrientStatementHeader nutrientStatementHeader) {
		if(nutrientStatementHeader == null){
			return null;
		}
		return new ScaleMaintenanceNutrientStatement()
				.setMeasureQuantity(nutrientStatementHeader.getMeasureQuantity())
				.setMetricQuantity(nutrientStatementHeader.getMetricQuantity())
				.setServingsPerContainer(nutrientStatementHeader.getServingsPerContainer())
				.setUomCommonCode(nutrientStatementHeader.getNutrientCommonUom() != null ?
						nutrientStatementHeader.getNutrientCommonUom().getNutrientUomDescription() : StringUtils.EMPTY)
				.setUomMetricCode(nutrientStatementHeader.getNutrientMetricUom() != null ?
						nutrientStatementHeader.getNutrientMetricUom().getNutrientUomDescription() : StringUtils.EMPTY);
	}
}
