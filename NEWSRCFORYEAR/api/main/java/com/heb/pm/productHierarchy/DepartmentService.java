package com.heb.pm.productHierarchy;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.jpa.LazyObjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to Department.
 *
 * @author m314029
 * @since 2.6.0
 */
@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ProductHierarchyUtils productHierarchyUtils;

	@Autowired
	private ClassCommodityRepository classCommodityRepository;

	@Autowired
	private BdmRepository bdmRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private SubDepartmentRepository subDepartmentRepository;

	private LazyObjectResolver<Department> resolver = new DepartmentResolver();

	private LazyObjectResolver<ClassCommodity> classCommodityResolver = new ClassCommodityResolver();

	private static final String SEARCH_PRODUCT_HIERARCHY_BY_BDM = "BDM";

	private static final String SEARCH_PRODUCT_HIERARCHY_BY_DISPLAY_NAME = "Product Hierarchy";

	private static final String BLANK_CHARACTER = " ";

	private static final String BDM_CODE_FORMAT = "%s   ";

	/**
	 * Call the department repository to find all departments, then resolve the list;
	 *
	 * @return the list of all departments
	 */
	@Cacheable(value = "productHierarchyCache")
	@CoreTransactional
	public List<Department> findAll(){
		List<Department> departments = this.departmentRepository.findAll();
		departments.forEach(
				department -> this.productHierarchyUtils.extrapolateItemClassListOfSubDepartmentList(
						department.getSubDepartmentList()));
		departments.forEach(department -> this.resolver.fetch(department));
		return departments;
	}

	/**
	 * This method filters the product hierarchy by a search string. When any level is found to contain the search
	 * string, this method will also add all levels above the match. Starting at the sub-commodity level, if the
	 * sub-commodity's displayName contains the search string (ignoreCase), add the subCommodity to a new list of
	 * sub-commodities. At the commodity level, if the new list of sub-commodities is not empty or the commodity's
	 * displayName contains the search string (ignoreCase), add the commodity to a new list of commodities. At the
	 * item-class level, if the new list of commodities is not empty or the item-class's displayName contains the
	 * search string (ignore case), add the item-class to a new list of item-classes. At the sub-department level, if
	 * the new list of item-classes is not empty or the sub-department's displayName contains the search string
	 * (ignore case), add the sub-department to a new list of sub-departments. At the department level, if the new
	 * list of sub-departments is not empty or the department's displayName contains the search string (ignore case),
	 * add the department to the return list of new departments. After going through all levels, return the list of
	 * departments generated from this search.
	 *
	 * @param fullHierarchy List of departments from full (non-filtered) product hierarchy.
	 * @param trimmedUpperCaseSearchString Search string already trimmed and upper case'd.
	 * @return List of departments that contains any level matching the search query and all levels above.
	 */
	public List<Department> findHierarchyLevelsByRegularExpression(List<Department> fullHierarchy, String trimmedUpperCaseSearchString, String searchType) {
		List<Department> returnList = new ArrayList<>();
		Department currentDepartment;
		List<SubDepartment> subDepartments;
		SubDepartment currentSubDepartment;
		List<ItemClass> itemClasses;
		ItemClass currentItemClass;
		List<ClassCommodity> commodities;
		ClassCommodity currentCommodity;
		List<SubCommodity> subCommodities;
		//If user search by product hierarchy.
		if (searchType.equals(SEARCH_PRODUCT_HIERARCHY_BY_DISPLAY_NAME)){
			// for each department in full hierarchy
			for(Department department : fullHierarchy){
				subDepartments = new ArrayList<>();
				// for each sub department in the current department from the full hierarchy
				for(SubDepartment subDepartment : department.getSubDepartmentList()){
					itemClasses = new ArrayList<>();
					// for each item class in the current sub department from the full hierarchy
					for(ItemClass itemClass : subDepartment.getItemClasses()){
						commodities = new ArrayList<>();
						// for each commodity in the current item class from the full hierarchy
						for(ClassCommodity commodity : itemClass.getCommodityList()){
							subCommodities = new ArrayList<>();
							// for each sub commodity in the current commodity from the full hierarchy
							for(SubCommodity subCommodity : commodity.getSubCommodityList()){
								// if the sub commodity's displayName contains the search string (ignore case)
								if(subCommodity.getDisplayName().toUpperCase().contains(trimmedUpperCaseSearchString)){
									// add the sub commodity to the new list of sub commodities
									subCommodities.add(new SubCommodity(subCommodity));
								}
							}
							// if the list of new sub commodities is not empty, or commodity's displayName contains the
							// search string (ignore case)
							if(!subCommodities.isEmpty() ||
									commodity.getDisplayName().toUpperCase().contains(trimmedUpperCaseSearchString)){
								currentCommodity = new ClassCommodity(commodity);
								// set the commodity master of each sub commodity to the new commodity
								for(SubCommodity subCommodity : subCommodities){
									subCommodity.setCommodityMaster(currentCommodity);
								}
								// set the list of sub commodities to the new sub commodity list
								currentCommodity.setSubCommodityList(subCommodities);
								// add the commodity to the new list of commodities
								commodities.add(currentCommodity);
							}
						}
						// if the list of new commodities is not empty, or item class's displayName contains the search
						// string (ignoreCase)
						if(!commodities.isEmpty() ||
								itemClass.getDisplayName().toUpperCase().contains(trimmedUpperCaseSearchString)){
							currentItemClass = new ItemClass(itemClass);
							// set the item class master for each commodity to the new item class
							for(ClassCommodity commodity : commodities){
								commodity.setItemClassMaster(currentItemClass);
							}
							// set the list of commodities to the new commodity list
							currentItemClass.setCommodityList(commodities);
							// add the item class to the new list of item classes
							itemClasses.add(currentItemClass);
						}
					}
					// if the list of new item classes is not empty, or sub department's displayName contains the search
					// string (ignore case)
					if(!itemClasses.isEmpty() ||
							subDepartment.getDisplayName().toUpperCase().contains(trimmedUpperCaseSearchString)){
						currentSubDepartment = new SubDepartment(subDepartment);
						// set the sub department master for each item class to the new sub department
						for(ItemClass itemClass : itemClasses){
							itemClass.setSubDepartmentMaster(currentSubDepartment);
						}
						// set the list of item classes to the new item class list
						currentSubDepartment.setItemClasses(itemClasses);
						// add the sub department to the new list of sub departments
						subDepartments.add(currentSubDepartment);
					}
				}
				// if the list of new sub departments is not empty, or department's displayName contains the search
				// string (ignore case)
				if(!subDepartments.isEmpty() ||
						department.getDisplayName().toUpperCase().contains(trimmedUpperCaseSearchString)){
					currentDepartment = new Department(department);
					// set the department master for each sub department to the new department
					for(SubDepartment subDepartment : subDepartments){
						subDepartment.setDepartmentMaster(currentDepartment);
					}
					// set the list of sub departments to the new sub department list
					currentDepartment.setSubDepartmentList(subDepartments);
					// add the department to the return list of departments
					returnList.add(currentDepartment);
				}
			}
			return returnList;
		}
		//If user search by BDM.
		else if (searchType.equals(SEARCH_PRODUCT_HIERARCHY_BY_BDM)) {
			List<Bdm> bdms;
			List<ClassCommodity> classCommodityList = new ArrayList<>();
			List<ItemClass> itemClassList = new ArrayList<>();
			List<SubDepartment> subDepartmentList = new ArrayList<>();
			List<Department> departmentList = new ArrayList<>();
			//find all bdm has display name contains search string.
			bdms = bdmRepository.findBdmBysearchString(trimmedUpperCaseSearchString);
			if (!bdms.isEmpty()){
				//find all commodities has bdm belong to bdms.
				for (Bdm bdm : bdms){
					if (!classCommodityRepository.findByBdmCode(String.format(BDM_CODE_FORMAT,bdm.getBdmCode())).isEmpty()) {
						classCommodityList.addAll(classCommodityRepository.findByBdmCode(String.format(BDM_CODE_FORMAT,bdm.getBdmCode())));
					}
				}
				if (!classCommodityList.isEmpty()){
					List<Integer> classCodes = new ArrayList<>();
					for (ClassCommodity classCommodity:classCommodityList){
						//Get SubCommodityList of Commodity.
						this.classCommodityResolver.fetch(classCommodity);
						// Find all class code ids that contains commodities belong to bdms.
						if (classCodes.indexOf(classCommodity.getKey().getClassCode()) == -1){
							classCodes.add(classCommodity.getKey().getClassCode());
						}
					}
					if(!classCodes.isEmpty()){
						List<SubDepartmentKey> subDepartmentKeys = new ArrayList<>();
						for (Integer classCode: classCodes){
							commodities = new ArrayList<>();
							ItemClass itemClass = itemClassRepository.findOne(classCode);
							if(itemClass != null){
								//If commodity of item class is exist in commodities belong to bdms
								// then add commodity into new commodity list.
								if(!itemClass.getCommodityList().isEmpty() && itemClass.getCommodityList()!= null){
									for (ClassCommodity classCommodity:itemClass.getCommodityList()){
										for (ClassCommodity classCommodity1:classCommodityList){
											if (classCommodity.getKey().equals(classCommodity1.getKey())){
												commodities.add(classCommodity);
												break;
											}
										}
									}
								}
								//Set new commodity list for item class.
								itemClass.setCommodityList(commodities);
								itemClassList.add(itemClass);
								//Find all Subdepartment keys of Subdepartments contains itemclasses.
								SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
								this.setValueOfDepartmentKey(subDepartmentKey,itemClass.getDepartmentId(),itemClass.getSubDepartmentId());
								if (subDepartmentKeys.indexOf(subDepartmentKey) == -1) {
									subDepartmentKeys.add(subDepartmentKey);
								}
							}
						}

						if (!subDepartmentKeys.isEmpty()){
							List<SubDepartmentKey> departmentKeys = new ArrayList<>();
							for (SubDepartmentKey subDepartmentKey: subDepartmentKeys){
								itemClasses = new ArrayList<>();
								SubDepartment subDepartment = subDepartmentRepository.findOne(subDepartmentKey);
								//If item class of subdepartment is exist in itemclass list.
								// then add item class into new item class  list.
								if(subDepartmentRepository.findOne(subDepartmentKey) != null){
									for (ItemClass itemClass:itemClassRepository.findByDepartmentIdAndSubDepartmentId(Integer.parseInt(subDepartmentKey.getDepartment()),subDepartmentKey.getSubDepartment())){
										for (ItemClass itemClass1:itemClassList){
											if (itemClass.getItemClassCode().equals(itemClass1.getItemClassCode())){
												itemClasses.add(itemClass);
												break;
											}
										}
									}
									//Set new item class list into subDepartment.
									subDepartment.setItemClasses(itemClasses);
									subDepartmentList.add(subDepartment);
									SubDepartmentKey departmentKey = new SubDepartmentKey();
									departmentKey.setDepartment(subDepartmentKey.getDepartment());
									departmentKey.setSubDepartment(BLANK_CHARACTER);
									if(departmentKeys.indexOf(departmentKey) == -1){
										departmentKeys.add(departmentKey);
									}
								}
							}
							if (!departmentKeys.isEmpty()){
								//Sort department keys.
                                departmentKeys.sort((o1, o2) -> {
                                    if (o1.getDepartment().equals(o2.getDepartment()))
                                        return 0;
                                    return Integer.parseInt(o1.getDepartment()) < Integer.parseInt(o2.getDepartment()) ? -1 : 1;
                                });
                                //Set new SubDepartment List for Department.
								for (SubDepartmentKey departmentKey : departmentKeys){
									subDepartments = new ArrayList<>();
									Department department = departmentRepository.findOne(departmentKey);
									if (department != null){
										for(SubDepartment subDepartment : department.getSubDepartmentList()){
											for(SubDepartment subDepartment1:subDepartmentList){
												if(subDepartment.getKey().equals(subDepartment1.getKey())){
													subDepartment.setDepartmentMaster(department);
													subDepartments.add(subDepartment);
													break;
												}
											}
										}
										department.setSubDepartmentList(subDepartments);
										departmentList.add(department);
									}
								}
							}
						}
					}
				}
			}
			return  departmentList;
		}
		else {
			throw new IllegalArgumentException("No search type is selected");
		}
	}

	/**
	 * Set value for Departmanet Key.
	 * @param subDepartmentKey the subDepartMentKey.
	 */
	private void setValueOfDepartmentKey (SubDepartmentKey subDepartmentKey, Integer departmentId, String subDepartment) {
		if (departmentId / 10 == 0) {
			subDepartmentKey.setDepartment('0' + departmentId.toString());
			subDepartmentKey.setSubDepartment(subDepartment.trim());
		} else {
			subDepartmentKey.setDepartment(departmentId.toString());
			subDepartmentKey.setSubDepartment(subDepartment.trim());
		}
	}
}
