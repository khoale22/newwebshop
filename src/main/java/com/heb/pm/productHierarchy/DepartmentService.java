package com.heb.pm.productHierarchy;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.DepartmentRepository;
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

	private LazyObjectResolver<Department> resolver = new DepartmentResolver();

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
	public List<Department> findHierarchyLevelsByRegularExpression(List<Department> fullHierarchy, String trimmedUpperCaseSearchString) {
		List<Department> returnList = new ArrayList<>();
		Department currentDepartment;
		List<SubDepartment> subDepartments;
		SubDepartment currentSubDepartment;
		List<ItemClass> itemClasses;
		ItemClass currentItemClass;
		List<ClassCommodity> commodities;
		ClassCommodity currentCommodity;
		List<SubCommodity> subCommodities;
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
}
