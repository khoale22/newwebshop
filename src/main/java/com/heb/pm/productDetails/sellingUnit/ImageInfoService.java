package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.CopyToHierarchyRequest;
import com.heb.pm.entity.DestinationChanges;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.ImageCategory;
import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ImagePriority;
import com.heb.pm.entity.ImageSource;
import com.heb.pm.entity.ImageStatus;
import com.heb.pm.entity.ImageToUpload;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.ImageCategoryRepository;
import com.heb.pm.repository.ImageInfoRepository;
import com.heb.pm.repository.ImageMetaDataRepository;
import com.heb.pm.repository.ImagePriorityRepository;
import com.heb.pm.repository.ImageSourceRepository;
import com.heb.pm.repository.ImageStatusRepository;
import com.heb.pm.repository.SalesChannelRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.sercurity.AntiVirusUtil;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Service for gathering image information for the Product Details -> Selling Unit-> Image Info view
 * @author s753601
 * @version 2.13.0
 */
@Service
public class ImageInfoService {

	@Autowired
	ImageInfoRepository imageInfoRepository;

	@Autowired
	ImageCategoryRepository imageCategoryRepository;

	@Autowired
	ImageStatusRepository imageStatusRepository;

	@Autowired
	SalesChannelRepository salesChannelRepository;

	@Autowired
	ImagePriorityRepository imagePriorityRepository;

	@Autowired
	ImageSourceRepository imageSourceRepository;

	@Autowired
	GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	ImageMetaDataRepository imageMetaDataRepository;

	@Autowired
	ContentManagementServiceClient cmsWebserviceClient;

	@Autowired
	AntiVirusUtil antiVirusUtil;

	@Autowired
	MessageSource messageSource;



	@Autowired
	ProductAttributeManagementServiceClient productAttributeManagementServiceClient;


	private static final Logger logger = LoggerFactory.getLogger(ImageInfoController.class);
	private static final String HIERARCHY_CONTEXT_CODE="CUST";
	private static final String BEGIN_VIRUS_SCAN_TEST="Scanning image for viruses";
	private static final String VIRUS_SCAN_SUCCESS="No viruses found";
	private static final String ADD_DESTINATIONS_ACTION_CD = "";
	private static final String REMOVE_DESTINATIONS_ACTION_CD="D";
	private static final boolean DEFAULT_ACTIVE_STATUS = true;
	private static final boolean DEFAULT_PARENT_SWITCH = true;
	private static final String DEFAULT_FORBIDDEN_IMAGE_TYPE="TGA";
	private static final String DUPLICATE_ROWS_DETECTION="Duplicate Uri_Txts (Clip Ids) detected in Prod_Scan_Img_URI please contact Support ";
	private static final String BEGIN_COPY_TO_HIERARCHY="User %s is submitting a request to copy images to the customer hierarchy with upcs: %s";
	private static final String END_COPY_TO_HIERARCHY="User %s has completed their request to copy images to the customer hierarchy with upcs: %s";

	/**
	 * This method gets a list of ProductScanImageURIs (uploaded Images) based on a upc
	 * @param upcs the upc of the product
	 * @return list of uploaded images
	 */
	public ModifiedEntity<List<ProductScanImageURI>> getImageInformation(List<Long> upcs, String returnMessage){
		List<ProductScanImageURI> imageInfoList = this.imageInfoRepository.findByKeyIdInAndActiveSwitchAndImageTypeImageFormatNot(upcs, DEFAULT_ACTIVE_STATUS, DEFAULT_FORBIDDEN_IMAGE_TYPE);
		Iterator<ProductScanImageURI> iterator=imageInfoList.iterator();
		boolean isError=false;
		while(iterator.hasNext()){
			ProductScanImageURI image = iterator.next();
			try{
				image.setImageMetaDataList(determineLowestLevel(image.getImageMetaDataList()));
			} catch (LazyInitializationException e){
				iterator.remove();
				isError=true;
			}
		}
		if(isError){
			returnMessage =DUPLICATE_ROWS_DETECTION;
		}
		ModifiedEntity<List<ProductScanImageURI>> test = new ModifiedEntity<>(imageInfoList, returnMessage);
		return test;
	}

	/**
	 * This method is used to find the deepest (lowest) level in the custom hierarchy that the image is tied to.
	 * @param metaData
	 * @return
	 */
	private List<ImageMetaData> determineLowestLevel(List<ImageMetaData> metaData){
		ArrayList<ImageMetaData> lowestMeta=new ArrayList<>();
		ArrayList<Long> lowestLevel= new ArrayList<>();
		ArrayList<Long> parentList= new ArrayList<>();
		for (ImageMetaData data: metaData) {
			GenericEntityRelationship relationship =
					this.genericEntityRelationshipRepository.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(data.getEntity().getId(), HIERARCHY_CONTEXT_CODE, DEFAULT_PARENT_SWITCH);
			if(!parentList.contains(relationship.getKey().getParentEntityId())){
				parentList.add(relationship.getKey().getParentEntityId());
			}
		}
		for (ImageMetaData data: metaData) {
			if(!lowestLevel.contains(data.getEntity().getId()) && !parentList.contains(data.getEntity().getId())){
				lowestMeta.add(data);
				lowestLevel.add(data.getEntity().getId());
			}
		}
		return lowestMeta;
	}

	/**
	 * This method contacts a client and gets the  image data from it
	 * @param uri unique image identifier
	 * @return
	 */
	public byte[] getImages(String uri){
		byte[] image = this.cmsWebserviceClient.getImage(uri);
		return image;
	}

	/**
	 * Returns the values from the image categories table
	 * @return
	 */
	public List<ImageCategory> getCategories(){
		return this.imageCategoryRepository.findAll();
	}

	/**
	 * Returns the values from the image status code table
	 * @return
	 */
	public List<ImageStatus> getStatuses(){
		return this.imageStatusRepository.findAll();
	}

	/**
	 * Returns the values from the SalesChannel code table
	 * @return
	 */
	public List<SalesChannel> getDestinations(){
		return this.salesChannelRepository.findAll();
	}

	/**
	 * Returns the values form the image priorities code table
	 * @return
	 */
	public List<ImagePriority> getPriorities(){
		return this.imagePriorityRepository.findAll();
	}

	/**
	 * Returns the values from the image source code table
	 * @return
	 */
	public List<ImageSource> getSources(){
		return this.imageSourceRepository.findByActiveImage(DEFAULT_ACTIVE_STATUS);
	}

	/**
	 * This method will take a imageToUpload object and scan it for viruses and if the image is clean the submit the
	 * image to the webservice to be uploaded
	 * @param imageToUpload the image to be uploaded
	 */
	public void uploadImage(ImageToUpload imageToUpload) throws AntiVirusException, CheckedSoapException{
		ImageInfoService.logger.info(BEGIN_VIRUS_SCAN_TEST);
		this.antiVirusUtil.virusCheck(imageToUpload.getImageData());
		ImageInfoService.logger.info(VIRUS_SCAN_SUCCESS);
		this.cmsWebserviceClient.uploadImageFromSellingUnitDetails(imageToUpload);
	}

	/**
	 * This method will take a imageToUpload object and scan it for viruses and if the image is clean the submit the
	 * image to the webservice to be uploaded
	 * @param imageToUpload the image to be uploaded
	 */
	public void uploadImageFromHierarchy(ImageToUpload imageToUpload) throws AntiVirusException, CheckedSoapException{
		ImageInfoService.logger.info(BEGIN_VIRUS_SCAN_TEST);
		this.antiVirusUtil.virusCheck(imageToUpload.getImageData());
		ImageInfoService.logger.info(VIRUS_SCAN_SUCCESS);
		this.cmsWebserviceClient.uploadImageFromCustomHierarchy(imageToUpload);
	}

	/**
	 * This method will take a list of image info changes and send them to the webservice client to update the database
	 * this method will then reload the updated imageInfo from the database
	 * @param updates the changes in image info
	 * @param userId the user requesting the changes
	 * @return
	 */
	public List<ProductScanImageURI> updateImageInfo(List<ProductScanImageURI> updates, String userId){
		this.productAttributeManagementServiceClient.updateImageInfo(updates, userId);
		ArrayList<Long> upcs = new ArrayList<>();
		for (ProductScanImageURI imageInfo: updates) {
			upcs.add(imageInfo.getKey().getId());
		}
		return this.imageInfoRepository.findByKeyIdInAndActiveSwitchAndImageTypeImageFormatNot(upcs, DEFAULT_ACTIVE_STATUS, DEFAULT_FORBIDDEN_IMAGE_TYPE);
	}

	/**
	 * This method will take all of the imageInfo destination updates and send them to the webservice to update the
	 * database
	 * @param destinationChanges the destination updates
	 * @param userId the user requesting the updates
	 */
	public void updateDestinations(List<DestinationChanges> destinationChanges, String userId){
		for (DestinationChanges changes: destinationChanges) {
			for (String channel: changes.getDestinationsAdded()) {
				this.productAttributeManagementServiceClient.updateDestinations(Long.toString(changes.getUpc()), Long.toString(changes.getSequenceNumber()),
						ADD_DESTINATIONS_ACTION_CD, channel, changes.getActiveSwitch(), userId);
			}
			for (String channel: changes.getDestinationsRemoved()) {
				this.productAttributeManagementServiceClient.updateDestinations(Long.toString(changes.getUpc()), Long.toString(changes.getSequenceNumber()),
						REMOVE_DESTINATIONS_ACTION_CD, channel, changes.getActiveSwitch(), userId);
			}
		}
	}

	/**
	 * This method will take a products id and determine the path from the products generic entity all the way up to
	 * the root node with both endpoints removed.
	 * @param prodId
	 * @return
	 */
	public List<GenericEntity> findCustomHierarchyPaths(long prodId){
		ArrayList<GenericEntity> path= new ArrayList<>();
		GenericEntityRelationship current;
		List<GenericEntityRelationship> baseGenericEntityRelationship =this.genericEntityRelationshipRepository.findByGenericChildEntityDisplayNumberAndHierarchyContextAndDefaultParent(prodId, HIERARCHY_CONTEXT_CODE, DEFAULT_PARENT_SWITCH);
		if(baseGenericEntityRelationship.size()>0){
			current = baseGenericEntityRelationship.get(0);
			while(current != null){
				path.add(current.getGenericParentEntity());
				current = this.genericEntityRelationshipRepository.
						findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(current.getGenericParentEntity().getId(), HIERARCHY_CONTEXT_CODE, DEFAULT_PARENT_SWITCH);
			}
			// removes the root endpoint
			path.remove(path.size()-1);
		}
		return path;
	}

	/**
	 * This method will take a copy to hierarchy request and send it to the webservice to write to the database
	 * @param request the list of image info and the hierarchy levels to be attached
	 * @param id the user making the request
	 * @throws CheckedSoapException from errors when trying to write to the database
	 */
	public void copyToHierarchy(CopyToHierarchyRequest request, String id) throws CheckedSoapException{
		ImageInfoService.logger.info(String.format(BEGIN_COPY_TO_HIERARCHY, id, request.getUpcs().toString()));
		this.cmsWebserviceClient.copyToHierarchy(request, id);
		ImageInfoService.logger.info(String.format(END_COPY_TO_HIERARCHY, id, request.getUpcs().toString()));
	}

	public ProductScanImageURI getPrimaryImageByProductId(long productId, String salesChannelCode) {
		ProductScanImageURI productScanImageURI = this.imageInfoRepository.findPrimaryImageByProductId(productId, salesChannelCode);
		if (productScanImageURI != null) {
			productScanImageURI.setImage(this.cmsWebserviceClient.getImage(productScanImageURI.getImageURI()));
		}
		return productScanImageURI;
	}

	public List<ImageMetaData> getImageInfoForCustomHierarchy(long entityId){
		List<ImageMetaData> images = this.imageMetaDataRepository.findByKeyId(entityId);
		List<ProductScanImageURI> currentURIs = new ArrayList<>();
		for(ImageMetaData image: images){
			List<ProductScanImageURI> test = this.imageInfoRepository.findByImageURI(image.getUriText());
			if(test.size() > 0){
				image.setProductScanImageURI(test.get(0));
			}
		}
		return images;
	}

	/**
	 * This method will updata all of the image data based on a hierarchy level
	 * @param imageMetaData the changes to be attempted
	 * @param user the user requesting the changes
	 * @return
	 * @throws Exception
	 */
	public List<ImageMetaData> updateImageMetaData(List<ImageMetaData> imageMetaData, String user) throws Exception{
		this.cmsWebserviceClient.editHierarchyImage(imageMetaData, user);
		//All of the image meta data should have the same id which ties it to an entity
		return this.getImageInfoForCustomHierarchy(imageMetaData.get(0).getKey().getId());
	}
}
